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

package hacks;

import exception.ValueNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import java.util.HashMap;
import java.util.Random;
import misc.Utils;
import system.COP_Instance;

/**
 * This class implements an hack to get Max Sum work even with symmetric problems.<br/>
 * The utility functions in the instance are modified a little to turn the problem in to an asymmetric form.<br/>
 * The screwerUp method keep saved information of how the functions are changed, so it is possible to get back the original problem.<br/>
 * If used on a cloned instance, restore step could be skipped.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class ScrewerUp {

    static final int debug = test.DebugVerbosity.debugScrewerUp;

    /**
     * the cop instance on which the ScrewerUp works
     */
    protected COP_Instance cop;

    protected HashMap<NodeVariable, Double> intervalTable;
    /**
     * Keep saved information of how the functions are changed.
     */
    protected HashMap<NodeVariable, Double[]> modifierTable;
    protected boolean screwed = false;

    public ScrewerUp(COP_Instance cop) {
        this.cop = cop;
        this.intervalTable = new HashMap<NodeVariable, Double>();
        this.modifierTable = new HashMap<NodeVariable, Double[]>();
    }

    /**
     * Alter the value of the utility function.
     * Using the modifierTable it lets to get back the original cop
     * @return the reference to the cop modified.
     */
    public COP_Instance screwItUp(){
        FunctionEvaluator fe = null;
        Double delta = null;
        boolean goon = true;


        // step 1: find the smaller cost-difference function for each node variable

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Step 1: find the smaller cost-difference function for each node variable");
                System.out.println("---------------------------------------");
        }

        for (NodeFunction nf : cop.getNodefunctions()){
            fe = nf.getFunction();

            for (NodeVariable nv : fe.getParameters()) {

                do {
                    try {
                        delta = fe.getDelta();
                        if (delta == 0){
                            delta = fe.getDeltaNotNull();
                            //FIXME: still zero if maxcost = 0 or mincost = 0
                        }
                        goon = true;
                    } catch (ValueNotSetException ex) {
                        fe.resetMaxMinCosts();
                        goon = false;

                    }
                } while (!goon);

                if (!this.intervalTable.containsKey(nv)) {
                    this.intervalTable.put(nv, delta);
                }
                else if (this.intervalTable.get(nv) > delta) {
                    this.intervalTable.put(nv, delta);
                }

                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "delta for "+nv + " in function "+ fe +" is "+ this.intervalTable.get(nv));
                        System.out.println("---------------------------------------");
                }

            }

        }

        // step 2: create the array of modifiers for each node variable

        double numArgument;
        Double[] modifier;
        Random rnd = new Random();
        for (NodeVariable nv : this.intervalTable.keySet()) {
            numArgument = this.intervalTable.get(nv);
            if (numArgument <= 0) {
                this.modifierTable.put(nv, new Double[nv.size()]);
            }
            else {
                modifier = new Double[nv.size()];
                for (int i = 0; i <= modifier.length-1; i++) {
                    modifier[i] = rnd.nextDouble() * numArgument / 100;

                }
                this.modifierTable.put(nv, modifier);
            }
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "variable "+nv+" has modifiers "+Utils.toString(this.modifierTable.get(nv))) ;
                    System.out.println("---------------------------------------");
            }

        }

        // step 3: add the modifier to the function evaluator
        int[] numArguments;
        NodeArgument[] args;
        for (NodeFunction nf : cop.getNodefunctions()){
            fe = nf.getFunction();
            numArguments = new int[fe.parametersNumber()];
            for (int i = 0; i <= numArguments.length - 1; i++) {
                // i-th argument of fe
                numArguments[i] = fe.getParameter(i).size();
            }
            // now run over all the possible values
            int [] v = new int[numArguments.length];
            int imax = v.length-1;
            int i=imax;
            //int quanti = 0;
            while(i>=0){
                while ( v[i] < numArguments[i] - 1 ) {
                    // HERE in v
                    this.updateCost(v, fe, 1);
                    //quanti++;
                    v[i]++;
                    for (int j = i+1; j <= imax; j++) {
                        v[j]=0;
                    }
                    i=imax;
                }
                i--;
            }
            // HERE in v
            this.updateCost(v, fe, 1);
            //quanti++;
        }

        this.screwed = true;

        return this.cop;
    }


    /**
     * Take an array with parameters index, and turn it into an array of NodeArgument
     * @param argumentsNumber
     * @param fe the function evaluator
     * @return NodeArgument array parameter of the function
     */
    public NodeArgument[] functionArgument (int[] argumentsNumber, FunctionEvaluator fe){
        NodeArgument[] fzArgument = new NodeArgument[argumentsNumber.length];
        for (int i = 0; i < fzArgument.length; i++) {
            fzArgument[i] = fe.getParameter(i).getArgument(argumentsNumber[i]);
        }
        return fzArgument;
    }

    /**
     * Update the utility function for arguments number. A modifier is allowed.
     * @param argumentsNumber
     * @param fe the function evaluator
     * @param mult the modifier
     */
    public void updateCost(int[] argumentsNumber, FunctionEvaluator fe, int mult){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "with arguments number " + Utils.toString(argumentsNumber)+ " fe "+fe + " mult " + mult);
                System.out.println("---------------------------------------");
        }
        NodeArgument[] args = this.functionArgument(argumentsNumber, fe);
        double delta = 0;
        for (NodeVariable x : fe.getParameters()){
            try {
                delta += (this.modifierTable.get(x))[
                        x.numberOfArgument(args[fe.getParameterPosition(x)])
                        ];
            } catch (Exception e) {

            }
        }
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "delta is "+delta);
                System.out.println("---------------------------------------");
        }
        fe.addParametersCost(args,
                (fe.evaluate(args)
                + delta*mult)
                );
    }

    /**
     * Is the cop screwed?
     * @return true if screwed.
     */
    public boolean isScrewed() {
        return screwed;
    }

    /**
     * Bring the cop instance to original values.
     * @return the cop as it was at the very beginning
     */
    public COP_Instance fixItUp(){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "fixit up? " + this.isScrewed());
                System.out.println("---------------------------------------");
        }
        if (this.isScrewed()){
            // step 3-bis: sub the modifier to the function evaluator
            FunctionEvaluator fe = null;
            int[] numArguments;
            NodeArgument[] args;
            for (NodeFunction nf : cop.getNodefunctions()){
                fe = nf.getFunction();
                numArguments = new int[fe.parametersNumber()];
                for (int i = 0; i <= numArguments.length - 1; i++) {
                    // i-th argument of fe
                    numArguments[i] = fe.getParameter(i).size();
                }
                // now run over all the possible values
                int [] v = new int[numArguments.length];
                int imax = v.length-1;
                int i=imax;
                int quanti = 0;
                while(i>=0){
                    while ( v[i] < numArguments[i] - 1 ) {
                        // HERE in v
                        this.updateCost(v, fe, -1);
                        quanti++;
                        v[i]++;
                        for (int j = i+1; j <= imax; j++) {
                            v[j]=0;
                        }
                        i=imax;
                    }
                    i--;
                }
                // HERE in v
                this.updateCost(v, fe, -1);
                quanti++;
            }

        this.screwed = false;
        }
        return this.cop;
    }


}
