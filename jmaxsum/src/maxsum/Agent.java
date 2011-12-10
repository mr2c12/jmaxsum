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

package maxsum;

import exception.OutOfAgentNumberException;
import exception.PostServiceNotSetException;
import exception.VariableNotSetException;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import messages.MessageQ;
import messages.MessageR;
import messages.PostService;
import misc.Utils;
import olimpo.Athena;
import operation.Operator;

/**
 * Agent that controls variables in a COP problem instance.
 * @author Michele Roncalli < roncallim at gmail dot com >
 */
public class Agent {

    /**
     * Static map to have unique id.
     */
    private static HashMap<Integer, Agent> table = new HashMap<Integer, Agent>();

    static final int debug = test.DebugVerbosity.debugAgent;

    /**
     * Operator that handle max sum
     */
    private Operator op;
    /**
     * PostService to send and retrieve messages. Used by the Nodes.
     */
    private PostService postservice = null;

    private int id;
    static int lastId = -1;

    /**
     * NodeVariables controlled by the agent
     */
    private HashSet<NodeVariable> variables;
    /**
     * NodeFunctions controlled by the agent
     */
    private HashSet<NodeFunction> functions;
    
    private Agent(int id){
        this.id = id;
        lastId = id;
        this.variables = new HashSet<NodeVariable>();
        this.functions = new HashSet<NodeFunction>();

    }

    public static Agent getAgent(int id){
        if (!(Agent.table.containsKey(id))){
            Agent.table.put(id, new Agent(id));
        }
        return Agent.table.get(id);
    }

    public static Agent getNewNextAgent(){
        int id = lastId + 1;
        while (Agent.table.containsKey(id)) {
            id++;
        }
        return Agent.getAgent(id);
    }

    public void setOp(Operator op) {
        this.op = op;
    }

    public PostService getPostservice() {
        return postservice;
    }

    public void setPostservice(PostService postservice) {
        this.postservice = postservice;
    }

    

    public Set<NodeVariable> getVariables(){
        return this.variables;
    }

    public Set<NodeFunction> getFunctions(){
        return this.functions;
    }

    public HashSet<NodeFunction> getFunctionsOfVariable(NodeVariable x){
        return x.getNeighbour();
    }

    public HashSet<NodeVariable> getVariablesOfFunction(NodeFunction f){
        return f.getNeighbour();
    }


    /**
     * Send Q-messages phase.
     * @throws PostServiceNotSetException if ps not set.
     * @return true if at least one message has been updated
     */
    public boolean sendQMessages() throws PostServiceNotSetException{

        if (this.postservice == null){
            throw new PostServiceNotSetException();
        }

        boolean atLeastOneUpdated = false;

        switch (Athena.shuffleMessage){

            case 1:
                Object[] arrayx = this.getVariables().toArray();
                arrayx = Utils.shuffleArrayFY(arrayx);

                for (Object nodeVariable : arrayx) {
                    Object[] arrayf = this.getFunctionsOfVariable(((NodeVariable)nodeVariable)).toArray();
                    arrayf = Utils.shuffleArrayFY(arrayf);

                    // TODO: shuffling is DA WAY
                    
                    for (Object nodeFunction : arrayf) {
                        //atLeastOneUpdated |= this.op.updateQ(variable, function, this.postservice);
                        atLeastOneUpdated |= this.op.updateQ((NodeVariable)nodeVariable, (NodeFunction)nodeFunction, this.postservice);
                    }


                }
                break;

            case 0:
            default:
            //do not shuffle, use them as-is
                Iterator<NodeVariable> iteratorv = this.getVariables().iterator();
                NodeVariable variable; // = null;
                NodeFunction function = null;

                while (iteratorv.hasNext()){
                    variable = iteratorv.next();

                    // gotcha a variable, looking for its functions

                    //Iterator<NodeFunction> iteratorf = this.variableToFunctions.get(variable).iterator();
                    Iterator<NodeFunction> iteratorf = this.getFunctionsOfVariable(variable).iterator();
                    while (iteratorf.hasNext()){

                        function = iteratorf.next();
                        // got variable, function

                        if (debug>=1) {
                                String dmethod = Thread.currentThread().getStackTrace()[1].getMethodName();
                                String dclass = Thread.currentThread().getStackTrace()[1].getClassName();
                                System.out.println("---------------------------------------");
                                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "agent "+this+" preparing Q from " + variable + " to " + function);
                                System.out.println("---------------------------------------");
                        }
                        atLeastOneUpdated |= this.op.updateQ(variable, function, this.postservice);
                        if (debug>=1) {
                                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                                System.out.println("---------------------------------------");
                                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "messageQ updated:");
                                MessageQ mq = this.postservice.readQMessage(variable, function);
                                System.out.println("Sender "+ mq.getSender() + " Receiver " + mq.getReceiver() + " message "+ mq );
                                System.out.println("---------------------------------------");
                        }

                    }

                }
                break;
                //end of case 0



        }
        return atLeastOneUpdated;
    }

    //////////////////////////////
    /**
     * Send R-messages phase.
     * @throws PostServiceNotSetException if ps not set.
     * @return true if at least one message has been updated
     */
    public boolean sendRMessages() throws PostServiceNotSetException{

        if (this.postservice == null){
            throw new PostServiceNotSetException();
        }

        boolean atLeastOneUpdated = false;

        switch (Athena.shuffleMessage){

            case 1:
                Object[] arrayf = this.getFunctions().toArray();
                arrayf = Utils.shuffleArrayFY(arrayf);

                for (Object nodeFunction : arrayf) {
                    Object[] arrayx = this.getVariablesOfFunction((NodeFunction)nodeFunction).toArray();
                    arrayx = Utils.shuffleArrayFY(arrayx);

                    // TODO: shuffling is DA WAY

                    for (Object nodeVariable : arrayx) {
                        //atLeastOneUpdated |= this.op.updateQ(variable, function, this.postservice);
                        atLeastOneUpdated |= this.op.updateR((NodeFunction)nodeFunction, (NodeVariable)nodeVariable, this.postservice);
                    }


                }
                break;

            case 0:
            default:

                Iterator<NodeFunction> iteratorf = this.getFunctions().iterator();
                NodeVariable variable = null;
                NodeFunction function = null;

                while (iteratorf.hasNext()){
                    function = iteratorf.next();

                    // gotcha a variable, looking for its functions

                    //Iterator<NodeFunction> iteratorf = this.variableToFunctions.get(variable).iterator();
                    Iterator<NodeVariable> iteratorv = this.getVariablesOfFunction(function).iterator();
                    while (iteratorv.hasNext()){

                        variable = iteratorv.next();
                        // got variable, function
                        if (debug>=1) {
                                String dmethod = Thread.currentThread().getStackTrace()[1].getMethodName();
                                String dclass = Thread.currentThread().getStackTrace()[1].getClassName();
                                System.out.println("---------------------------------------");
                                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "agent "+this+"  preparing R from " + function + " to " + variable);
                                System.out.println("---------------------------------------");
                        }
                        atLeastOneUpdated |= this.op.updateR(function, variable, this.postservice);
                        if (debug>=1) {
                                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                                System.out.println("---------------------------------------");
                                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "messageR updated:");
                                MessageR mq = this.postservice.readRMessage(function, variable);
                                System.out.println("Sender "+ mq.getSender() + " Receiver " + mq.getReceiver() + " message "+ mq );
                                System.out.println("---------------------------------------");
                        }
                    }

                }
        }
        return atLeastOneUpdated;
    }

    public void setFunctions(HashSet<NodeFunction> functions) {
        this.functions = functions;
    }



    public void setVariables(HashSet<NodeVariable> variables) {
        this.variables = variables;
    }


    /**
     * Compute the Z-messages and set the variables to the value of argmax.
     * @throws PostServiceNotSetException if no post service is set
     */
    public void sendZMessages() throws PostServiceNotSetException{
        
        if (this.postservice == null){
            throw new PostServiceNotSetException();
        }

        switch (Athena.shuffleMessage){

            case 1:
                Object[] arrayx = this.getVariables().toArray();
                arrayx = Utils.shuffleArrayFY(arrayx);

                for (Object nodeVariable : arrayx) {
                    this.op.updateZ((NodeVariable)nodeVariable, postservice);
                }
                break;

            case 0:
            default:
                for (NodeVariable nodeVariable:this.getVariables()){
                    if (debug>=3) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] agent "+this+"" + "preparing to update ZMessage for "+nodeVariable);
                            System.out.println("---------------------------------------");
                    }
                    this.op.updateZ(nodeVariable, postservice);
                }
        }

    }

    public void addNodeVariable(NodeVariable nodevariable) {
        this.variables.add(nodevariable);
    }

    public void addNodeFunction(NodeFunction nodefunction) {
        this.functions.add(nodefunction);
    }

    public String toString(){
        return "Agent_"+this.id;
    }

    public int id() {
        return this.id;
    }

    /**
     * Set the NodeVariable x value as the argMax of Z-message
     * @param x the NodeVariable to set.
     */
    public void setVariableArgumentFromZ(NodeVariable x){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "for the nodevariable "+x+" set the value "+this.op.argOfInterestOfZ(x, postservice));
                System.out.println("---------------------------------------");
        }
        x.setStateIndex(
                this.op.argOfInterestOfZ(x, postservice)
                );
    }

    public void updateVariableValue(){
        for (NodeVariable x : this.getVariables() ){
            this.setVariableArgumentFromZ(x);
        }
    }

    public String variableValueToString(){
        StringBuilder string = new StringBuilder();
        for (NodeVariable x : this.getVariables() ){
            try {
                //string.append("[" + x + "] value: " + x.getStateArgument() + " at position: " + (x.getStateIndex() + 1) + "/" + x.getValues().size() + "\n");
                string.append("[").append(x).append("] value: ").append(x.getStateArgument()).append(" at position: ").append(x.getStateIndex() + 1).append("/").append(x.getValues().size()).append("\n");
            } catch (VariableNotSetException ex) {
                string.append("[").append(x).append("] IS NOT SET\n");
            }
        }
        return string.toString();
    }

    @Override
    public int hashCode(){
        return ("Agent_"+this.id).hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Agent) {
            return ((Agent)o).id() == this.id();
        }
        else {
            return false;
        }
    }

    public Agent getClone() throws OutOfAgentNumberException{

        Agent cloned = Agent.getNewNextAgent();
        cloned.setOp(this.op);
        cloned.setPostservice(this.postservice);

        for (NodeVariable oldx : this.getVariables()) {
            cloned.addNodeVariable(oldx);
        }

        for (NodeFunction oldf : this.getFunctions()) {
            cloned.addNodeFunction(oldf);
        }

        return cloned;

    }

    /**
     * Used in Clone, to change the NodeVariables
     * @param oldv
     * @param newv
     */
    public void changeVariable(NodeVariable oldv, NodeVariable newv) {
        this.variables.remove(oldv);
        this.variables.add(newv);
    }

    /**
     * Used in Clone, to change the NodeFunction
     * @param oldv
     * @param newv
     */
    public void changeFunction(NodeFunction oldv, NodeFunction newv) {
        this.functions.remove(oldv);
        this.functions.add(newv);
    }

    public static void resetIds(){
        table = new HashMap<Integer, Agent>();
        lastId = -1;
    }
}
