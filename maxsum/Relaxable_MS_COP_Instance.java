/*
 *  Copyright (C) 2012 Michele Roncalli <roncallim at gmail dot com>
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

package maxsum;

import exception.VariableNotSetException;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import function.RelaxableFunctionEvaluator;
import java.util.HashSet;
import system.RelaxableCop_Instance;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Relaxable_MS_COP_Instance extends MS_COP_Instance implements RelaxableCop_Instance{

    public Relaxable_MS_COP_Instance(){
        super();
    }

    public Relaxable_MS_COP_Instance(HashSet<NodeVariable> nodevariables, HashSet<NodeFunction> nodefunctions,  HashSet<Agent> agents) {
        super(nodevariables, nodefunctions, agents);
    }

    public double actualRelaxedValue() throws VariableNotSetException {
        double value = 0.0;
        double actualFValue;
        FunctionEvaluator fe;
        RelaxableFunctionEvaluator rfe;
        for(NodeFunction nf : this.factorgraph.getNodefunctions()){
            fe = nf.getFunction();
            
            if (fe instanceof RelaxableFunctionEvaluator){
                rfe = (RelaxableFunctionEvaluator) fe;
                actualFValue = rfe.relaxedActualValue();
                value += actualFValue;
            }
            
        }
        //FIXME actual relaxed cost
        return value;
    }


}
