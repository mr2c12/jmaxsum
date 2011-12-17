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

package maxsum;

import exception.VariableNotSetException;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashSet;
import system.COP_Instance;

/**
 * A instance of Max Sum COP problem.<br/>
 * The difference with a COP problem is how to get actual value. Here is the sum.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class MS_COP_Instance extends COP_Instance{

    public MS_COP_Instance(){
        super();
    }

    public MS_COP_Instance(HashSet<NodeVariable> nodevariables, HashSet<NodeFunction> nodefunctions,  HashSet<Agent> agents) {
        super(nodevariables, nodefunctions, agents);
    }

    @Override
    // make it "static"?!
    public double actualValue() throws VariableNotSetException {
        double value = 0.0;
        double actualFValue;
        for(NodeFunction nf : this.factorgraph.getNodefunctions()){
            actualFValue = nf.actualValue();

            // TODO: check if it works
            /*if (actualFValue == Double.NEGATIVE_INFINITY){
                return Double.NEGATIVE_INFINITY;
            }
            else if (actualFValue == Double.POSITIVE_INFINITY){
                return Double.POSITIVE_INFINITY;
            }
            else {*/
                value += actualFValue;
            //}
        }

        return value;
    }

    @Override
    public COP_Instance getNewMe() {
        return new MS_COP_Instance();
    }



}
