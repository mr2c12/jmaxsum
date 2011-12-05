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

import factorgraph.NodeArgument;

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

    public TabularFunctionOnlyValidRows(double oth) {
        super();
        this.otherValue = oth;
    }

    public double evaluate(NodeArgument[] params) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            key.append(params[i].toString()).append(";");
        }
        if (!this.costTable.containsKey(key.toString())) {
            return this.otherValue;
        } else {
            return this.costTable.get(key.toString());
        }
    }
}
