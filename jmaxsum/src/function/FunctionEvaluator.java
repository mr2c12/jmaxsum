/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package function;

import exception.ParameterNotFoundException;
import exception.ValueNotSetException;
import exception.VariableNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import messages.MessageQ;
import misc.Utils;

/**
 * Function evaluator for MaxSum.
 * This is an abstract class that gives the implementation of evaluateMod.
 * Other methods must be implemented.
 *
 * @author Michele Roncalli < roncallim at gmail dot com >
 */
public abstract class FunctionEvaluator {

    final static int debug = test.DebugVerbosity.debugFunctionEvaluator;

    protected ArrayList<NodeVariable> parameters = new ArrayList<NodeVariable>();
    protected HashSet<NodeVariable> parametersset = new HashSet<NodeVariable>();

    protected Double minCost = null;
    protected Double maxCost = null;


    public HashSet<NodeVariable> getNeighbour() {
        return this.parametersset;
    }
    /**
     * set the NodeVariable parameters of the function
     * @param parameters Arraylist of NodeVariable
     */
    public void setParameters(ArrayList<NodeVariable> parameters){
        this.parameters = parameters;
        for(NodeVariable x : parameters){
            this.parametersset.add(x);
        }
    }

    /**
     * add the parameter as the last
     * @param x the parameter to add
     */
    public void addParameter(NodeVariable x){
        if (this.parametersset.add(x)) {
            this.parameters.add(x);
        }
    }

     /**
     * Evalute f over the passed values of its arguments
     * @param params the values of arguments. Note that order is very important
     * @return the value of f
     */
    public abstract double evaluate(NodeArgument[] params) ;


    /**
     * This method evaluate the function when a list of qmessages are given
     * @param modifierTable it links a parameter to the qmessage to apply to the function
     * @return the value of the function
     */
    public  double evaluateMod(NodeArgument[] params, HashMap<NodeVariable, MessageQ> modifierTable)
    {
        if (modifierTable.isEmpty()){
            return this.evaluate(params);
        }
        if(debug>=3){
            System.out.println("Table non empty, there are "+modifierTable.keySet().size()+" keys");

            Iterator<NodeVariable> itv = modifierTable.keySet().iterator();
            while (itv.hasNext()) {
                NodeVariable nodeVariable = itv.next();
                System.out.println("Key: "+nodeVariable+" Value: "+modifierTable.get(itv));
            }
        }
        double cost = this.evaluate(params);

        // seems to work
        Iterator<Entry<NodeVariable, MessageQ>> it = modifierTable.entrySet().iterator();
                //modifierTable.keySet().iterator();
        NodeVariable nodeVariable = null;
        int indexOfModifier = -15;
        while (it.hasNext()){
            nodeVariable = it.next().getKey();
            if(debug>=3){
                System.out.println("NodeVariable is "+nodeVariable);
            }

            try {

                indexOfModifier = nodeVariable.numberOfArgument(params[this.getParameterPosition(nodeVariable)]);
            
                cost += modifierTable.get(nodeVariable).getValue(indexOfModifier);

            } catch (ParameterNotFoundException ex) {
                // do nothing?
            }

        }
        return cost;
    }

    /**
     * Returns the number of parameters used by the function
     * @returnthe number of parameters used by the function
     */
    public int parametersNumber(){
        return this.parameters.size();
    }

    /**
     *
     * @param index the position of the parameter
     * @return the parameter at the index-th position
     */
    public NodeVariable getParameter(int index){
        return this.parameters.get(index);
    }
    /**
     *
     * @param x the parameter whose position is desired
     * @return the position of the input x
     */
    public int getParameterPosition ( NodeVariable x) throws ParameterNotFoundException{
        if(debug>=3){
            System.out.println("Searching for "+ x +" in FunctionEvaluatorMaxSum");
            System.out.print("There are "+ this.parameters.size()+ " parameters: ");
            for (int i = 0; i < this.parameters.size(); i++) {
                System.out.print(this.parameters.get(i) + " ");
            }
            System.out.println("");
            System.out.println("I'm gonna return "+this.parameters.indexOf(x));
        }
        int position = this.parameters.indexOf(x);
        if (position >= 0) {
            return position;
        }
        else {
            throw new ParameterNotFoundException();

        }
    }
    /**
     * Return the array of parameters. Remember that order MATTERS!
     * @return arraylist of the parameters of the functions
     */
    public ArrayList<NodeVariable> getParameters(){
        return this.parameters;
    }

    /**
     * Computes the value of the function on the actual value of its parameters. If there is at least a parameter not set (that is, a nodevariable without a nodeargument set) throws an exception.
     * @return the cost of the funtion for the actual parameters values.
     * @throws VariableNotSetException if there is at least one parameter still not set
     */
    public double actualValue() throws VariableNotSetException{
        NodeArgument[] params = new NodeArgument[this.parametersNumber()];
        for(NodeVariable param : this.parameters) {

            params[this.parameters.indexOf(param)] = param.getStateArgument();

        }
        return this.evaluate(params);
    }

    /**
     * Create an association between an array of arguments and the cost.
     * It's like setting f(1,2,2)=3
     * @param params the values of parameters
     * @param cost the cost corresponding to these parameters
     */
    public abstract void addParametersCost(NodeArgument[] params, double cost);

    /**
     * Return an hashmap that represents all the possible cost of the function
     * @return a HashMap<NodeArgument[], Double> where the key is an array of parameters and the value
     * is the corresponding cost
     */
    public abstract HashMap<NodeArgument[], Double> getParametersCost();

     /**
     * Create a string representing the function. This string is used when saving the instance to a file
     * @return string representation of this function and its values
     */
    public abstract String toStringForFile();

    /**
     *
     * @return number of associations parameters->cost
     */
    public abstract int entryNumber();

    /**
     *
     * @return the minimum cost
     */
    public double minCost() throws ValueNotSetException{
        if (this.minCost == null) {
            throw new ValueNotSetException("MinCost not set");
        }
        return this.minCost;
    }

    /**
     *
     * @return the maximum cost
     */
    public double maxCost() throws ValueNotSetException{
        if (this.maxCost == null) {
            throw new ValueNotSetException("MaxCost not set");
        }
        return this.maxCost;
    }

    /**
     *
     * @return the array of all the possible costs of this function
     */
    public abstract ArrayList<Double> getCostValues();

    /**
     * reset the min cost and the min cost
     */
    public void resetMaxMinCosts(){
        ArrayList<Double> costs = this.getCostValues();
        if (!costs.isEmpty()){
            Collections.sort(costs);
            this.minCost = costs.get(0);
            this.maxCost = costs.get(costs.size()-1);
        }
    }

    public double getDelta() throws ValueNotSetException{
        return this.maxCost() - this.minCost();
    }

    public abstract FunctionEvaluator getClone();


    public void changeNeighbour(NodeVariable oldN, NodeVariable newN) {
        

        /* COMPLETELY WRONG, IT CHANGES THE ORDER!
         if (this.parameters.contains(oldN)){
            // oldN is present, swap it with newN
            this.parameters.remove(oldN);
            this.parameters.add(newN);
        }*/
        
        if (this.parameters.contains(oldN)){
            this.parameters.set(this.parameters.indexOf(oldN), newN);
        }


        if (this.parametersset.contains(oldN)){
            // oldN is present, swap it with newN
            this.parametersset.remove(oldN);
            this.parametersset.add(newN);
        }

    }


    public NodeArgument[] functionArgument (int[] argumentsNumber){
        NodeArgument[] fzArgument = new NodeArgument[argumentsNumber.length];
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Looking for "+argumentsNumber.length+" arguments: "+ Utils.toString(argumentsNumber));
                System.out.println("---------------------------------------");
        }
        for (int i = 0; i < fzArgument.length; i++) {
            fzArgument[i] = this.getParameter(i).getArgument(argumentsNumber[i]);
        }
        return fzArgument;
    }



    public double[] maxFfixedX(NodeVariable x) throws ParameterNotFoundException{
        if (!this.parameters.contains(x)) {
            throw new ParameterNotFoundException(this+" does NOT contain "+x);
        }

        int fixed_x_position = this.getParameterPosition(x);
        int x_number_of_values = x.size();

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "x position is:"+fixed_x_position + " and has "+x_number_of_values+" number of values");
                System.out.println("---------------------------------------");
        }
        
        int maxes_size = 1;
        for (NodeVariable param : this.parameters){
            if (!param.equals(x)){
                maxes_size *= param.size();
            }
        }




        double[] maxes = new double[maxes_size];
        // TODO: initialization?
        for (int i = 0; i< maxes.length; i++) {
            maxes[i] = -100000;
        }


        if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "maxes is: "+Utils.toString(maxes));
            System.out.println("---------------------------------------");
        }

        int[] arg_size = new int[this.parametersNumber()];
        for (int index = 0; index < this.parameters.size(); index++){
            if (index == fixed_x_position) {
                arg_size[index] = 0;
            }
            else {
                arg_size[index] = this.parameters.get(index).size() - 1;
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
                    temp_evaluation = this.evaluate(this.functionArgument(values));
                    if (maxes[maxes_index_to_change] < temp_evaluation ){
                        maxes[maxes_index_to_change] = temp_evaluation;
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
            temp_evaluation = this.evaluate(this.functionArgument(values));
            if (maxes[maxes_index_to_change] < temp_evaluation ){
                maxes[maxes_index_to_change] = temp_evaluation;
            }
        }
        // maximization ends






        return maxes;

    }

}
