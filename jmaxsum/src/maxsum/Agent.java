

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
import operation.Operator;

/**
 * Agent that controls variables
 * @author Michele Roncalli < roncallim at gmail dot com >
 */
public class Agent {

    private static HashMap<Integer, Agent> table = new HashMap<Integer, Agent>();

    static final int debug = test.DebugVerbosity.debugAgent;

    private Operator op;
    private PostService postservice = null;

    private int id;
    public static final int MAXAGENTNUMBER = 1000;

    private HashSet<NodeVariable> variables;
    private HashSet<NodeFunction> functions;
    
    private Agent(int id){
        this.id = id;
        this.variables = new HashSet<NodeVariable>();
        this.functions = new HashSet<NodeFunction>();

    }

    public static Agent getAgent(int id){
        if (!(Agent.table.containsKey(id))){
            Agent.table.put(id, new Agent(id));
        }
        return Agent.table.get(id);
    }

    public static Agent getNewNextAgent() throws OutOfAgentNumberException{
        for (int id = 1; id < Agent.MAXAGENTNUMBER; id++){
            if (!Agent.table.containsKey(id)) {
                return Agent.getAgent(id);
            }
        }
        throw new OutOfAgentNumberException();
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


    public void sendQMessages() throws PostServiceNotSetException{

        if (this.postservice == null){
            throw new PostServiceNotSetException();
        }

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
                this.op.updateQ(variable, function, this.postservice);
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
    }

    //////////////////////////////
    public void sendRMessages() throws PostServiceNotSetException{

        if (this.postservice == null){
            throw new PostServiceNotSetException();
        }

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
                this.op.updateR(function, variable, this.postservice);
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

    public void setFunctions(HashSet<NodeFunction> functions) {
        this.functions = functions;
    }



    public void setVariables(HashSet<NodeVariable> variables) {
        this.variables = variables;
    }


    /**
     * Compute the Z-messages and set the variables to the value
     * @throws PostServiceNotSetException if no post service is set
     */
    public void sendZMessages() throws PostServiceNotSetException{

        if (this.postservice == null){
            throw new PostServiceNotSetException();
        }


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

    public void setVariableArgumentFromZ(NodeVariable x){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "for the nodevariable "+x+" set the value "+this.op.argMaxZ(x, postservice));
                System.out.println("---------------------------------------");
        }
        x.setStateIndex(
                this.op.argMaxZ(x, postservice)
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


    public void changeVariable(NodeVariable oldv, NodeVariable newv) {
        this.variables.remove(oldv);
        this.variables.add(newv);
    }

    public void changeFunction(NodeFunction oldv, NodeFunction newv) {
        this.functions.remove(oldv);
        this.functions.add(newv);
    }

}
