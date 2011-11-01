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

package boundedMaxSum;

import exception.LengthMismatchException;
import exception.NodeTypeException;
import exception.ParameterNotFoundException;
import factorgraph.Edge;
import factorgraph.FactorGraph;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import misc.DisjointSetEdge;
import misc.DisjointSetNode;
import misc.Utils;
import system.COP_Instance;
import test.DebugVerbosity;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class BoundedMaxSum {

    protected FactorGraph factorgraph;
    private static final int debug = DebugVerbosity.debugBoundedMaxSum;

    public BoundedMaxSum(COP_Instance cop){
        this.factorgraph = cop.getFactorgraph();
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "BoundedMaxSum initialization with factorgraph:\n"+this.factorgraph);
                System.out.println("---------------------------------------");
        }
    }

    public BoundedMaxSum(FactorGraph factorgraph) {
        this.factorgraph = factorgraph;
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "BoundedMaxSum initialization with factorgraph:\n"+this.factorgraph);
                System.out.println("---------------------------------------");
        }
    }

    public FactorGraph getFactorgraph() {
        return factorgraph;
    }

    public void setFactorgraph(FactorGraph factorgraph) {
        this.factorgraph = factorgraph;
    }





    public void weightTheGraph(){
        // for each edge
        for (Edge e : this.factorgraph.getEdges()) {
            try {
                // weight this edge
                this.weightTheEdge(e);
            } catch (ParameterNotFoundException ex) {
                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Failed to weight the edge: "+e);
                        System.out.println("---------------------------------------");
                }
            } catch (LengthMismatchException ex) {
                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Failed to weight the edge: "+e);
                        System.out.println("---------------------------------------");
                }
            } catch (NullPointerException ex) {
                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Failed to weight the edge: "+e);
                        System.out.println("---------------------------------------");
                }
            } catch (NodeTypeException ex) {
                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Failed to weight the edge: "+e);
                        System.out.println("---------------------------------------");
                }
            }
        }
    }


    public void weightTheEdge(Edge e) throws ParameterNotFoundException, LengthMismatchException, NullPointerException, NodeTypeException{

        if (debug>=2) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Trying to weight "+e);
                System.out.println("---------------------------------------");
        }

        NodeVariable x = e.getDest();
        NodeFunction f = e.getSource();
        FunctionEvaluator fe = f.getFunction();

        int fixed_x_position = fe.getParameterPosition(x);
        int x_number_of_values = x.size();

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "x position is:"+fixed_x_position + " and has "+x_number_of_values+" number of values");
                System.out.println("---------------------------------------");
        }

        int maxes_size = 1;
        for (NodeVariable param : fe.getParameters()){
            if (!param.equals(x)){
                maxes_size *= param.size();
            }
        }




        Double[] maxes = new Double[maxes_size];// new double[maxes_size];
        Double[] minis = new Double[maxes_size];

        for (int i = 0; i< maxes.length; i++) {
            maxes[i] = null;
            minis[i] = null;
        }


        if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "maxes is: "+Utils.toString(maxes));
            System.out.println("---------------------------------------");
        }

        int[] arg_size = new int[fe.parametersNumber()];
        for (int index = 0; index < fe.parametersNumber(); index++){
            if (index == fixed_x_position) {
                arg_size[index] = 0;
            }
            else {
                arg_size[index] = fe.getParameter(index).size() -1 ; // this.parameters.get(index).size() - 1;
            }
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "arg_size is: "+Utils.toString(arg_size));
                System.out.println("---------------------------------------");
        }

        int maxes_index_to_change = 0;
        double temp_evaluation;
        // enumerate all the possible arguments
        int[] values = new int[arg_size.length];
        int imax = values.length-1;
        int i=imax;
        while(i>=0){
            while ( values[i] <= arg_size[i] - 1 ) {
                // here we are
                // maximization starts
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Maximization level 1, with values="+Utils.toString(values)+" and maxes_to_change="+maxes_index_to_change);
                        System.out.println("---------------------------------------");
                }
                for (int n_arg = 0; n_arg < x_number_of_values; n_arg++){
                    values[fixed_x_position] = n_arg;
                    temp_evaluation = fe.evaluate(fe.functionArgument(values));
                    // max
                    if (maxes[maxes_index_to_change] == null){
                        maxes[maxes_index_to_change] = temp_evaluation;
                    }
                    else if(maxes[maxes_index_to_change] < temp_evaluation)
                    {
                        maxes[maxes_index_to_change] = temp_evaluation;
                    }
                    // min
                    if (minis[maxes_index_to_change] == null){
                        minis[maxes_index_to_change] = temp_evaluation;
                    }
                    else if(minis[maxes_index_to_change] > temp_evaluation)
                    {
                        minis[maxes_index_to_change] = temp_evaluation;
                    }

                }
                maxes_index_to_change++;
                // maximization ends
                values[i]++;
                for (int j = i+1; j <= imax; j++) {
                    values[j]=0;
                }
                i=imax;
            }
            i--;
        }
        // here we are, again
        // maximization starts
        if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Maximization level 2, with values="+Utils.toString(values)+" and maxes_to_change="+maxes_index_to_change);
                        System.out.println("---------------------------------------");
                }
        for (int n_arg = 0; n_arg < x_number_of_values; n_arg++){
            values[fixed_x_position] = n_arg;
            temp_evaluation = fe.evaluate(fe.functionArgument(values));
            // max
            if (maxes[maxes_index_to_change] == null){
                maxes[maxes_index_to_change] = temp_evaluation;
            }
            else if(maxes[maxes_index_to_change] < temp_evaluation)
            {
                maxes[maxes_index_to_change] = temp_evaluation;
            }
            // min
            if (minis[maxes_index_to_change] == null){
                minis[maxes_index_to_change] = temp_evaluation;
            }
            else if(minis[maxes_index_to_change] > temp_evaluation)
            {
                minis[maxes_index_to_change] = temp_evaluation;
            }
        }
        // maximization ends

        Double[] diff = Utils.opArray(-1,maxes,minis);

        double weight = diff[0];
        for (int j = 1; j < diff.length; j++) {
            if (diff[j]>weight){
                weight = diff[j];
            }
        }

        if (debug>=2) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + e+" has weight "+weight);
                System.out.println("---------------------------------------");
        }

        this.factorgraph.setWeight(e, weight);

    }

    /**
     * Compute the PriorityQueue, changing the values of the weight like:
     * w' = modifier * w
     * @param modifier the int modifier
     * @return the priority queue
     */
    public PriorityQueue<Edge> getEdgeQueue(int modifier){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Edges number: "+this.getFactorgraph().getEdges().size());
                System.out.println("---------------------------------------");
        }

        PriorityQueue<Edge> pq;

        if (modifier == 1){
            pq = new PriorityQueue<Edge>(this.getFactorgraph().getEdges().size(), new EdgeComparator(this.getFactorgraph().getEdgeWeights()));
        }
        else {
            HashMap<Edge, Double> newMap = new HashMap<Edge, Double>();
            for (Edge k : this.getFactorgraph().getEdgeWeights().keySet()){
                newMap.put(k,
                        (this.getFactorgraph().getEdgeWeights().get(k)*modifier)
                        );
            }
            pq = new PriorityQueue<Edge>(this.getFactorgraph().getEdges().size(), new EdgeComparator(newMap));

        }


        for (Edge e:this.getFactorgraph().getEdges()){
            pq.add(e);
        }
        return pq;
    }

    /**
     * It takes the factor graph, weight his edges, compure the maximum spanning
     * tree, remove the edge and update the functions.
     */
    public void letsBound(){
        
        // first: weight the graph!
        this.weightTheGraph();
        
        // now, on the factor graph, compute the maximum spanning tree
        // and save the list of edges to be removed

        // priority queue, the first has biggest cost
        // -1 -> MAXIMUM spanning tree
        PriorityQueue<Edge> edges = this.getEdgeQueue(-1);
        
        // Disjoint sets
        //DisjointSetEdge.initDS(this.getFactorgraph().getEdges());
        DisjointSetNode.initDS(this.getFactorgraph().getNodes());

        HashSet<Edge> edges_to_keep = new HashSet<Edge>();
        HashSet<Edge> edges_to_remove = new HashSet<Edge>();
        
        Edge edge_extracted;


        // mst with kruskal
        while (!edges.isEmpty()) {

            edge_extracted = edges.poll();

            if (!DisjointSetNode.sameDS(
                    edge_extracted.getSource(),
                    edge_extracted.getDest()
                    )){
                // different sets -> keep the edge
                edges_to_keep.add(edge_extracted);
                // join the sets
                DisjointSetNode.union(
                        edge_extracted.getSource(),
                        edge_extracted.getDest()
                        );
            }
            else {
                // same sets -> trash the edge
                edges_to_remove.add(edge_extracted);
            }


        }
        // kruskal is over
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "After Kruskal, these are the edges to be removed:");
                for (Edge er : edges_to_remove){
                    System.out.println("To remove: "+er);
                }
                System.out.println("---------------------------------------");
        }

        // do remove the edges
        this.getFactorgraph().removeEdge(edges_to_remove);

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "After the remove, the factorgraph is:\n"+this.getFactorgraph());
                System.out.println("---------------------------------------");
        }

    }

}
