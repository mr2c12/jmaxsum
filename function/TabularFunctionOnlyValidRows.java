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
import exception.ValueNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import messages.MessageQ;
import misc.NodeArgumentArray;

/**
 * A version of TabularFunction where only valid tuples are stored.
 * Every function evaluation over parameters values not stored in this table return a special value (e.g. positive or negative) infinite.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class TabularFunctionOnlyValidRows extends TabularFunction {



    /**
     * Value of invalid rows
     */
    private double otherValue;
    protected HashMap<NodeArgumentArray, Double> costTable;

    public TabularFunctionOnlyValidRows(double oth) {
        this.costTable = new HashMap<NodeArgumentArray, Double>();

        this.otherValue = oth;
    }

    @Override
    public void addParametersCost(NodeArgument[] params, double cost) {
        this.costTable.put(NodeArgumentArray.getNodeArgumentArray(params), cost);

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

    public double getDelta() throws ValueNotSetException{
        // FIXME
        return 0;
    }
    
    public double evaluate(NodeArgument[] params) {
        /*StringBuilder key = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            key.append(params[i].toString()).append(";");
        }*/

        NodeArgumentArray paramsa = NodeArgumentArray.getNodeArgumentArray(params);

        if (!this.costTable.containsKey(paramsa)) {
            return this.otherValue;
        } else {
            return this.costTable.get(paramsa);
        }
    }

    // TODO: nodearguments can only be a string since the representation of KEY
    @Override
    public double[] maxminWRT(String op, NodeVariable x, HashMap<NodeVariable, MessageQ> modifierTable) throws ParameterNotFoundException {
        double[] maxes = new double[x.size()];
        for (int i = 0; i < maxes.length; i++) {
            maxes[i] = this.otherValue;
        }
        int xIndex = this.getParameterPosition(x);
        int modIndex;
        StringTokenizer t;
        NodeArgument[] args;
        double cost;
        int parametersNumber = this.parametersNumber();

        /*for (String key : this.costTable.keySet()) {

            t = new StringTokenizer(key, ";");
            args = new NodeArgument[parametersNumber];
            int index = 0;
            while (t.hasMoreTokens()) {

                args[index] = NodeArgument.getNodeArgument(Integer.parseInt(t.nextToken()));
                index++;
            }*/
        

        for (NodeArgumentArray argsa : this.costTable.keySet()){

            

            args = argsa.getArray();


            if (modifierTable == null) {
                //cost = this.costTable.get(key);
                //cost = this.costTable.get(argsa);
                cost =this.evaluate(args);
            } else {
                cost = this.evaluateMod(args, modifierTable);
            }

            /*System.out.println("x is: "+x.toString() +" with arguments: ");
            for (NodeArgument a : x.getValues()){
            System.out.println(a);
            }
            System.out.println("looking for: "+ args[xIndex]);

            modIndex = x.numberOfArgument(  args[xIndex]  );

            System.out.println("modIndex is: "+ modIndex);*/

            modIndex = x.numberOfArgument(args[xIndex]);
            if (op.equals("max")) {
                if (maxes[modIndex] < cost) {
                    maxes[modIndex] = cost;
                }

            } else if (op.equals("min")) {
                if (maxes[modIndex] > cost) {
                    maxes[modIndex] = cost;
                }
            }



        }



        return maxes;
    }


    public String toStringForFile() {
        StringBuilder string = new StringBuilder();

        for (NodeArgumentArray key : this.costTable.keySet() ){
            NodeArgument[] nodeArguments = key.getArray();
            string.append("F ");
            for (int i = 0; i < nodeArguments.length; i++) {
                string.append(nodeArguments[i]).append(" ");
            }
            string.append(costTable.get(key)).append("\n");
        }
        
        return string.toString();
    }

    public double getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(double otherValue) {
        this.otherValue = otherValue;
    }

        @Override
    public String getType() {
        return "CONSTRAINTOVV ";
    }
}
