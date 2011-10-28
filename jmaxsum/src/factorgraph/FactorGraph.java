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
import java.util.HashSet;
import misc.TwoKeysHashtable;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class FactorGraph {

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

    // TODO: please check
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




}
