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

package factorgraph;

import exception.NodeTypeException;
import exception.WeightNotSetException;
import function.FunctionEvaluator;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import misc.TwoKeysHashtable;

/**
 * Class that represents a Factor Graph.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class FactorGraph{

    /**
     * Set of Nodes in the Factor Graph
     */
    protected HashSet<Node> nodes;
    /**
     * NodeVariables
     */
    protected HashSet<NodeVariable> nodevariables;
    /**
     * NodeFunctions
     */
    protected HashSet<NodeFunction> nodefunctions;
    /**
     * Edges
     */
    protected HashSet<Edge> edges;
    /**
     * Weight table. It usually is filled up by BoundedMaxSum
     */
    protected HashMap<Edge, Double> weightTable;
    
    
    public FactorGraph() {
        this.nodes = new HashSet<Node>();
        this.nodevariables = new HashSet<NodeVariable>();
        this.nodefunctions = new HashSet<NodeFunction>();
        //this.weightTable = new TwoKeysHashtable<NodeFunction, NodeVariable, Double>();
        this.weightTable = new HashMap<Edge, Double>();
        this.edges = new HashSet<Edge>();
    }

    public FactorGraph(HashSet<NodeVariable> nodevariables, HashSet<NodeFunction> nodefunctions) {
        this.nodevariables = nodevariables;
        this.nodefunctions = nodefunctions;
        this.nodes = new HashSet<Node>();
        this.edges = new HashSet<Edge>();
        // initialize the nodes set
        for (NodeFunction f : this.nodefunctions){
            this.nodes.add(f);
            for (NodeVariable arg : f.getNeighbour()){
                this.edges.add(Edge.getEdge(f, arg));
            }
        }
        for (NodeVariable x : this.nodevariables){
            this.nodes.add(x);
        }
        //this.weightTable = new TwoKeysHashtable<NodeFunction, NodeVariable, Double>();
        this.weightTable = new HashMap<Edge, Double>();
    }

    /**
     * It adds all the edges (from f to x) to the set of edges of the factorgraph
     */
    public void createTheEdges(){
        for (NodeFunction f : this.nodefunctions){
            for (NodeVariable arg : f.getNeighbour()){
                this.edges.add(Edge.getEdge(f, arg));
            }
        }
    }

    public HashSet<Edge> getEdges(){
        /*HashSet<Edge> edges = new HashSet<Edge>();
        for (NodeFunction f : this.getNodefunctions()){
            for (NodeVariable x : f.getNeighbour()) {
                edges.add(Edge.getEdge(f, x));
            }
        }
        return edges;*/
        return this.edges;
    }




    public boolean addNodeFunction(NodeFunction nf){
        return (this.nodefunctions.add(nf) && this.nodes.add(nf));
    }

    public boolean addNodeVariable(NodeVariable nv){
        return (this.nodevariables.add(nv) && this.nodes.add(nv));
    }

    public boolean addNode(Node n){

        if ( n instanceof NodeVariable) {
            return this.nodes.add(n) && this.nodevariables.add((NodeVariable) n);
        }
        else if (n instanceof NodeFunction) {
            return this.nodes.add(n) && this.nodefunctions.add((NodeFunction) n);
        }
        else {
            // any other type of node shouldn't be added
            // that's the case of NodeArgument
            return false;
        }

    }


    public double getWeight(Node n1, Node n2) throws WeightNotSetException, NodeTypeException{
        if ((n1 instanceof NodeFunction) && (n2 instanceof NodeVariable)) {
            return this.getWeight(
                    Edge.getEdge((NodeFunction)n1, (NodeVariable)n2)
                    );
        }
        else if ((n2 instanceof NodeFunction) && (n1 instanceof NodeVariable)) {
            return this.getWeight(
                    Edge.getEdge((NodeFunction)n2, (NodeVariable)n1)
                    );
        }
        else {
            throw new NodeTypeException("Wrong type of n1:"+n1+" and/or n2:"+n2);
        }
    }

    public boolean isWeighted(Node n1, Node n2) throws NodeTypeException{
        if ((n1 instanceof NodeFunction) && (n2 instanceof NodeVariable)) {
            return this.isWeighted(
                    Edge.getEdge((NodeFunction)n1, (NodeVariable)n2)
                    );
        }
        else if ((n2 instanceof NodeFunction) && (n1 instanceof NodeVariable)) {
            return this.isWeighted(
                    Edge.getEdge((NodeFunction)n2, (NodeVariable)n1)
                    );
        }
        else {
            throw new NodeTypeException("Wrong type of n1:"+n1+" and/or n2:"+n2);
        }
    }

    /**
     * Does edge <i>e</i> has a weight?
     * @param e
     * @return
     */
    public boolean isWeighted(Edge e){
        return this.weightTable.containsKey(e);
    }


   public void setWeight(Node n1, Node n2, double w) throws NodeTypeException{
        if ((n1 instanceof NodeFunction) && (n2 instanceof NodeVariable)) {
            this.setWeight(
                    Edge.getEdge((NodeFunction)n1, (NodeVariable)n2),
                    w
                    );
        }
        else if ((n2 instanceof NodeFunction) && (n1 instanceof NodeVariable)) {
            this.setWeight(
                    Edge.getEdge((NodeFunction)n2, (NodeVariable)n1),
                    w
                    );
        }
        else {
            throw new NodeTypeException("Wrong type of n1:"+n1+" and/or n2:"+n2);
        }
    }


    public HashSet<NodeFunction> getNodefunctions() {
        return nodefunctions;
    }

    public void setNodefunctions(HashSet<NodeFunction> nodefunctions) {
        this.nodefunctions = nodefunctions;
    }

    public HashSet<Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashSet<Node> nodes) {
        this.nodes = nodes;
    }

    public HashSet<NodeVariable> getNodevariables() {
        return nodevariables;
    }

    public void setNodevariables(HashSet<NodeVariable> nodevariables) {
        this.nodevariables = nodevariables;
    }

    public void setWeight(Edge e, double weight) {
        this.weightTable.put(e, weight);
    }

    public double getWeight(Edge e) throws WeightNotSetException{
        if (this.weightTable.containsKey(e)){
            return this.weightTable.get(e);
        }
        else {
            throw new WeightNotSetException();
        }
    }

    public HashMap<Edge, Double> getEdgeWeights(){
        return this.weightTable;
    }


    /**
     * Remove the edges listed in e_list from the Factor Graph.<br/>
     * Removing an edge from f to x means:
     * <ol>
     * <li>remove x to the neighbours of f</li>
     * <li>remove f to the neighbours of x</li>
     * <li>remove e from the edges of the Factor Graph</li>
     * </ol>
     * @param e_list list of edges to remove
     */
    public void removeEdge(Collection<Edge> e_list){
        NodeFunction f;// = e.getSource();
        NodeVariable x;// = e.getDest();
        FunctionEvaluator fe;// = f.getFunction();
        
        HashMap<NodeFunction, LinkedList<NodeVariable>> f_xs = new HashMap<NodeFunction, LinkedList<NodeVariable>>();
        HashMap<NodeVariable, LinkedList<NodeFunction>> x_fs = new HashMap<NodeVariable, LinkedList<NodeFunction>>();

        for (Edge e:e_list){
            f = e.getSource();
            x = e.getDest();


            // add f -> x1,x2,..,xn
            // of variables to be removed from f
            if (!f_xs.containsKey(f)){
                f_xs.put(f, new LinkedList<NodeVariable>());
            }
            f_xs.get(f).add(x);

            // add x -> f1,f2,..fn
            // of functions to be removed from x
            if (!x_fs.containsKey(x)){
                x_fs.put(x, new LinkedList<NodeFunction>());
            }
            x_fs.get(x).add(f);

            // remove e from this factorgraph edges set
            this.edges.remove(e);

        }

        // step 1&2
        // minimize function on remaining args
        // remove NodeVariable from function
        // TODO: change with entryset
        for (NodeFunction key_f : f_xs.keySet()){
            key_f.removeNeighbours(
                    f_xs.get(key_f)
                    );
        }

        // step 3
        // remove NodeFunction from variable
        for (NodeVariable key_x : x_fs.keySet()){
            key_x.removeNeighbours(
                    x_fs.get(key_x)
                    );
        }
    }

    public String toString(){

        StringBuilder string = new StringBuilder();

        for (NodeVariable x : this.getNodevariables()){
            string.append(x).append(" with neighbours:"+x.stringOfNeighbour()+"\n");
        }
        for (NodeFunction f: this.getNodefunctions()){
            string.append(f).append(" with neighbours:"+f.stringOfNeighbour()+"\n");
        }
        for (Edge e: this.getEdges()){
            string.append(e).append("\n");
        }
        return string.toString();
    }

    public int getEdgeNumber(){
        return this.edges.size();
    }
}
