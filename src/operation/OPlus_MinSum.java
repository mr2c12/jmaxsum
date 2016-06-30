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

package operation;

import exception.ParameterNotFoundException;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import messages.MessageContent;
import messages.MessageFactory;
import messages.MessageQ;
import messages.MessageR;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class OPlus_MinSum implements OPlus{

    static final int debug = test.DebugVerbosity.debugOPlus_MaxSum;
    
    private MessageFactory factory;

    public OPlus_MinSum(MessageFactory factory) {
        this.factory = factory;
    }

    

    public MessageR oplus(NodeFunction sender, NodeVariable x, FunctionEvaluator fe, LinkedList<MessageQ> messagelist) {



        HashMap<NodeVariable, MessageQ> modifierTable = new HashMap<NodeVariable, MessageQ>();

        Iterator<MessageQ> iterator = messagelist.iterator();

        while (iterator.hasNext()) {
            MessageQ messageQ = iterator.next();

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[1].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[1].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "messageQ="+messageQ+ " with sender="+messageQ.getSender());
                    System.out.println("---------------------------------------");
            }



            modifierTable.put(messageQ.getSender(), messageQ);
        }

        if(debug>=3){
            String dmethod = Thread.currentThread().getStackTrace()[1].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[1].getClassName();
            System.out.println("[class: "+dclass+" method: "+dmethod + "] " + "modifier table size="+modifierTable.size());
            if (modifierTable.size()>=1){
                Iterator<NodeVariable> it = modifierTable.keySet().iterator();
                while (it.hasNext()) {
                    NodeVariable nodeVariable = it.next();
                    System.out.print(" Node:"+nodeVariable+" Value:"+modifierTable.get(nodeVariable));
                }

            }
            System.out.println("");
            System.out.println("---------------------------------------");

        }

        return this.computeR(sender, x, fe, modifierTable);

    }

    public MessageR nullMessage(NodeFunction sender, NodeVariable receiver, int size) {
        double nullValue = 1.0;
        double[] content = new double[size];
        for (int i = 0; i < size; i++) {
            content[i] = nullValue;
        }
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "preparing oplus null message from "+sender+" to "+receiver);
                MessageR temp = this.factory.getMessageR(sender, receiver, content);
                System.out.println("MessageR temp with sender "+ temp.getSender() + " and receiver "+ temp.getReceiver());
                System.out.println("---------------------------------------");
        }
        return this.factory.getMessageR(sender, receiver, content);
    }


        /**
     * It compute the r message from a function f to a variable x
     * @param x the node variable representing x
     * @param fe the function evaluator
     * @param modifierTable the hash map used to change the value of f. It maps an integer, that is the position of a argument of f, to the array of value to pass to f. The i-th cell of the array is the quantity to give to apply to the function cost corrisponding to the i-th value of the variable
     * @return the r-message
     */
    public MessageR computeR(NodeFunction sender, NodeVariable x, FunctionEvaluator fe, HashMap<NodeVariable, MessageQ> modifierTable) {


        if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "");




            System.out.print("Oplus_MinSum.computeR: modifier table size="+modifierTable.size());
            if (modifierTable.size()>=1){
                Iterator<NodeVariable> it = modifierTable.keySet().iterator();
                while (it.hasNext()) {
                    NodeVariable nodeVariable = it.next();
                    System.out.print(" Node:"+nodeVariable+" Value:"+modifierTable.get(nodeVariable));
                }

            }
            System.out.println("");
            System.out.println("---------------------------------------");
        }

        try {
            /*int xIndex = fe.getParameterPosition(x);

            // number of parameters of f
            int fzParametersNumber = fe.parametersNumber();
            // at the i-th position there is the number of possible values of the i-th argument of f
            // at the position of x, there will be only one value available
            int[] numberOfValues = new int[fzParametersNumber];
            // the array filled with variable value positions that's gonna be evaluated
            int[] functionArgument = new int[fzParametersNumber];
            // maximization array, wrt x possible values
            
            Double[] maxCost = new Double [ x.size() ];

            for (int i = 0; i < fzParametersNumber; i++) {
                numberOfValues[i] = fe.getParameter(i).size();
            }
            numberOfValues[xIndex] = 1;

            ///////////////////////
            int imax = numberOfValues.length-1;
            int i=imax;
            while(i>=0){
                while ( functionArgument[i] < numberOfValues[i] - 1 ) {
                    // here an array ready for being evaluated
                    maxCost = this.minimizeMod(maxCost, functionArgument, x, xIndex, fe, modifierTable);
                    functionArgument[i]++;
                    for (int j = i+1; j <= imax; j++) {
                        functionArgument[j]=0;
                    }
                    i=imax;
                }
                i--;
            }
            // here an array ready for being evaluated
            maxCost = this.minimizeMod(maxCost, functionArgument, x, xIndex, fe, modifierTable);
            //////////////////////


            //return new MessageRArrayDouble(maxCost);*/
            double[] maxCost = fe.minimizeWRT(x, modifierTable);
            return this.factory.getMessageR(sender, x, maxCost);
        } catch (ParameterNotFoundException ex) {
            // if x is not found, return a null message
            return this.nullMessage(sender, x, x.size());
        }
    }






    public Double[] minimizeMod(Double[] min, int[] numberOfValues, NodeVariable x, int xIndex, FunctionEvaluator fe, HashMap<NodeVariable, MessageQ> modifierTable){

        if(debug>=3){
            System.out.print("Oplus_MinSum.minimizeMod: modifier table size="+modifierTable.size());
            if (modifierTable.size()>=1){
                Iterator<NodeVariable> it = modifierTable.keySet().iterator();
                while (it.hasNext()) {
                    NodeVariable nodeVariable = it.next();
                    System.out.print(" Node:"+nodeVariable+" Value:"+modifierTable.get(nodeVariable));
                }

            }
            System.out.println("");
        }
        double cost = 0;
        for (int xParamIndex = 0; xParamIndex  < x.size(); xParamIndex ++) {
            numberOfValues[xIndex] = xParamIndex;
            // NOW it's pretty ready
            // this is the part where it is maximized
            cost = ( fe.evaluateMod(fe.functionArgument(numberOfValues), modifierTable));
            if (min[xParamIndex] == null){
                min[xParamIndex] = cost;
            }
            else {
                min[xParamIndex] = ( cost < min[xParamIndex] ) ? cost : min[xParamIndex];
            }
        }
        return min;
    }


    /**
     * Given Z, it gives back the argmin
     * @param z array of summarized r-messages
     * @return the (lower) index of array with the maximum/minimum value, corresponding to the argmin/argmin
     */
    public int argOfInterestOfZ(MessageContent z) {
        // we look for the argmin
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "computing the Z index of"+z);
                System.out.println("---------------------------------------");
        }
        int argmin = 0;
        double minvalue = z.getValue(0);
        for (int index= 0; index < z.size()-1; index++) {

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "loop number "+index+" minvalue="+minvalue);
                    System.out.println("---------------------------------------");
            }


            if (minvalue > z.getValue(index+1)) {
                argmin = index+1;
                minvalue = z.getValue(index+1);
            }
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "the argmin is "+argmin);
                System.out.println("---------------------------------------");
        }

        return argmin;
    }
}



