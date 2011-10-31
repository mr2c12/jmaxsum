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
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class FactorGraph{

    protected HashSet<Node> nodes;
    protected HashSet<NodeVariable> nodevariables;
    protected HashSet<NodeFunction> nodefunctions;
    // TODO: create the edge list!
    // TODO: edges starts from functions?
    protected TwoKeysHashtable<NodeFunction, NodeVariable, Double> weightTable;
    
    
    
    public FactorGraph() {
        this.nodes = new HashSet<Node>();
        this.nodevariables = new HashSet<NodeVariable>();
        this.nodefunctions = new HashSet<NodeFunction>();
        this.weightTable = new TwoKeysHashtable<NodeFunction, NodeVariable, Double>();
    }

    public FactorGraph(HashSet<NodeVariable> nodevariables, HashSet<NodeFunction> nodefunctions) {
        this.nodevariables = nodevariables;
        this.nodefunctions = nodefunctions;
        this.nodes = new HashSet<Node>();
        // initialize the nodes set
        for (NodeFunction f : this.nodefunctions){
            this.nodes.add(f);
        }
        for (NodeVariable x : this.nodevariables){
            this.nodes.add(x);
        }
        this.weightTable = new TwoKeysHashtable<NodeFunction, NodeVariable, Double>();
    }


    public HashSet<Edge> getEdges(){
        HashSet<Edge> edges = new HashSet<Edge>();
        for (NodeFunction f : this.getNodefunctions()){
            for (NodeVariable x : f.getNeighbour()) {
                edges.add(Edge.getEdge(f, x));
            }
        }
        return edges;
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
            return this.weightTable.get((NodeFunction)n1, (NodeVariable)n2);
        }
        else if ((n2 instanceof NodeFunction) && (n1 instanceof NodeVariable)) {
            return this.weightTable.get((NodeFunction)n2, (NodeVariable)n1);
        }
        else {
            throw new NodeTypeException("Wrong type of n1:"+n1+" and/or n2:"+n2);
        }
    }

    public boolean isWeighted(Node n1, Node n2) throws NodeTypeException{
        if ((n1 instanceof NodeFunction) && (n2 instanceof NodeVariable)) {
            return this.weightTable.containsKey((NodeFunction)n1, (NodeVariable)n2);
        }
        else if ((n2 instanceof NodeFunction) && (n1 instanceof NodeVariable)) {
            return this.weightTable.containsKey((NodeFunction)n2, (NodeVariable)n1);
        }
        else {
            throw new NodeTypeException("Wrong type of n1:"+n1+" and/or n2:"+n2);
        }
    }

    public boolean isWeighted(Edge e) throws NodeTypeException{
        return this.isWeighted(e.getSource(), e.getDest());
    }


   public void setWeight(Node n1, Node n2, double w) throws NodeTypeException{
        if ((n1 instanceof NodeFunction) && (n2 instanceof NodeVariable)) {
            this.weightTable.put((NodeFunction)n1, (NodeVariable)n2,w);
        }
        else if ((n2 instanceof NodeFunction) && (n1 instanceof NodeVariable)) {
            this.weightTable.put((NodeFunction)n2, (NodeVariable)n1,w);
        }
        else {
            throw new NodeTypeException("Wrong type of n1:"+n1+" and/or n2:"+n2);
        }
    }



    /*public double getWeight(NodeFunction f, NodeVariable x) throws WeightNotSetException{
        if (!this.weightTable.containsKey(f, x)) {
            throw new WeightNotSetException();
        }
        return this.weightTable.get(f, x);
    }

    public double getWeight(NodeVariable x, NodeFunction f) throws WeightNotSetException {
        return this.getWeight(f, x);
    }

    public void setWeight(NodeFunction f, NodeVariable x, double w){
        this.weightTable.put(f, x, w);
    }*/






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

    public TwoKeysHashtable<NodeFunction, NodeVariable, Double> getWeightTable() {
        return weightTable;
    }

    public void setWeightTable(TwoKeysHashtable<NodeFunction, NodeVariable, Double> weightTable) {
        this.weightTable = weightTable;
    }


    public void setWeight(Edge e, double weight) throws NodeTypeException {
        this.setWeight(e.getSource(), e.getDest(), weight);
    }

    public double getWeight(Edge e) throws WeightNotSetException, NodeTypeException {
        return this.getWeight(e.getSource(), e.getDest());
    }

    public HashMap<Edge, Double> getEdgeWeights(){
        HashMap<Edge, Double> map = new HashMap<Edge, Double>();
        for (NodeFunction k1 : this.weightTable.firstKeySet()){
            
            for (NodeVariable k2 : this.weightTable.secondKeySet(k1)){
                
                map.put(Edge.getEdge(k1, k2), this.weightTable.get(k1, k2));
                
                
            }
            
            
        }

        return map;
    }


    // TODO: VERY IMPORTANT
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

        }

        // step 1&2
        // minimize function on remaining args
        // remove NodeVariable from function
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
}
