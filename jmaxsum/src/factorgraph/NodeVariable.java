package factorgraph;

import exception.OutOfNodeVariableNumberException;
import exception.VariableNotSetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The node variable of the factor graph
 * @author mik
 */
public class NodeVariable implements Node{

    private static HashMap<Integer, NodeVariable> table = new HashMap<Integer, NodeVariable>();

    private static int debug = test.DebugVerbosity.debugNodeVariable;

    // represent M(i), that is the set of function nodes connected to the variable i
    HashSet<NodeFunction> neighbours;
    private int id;

    // max number of nodevariables
    public static final int MAXNODEVARIABLENUMBER = 1000;

    /**
     * arraylist of the possible values of the variable represented by this node
     */
    protected ArrayList<NodeArgument> values;

    protected int index_actual_argument = -1;

    private NodeVariable (int id){
        this.id = id;
        this.values = new ArrayList<NodeArgument>();
        this.neighbours = new HashSet<NodeFunction>();
    }

    public void addValue(NodeArgument v){
        this.values.add(v);
    }

    public void addIntegerValues(int number_of_values){
        for (int i = 0; i < number_of_values; i++) {
            this.addValue(NodeArgument.getNodeArgument(i));
        }
    }

    public ArrayList<NodeArgument> getValues(){
        return this.values;
    }

    public void addNeighbour(Node x){
        if (x instanceof NodeFunction)
            this.neighbours.add((NodeFunction) (x));
    }

    /**
     * @return the number of possible values of this variable
     */
    public int size(){
        return this.values.size();
    }

    /**
     * it gives the position of the argument over the possible values
     * @param node the argument desired
     * @return the position of the argument
     */
    public int numberOfArgument(NodeArgument node){
        if(debug>=3){
            System.out.println("Searching in "+this+" argument "+node);
        }
        return values.indexOf(node);
    }


    public NodeArgument getArgument ( int index ){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.print("[class: "+dclass+" method: " + dmethod+ "] " + "node=" + this+ " index=" + index +" of [");
                for (NodeArgument arg : this.values) {
                    System.out.print(arg+",");
                }
                System.out.println("]");

                System.out.println("---------------------------------------");
        }
        return this.values.get(index);
    }

    public HashSet<NodeFunction> getNeighbour() {
        return this.neighbours;
    }

    public static NodeVariable getNodeVariable(Integer id){
        if (!(NodeVariable.table.containsKey(id))){
            NodeVariable.table.put(id, new NodeVariable(id));
        }
        return NodeVariable.table.get(id);
    }

    public static NodeVariable getNewNextNodeVariable() throws OutOfNodeVariableNumberException{
        for (int id = 1; id < NodeVariable.MAXNODEVARIABLENUMBER; id++){
            if (!NodeVariable.table.containsKey(id)) {
                return NodeVariable.getNodeVariable(id);
            }
        }
        throw new OutOfNodeVariableNumberException();
    }

    void clearValues() {
        this.values = new ArrayList<NodeArgument>();
    }

    public String toString(){
        return "NodeVariable_"+this.id;
    }

    public String stringOfNeighbour() {
        StringBuilder neighbours = new StringBuilder();
        Iterator<NodeFunction> itnode = this.neighbours.iterator();
        while (itnode.hasNext()) {
            NodeFunction nodeVariable = itnode.next();
            neighbours.append(nodeVariable).append(" ");
        }
        return neighbours.toString();
    }

    public boolean equals(Object n) {
        return (n instanceof NodeVariable)
                && (this.getId() == ((NodeVariable)n).getId());
    }

    public int getId() {
        return this.id;
    }

    public void setStateIndex(int index){
        this.index_actual_argument = index;
    }

    public void setStateArgument(NodeArgument n){
        this.index_actual_argument = this.numberOfArgument(n);
    }

    public int getStateIndex(){
        return this.index_actual_argument;
    }

    public NodeArgument getStateArgument() throws VariableNotSetException{
        if (this.index_actual_argument == -1) {
            throw new VariableNotSetException();
        }
        return this.getArgument(this.index_actual_argument);
    }

    public int id() {
        return this.id;
    }


    public int hashCode(){
        return ("NodeVariable_"+this.id).hashCode();
    }

    public NodeVariable getClone() throws OutOfNodeVariableNumberException{
        
        NodeVariable nv = NodeVariable.getNewNextNodeVariable();
        // copy the possible values
        for ( NodeArgument argument : this.getValues() ){
            nv.addValue(argument);
        }
        // copy the neighbours
        for ( NodeFunction function : this.getNeighbour() ) {
            nv.addNeighbour(function);
        }
        return nv;
        
    }

    public void changeNeighbour(NodeFunction oldN, NodeFunction newN) {
        if (this.neighbours.contains(oldN)){
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Function "+oldN+" is present");
                    System.out.println("---------------------------------------");
            }
            
            // oldN is present, swap it with newN
            this.neighbours.remove(oldN);
            this.neighbours.add(newN);
        }
        else {
            if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Function "+oldN+" is not present");
                        System.out.println("---------------------------------------");
            }
        }
    }

    public static void resetIds(){
        NodeVariable.table = new HashMap<Integer, NodeVariable>();
    }

}
