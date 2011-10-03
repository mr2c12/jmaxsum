package factorgraph;

import exception.FunctionEvaluatorNotSetException;
import exception.VariableNotSetException;
import function.FunctionEvaluator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The node function in the factor graph
 * @author mik
 */
public class NodeFunction implements Node{


    // order is represented in functionevaluator
    // the set of neighbours, representing N(j), is retrieved from functionevaluator


    private FunctionEvaluator function;

    private static HashMap<Integer, NodeFunction> table = new HashMap<Integer, NodeFunction>();
    private int id;

    private NodeFunction (int id){
        this.id = id;
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

    public static NodeFunction getNodeFunction(Integer id, FunctionEvaluator fe){
        if (!(NodeFunction.table.containsKey(id))){
            NodeFunction.table.put(id, new NodeFunction(id));
        }
        NodeFunction.table.get(id).setFunction(fe);
        return NodeFunction.table.get(id);
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

    public boolean equals(Node n) {
        return ( n instanceof NodeFunction )
                && (this.getId() == n.getId());
    }

    public int getId() {
        return this.id;
    }

    public int id() {
        return this.id;
    }

    public boolean equals(NodeVariable n){
        return this.id == n.id();
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
}
