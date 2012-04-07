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
import exception.WeightNotSetException;
import factorgraph.Edge;
import factorgraph.FactorGraph;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import misc.DisjointSetNode;
import misc.Utils;
import system.COP_Instance;
import test.DebugVerbosity;

/**
 * Class that implements the "bounded" phase of Bounded Max Sum.<br/>
 * It takes a factor graph, weights each edge and computes the maximum spanning tree.<br/>
 * Once that a solution is available, this class can compute the Approximation Ratio, too.<br/>
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class BoundedMaxSum {

    /**
     * The original Factor Graph, on which a maximum spanning tree is going to be estimated.
     */
    protected FactorGraph factorgraph;
    private static final int debug = DebugVerbosity.debugBoundedMaxSum;
    /**
     * The sum of weights of removed edges.<br/>
     * It is null until the factor graph is weighted.
     */
    private Double weight_removed = null;

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


    public double getB(){
        return this.weight_removed;
    }


    /**
     * Evalutes the weight of each edge in the Factor Graph of this instance.<br/>
     * It simply call method weightTheEdge on each edge.
     */
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

    /**
     * Weight the edge.<br/>
     * It's a very complex to understand method. It's worth to take a look to the
     * comments in the code to understand its behaviour.
     * @param e the edge whose weight is going to be evaluated.
     * @throws ParameterNotFoundException
     * @throws LengthMismatchException
     * @throws NullPointerException
     * @throws NodeTypeException
     */

    public void weightTheEdge(Edge e) throws ParameterNotFoundException, LengthMismatchException, NullPointerException, NodeTypeException{

        if (debug>=2) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Trying to weight "+e);
                System.out.println("---------------------------------------");
        }

        // destination node of edge e
        NodeVariable x = e.getDest();
        // source node
        NodeFunction f = e.getSource();
        // function evaluator related to f
        FunctionEvaluator fe = f.getFunction();

        // position of parameter x in fe
        int fixed_x_position = fe.getParameterPosition(x);
        // number of possible values of x (its domain)
        int x_number_of_values = x.size();

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "x position is:"+fixed_x_position + " and has "+x_number_of_values+" number of values");
                System.out.println("---------------------------------------");
        }

        // this is the length of array of max (or min) values
        int maxes_size = 1;
        for (NodeVariable param : fe.getParameters()){
            if (!param.equals(x)){
                maxes_size *= param.size();
            }
        }


        

        // used for maximization of f over its arguments, except x
        double[] maxes = new double[maxes_size];// new double[maxes_size];
        // used for minimization of f over its arguments, except x
        double[] minis = new double[maxes_size];
        // both initializated to null (special value that represent -infinite or +infinite)
        for (int i = 0; i< maxes.length; i++) {
            maxes[i] = Double.NEGATIVE_INFINITY;
            minis[i] = Double.POSITIVE_INFINITY;
        }


        if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "maxes is: "+Utils.toString(maxes));
            System.out.println("---------------------------------------");
        }

        // arg_size[i] is the number of possible values of i-th parameter of fe
        int[] arg_size = new int[fe.parametersNumber()];
        for (int index = 0; index < fe.parametersNumber(); index++){
            if (index == fixed_x_position) {
                arg_size[index] = 0;
            }
            else {
                // the -1 because:
                // x = { a | b | c }
                // x.size() = 3
                // x[0] = a, .. ,x[2] =b
                // so arg_size[position_of_x] should be 2 (you'll understand why later)
                arg_size[index] = fe.getParameter(index).size() -1 ;
            }
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "arg_size is: "+Utils.toString(arg_size));
                System.out.println("---------------------------------------");
        }

        // index to change (to valuate if lower or greater) in max (or min) array
        int maxes_index_to_change = 0;
        double temp_evaluation;
        // enumerate all the possible arguments
        // values = { 1, 3, 2 }
        // fe is a function of x1,x2,x3
        // x_i = {a,b,c,d,e} foreach i
        // e.g. x1[0] = a
        // fe.evaluate(fe.functionArgument(values)) means:
        //      fe.functionArgument(values) = { b, d, c }
        //      fe.evaluate( { b, d, c } );
        int[] values = new int[arg_size.length];

        // these lines of code is a algorithm that compute all the possible array, given a maximum value foreach position.
        // look at class util for a clearer explanation.
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
                    if(maxes[maxes_index_to_change] < temp_evaluation)
                    {
                        maxes[maxes_index_to_change] = temp_evaluation;
                    }
                    // min
                    if(minis[maxes_index_to_change] > temp_evaluation)
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
            if(maxes[maxes_index_to_change] < temp_evaluation)
            {
                maxes[maxes_index_to_change] = temp_evaluation;
            }
            // min
            if(minis[maxes_index_to_change] > temp_evaluation)
            {
                minis[maxes_index_to_change] = temp_evaluation;
            }
        }
        // maximization ends
        
        double[] diff = Utils.opArray(-1,maxes,minis);

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
     * Compute the PriorityQueue, changing the values of the weight like:<br/>
     * w' = modifier * w<br/>
     * With -1, the queue's head has the biggest weight.
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
     * tree, remove the edge and update the functions.<br/>
     * It compute the Approximation Ratio, too.
     */
    public void letsBound(){
        
        // first: weight the graph!
        this.weightTheGraph();
        // initialize the removed weight
        this.weight_removed = 0.0;
        
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
                if (edges_to_remove.add(edge_extracted)){
                    try {
                        // the edge is added to the to-be-removed set, so:
                        this.weight_removed += this.getFactorgraph().getWeight(edge_extracted);
                    } catch (WeightNotSetException ex) {
                        // do not add anything
                        // is it possible?!
                    }
                }
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

    /**
     * Approximation ratio for this instance.
     * Computed as the formula:
     * rho_{FG} = ( V^{m} + B + V ) / V
     * where:
     * V^{m}    is the optimal solution to the tree structured constraint network
     * B        is the sum of weights of removed edges
     * V        is the approximate solution on the original graph
     * 
     * @param solutionOnTree is the optimal solution to the tree structured constraint network
     * @param solutionOnOriginalGraph is the approximate solution on the original graph
     * @return the approximation ratio for this instance
     * @throws WeightNotSetException if the sum of weights of removed edges is not set
     */
    public double getApproximationRatio(double solutionOnTree, double solutionOnOriginalGraph) throws WeightNotSetException{
        if (this.weight_removed == null) {
            throw new WeightNotSetException("Unable to compute the approximation: no weight-removed set. This is not supposed to happen. Did you run letsBound() before calling getApproximationRatio()?");
        }
        double ratio = ( ( solutionOnTree + this.weight_removed - solutionOnOriginalGraph) / solutionOnOriginalGraph);
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Approximation ratio with:");
                System.out.println("solutionOnTree="+solutionOnTree);
                System.out.println("solutionOnOriginalGraph="+solutionOnOriginalGraph);
                System.out.println("B="+this.weight_removed);
                System.out.println("AR="+ratio);
                System.out.println("---------------------------------------");
        }
        return ratio;
    }

        /**
     * Approximation ratio for this instance.<br/>
     * Computed as the formula:<br/>
     * rho_{FG} = 1 + ( V^{m} + B + V ) / V<br/>
     * where:<br/>
     * V^{m}    is the optimal solution to the tree structured constraint network<br/>
     * B        is the sum of weights of removed edges<br/>
     * V        is the approximate solution on the original graph<br/>
     *<br/>
     * @param solutionOnTree is the optimal solution to the tree structured constraint network
     * @param solutionOnOriginalGraph is the approximate solution on the original graph
     * @return the approximation ratio for this instance
     * @throws WeightNotSetException if the sum of weights of removed edges is not set
     */
    public double getApproximationRatio_OnePlus(double solutionOnTree, double solutionOnOriginalGraph) throws WeightNotSetException{
        if (this.weight_removed == null) {
            throw new WeightNotSetException("Unable to compute the approximation: no weight-removed set. This is not supposed to happen. Did you run letsBound() before calling getApproximationRatio()?");
        }
        double ratio = 1 + this.getApproximationRatio(solutionOnTree, solutionOnOriginalGraph);
        return ratio;
    }

    /**
     * Perform a the sanity check on the final solutions:<br/>
     * V^{m} <= V <= V^{m} + B<br/>
     * where:<br/>
     * V^{m}    is the optimal solution to the tree structured constraint network<br/>
     * B        is the sum of weights of removed edges<br/>
     * V        is the approximate solution on the original graph<br/>
     * <br/>
     * @param solutionOnTree is the optimal solution to the tree structured constraint network
     * @param solutionOnOriginalGraph is the approximate solution on the original graph
     * @return true if the check is passed
     * @throws WeightNotSetException if the sum of weights of removed edges is not set
     */
    public boolean sanityCheckOnSolution(double solutionOnTree, double solutionOnOriginalGraph) throws WeightNotSetException{
        if (this.weight_removed == null) {
            throw new WeightNotSetException("Unable to compute the approximation: no weight-removed set. This is not supposed to happen. Did you run letsBound() before calling getApproximationRatio()?");
        }
        boolean result = true;
        // first check:
        // V^{m} <= V
        result &= ( solutionOnTree <= solutionOnOriginalGraph );
        // second check:
        // V <= V^{m} + B
        result &= ( solutionOnOriginalGraph <= ( solutionOnTree + this.weight_removed ) );
        return result;
    }
}
