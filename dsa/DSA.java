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

import exception.PostServiceNotSetException;
import exception.NoMoreValuesException;
import exception.ParameterNotFoundException;
import exception.ResultOkException;
import exception.VariableNotSetException;

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import system.COP_Instance;
import operation.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * This class implements the common features among all versions of DSA.<br/>
 * It is abstract, as it is extended by the DSA-* subclasses.
 * @author Filippo Bistaffa <filippo dot bistaffa at univr dot it>
 */

public class DSA implements Solver {

	private COP_Instance cop;
	private String op;
	private ArrayList<NodeVariable> variables;
	private ArrayList<FunctionEvaluator> functions;
	private double costValue = Double.NaN;
	private double haltValue = Double.NaN;
	private boolean pleaseReport = false;
	private String reportpath = "";
	private String report = "";
	private boolean stepbystep = false;
	private boolean updateOnlyAtEnd = true;
	private double costo;
	int steps = -1;
	private int kMax = 10000;

	/**
	 * Please report all the information and put them in file
	 * @param file report path
	 */
	public void pleaseReport(String file) {

		this.pleaseReport = true;
		this.reportpath = file;
	}

	/**
	 * How many steps to do?
	 * @param n number of steps.
	 */
	public void setIterationsNumber(int n) {

	        this.kMax = n;
	}

	public void setStepbystep(boolean stepbystep) {

		this.stepbystep = stepbystep;
	}

	/**
	 * Set if the update is only after the algorithm is finished or after each step.
	 * @param updateOnlyAtEnd yes if to update only when finished.
	 */
	public void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd) {

		this.updateOnlyAtEnd = updateOnlyAtEnd;
	}

	public void solve() throws PostServiceNotSetException {

	}

	public DSA(COP_Instance cop, String op) throws ParameterNotFoundException {

		double changeInfinityTo = 0;
		double infinity;

		if (op.equalsIgnoreCase("max")) {
			this.op = "max";
			this.costValue = Double.NEGATIVE_INFINITY;
			this.haltValue = Double.POSITIVE_INFINITY;
			infinity = Double.NEGATIVE_INFINITY;
			changeInfinityTo = -1e05;
		} else if (op.equalsIgnoreCase("min")) {
			this.op = "min";
			this.costValue = Double.POSITIVE_INFINITY;
			this.haltValue = Double.NEGATIVE_INFINITY;
			infinity = Double.POSITIVE_INFINITY;
			changeInfinityTo = 5000; //1e05;
		} else {
			throw new ParameterNotFoundException("Unknown operation: " + op);
		}

		this.cop = cop;
		this.variables = new ArrayList<NodeVariable>();

		for (NodeVariable nv : this.cop.getNodevariables()) {
			if (nv.size() > 1) {
				this.variables.add(nv);
			} else if (nv.size() == 1) {
				nv.setStateIndex(0);
			}
		}

		this.functions = new ArrayList<FunctionEvaluator>();
		FunctionEvaluator oldFe, newFe;

		for (NodeFunction oldNf : this.cop.getNodefunctions()) {
			oldFe = oldNf.getFunction();
			newFe = oldFe.getClone();
			if (newFe.changeValueToValue(infinity, changeInfinityTo)) {
				functions.add(newFe);
			} else {
				functions.add(oldFe);
			}
		}
	}

	private double getCosto() throws VariableNotSetException {

		double cost = 0;

		for (FunctionEvaluator fe : this.functions)
			cost += fe.actualValue();

		return cost;
	}

	public void randomInit() {

		for (NodeVariable x : this.variables) {
			try {
				x.setRandomValidValue();
			} catch (NoMoreValuesException ex) {
				// do nothing
			}
		}

		try {
			this.costo = this.getCosto();
		} catch (VariableNotSetException ex) {
			ex.printStackTrace();
		}

		this.steps = -1;
	}

	public void selectNextValue() {

	}
}
