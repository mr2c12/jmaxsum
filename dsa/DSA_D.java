/*
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

package dsa;

import exception.ParameterNotFoundException;
import factorgraph.NodeVariable;
import java.util.ArrayList;
import system.COP_Instance;

/**
 * This class implements the next value selection strategy of the DSA-D algorithm.
 * @author Filippo Bistaffa <filippo dot bistaffa at univr dot it>
 */

public class DSA_D extends DSA {

	public DSA_D(COP_Instance cop, String op, long seed) throws ParameterNotFoundException {

		super(cop, op, seed);
	}

	public void selectNextValue(NodeVariable x, ArrayList<NodeVariable> variables) {

	}
}
