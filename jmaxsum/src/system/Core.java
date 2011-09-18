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
package system;

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
import operation.MaxSumOperator;
import operation.OPlus;
import operation.OPlus_MaxSum;
import operation.OTimes;
import operation.OTimes_MaxSum;
import operation.Operator;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Core {

    private COP_Instance cop;
    private PostService ps;
    private MessageFactory mfactory;
    private Operator op;
    private OTimes otimes;
    private OPlus oplus;
    private int iterationsNumber = 3;
    final static int debug = test.DebugVerbosity.debugCore;
    private boolean stepbystep = false;

    private boolean updateOnlyAtEnd = true;

    private boolean pleaseReport = false;
    private String reportpath = "";
    private String report = "";

    public Core(COP_Instance cop) {
        this.cop = cop;
        this.ps = new MailMan();
        this.mfactory = new MessageFactoryArrayDouble();

        this.otimes = new OTimes_MaxSum(mfactory);
        this.oplus = new OPlus_MaxSum(mfactory);

        this.op = new MaxSumOperator(otimes, oplus);
    }

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


    public void pleaseReport(String file){
    /*private boolean updateOnlyAtEnd = true;
    private boolean pleaseReport = false;
    private String reportpath = "";*/
        //this.updateOnlyAtEnd = false;
        this.pleaseReport = true;
        this.reportpath = file;

    }

    public void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd) {
        this.updateOnlyAtEnd = updateOnlyAtEnd;
    }

    

    public void solve() throws PostServiceNotSetException {
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

        // REMEMBER TO CALL Core.Conclude()

    }

    /**
     * Write the conclusion and flush the report
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
                ex.printStackTrace();
            }
        }
        
    }

    public String stringStatus(int iteration){
        //String status = "";
        String status = ((iteration >= 0) ? ("iteration_"+iteration+"=") : "final=");
        try {
            status += this.cop.actualValue()+";";
        } catch (VariableNotSetException ex) {
            status += "err;";
        }
                
        for (Agent agent : this.cop.getAgents()){
            for (NodeVariable variable : agent.getVariables()){
                try {
                    status += variable.toString() +"="+ variable.getStateArgument().toString()+ ";";
                }catch (Exception e) {
                    status += variable.toString() +"=err;";
                }
            }
        }

        return status;
    }
}