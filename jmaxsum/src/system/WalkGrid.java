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
import exception.NoMoreGeneratorsException;
import exception.NoMoreValuesException;
import exception.PostServiceNotSetException;
import exception.ResultOkException;
import exception.VariableNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import function.RelaxableFunctionEvaluator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import maxsum.Relaxable_MS_COP_Instance;
import operation.Solver;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class WalkGrid implements Solver {

    //COP_Instance cop;
    Relaxable_MS_COP_Instance cop;
    ArrayList<NodeVariable> variables = new ArrayList<NodeVariable>();
    ArrayList<NodeFunction> functions = new ArrayList<NodeFunction>();
    private int maximumNumberOfIterations = 100;
    HashMap<NodeVariable, Integer> assignments;

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
    }

    public NodeFunction getOverloadedGenerator() throws ResultOkException {
        NodeFunction f = null;
        Random rnd = new Random();
        ArrayList<NodeFunction> tempList = new ArrayList<NodeFunction>(this.functions);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIterationsNumber(int n) {
        this.maximumNumberOfIterations = n;
    }

    public void setStepbystep(boolean stepbystep) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void solve() throws PostServiceNotSetException {

        this.randomInit();
        int iterationNumber = 0;
        int solutionFoundAtIteration;
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

        try {
            while (iterationNumber < maximumNumberOfIterations) {


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
                    this.backupAssignment();
                    solutionFoundAtIteration = iterationNumber+1;
                }
                else {
                    // with probability p..
                }


                iterationNumber++;
            }
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        } catch (NoMoreGeneratorsException eg) {
            eg.printStackTrace();
        } catch (FunctionNotPresentException ex) {
            ex.printStackTrace();
        } catch (ResultOkException ex) {
            // do something
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
        for (NodeVariable x : f.getNeighbour()) {
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
}
