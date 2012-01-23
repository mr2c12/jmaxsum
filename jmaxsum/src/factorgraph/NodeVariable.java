package factorgraph;

import exception.NoMoreValuesException;
import exception.OutOfNodeVariableNumberException;
import exception.VariableNotSetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * The NodeVariable of the Factor Graph.
 * @author Michele Roncalli
 */
public class NodeVariable implements Node {

    /**
     * Static map of NodeVariables created. Like NodeFunction, NodeArgument, Edge.
     */
    private static HashMap<Integer, NodeVariable> table = new HashMap<Integer, NodeVariable>();
    private static int debug = test.DebugVerbosity.debugNodeVariable;
    /**
     * represent M(i), that is the set of function nodes connected to the variable i
     */
    HashSet<NodeFunction> neighbours;
    /**
     * id, old story..
     */
    private int id;
    static int lastId = -1;
    /**
     * arraylist of the possible values of the variable represented by this node
     */
    protected ArrayList<NodeArgument> values;
    /**
     * The index of the actual value of this NodeVariable.
     */
    protected int index_actual_argument = -1;

    private NodeVariable(int id) {
        this.id = id;
        lastId = id;
        this.values = new ArrayList<NodeArgument>();
        this.neighbours = new HashSet<NodeFunction>();
    }

    /**
     * Add a new possible value
     * @param v new NodeArgument for this
     */
    public void addValue(NodeArgument v) {
        this.values.add(v);
    }

    /**
     * Utility that, given a number of values, creates for this variables the corresponding NodeArguments.<br/>
     * E.g. x.addIntegerValues(3) means that x = { 0 | 1 | 2 }
     * @param number_of_values number of integer values for this.
     */
    public void addIntegerValues(int number_of_values) {
        for (int i = 0; i < number_of_values; i++) {
            this.addValue(NodeArgument.getNodeArgument(i));
        }
    }

    public ArrayList<NodeArgument> getValues() {
        return this.values;
    }

    public void addNeighbour(Node x) {
        if (x instanceof NodeFunction) {
            this.neighbours.add((NodeFunction) (x));
        }
    }

    public void removeNeighbours(Collection<NodeFunction> c) {
        for (NodeFunction f : c) {
            this.neighbours.remove(f);
        }
    }

    /**
     * @return the number of possible values of this variable
     */
    public int size() {
        return this.values.size();
    }

    /**
     * it gives the position of the argument over the possible values
     * @param node the argument desired
     * @return the position of the argument
     */
    public int numberOfArgument(NodeArgument node) {
        if (debug >= 3) {
            System.out.println("Searching in " + this + " argument " + node);
        }
        return values.indexOf(node);
    }

    public NodeArgument getArgument(int index) {
        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.print("[class: " + dclass + " method: " + dmethod + "] " + "node=" + this + " index=" + index + " of [");
            for (NodeArgument arg : this.values) {
                System.out.print(arg + ",");
            }
            System.out.println("]");

            System.out.println("---------------------------------------");
        }
        return this.values.get(index);
    }

    public HashSet<NodeFunction> getNeighbour() {
        return this.neighbours;
    }

    public static NodeVariable getNodeVariable(Integer id) {
        if (!(NodeVariable.table.containsKey(id))) {
            NodeVariable.table.put(id, new NodeVariable(id));
        }
        return NodeVariable.table.get(id);
    }

    public static NodeVariable getNewNextNodeVariable() {
        int idn = NodeVariable.lastId + 1;
        while (NodeVariable.table.containsKey(idn)) {
            idn++;
        }
        return NodeVariable.getNodeVariable(idn);
    }

    void clearValues() {
        this.values = new ArrayList<NodeArgument>();
    }

    public String toString() {
        return "NodeVariable_" + this.id;
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
                && (this.getId() == ((NodeVariable) n).getId());
    }

    public int getId() {
        return this.id;
    }

    /**
     * Set the index of actual parameter
     * @param index index of actual parameter
     */
    public void setStateIndex(int index) {
        this.index_actual_argument = index;
    }

    /**
     * Set the actual NodeArgument
     * @param n the actual NodeArgument
     */
    public void setStateArgument(NodeArgument n) {
        this.index_actual_argument = this.numberOfArgument(n);
    }

    public int getStateIndex() {
        return this.index_actual_argument;
    }

    public NodeArgument getStateArgument() throws VariableNotSetException {
        if (this.index_actual_argument == -1) {
            throw new VariableNotSetException();
        }
        return this.getArgument(this.index_actual_argument);
    }

    public void setAnotherRandomValidValue() throws NoMoreValuesException {
        if (this.size() == 1) {
            if (this.index_actual_argument == -1) {
                this.setStateIndex(0);
            } else {
                throw new NoMoreValuesException();
            }
        } else if (this.size() > 1) {
            Random rnd = new Random();
            int oldValue = this.getStateIndex();
            int pos;
            do {
                pos = rnd.nextInt(this.size());
            } while (pos == oldValue);
            this.setStateIndex(pos);
        }
    }

    public void setRandomValidValue() throws NoMoreValuesException {
        if (this.size() == 1) {
            if (this.index_actual_argument == -1) {
                this.setStateIndex(0);
            } else {
                throw new NoMoreValuesException();
            }
        } else if (this.size() > 1) {

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + this + " actual state argument="+this.getStateIndex());
                    System.out.println("---------------------------------------");
            }

            Random rnd = new Random();

            int pos = rnd.nextInt(this.size());

            this.setStateIndex(pos);

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + this + " new state argument="+this.getStateIndex());
                    System.out.println("---------------------------------------");
            }

        }
    }

    public int id() {
        return this.id;
    }

    public int hashCode() {
        return ("NodeVariable_" + this.id).hashCode();
    }

    /**
     * Get a clone of this NodeVariable
     * @return a new this
     * @throws OutOfNodeVariableNumberException
     */
    public NodeVariable getClone() throws OutOfNodeVariableNumberException {

        NodeVariable nv = NodeVariable.getNewNextNodeVariable();
        // copy the possible values
        for (NodeArgument argument : this.getValues()) {
            nv.addValue(argument);
        }
        // copy the neighbours
        for (NodeFunction function : this.getNeighbour()) {
            nv.addNeighbour(function);
        }
        return nv;

    }

    /**
     * Change neighbour oldN to newN
     * @param oldN
     * @param newN
     */
    public void changeNeighbour(NodeFunction oldN, NodeFunction newN) {
        if (this.neighbours.contains(oldN)) {
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Function " + oldN + " is present");
                System.out.println("---------------------------------------");
            }

            // oldN is present, swap it with newN
            this.neighbours.remove(oldN);
            this.neighbours.add(newN);
        } else {
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Function " + oldN + " is not present");
                System.out.println("---------------------------------------");
            }
        }
    }

    public static void resetIds() {
        NodeVariable.table = new HashMap<Integer, NodeVariable>();
        lastId = -1;
    }

    public ArrayList<NodeArgument> getArguments(){
        return this.values;
    }
}
