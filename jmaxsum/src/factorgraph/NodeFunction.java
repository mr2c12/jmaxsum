package factorgraph;

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
    // represent N(j), that is the set of variable nodes connected to the function j
    protected HashSet<NodeVariable> neighbours;

    private FunctionEvaluator function;

    private static HashMap<Integer, NodeFunction> table = new HashMap<Integer, NodeFunction>();
    private int id;

    private NodeFunction (int id){
        this.id = id;
        this.neighbours = new HashSet<NodeVariable>();
    }

    public void setFunction ( FunctionEvaluator function ){
        this.function = function;
    }

    public FunctionEvaluator getFunction(){
        return this.function;
    }


    public void addNeighbour(Node x){
        if (x instanceof NodeVariable)
            this.neighbours.add((NodeVariable) (x));
    }

    /**
     *
     * @return the number of argumensts of the function
     */
    public int size(){
        return this.neighbours.size();
    }

    public HashSet<NodeVariable> getNeighbour() {
        return this.neighbours;
    }

    public static NodeFunction getNodeFunction(Integer id){
        if (!(NodeFunction.table.containsKey(id))){
            NodeFunction.table.put(id, new NodeFunction(id));
        }
        return NodeFunction.table.get(id);
    }

    public String toString(){
        return "NodeFunction_"+this.id;
    }

    public String stringOfNeighbour() {
        String neighbours = "";
        Iterator<NodeVariable> itnode = this.neighbours.iterator();
        while (itnode.hasNext()) {
            NodeVariable nodeVariable = itnode.next();
            neighbours += nodeVariable+" ";
        }
        return neighbours;
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
}
