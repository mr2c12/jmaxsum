package factorgraph;

import exception.FunctionEvaluatorNotSetException;
import exception.FunctionNotPresentException;
import exception.OutOfNodeFunctionNumberException;
import exception.VariableNotSetException;
import function.FunctionEvaluator;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import test.DebugVerbosity;

/**
 * The NodeFunction of Factor Graph.<br/>
 * The object that implement the function is a Function Evaluator.<br/>
 * Each NodeFunction has a Function Evaluator.
 * @author Michele Roncalli
 */
public class NodeFunction implements Node{


    /**
     * The FunctionEvaluator that implements the function represented by this NodeFunction.
     */
    private FunctionEvaluator function;

    /**
     * Like Node or Edge. Look at their doc, please.
     */
    private static HashMap<Integer, NodeFunction> table = new HashMap<Integer, NodeFunction>();
    /**
     * id.. what else?
     */
    private int id;

    static int lastId = -1;
    private static final int debug = DebugVerbosity.debugNodeFunction;

    private NodeFunction (int id){
        this.id = id;
        lastId = id;
        //this.neighbours = new HashSet<NodeVariable>();
    }

    public void setFunction ( FunctionEvaluator function ){
        this.function = function;
    }

    public FunctionEvaluator getFunction(){
        return this.function;
    }


    public void addNeighbour(Node x){
         if ((x instanceof NodeVariable)){
            this.function.addParameter((NodeVariable) x);
        }
    }

    public void removeNeighbours(Collection<NodeVariable> c){
        this.function.removeArgs(c);
    }

    /**
     *
     * @return the number of argumensts of the function
     */
    public int size(){
        return this.function.getNeighbour().size();
    }

    /**
     *
     * @return the neighbours of nodeFunction
     */
    public HashSet<NodeVariable> getNeighbour() {
        return this.function.getNeighbour();
    }


    /* this method is hidden cause it can be dangerous: declaring a new nodeFunction
     without setting the proper functionevaluator could lead to misterious error
     * use the method below where functionevaluator is required
    */
/*    public static NodeFunction getNodeFunction(Integer id){
        if (!(NodeFunction.table.containsKey(id))){
            NodeFunction.table.put(id, new NodeFunction(id));
        }
        return NodeFunction.table.get(id);
    }*/

    /**
     * Save the NodeFunction with id and fe.<br/>
     * Used in initialization and for creating a new object.
     * @param id NodeFunction's id
     * @param fe NodeFunction FunctionEvaluator
     * @return the NodeFunction of id and fe requested
     */
    public static NodeFunction putNodeFunction(Integer id, FunctionEvaluator fe){
        if (!(NodeFunction.table.containsKey(id))){
            NodeFunction.table.put(id, new NodeFunction(id));
        }
        NodeFunction.table.get(id).setFunction(fe);
        return NodeFunction.table.get(id);
    }

    /**
     * Just to retrieve an existing NodeFunction
     * @param id the id requested
     * @return the desired NodeFunction
     * @throws FunctionNotPresentException if a NodeFunction with requested id does not exists.
     */
    public static NodeFunction getNodeFunction(Integer id) throws FunctionNotPresentException{
        if (!(NodeFunction.table.containsKey(id))){
            throw new FunctionNotPresentException("id "+id +" does NOT exists. Sorry.");
        }
        return NodeFunction.table.get(id);
    }


    /**
     * Get a new NodeFunction with the first id available.
     * @param fe FunctionEvaluator for the new NodeFunction
     * @return the new NodeFunction
     * @throws OutOfNodeFunctionNumberException if there are more than NodeFunction.MAXNODEFUNCTIONNUMBER NodeFunctions.
     */
    public static NodeFunction getNewNextNodeFunction(FunctionEvaluator fe){
        int idn = lastId + 1;
        while (NodeFunction.table.containsKey(idn)) {
            idn++;
        }
        
        return NodeFunction.putNodeFunction(idn, fe);
    }

    public String toString(){
        return "NodeFunction_"+this.id;
    }

    public String stringOfNeighbour() {
        StringBuilder neighbours = new StringBuilder();
        Iterator<NodeVariable> itnode = this.function.getNeighbour().iterator();
        while (itnode.hasNext()) {
            NodeVariable nodeVariable = itnode.next();
            neighbours.append(nodeVariable).append(" ");
        }
        return neighbours.toString();
    }
/*
    public boolean equals(Object n) {
        return ( n instanceof NodeFunction )
                && (this.getId() == ((NodeFunction)n).getId());
    }
*/


    public int getId() {
        return this.id;
    }

    public int id() {
        return this.id;
    }

    public double actualValue() throws VariableNotSetException {
        return this.function.actualValue();
    }

    public int hashCode(){
        return ("NodeFunction_"+this.id).hashCode();
    }

    public boolean equals (Object o){
        if (!(o instanceof NodeFunction)){
            return false;
        }
        else {
            return this.id() == ((NodeFunction)o).id();
        }
    }

    /**
     *
     * @return a clone of this
     * @throws OutOfNodeFunctionNumberException if there are more than NodeFunction.MAXNODEFUNCTIONNUMBER NodeFunctions.
     */
    public NodeFunction getClone() throws OutOfNodeFunctionNumberException{

       return NodeFunction.getNewNextNodeFunction(this.function.getClone());

    }

    /**
     * Change neighbour oldN with newN
     * @param oldN
     * @param newN
     */
    public void changeNeighbour(NodeVariable oldN, NodeVariable newN) {
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "trying to change "+oldN+" with "+newN);
                System.out.println("---------------------------------------");
        }
        this.function.changeNeighbour(oldN, newN);
    }


    public static void resetIds(){
        NodeFunction.table = new HashMap<Integer, NodeFunction>();
        lastId = -1;
    }

    /**
     * Used to save function to file
     * @return
     */
    public String getTypeOfFe(){
        return this.function.getType();
    }
}