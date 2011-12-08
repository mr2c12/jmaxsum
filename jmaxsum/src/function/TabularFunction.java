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
package function;

import exception.ParameterNotFoundException;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import messages.MessageQ;
import misc.Utils;

/**
 * Tabular Function, implementation of abstract Function Evaluator.<br/>
 * Used for discrete functions.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class TabularFunction extends FunctionEvaluator {

    final static int debug = test.DebugVerbosity.debugTabularFunction;
    /**
     * Correspondence between parameters and function values.<br/>
     * The parameters are represented in a String.
     */
    protected HashMap<String, Double> costTable;

    public TabularFunction() {
        this.costTable = new HashMap<String, Double>();
    }

    /**
     * Save the function value for NodeArgument[] of parameter.<br/>
     * The params become the key of the cost table. A string is builded, where each parameter is followed by a ";".
     * @param params the array of NodeArgument
     * @param cost the cost for the params
     */
    public void addParametersCost(NodeArgument[] params, double cost) {
        StringBuilder key = new StringBuilder();
        key.append("");
        for (int i = 0; i < params.length; i++) {
            key.append(params[i].toString()).append(";");
        }
        this.costTable.put(key.toString(), cost);

        // set the min and the max
        if ((cost!= Double.NEGATIVE_INFINITY) && (cost!= Double.POSITIVE_INFINITY) ){
            if (this.minCost == null || cost < this.minCost) {
                this.minCost = cost;
            }
            if (this.maxCost == null || cost > this.maxCost) {
                this.maxCost = cost;
            }
        }
    }

    @Override
    public double evaluate(NodeArgument[] params) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            key.append(params[i].toString()).append(";");
        }
        return this.costTable.get(key.toString());
    }

    /**
     * How much values does this function have?
     * @return
     */
    public int entryNumber() {
        return this.costTable.size();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        StringBuilder append = string.append("Function evaluator with " + this.entryNumber() + " entries\n");
        string.append("NodeVariable used: ");
        /*Iterator<NodeVariable> it = this.parameters.iterator();
        while (it.hasNext()) {
        NodeVariable nodeVariable = it.next();
        StringBuilder append1 = string.append(nodeVariable + " ");
        }*/
        for (int i = 0; i < this.parameters.size(); i++) {
            StringBuilder append1 = string.append(this.parameters.get(i) + " ");
        }
        string.append("\n");
        /*Iterator<String> keyit = this.costTable.keySet().iterator();
        while (keyit.hasNext()) {
        String key = keyit.next();
        string += "["+key+"] "+this.costTable.get(key)+"\n";
        }*/
        HashMap<NodeArgument[], Double> table = this.getParametersCost();
        Iterator<NodeArgument[]> ita = table.keySet().iterator();
        while (ita.hasNext()) {
            NodeArgument[] nodeArguments = ita.next();
            string.append("[ ");
            for (int i = 0; i < nodeArguments.length; i++) {
                string.append(nodeArguments[i]).append(" ");
            }
            string.append("] ").append(table.get(nodeArguments)).append("\n");
        }

        return string.toString();
    }

    public HashMap<NodeArgument[], Double> getParametersCost() {
        HashMap<NodeArgument[], Double> table = new HashMap<NodeArgument[], Double>();
        Iterator<String> its = this.costTable.keySet().iterator();
        String string;
        StringTokenizer t;
        LinkedList<NodeArgument> argumentlist;
        while (its.hasNext()) {
            string = its.next();
            t = new StringTokenizer(string, ";");
            argumentlist = new LinkedList<NodeArgument>();
            while (t.hasMoreTokens()) {
                argumentlist.add(NodeArgument.getNodeArgument(t.nextToken()));
            }
            NodeArgument[] array = argumentlist.toArray(new NodeArgument[0]);
            table.put(array, this.costTable.get(string));
        }


        return table;
    }

    public NodeArgument[] getArgsFromKey(String string){
        StringTokenizer t = new StringTokenizer(string, ";");
        NodeArgument[] args = new NodeArgument[this.parametersNumber()];
        int index = 0;
        while (t.hasMoreTokens()) {
            args[index] = NodeArgument.getNodeArgument(t.nextToken());
            index++;
        }
        return args;
    }

    public String toStringForFile() {
        StringBuilder string = new StringBuilder();
        HashMap<NodeArgument[], Double> table = this.getParametersCost();
        Iterator<NodeArgument[]> ita = table.keySet().iterator();
        while (ita.hasNext()) {
            NodeArgument[] nodeArguments = ita.next();
            string.append("F ");
            for (int i = 0; i < nodeArguments.length; i++) {
                string.append(nodeArguments[i]).append(" ");
            }
            string.append(table.get(nodeArguments)).append("\n");
        }

        return string.toString();
    }

    @Override
    public ArrayList<Double> getCostValues() {
        return new ArrayList<Double>(this.costTable.values());
    }

    @Override
    public FunctionEvaluator getClone() {
        TabularFunction clonedT = new TabularFunction();
        for (NodeVariable parameter : this.getParameters()) {
            clonedT.addParameter(parameter);
        }

        if (debug >= 2) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "original parameters:");
            System.out.print("Originals: ");
            for (int i = 0; i < this.parameters.size(); i++) {
                System.out.print(this.parameters.get(i) + ",");
            }
            System.out.println("\ncloned:");
            System.out.print("Cloned: ");
            for (int i = 0; i < clonedT.getParameters().size(); i++) {
                System.out.print(clonedT.getParameters().get(i) + ",");
            }
            System.out.println("---------------------------------------");
        }

        for (NodeArgument[] arguments : this.getParametersCost().keySet()) {
            clonedT.addParametersCost(arguments, this.evaluate(arguments));
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.print("[class: " + dclass + " method: " + dmethod + "] " + "adding for [");
                for (NodeArgument arg : arguments) {
                    System.out.print(arg + ",");
                }
                System.out.println("] {" + this.evaluate(arguments) + "}");

                System.out.print("Now is: [");
                for (NodeArgument arg : arguments) {
                    System.out.print(arg + ",");
                }
                System.out.println("] {" + clonedT.evaluate(arguments) + "}");
                if (this.evaluate(arguments) != clonedT.evaluate(arguments)) {
                    System.exit(-1);
                }
                System.out.println("---------------------------------------");
            }
        }

        return clonedT;
    }

    @Override
    public void clearCosts() {
        this.costTable = new HashMap<String, Double>();
    }

    @Override
    /** {@inheritDoc} */
    public double[] minimizeWRT(NodeVariable x) throws ParameterNotFoundException {
        return this.maxminWRT("min", x, null);
    }

    @Override
    /** {@inheritDoc} */
    public double[] minimizeWRT(NodeVariable x, HashMap<NodeVariable, MessageQ> modifierTable) throws ParameterNotFoundException {
        return this.maxminWRT("min", x, modifierTable);
    }

    @Override
    /** {@inheritDoc} */
    public double[] maximizeWRT(NodeVariable x) throws ParameterNotFoundException {
        return this.maxminWRT("max", x, null);
    }

/*
    private double[] maxminWRT(String op, NodeVariable x) throws ParameterNotFoundException {
        int xIndex = this.getParameterPosition(x);

            // number of parameters of f
            int fzParametersNumber = this.parametersNumber();
            // at the i-th position there is the number of possible values of the i-th argument of f
            // at the position of x, there will be only one value available
            int[] numberOfValues = new int[fzParametersNumber];
            // the array filled with variable value positions that's gonna be evaluated
            int[] functionArgument = new int[fzParametersNumber];
            // maximization array, wrt x possible values

            double[] maxes = new double[x.size()];

            for (int i = 0; i < fzParametersNumber; i++) {
                numberOfValues[i] = this.getParameter(i).size();
            }
            numberOfValues[xIndex] = 1;

            ///////////////////////
            int imax = numberOfValues.length - 1;
            int i = imax;
            while (i >= 0) {
                while (functionArgument[i] < numberOfValues[i] - 1) {
                    // here an array ready for being evaluated
                    if (op.equals("max")){
                        maxes = this.maximize(maxes, functionArgument, x, xIndex);
                    }
                    functionArgument[i]++;
                    for (int j = i + 1; j <= imax; j++) {
                        functionArgument[j] = 0;
                    }
                    i = imax;
                }
                i--;
            }
            // here an array ready for being evaluated
            if (op.equals("max")){
                maxes = this.maximize(maxes, functionArgument, x, xIndex);
            }
            //////////////////////

        return maxes;
    }
*/
    @Override
    /** {@inheritDoc} */
    public double[] maximizeWRT(NodeVariable x, HashMap<NodeVariable, MessageQ> modifierTable) throws ParameterNotFoundException {
        return maxminWRT("max", x, modifierTable);

    }

    public double[] maxminWRT(String op, NodeVariable x, HashMap<NodeVariable, MessageQ> modifierTable) throws ParameterNotFoundException {

            int xIndex = this.getParameterPosition(x);

            // number of parameters of f
            int fzParametersNumber = this.parametersNumber();
            // at the i-th position there is the number of possible values of the i-th argument of f
            // at the position of x, there will be only one value available
            int[] numberOfValues = new int[fzParametersNumber];
            // the array filled with variable value positions that's gonna be evaluated
            int[] functionArgument = new int[fzParametersNumber];
            // maximization array, wrt x possible values

            double[] maxes = new double[x.size()];
            for (int index = 0; index < maxes.length; index++) {
                if (op.equals("max")){
                    maxes[index] = Double.NEGATIVE_INFINITY;
                }
                else if (op.equals("min")){
                    maxes[index] = Double.POSITIVE_INFINITY;
                }

            }

            for (int i = 0; i < fzParametersNumber; i++) {
                numberOfValues[i] = this.getParameter(i).size();
            }
            numberOfValues[xIndex] = 1;

            ///////////////////////
            int imax = numberOfValues.length - 1;
            int i = imax;
            while (i >= 0) {
                while (functionArgument[i] < numberOfValues[i] - 1) {
                    // here an array ready for being evaluated
                    /*if (op.equals("max")){
                        if (modifierTable == null){
                            maxes = this.maximize(maxes, functionArgument, x, xIndex);
                        }
                        else {
                            maxes = this.maximizeMod(maxes, functionArgument, x, xIndex, modifierTable);
                        }
                    }*/
                    maxes = this.maxmin(op, maxes, functionArgument, x, xIndex, modifierTable);
                    functionArgument[i]++;
                    for (int j = i + 1; j <= imax; j++) {
                        functionArgument[j] = 0;
                    }
                    i = imax;
                }
                i--;
            }
            // here an array ready for being evaluated
            maxes = this.maxmin(op, maxes, functionArgument, x, xIndex, modifierTable);
            //////////////////////

        return maxes;

    }

    /*public double[] maximizeMod(double[] max, int[] numberOfValues, NodeVariable x, int xIndex, HashMap<NodeVariable, MessageQ> modifierTable){
        double cost = 0;
        for (int xParamIndex = 0; xParamIndex  < x.size(); xParamIndex ++) {
            numberOfValues[xIndex] = xParamIndex;
            // NOW it's pretty ready
            // this is the part where it is maximized
            cost = ( this.evaluateMod(this.functionArgument(numberOfValues), modifierTable));
            if (max[xParamIndex] < cost ){
                max[xParamIndex] = ( cost );
            }
        }
        return max;
    }

    public double[] maximize(double[] max, int[] numberOfValues, NodeVariable x, int xIndex){
        double cost = 0;
        for (int xParamIndex = 0; xParamIndex  < x.size(); xParamIndex ++) {
            numberOfValues[xIndex] = xParamIndex;
            // NOW it's pretty ready
            // this is the part where it is maximized
            cost = ( this.evaluate(this.functionArgument(numberOfValues)));
            if (max[xParamIndex] < cost ){
                max[xParamIndex] = ( cost );
            }
        }
        return max;
    }*/

    public double[] maxmin(String op, double[] max, int[] numberOfValues, NodeVariable x, int xIndex, HashMap<NodeVariable, MessageQ> modifierTable){
        double cost;
        if (op.equals("max")){
            cost = Double.NEGATIVE_INFINITY;
        }
        else if (op.equals("min")){
            cost = Double.POSITIVE_INFINITY;
        }

        for (int xParamIndex = 0; xParamIndex  < x.size(); xParamIndex ++) {
            numberOfValues[xIndex] = xParamIndex;
            // NOW it's pretty ready
            // this is the part where it is maximized
            if (modifierTable == null){
                cost =  this.evaluate(this.functionArgument(numberOfValues));
            }
            else {
                cost = ( this.evaluateMod(this.functionArgument(numberOfValues), modifierTable));
            }

            if (op.equals("max")){
                if (max[xParamIndex] < cost ){
                    max[xParamIndex] = ( cost );
                }
            }
            else if (op.equals("min")){
                if (max[xParamIndex] > cost ){
                    max[xParamIndex] = ( cost );
                }
            }
        }
        return max;
    }

    @Override
    public String getType() {
        return "CONSTRAINT ";
    }
}
