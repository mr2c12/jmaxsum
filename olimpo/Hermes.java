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

package olimpo;

import boundedMaxSum.BoundedMaxSum;
import boundedMaxSum.InstanceCloner;
import exception.PostServiceNotSetException;
import exception.VariableNotSetException;
import exception.WeightNotSetException;
import factorgraph.NodeVariable;
import hacks.ScrewerUp;
import com.sanityinc.jargs.CmdLineParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import misc.Utils;
import operation.Solver;
import powerGrid.PowerGrid;
import system.COP_Instance;

/**
 * Hermes is the great messenger of the gods in Greek mythology and a guide to the Underworld.<br/>
 * <br/>
 * Command-line control interface for Max Sum<br/>
 * @author Michele Roncalli <roncallim at gmail dot com>
 * @author Filippo Bistaffa <filippo dot bistaffa at univr dot it>
 */
public class Hermes {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsage();
			System.exit(2);
		}

		CmdLineParser parser = new CmdLineParser();

		// Required
		CmdLineParser.Option<Integer> M = parser.addIntegerOption("generators");
		CmdLineParser.Option<Integer> D = parser.addIntegerOption("loads");
		CmdLineParser.Option<Integer> R = parser.addIntegerOption("ancillary");
		CmdLineParser.Option<Double> C = parser.addDoubleOption("center");
		CmdLineParser.Option<Double> W = parser.addDoubleOption("width");

		// Optional
		CmdLineParser.Option<String> solver = parser.addStringOption("algorithm");
		CmdLineParser.Option<String> oplus = parser.addStringOption("oplus");
		CmdLineParser.Option<String> otimes = parser.addStringOption("otimes");
		CmdLineParser.Option<Long> seed = parser.addLongOption("seed");
		CmdLineParser.Option<Integer> iterationnumber = parser.addIntegerOption("iterations");
		CmdLineParser.Option<Boolean> printFactorGraph = parser.addBooleanOption("printfactor");
		CmdLineParser.Option<Boolean> screwup = parser.addBooleanOption("screw");
		CmdLineParser.Option<Boolean> noBounded = parser.addBooleanOption("bounded");
		CmdLineParser.Option<Boolean> time = parser.addBooleanOption("time");
		CmdLineParser.Option<String> report = parser.addStringOption("report");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(2);
		}

		Integer M_V = (Integer) parser.getOptionValue(M);
		Integer D_V = (Integer) parser.getOptionValue(D);
		Integer R_V = (Integer) parser.getOptionValue(R);
		Double C_V = (Double) parser.getOptionValue(C);
		Double W_V = (Double) parser.getOptionValue(W);

		Boolean stepbystepV = false;
		String solverV = (String) parser.getOptionValue(solver, "maxsum");
		String oplusV = (String) parser.getOptionValue(oplus, "min");
		String otimesV = (String) parser.getOptionValue(otimes, "sum");
		Long seedV = (Long) parser.getOptionValue(seed, new Long(-1));
		Integer iterationsV = (Integer) parser.getOptionValue(iterationnumber, new Integer(300));
		Boolean printFactorGraphV = (Boolean) parser.getOptionValue(printFactorGraph, false);
		Boolean screwupV = (Boolean) parser.getOptionValue(screwup, false);
		Boolean boundedV = (Boolean) parser.getOptionValue(noBounded, false);
		Boolean timeV = (Boolean) parser.getOptionValue(time, false);
		String reportV = (String) parser.getOptionValue(report);

		// Check for argument correctness

		try {
			if ((M_V == null) || (D_V == null) || (R_V == null) || (C_V == null) || (W_V == null))
				throw new Exception("Missing one or more required powergrid parameters!\n");

			if (!((oplusV.equalsIgnoreCase("max") || oplusV.equalsIgnoreCase("min"))))
				throw new Exception("Invalid value for OPlus!\n");

			if (!( (otimesV.equalsIgnoreCase("sum")) || (otimesV.equalsIgnoreCase("prod"))))
				throw new Exception("Invalid value for OTimes!\n");

			if (!((solverV.equalsIgnoreCase("maxsum")) || (solverV.equalsIgnoreCase("annealing"))))
				throw new Exception("Invalid algorithm!\n");

		} catch (Exception e) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(2);
		}

		try {

			long startTime = System.currentTimeMillis();
			COP_Instance original_cop;
			PowerGrid pg = new PowerGrid(M_V, D_V, R_V, C_V, W_V, seedV);
			original_cop = pg.getCopM();
			InstanceCloner ic = null;
			COP_Instance cop;

			if (screwupV || boundedV){
				ic = new InstanceCloner(original_cop);
				cop = ic.getClonedInstance();
			} else {
				cop = original_cop;
			}

			ScrewerUp screwerup = null;

			if (screwupV) {
				screwerup = new ScrewerUp(cop, seedV);
				cop = screwerup.screwItUp();
			}

			BoundedMaxSum BMax = null;

			if (boundedV) {
				// time for BoundedMaxSum to do his best!
				BMax = new BoundedMaxSum(cop.getFactorgraph());
				//BMax.weightTheGraph();
				BMax.letsBound();
			}

			Solver core = null;

			if (solverV.equalsIgnoreCase("maxsum")){
				core = new Athena(cop, oplusV, otimesV);
			}
			else if (solverV.equalsIgnoreCase("annealing")){
				core = new Eris(oplusV, cop, "modinf", seedV);
			}

			core.setIterationsNumber(iterationsV);
			core.setStepbystep(stepbystepV);
			core.setUpdateOnlyAtEnd(!stepbystepV);

			if (reportV != null) {
				core.pleaseReport(reportV);
			}

			core.solve();

			if (screwupV || boundedV){
				// set the variables value to the original instance
				ic.setOriginalVariablesValues();
			}

			long endTime = System.currentTimeMillis();
			String finale = "";

			if (printFactorGraphV) {
				finale += "==========================================\n"
			        	+ "Original factor graph:\n" + original_cop.getFactorgraph().toString()
					+ "==========================================\n";
				if (boundedV) {
					// bounded version printed only if created
					finale += "==========================================\n"
						+ "Bounded factor graph:\n" + cop.getFactorgraph().toString()
						+ "==========================================\n";
				}
			}

			if (boundedV) {
				// bounded version printed only if created
				try {
					finale += "Approximation Ratio: "
						+ BMax.getApproximationRatio(cop.actualValue(), original_cop.actualValue())
						+ "\n";
					finale += "Sanity check: ";

					if (BMax.sanityCheckOnSolution(cop.actualValue(), original_cop.actualValue()))
						finale += "OK\n";
					else
						finale += "ERROR\n";
				} catch (VariableNotSetException e1) {
					System.out.println("Unable to get the Approximation Ratio: at least one variable is not set");
				} catch (WeightNotSetException e2) {
					System.out.println(e2);
				}
			}

			finale += "Conclusion for original cop=" + original_cop.status() + "\n";

			if (timeV) {
				finale += "Time needed: " + ((endTime - startTime) / (double) 1000) + " [s]";
			}

			if (reportV != null) {
				try {
					Utils.stringToFile(finale, reportV);
				} catch (IOException ex) {
					System.out.println("Unable to write the report to " + reportV);
				}
			} else {
				System.out.println(finale);
			}

			System.out.println("Value = " + cop.actualValue());

		} catch (IllegalArgumentException iaex) {
			System.out.println(iaex.getMessage());
		} catch (PostServiceNotSetException pse) {
			System.out.println("Fatal problem in the process of initialization: no PostService set!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void printUsage() {

		String usage = "Usage: java -jar solver.jar [OPTIONS] --generators M --loads D --ancillary R --center C --width W\n\n";
		usage += "--generators M\tThe number of generators\n";
		usage += "--loads D\tThe number of loads connected to each generator\n";
		usage += "--ancillary R\tThe number ancillary lines for each generator\n";
		usage += "--center C\tThe center of the uniform distribution\n";
		usage += "--width W\tThe width of the uniform distribution\n\n";
		usage += "OPTIONS include:\n\n";
		usage += "--algorithm A\tCan be \"maxsum\" for Max Sum, \"annealing\" for Simulated Annealing or \"dsa\" for DSA (default: \"maxsum\")\n";
		usage += "--oplus OP\tThe oplus operator, can be \"max\" or \"min\" (default: \"min\")\n";
		usage += "--otimes OT\tThe otimes operator, only \"sum\" available at the moment (default: \"sum\")\n";
		usage += "--seed S\tSeed used to generate the random powergrid instance (default: random)\n";
		usage += "--iterations I\tThe number of iterations of the algorithm (default: 300)\n";
		usage += "--printfactor\tPrint both version of factor graph, original and after the bounded phase\n";
		usage += "--screw\t\tUse the preferences-on-values hack\n";
		usage += "--bounded\tUse the Bounded Max Sum phase\n";
		usage += "--time\t\tPrint total time usage\n";
		usage += "--report FILE\tWrite the report of the execution on FILE";

		System.out.println(usage);
	}
}
