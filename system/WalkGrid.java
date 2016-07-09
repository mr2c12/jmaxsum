/*
 *  Copyright (C) 2012 Michele Roncalli <roncallim at gmail dot com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package system;

import exception.FunctionNotPresentException;
import exception.InitializatedException;
import exception.MaximumNumberOfAttemptsException;
import exception.NoMoreGeneratorsException;
import exception.NoMoreValuesException;
import exception.PostServiceNotSetException;
import exception.ResultOkException;
import exception.UnInitializatedException;
import exception.VariableNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.RelaxableFunctionEvaluator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import maxsum.Relaxable_MS_COP_Instance;
import misc.Utils;
import operation.Solver;
import powerGrid.PowerGrid;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class WalkGrid implements Solver {

    //COP_Instance cop;
    Relaxable_MS_COP_Instance cop;
    ArrayList<NodeVariable> variables = new ArrayList<NodeVariable>();
    ArrayList<NodeFunction> functions = new ArrayList<NodeFunction>();
    // TEMPERATURE MAX:
    private int maximumNumberOfIterations = 20000;
    HashMap<NodeVariable, Integer> assignments;
    boolean stepByStep = false;
    boolean updateOnlyAtEnd = true;
    boolean pleaseReport = false;
    String reportpath = null;
    double maxProbability = 0.8;

    final static int debug = test.DebugVerbosity.debugWalkGrid;

    public WalkGrid(Relaxable_MS_COP_Instance cop) {
        this.cop = cop;

        for (NodeVariable x : cop.getNodevariables()) {
            if (x.size() == 1) {
                x.setStateIndex(0);
            } else {
                variables.add(x);
            }
        }
        for (NodeFunction f : cop.getNodefunctions()) {
            functions.add(f);
        }
        this.assignments = new HashMap<NodeVariable, Integer>(variables.size());
    }

    public void randomInit() {
        for (NodeVariable x : this.variables) {
            try {
                x.setRandomValidValue();
            } catch (NoMoreValuesException ex) {
                ex.printStackTrace();
            }
        }
        //TODO: salva assegnamento
        this.backupAssignment();
    }

    public NodeFunction getOverloadedGenerator() throws ResultOkException {
        NodeFunction f = null;
        Random rnd = new Random();
        ArrayList<NodeFunction> tempList = new ArrayList<NodeFunction>(this.functions);
        Collections.shuffle(tempList);
        try {
            do {
                if (tempList.size() > 0) {
                    f = tempList.remove(rnd.nextInt(tempList.size()));
                } else {
                    throw new ResultOkException();
                }
            } while (f.actualValue() < Double.POSITIVE_INFINITY);
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        }

        return f;
    }

    public void pleaseReport(String file) {
        this.pleaseReport = true;
        this.reportpath = file;
    }

    public void setIterationsNumber(int n) {
        this.maximumNumberOfIterations = n;
    }

    public void setStepbystep(boolean stepbystep) {
        this.stepByStep = stepbystep;
    }

    public void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd) {
        this.updateOnlyAtEnd = updateOnlyAtEnd;
    }

    public void solve() throws PostServiceNotSetException {

        this.randomInit();
        int iterationNumber = 0;
        int solutionFoundAtIteration = Integer.MAX_VALUE;
        NodeFunction f = null, otherF = null;
        RelaxableFunctionEvaluator fe, otherFe;
        NodeVariable x = null;
        LinkedList<NodeVariable> loadsOfGenerator = new LinkedList<NodeVariable>();
        Random rnd = new Random();
        boolean searchAgain = false;
        ArrayList<NodeFunction> tempList = new ArrayList<NodeFunction>();
        int oldXValue;
        double delta;
        double newOtherCost;
        double overloadF;
        // class Double to let it be updated
        Double prob = 0.18;
        int attempt = 0;

        Long startTime = System.currentTimeMillis();
        String report = "";

        String status;

        boolean ffFound = false;

        report += "max_iterations_number=" + this.maximumNumberOfIterations + "\n";

        try {
            while (iterationNumber < maximumNumberOfIterations) {

                try {

                    //f = this.getOverloadedGenerator();
                    tempList.clear();
                    for (NodeFunction nf : this.functions) {
                        try {
                            if (nf.actualValue() >= Double.POSITIVE_INFINITY) {
                                tempList.add(nf);
                            }
                        } catch (VariableNotSetException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (tempList.isEmpty()) {
                        throw new ResultOkException();
                    }

                    Collections.shuffle(tempList);

                    if (debug>=3) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "There are "+tempList.size()+" overloaded F");
                            System.out.println("---------------------------------------");
                    }

                    do {
                        searchAgain = false;
                        try {

                            if (tempList.size() > 0) {
                                f = tempList.remove(rnd.nextInt(tempList.size()));
                            } else {
                                // no valid f can be used!
                                throw new NoMoreGeneratorsException();
                            }


                            x = this.getFixableLoad(f);
                        } catch (NoMoreValuesException ex) {
                            searchAgain = true;
                        }
                    } while (searchAgain);

                    if (debug>=3) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Generator choosen: "+f);
                            System.out.println("---------------------------------------");
                    }

                    overloadF = ((RelaxableFunctionEvaluator) f.getFunction()).relaxedActualValue() - 1;

                    // x is a valid load -> can be switched

                    oldXValue = x.getStateIndex();
                    for (NodeArgument na : x.getArguments()) {
                        Integer id_otherF = (Integer) na.getValue();

                        if (id_otherF != f.id()
                                && NodeFunction.getNodeFunction(id_otherF).actualValue() < Double.POSITIVE_INFINITY) {
                            otherF = NodeFunction.getNodeFunction(id_otherF);
                            break;
                        }


                    }

                    otherFe = (RelaxableFunctionEvaluator) otherF.getFunction();

                    //delta = otherFe.relaxedActualValue();


                    // connect x to otherF and check
                    x.setStateArgument(
                            NodeArgument.getNodeArgument(otherF.getId()));

                    newOtherCost = otherFe.relaxedActualValue();
                    //delta -= newOtherCost;

                    // if f is no more overloaded
                    // and otherF is still no overloaded or overloaded of no more than overloadF quantity

                    if (
                            (f.actualValue() < Double.POSITIVE_INFINITY)
                            &&
                            (   (otherF.actualValue() < Double.POSITIVE_INFINITY)
                                ||
                                (newOtherCost<=overloadF)
                            )
                            ){
                        // accept
                        if (debug>=3) {
                                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                                System.out.println("---------------------------------------");
                                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Move accepted");
                                System.out.println("---------------------------------------");
                        }
                        this.backupAssignment();
                        solutionFoundAtIteration = iterationNumber+1;
                    }
                    else {
                        // with probability p..
                        //probab = Math.exp(-delta / temperatura);
                        if (rnd.nextDouble() < prob) {
                            // update
                            if (debug>=3) {
                                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                                    System.out.println("---------------------------------------");
                                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Move accepted due to probability");
                                    System.out.println("---------------------------------------");
                            }
                        }
                        else {
                            // does not update
                            if (debug>=3) {
                                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                                    System.out.println("---------------------------------------");
                                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Move rejected");
                                    System.out.println("---------------------------------------");
                            }
                            x.setStateIndex(oldXValue);
                        }
                    }



                    // TODO: update prob
                    prob = updateProb(iterationNumber, prob);
                    iterationNumber++;

                    if (debug>=3) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "iteration: "+ (iterationNumber+1) +" probability become: "+prob);
                            System.out.println("---------------------------------------");
                    }


                    if (!this.updateOnlyAtEnd) {
                        status = "iteration_" + iterationNumber + "=" + this.cop.status();
                        if (this.pleaseReport) {
                            report += status + "\n";
                        } else {
                            System.out.println(status);
                        }
                    }

                    if (this.stepByStep) {
                        System.out.print("Iteration " + iterationNumber + "/" + this.maximumNumberOfIterations + " completed, press enter to continue");
                        try {
                            System.in.read();
                        } catch (IOException ex) {
                            //skip
                        }
                        System.out.println("");
                    }
                } catch (NoMoreGeneratorsException eg) {
                    // reinit?
                    this.randomInit();
                    iterationNumber = 0;
                    solutionFoundAtIteration = Integer.MAX_VALUE;
                    searchAgain = false;
                    prob = 0.18;
                    attempt++;

                    if (attempt>=50){
                        throw new MaximumNumberOfAttemptsException();
                    }

                }


            }
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        } catch (FunctionNotPresentException ex) {
            ex.printStackTrace();
        } catch (MaximumNumberOfAttemptsException en){
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Maximum number of attempts reached.");
                    System.out.println("---------------------------------------");
            }
        } catch (ResultOkException ex) {
            // do something
            this.restoreAssignment();
            solutionFoundAtIteration = iterationNumber+1;
            ffFound = true;
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "after "+(iterationNumber+1)+" iterations the result is: "+cop.status());
                System.out.println("---------------------------------------");
        }

        long endTime = System.currentTimeMillis();
        int totalIteration = (iterationNumber+1);

        status = "final=" + cop.status();

        if (!this.updateOnlyAtEnd) {
            if (this.pleaseReport) {
            } else {
                System.out.println(status);
            }
        }


        report += status + "\n";
        report += "total time [ms]=" + (endTime - startTime) + "\n";


        report += "latest value got at iteration=" + solutionFoundAtIteration + "\n";
        report += "total number of iteration=" + totalIteration + "\n";
        report += "fixed point found=";
        if (ffFound) {
            report += "Y";
        } else {
            report += "N";
        }
        report += "\n";
        report += "attempt number=" + (attempt+1);
        if (this.pleaseReport) {
            try {
                Utils.stringToFile(report, reportpath);
            } catch (IOException ex) {
                System.out.println("Sorry but I'm unable to write the report to the file " + reportpath);
            }
        } else {
            System.out.println(report);
        }

    }

    private void backupAssignment() {
        for (NodeVariable x : this.variables) {
            this.assignments.put(x, x.getStateIndex());
        }
    }

    private void restoreAssignment() {
        for (Entry<NodeVariable, Integer> entry : this.assignments.entrySet()) {
            entry.getKey().setStateIndex(entry.getValue());
        }
    }

    private NodeVariable getFixableLoad(NodeFunction f) throws NoMoreValuesException {
        LinkedList<NodeVariable> xes = new LinkedList<NodeVariable>(f.getNeighbour());
        Collections.shuffle(xes);
        //for (NodeVariable x : f.getNeighbour()) {
        // xes let the load to be chosen in random order (not always the first one available)
        for (NodeVariable x : xes) {
            try {
                if (x.getStateArgument().getValue() == (Integer) f.id()) {
                    for (NodeArgument na : x.getArguments()) {
                        Integer id_f = (Integer) na.getValue();
                        if (id_f != f.id()
                                && NodeFunction.getNodeFunction(id_f).actualValue() < Double.POSITIVE_INFINITY) {
                            return x;
                        }
                    }
                }
            } catch (VariableNotSetException ex) {
                ex.printStackTrace();

            } catch (FunctionNotPresentException ex) {
                ex.printStackTrace();
            }
        }
        throw new NoMoreValuesException();
    }

    private Double updateProb(int iterationNumber, Double prob) {
        // linear decreasing
        // prob = 0.18 * ( iterationNumber / maxIteration ) + 0.18
        return - this.maxProbability * ( (double)iterationNumber / (double)this.maximumNumberOfIterations ) + this.maxProbability;
    }



    public static void main(String[] args){
        try {
            PowerGrid pg = new PowerGrid("/home/mik/Documenti/univr/Ragionamento Automatico/stage/report/200/0.29/2.pg");
            Relaxable_MS_COP_Instance cop = pg.getCopMnotInfNoCo2();
            WalkGrid instance = new WalkGrid(cop);
            instance.solve();
            System.out.println("After solve, istance is:" + cop.status());
            System.out.println("Relaxed cost is:" + cop.actualRelaxedValue());
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        } catch (PostServiceNotSetException ex) {
            ex.printStackTrace();
        } catch (UnInitializatedException ex) {
            ex.printStackTrace();
        } catch (InitializatedException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
