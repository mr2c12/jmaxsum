/*
 *  Copyright (C) 2011 Michele Roncalli <roncallim at gmail dot com>
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
package olimpo;

import operation.Solver;
import exception.MessagesFixedPointException;
import exception.PostServiceNotSetException;
import exception.VariableNotSetException;
import factorgraph.NodeVariable;

import java.io.IOException;
import java.util.Iterator;
import maxsum.Agent;
import messages.MailMan;
import messages.MessageFactory;
import messages.MessageFactoryArrayDouble;
import messages.PostService;
import misc.Utils;
import operation.MSumOperator;
import operation.OPlus;
import operation.OPlus_MaxSum;
import operation.OPlus_MinSum;
import operation.OTimes;
import operation.OTimes_MaxSum;
import operation.Operator;
import system.COP_Instance;

/**
 * Athena is the goddess of wisdom, courage, inspiration, civilization, warfare, strength, strategy, the arts, crafts, justice, and skill.<br/>
 * <br/>
 * This is the solver module.<br/>
 * It implements the Max Sum Algorithm.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Athena implements Solver {

    /**
     * Shuffle the order of messages sent?
     * 0 -> no
     * 1 -> shuffle array
     */
    // TODO: set it to 1
    public static int shuffleMessage = 0;

    private COP_Instance cop;
    private PostService ps;
    private MessageFactory mfactory;
    private Operator op;
    private OTimes otimes;
    private OPlus oplus;
    private double latestValue_start;
    private int iterationsNumber = 500;
    final static int debug = test.DebugVerbosity.debugAthena;
    private boolean stepbystep = false;

    private boolean updateOnlyAtEnd = true;

    private boolean pleaseReport = false;
    private String reportpath = "";
    private String report = "";

	private boolean writeOutput = false;
	private String writeOutputPath = "";

    public Athena(COP_Instance cop, String plus_operation, String times_operation) throws IllegalArgumentException {
        this.cop = cop;
        this.ps = new MailMan();
        this.mfactory = new MessageFactoryArrayDouble();

        // change this for sum or prod
        // LOOK: check the code!
        if (times_operation == null){
            throw new IllegalArgumentException("Times Operation can NOT be null.");
        }
        if (times_operation.equalsIgnoreCase("sum")){
            this.otimes = new OTimes_MaxSum(mfactory);
        } else {
            throw new IllegalArgumentException("Times Operation \""+plus_operation+"\" not recognized, sorry");
        }


        // change this for max or min
        if (plus_operation == null){
            throw new IllegalArgumentException("Plus Operation can NOT be null.");
        }
        if (plus_operation.equalsIgnoreCase("max")){
            this.oplus = new OPlus_MaxSum(mfactory);
            this.latestValue_start = Double.NEGATIVE_INFINITY;
        }
        else if (plus_operation.equalsIgnoreCase("min")){
            this.oplus = new OPlus_MinSum(mfactory);
            this.latestValue_start = Double.POSITIVE_INFINITY;
        } else {
            throw new IllegalArgumentException("Plus Operation \""+plus_operation+"\" not recognized, sorry");
        }

        this.op = new MSumOperator(otimes, oplus);
    }

    /**
     * How many steps to do?
     * @param n number of steps.
     */
    public void setIterationsNumber(int n) {
        this.iterationsNumber = n;
    }
    public COP_Instance getCop() {
        return cop;
    }

    public void setCop(COP_Instance cop) {
        this.cop = cop;
    }

    private void init() {
        // set the postservice
        this.cop.setPostService(ps);
        // set the operator
        this.cop.setOperator(op);
        // reset the report buffer
        this.report = "";
    }

    public boolean isStepbystep() {
        return stepbystep;
    }

    public void setStepbystep(boolean stepbystep) {
        this.stepbystep = stepbystep;
    }


    /**
     * Please report all the information and put them in file
     * @param file report path
     */
    public void pleaseReport(String file){
    /*private boolean updateOnlyAtEnd = true;
    private boolean pleaseReport = false;
    private String reportpath = "";*/
        //this.updateOnlyAtEnd = false;
        this.pleaseReport = true;
        this.reportpath = file;

    }

    /**
     * Set if the update is only after the algorithm is finished or after each step.
     * @param updateOnlyAtEnd yes if to update only when finished.
     */
    public void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd) {
        this.updateOnlyAtEnd = updateOnlyAtEnd;
    }


    // TODO: change solver to fp + n
    // TODO: stop when fixed
    // TODO: compute time used for solve
    public void solve() throws PostServiceNotSetException {
        this.solve_complete();
    }

    /**
     * Apply the Max Sum algorithm.
     * Deprecated.
     * @throws PostServiceNotSetException if Post Service is not set, strictly required for messages sending and reading.
     */
    public void solve_nIteration() throws PostServiceNotSetException {
        if (debug >= 3) {
            System.out.println("Core: init()");
        }
        this.init();
        String status = "";

        if (this.pleaseReport) {
            this.report += "iterations_number="+this.iterationsNumber+"\n";
            this.report += "agents_number="+this.cop.getAgents().size()+"\n";
            this.report += "variables_number="+this.cop.getNodevariables().size()+"\n";
            this.report += "functions_number="+this.cop.getNodefunctions().size()+"\n";
        }

        Iterator<Agent> itAgent;
        Agent agent = null;
        for (int i = 0; i < this.iterationsNumber; i++) {
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Iteration number "+ (i+1));
                    System.out.println("---------------------------------------");
            }


            itAgent = this.cop.getAgents().iterator();
            while (itAgent.hasNext()) {
                agent = itAgent.next();
                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "at iteration "+ (i+1) +" using agent "+ agent + "----------------------------------------------------------");
                        System.out.println("---------------------------------------");
                }

                //agent.sendRMessages();
                agent.sendQMessages();
                agent.sendRMessages();

                if (!this.updateOnlyAtEnd) {
                    agent.sendZMessages();
                    agent.updateVariableValue();


                    //System.out.println("iteration_" + (i+1) + "=" + cop.actualValue() + "\n");
                    status = this.stringStatus((i+1));
                    System.out.println(status);
                    if (this.pleaseReport) {
                        this.report += status +"\n";
                    }

                    
                   
                }

            }

            // pause

            if (this.stepbystep) {
                System.out.print("Iteration "+(i+1)+"/"+ this.iterationsNumber+" completed, press enter to continue");
                try {
                    System.in.read();
                } catch (IOException ex) {
                    //skip
                }
                System.out.println("");
            }
            

            // continue
        }

        //finish
        
        if (this.updateOnlyAtEnd) {
                    // after the cicle, computeZ and update the variables.
                    itAgent = this.cop.getAgents().iterator();
                    while (itAgent.hasNext()) {
                        agent = itAgent.next();
                        agent.sendZMessages();
                        agent.updateVariableValue();
                        
                    }

        }
        
        // call conclude

    }

    public void solve_complete() throws PostServiceNotSetException {
        if (debug >= 3) {
            System.out.println("Core: init()");
        }
        this.init();
        String status = "";

        this.report += "max_iterations_number="+this.iterationsNumber+"\n";
        this.report += "agents_number="+this.cop.getAgents().size()+"\n";
        this.report += "variables_number="+this.cop.getNodevariables().size()+"\n";
        this.report += "functions_number="+this.cop.getNodefunctions().size()+"\n";


        Iterator<Agent> itAgent;
        Agent agent = null;

        double latestValue = Double.NaN;
        double actualValue;
        int iteration_latestValue;

        long startTime = System.currentTimeMillis();
        boolean keepGoing;

        int i=0;
        int lastIteration=0;
        boolean ffFound = false;

        try{
            for ( i = 0; i < this.iterationsNumber; i++) {
                lastIteration = i+1;
                keepGoing = false;

                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Iteration number "+ (i+1));
                        System.out.println("---------------------------------------");
                }


                itAgent = this.cop.getAgents().iterator();
                while (itAgent.hasNext()) {
                    agent = itAgent.next();
                    if (debug>=1) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "at iteration "+ (i+1) +" using agent "+ agent + "----------------------------------------------------------");
                            System.out.println("---------------------------------------");
                    }


                    keepGoing |= agent.sendQMessages();
                    keepGoing |= agent.sendRMessages();

                    if (!keepGoing){
                        ffFound = true;
                        // messages not changed!
                        throw new MessagesFixedPointException();
                    }



                    if (!this.updateOnlyAtEnd) {
                        agent.sendZMessages();
                        agent.updateVariableValue();
                        try {
                            actualValue = this.cop.actualValue();
                            if (actualValue != latestValue){
                                latestValue = actualValue;
                                iteration_latestValue = i;
                            }
                        } catch (VariableNotSetException ex) {
                            ex.printStackTrace();
                        }


                        //System.out.println("iteration_" + (i+1) + "=" + cop.actualValue() + "\n");
                        status = this.stringStatus((i+1));
                        System.out.println(status);
                        if (this.pleaseReport) {
                            this.report += status +"\n";
                        }



                    }

                }

                // pause

                if (this.stepbystep) {
                    System.out.print("Iteration "+(i+1)+"/"+ this.iterationsNumber+" completed, press enter to continue");
                    try {
                        System.in.read();
                    } catch (IOException ex) {
                        //skip
                    }
                    System.out.println("");
                }


                // continue
            }
        } catch (MessagesFixedPointException e){
            
        }

        long timeElapsed = System.currentTimeMillis() - startTime;

        //finish

        if (this.updateOnlyAtEnd || ffFound) {
                    // after the cicle, computeZ and update the variables.
                    itAgent = this.cop.getAgents().iterator();
                    while (itAgent.hasNext()) {
                        agent = itAgent.next();
                        agent.sendZMessages();
                        agent.updateVariableValue();

                    }

        }

        try {
            actualValue = this.cop.actualValue();
            if (actualValue != latestValue){
                latestValue = actualValue;
                iteration_latestValue = i;
            }
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        }

        status = this.stringStatus((-1));
        this.report += status +"\n";
        this.report += "total time [ms]="+timeElapsed+"\n";
        this.report += "latest value got at iteration="+i+"\n";
        this.report += "total number of iteration="+lastIteration+"\n";
        this.report += "fixed point found=";
        if (ffFound){
            this.report += "Y";
        }
        else {
            this.report += "N";
        }
        this.report +="\n";
        if (this.pleaseReport) {

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "report to write is\n"+this.report);
                    System.out.println("---------------------------------------");
            }
            try {
                Utils.stringToFile(this.report, this.reportpath);
            } catch (IOException ex) {
                System.out.println("Sorry but I'm unable to write the report to the file "+this.reportpath);
            }
        }
        else {
            System.out.println(report);
        }

    }

    /**
     * Apply the Max Sum algorithm.
     * Deprecated.
     * @throws PostServiceNotSetException if Post Service is not set, strictly required for messages sending and reading.
     */
    public void solve_fixedPoint() throws PostServiceNotSetException {
        if (debug >= 3) {
            System.out.println("Core: init()");
        }
        this.init();
        String status = "";

        if (this.pleaseReport) {
            this.report += "iterations_number="+this.iterationsNumber+"\n";
            this.report += "agents_number="+this.cop.getAgents().size()+"\n";
            this.report += "variables_number="+this.cop.getNodevariables().size()+"\n";
            this.report += "functions_number="+this.cop.getNodefunctions().size()+"\n";
        }

        Iterator<Agent> itAgent;
        Agent agent = null;
        boolean keepGoing = false;
        int i = 0;
        //for (int i = 0; i < this.iterationsNumber; i++) {
        //do
        do {
            i++;
            keepGoing = false;

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Iteration number "+ (i+1));
                    System.out.println("---------------------------------------");
            }


            itAgent = this.cop.getAgents().iterator();
            while (itAgent.hasNext()) {
                agent = itAgent.next();
                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "at iteration "+ (i+1) +" using agent "+ agent + "----------------------------------------------------------");
                        System.out.println("---------------------------------------");
                }

                //agent.sendRMessages();
                keepGoing |= agent.sendQMessages();
                keepGoing |= agent.sendRMessages();

                if (!this.updateOnlyAtEnd) {
                    agent.sendZMessages();
                    agent.updateVariableValue();


                    //System.out.println("iteration_" + (i+1) + "=" + cop.actualValue() + "\n");
                    status = this.stringStatus((i+1));
                    System.out.println(status);
                    if (this.pleaseReport) {
                        this.report += status +"\n";
                    }



                }

            }

            // pause

            if (this.stepbystep) {
                System.out.print("Iteration "+(i+1)+"/"+ this.iterationsNumber+" completed, press enter to continue");
                try {
                    System.in.read();
                } catch (IOException ex) {
                    //skip
                }
                System.out.println("");
            }


            // continue
        //while
        } while (keepGoing);

        //finish

        if (this.updateOnlyAtEnd) {
                    // after the cicle, computeZ and update the variables.
                    itAgent = this.cop.getAgents().iterator();
                    while (itAgent.hasNext()) {
                        agent = itAgent.next();
                        agent.sendZMessages();
                        agent.updateVariableValue();

                    }

        }

        // REMEMBER TO CALL Athena.Conclude()

    }

    /**
     * Write the conclusion and flush the report
     * Deprecated.
     */
    public void conclude(){
        String status = this.stringStatus((-1));
        System.out.println(status);
        if (this.pleaseReport) {
            this.report += status +"\n";
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "report to write is\n"+this.report);
                    System.out.println("---------------------------------------");
            }
            try {
                Utils.stringToFile(this.report, this.reportpath);
            } catch (IOException ex) {
                System.out.println("Sorry but I'm unable to write the report to the file "+this.reportpath);
            }
        }
        
        
    }

    public String stringStatus(int iteration){
        //String status = "";
        String status_i = ((iteration >= 0) ? ("iteration_"+iteration+"=") : "final=");
        StringBuilder status = new StringBuilder();
        status.append(status_i);
        /*try {
            status.append(this.cop.actualValue()).append(";");
        } catch (VariableNotSetException ex) {
            status.append("err;");
        }
                
        for (Agent agent : this.cop.getAgents()){
            for (NodeVariable variable : agent.getVariables()){
                try {
                    status.append(variable.toString() +"="+ variable.getStateArgument().toString()+ ";");
                }catch (Exception e) {
                    status.append(variable.toString() +"=err;");
                }
            }
        }*/
        // string status moved in COP_Instance
        status.append(this.cop.status());
        return status.toString();
    }

}
