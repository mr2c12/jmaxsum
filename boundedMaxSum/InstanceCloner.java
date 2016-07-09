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

package boundedMaxSum;

import exception.NodeVariableNotInMapException;
import exception.OutOfAgentNumberException;
import exception.OutOfNodeFunctionNumberException;
import exception.OutOfNodeVariableNumberException;
import exception.VariableNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import maxsum.Agent;
import system.COP_Instance;
import test.DebugVerbosity;

/**
 * Class to clone a COP problem instance.
 * It ask for the original instance, and keep a pointer to the originale
 * and to the cloned one.
 * Example of use:
 *
 *  COP_Instance cop = new MS_COP_Instance(path_to_file);
 *  InstanceCloner ic = new InstanceCloner(cop);
 *  COP_Instance cop_cloned = ic.getClonedInstance();
 *  // do whatever you like on cop_cloned, like solving it
 *  // now it's time to set the original variables with to the values computed
 *  ic.setOriginalVariablesValues();
 *  // ok, you're ready to evaluate original instance
 *  System.out.println("Utility is: " + cop.actualValue()
 *
 * 
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class InstanceCloner{

    private static final int debug = DebugVerbosity.debugInstanceCloner;

    /**
     * the original instance
     */
    private COP_Instance originalInstance;

    /**
     * the cloned instance
     */
    private COP_Instance clonedInstance;

    /**
     * This is used to easily find the original node from the cloned one.
     * Useful when you have to set the original nodevariable to the actual value
     * and compute the original instance value.
     */
    private HashMap<NodeVariable, NodeVariable> variables_correspondence_cloned_original;

    /**
     * This is used to easily find the cloned node from the original one.
     * Useful when the neighbours must be changed with the cloned one.
     */
    private HashMap<NodeVariable, NodeVariable> variables_correspondence_original_cloned;
     /**
     * This is used to easily find the cloned node from the original one.
     * Useful when the neighbours must be changed with the cloned one.
     */
    private HashMap<NodeFunction, NodeFunction> functions_correspondence_original_cloned;

    /**
     * Constructor. It prepares the cloned instance and set its pointer to that.
     * @param originalInstance the instance to be cloned
     */
    public InstanceCloner(COP_Instance originalInstance) {
        try {
            // set the original instance
            this.originalInstance = originalInstance;
            this.clonedInstance = originalInstance.getNewMe();

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Original instance set for cloned one");
                    System.out.println("---------------------------------------");
            }


            /* NODEARGUMENT DEEP CHANGE: they disappear
            // set all the nodeargumen AS IS, no need to change
            for (NodeArgument arg : this.originalInstance.getNodeargumens()) {
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Adding argument "+arg);
                        System.out.println("---------------------------------------");
                }
                //this.nodearguments.add(arg);
                this.clonedInstance.addNodeArgument(arg);
            }

             */

            this.variables_correspondence_cloned_original = new HashMap<NodeVariable, NodeVariable>();
            this.variables_correspondence_original_cloned = new HashMap<NodeVariable, NodeVariable>();
            // clone variables
            // keep a link beetwin a cloned variable and the original one
            NodeVariable newx;
            for (NodeVariable oldx : this.originalInstance.getNodevariables()) {
                newx = oldx.getClone();
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Old variable: "+oldx + "\nNew variable: "+newx);
                        System.out.println("---------------------------------------");
                }
                //this.nodevariables.add(newx);
                this.clonedInstance.addNodeVariable(newx);
                this.variables_correspondence_cloned_original.put(newx, oldx);
                this.variables_correspondence_original_cloned.put(oldx, newx);
            }


            this.functions_correspondence_original_cloned = new HashMap<NodeFunction, NodeFunction>();
            // clone functions
            // keep a link beetwin a cloned function and the original one
            NodeFunction newf;
            for (NodeFunction oldf : this.originalInstance.getNodefunctions()) {
                newf = oldf.getClone();
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Old function: "+oldf + "\nNew function: "+newf);
                        System.out.println("---------------------------------------");
                }
                //this.nodefunctions.add(newf);
                this.clonedInstance.addNodeFunction(newf);
                this.functions_correspondence_original_cloned.put(oldf, newf);
            }

            // connect new variables to new function
            //for (NodeVariable x : this.nodevariables) {
            for (NodeVariable x : this.clonedInstance.getNodevariables()) {
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Variable "+x+" has "+x.getNeighbour().size()+ " neighbours");
                        System.out.println("---------------------------------------");
                }

                ConcurrentLinkedQueue<NodeFunction> n_queue = new ConcurrentLinkedQueue<NodeFunction>();

                for (NodeFunction oldNeighbour: x.getNeighbour()) {
                    if (debug>=3) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Trying to change "+oldNeighbour + " with "+this.functions_correspondence_original_cloned.get(oldNeighbour));
                            System.out.println("---------------------------------------");
                    }

                    n_queue.add(oldNeighbour);

                }
                while (!n_queue.isEmpty()){
                    x.changeNeighbour(n_queue.peek(), this.functions_correspondence_original_cloned.get(n_queue.poll()));
                }
            }

            // connect functions to variable

            for (NodeFunction f : this.clonedInstance.getNodefunctions()) {
                ConcurrentLinkedQueue<NodeVariable> n_queue = new ConcurrentLinkedQueue<NodeVariable>();
                for (NodeVariable oldNeighbour : f.getNeighbour()) {
                    n_queue.add(oldNeighbour);
                }

                while (!n_queue.isEmpty()) {
                    f.changeNeighbour(n_queue.peek(), this.variables_correspondence_original_cloned.get(n_queue.poll()));
                }


            }

            // create the edges
            // version 1
            this.clonedInstance.getFactorgraph().createTheEdges();
            // version 2: create the edges from the cloned nodes. More complex than FactorGraph.createTheEdge()


            // clone agents
            for (Agent oldAgent : this.originalInstance.getAgents()) {

                Agent clonedA = oldAgent.getClone();
                //this.agents.add(clonedA);
                this.clonedInstance.addAgent(clonedA);

                // change agent old variables
                ConcurrentLinkedQueue<NodeVariable> x_queue = new ConcurrentLinkedQueue<NodeVariable>();
                for (NodeVariable x : clonedA.getVariables()) {
                    x_queue.add(x);
                }
                while (!x_queue.isEmpty()) {
                    clonedA.changeVariable(x_queue.peek(), this.variables_correspondence_original_cloned.get(x_queue.poll()));
                }

                // change agents old functions
                ConcurrentLinkedQueue<NodeFunction> f_queue = new ConcurrentLinkedQueue<NodeFunction>();
                for (NodeFunction f : clonedA.getFunctions()) {
                    f_queue.add(f);
                }
                while (!f_queue.isEmpty()) {
                    clonedA.changeFunction(f_queue.peek(), this.functions_correspondence_original_cloned.get(f_queue.poll()));
                }
            }

        

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    

    public COP_Instance getOriginalInstance() {
        return originalInstance;
    }

    public void setOriginalInstance(COP_Instance originalInstance) {
        this.originalInstance = originalInstance;
    }

    public COP_Instance getClonedInstance() {
        return clonedInstance;
    }

    public void setClonedInstance(COP_Instance clonedInstance) {
        this.clonedInstance = clonedInstance;
    }

    


    /**
     * Compute the utility function of the original instance
     * @return the utility function value
     * @throws VariableNotSetException if at least one of the original instance variable is not set
     */
    public double getActualOriginalValue() throws VariableNotSetException {
        return this.originalInstance.actualValue();
    }

    /**
     * It updates the values of the original instance variables with respect to the cloned instance ones
     * @return true if everything goes right
     * @throws VariableNotSetException if at least one of the original instance variable is not set
     * @throws NodeVariableNotInMapException if at least one of the cloned instance variable has no entry in the current map pointing its original instance variable
     */
    public boolean setOriginalVariablesValues() throws VariableNotSetException, NodeVariableNotInMapException{
        //Iterator<NodeVariable> it = this.nodevariables.iterator();
        Iterator<NodeVariable> it = this.clonedInstance.getNodevariables().iterator();
        NodeVariable cloned;
        while (it.hasNext()){
            cloned = it.next();
            if (!(this.variables_correspondence_cloned_original.containsKey(cloned))){
                throw new NodeVariableNotInMapException();
            }
            
            this.variables_correspondence_cloned_original.get(cloned).setStateArgument(cloned.getStateArgument());

        }
        return true;
    }


    /**
     * Only for test.
     * Setting debug to 3 should be better.
     * @return a string representing the three hashmap used by this cloner
     */
    public String testString(){
        String test = "";
        test += "variables_correspondence_cloned_original\n";
        for (NodeVariable key : this.variables_correspondence_cloned_original.keySet()) {
            test += "["+key+"] "+ this.variables_correspondence_cloned_original.get(key) +"\n";
        }
        test += "\n\n";
        test += "variables_correspondence_original_cloned\n";
        for (NodeVariable key : this.variables_correspondence_original_cloned.keySet()) {
            test += "["+key+"] "+ this.variables_correspondence_original_cloned.get(key) +"\n";
        }
        test += "\n\n";
        test += "functions_correspondence_original_cloned\n";
        for (NodeFunction key : this.functions_correspondence_original_cloned.keySet()) {
            test += "["+key+"] "+ this.functions_correspondence_original_cloned.get(key) +"\n";
        }

        return test;

    }
}
