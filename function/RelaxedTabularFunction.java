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

package function;

import exception.VariableNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.HashMap;
import misc.NodeArgumentArray;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class RelaxedTabularFunction extends TabularFunction implements RelaxableFunctionEvaluator{

    /**
     * Correspondence between parameters and relaxed function values.<br/>
     */
    protected HashMap<NodeArgumentArray, Double> relaxedCostTable;

    public RelaxedTabularFunction() {
        super();
        this.relaxedCostTable = new HashMap<NodeArgumentArray, Double>();
    }

    public double relaxedEvaluate(NodeArgument[] params) throws VariableNotSetException {
        if (this.relaxedCostTable.containsKey(NodeArgumentArray.getNodeArgumentArray(params))) {
            return this.relaxedCostTable.get(NodeArgumentArray.getNodeArgumentArray(params));
        }
        else {
            return this.evaluate(params);
        }
    }

    public void addParametersRelaxedCost(NodeArgument[] params, double cost) {
        this.relaxedCostTable.put(NodeArgumentArray.getNodeArgumentArray(params), cost);
    }

    public double relaxedActualValue() throws VariableNotSetException {
        NodeArgument[] params = new NodeArgument[this.parametersNumber()];
        for(NodeVariable param : this.parameters) {
            params[this.parameters.indexOf(param)] = param.getStateArgument();
        }
        return this.relaxedEvaluate(params);
    }

}
