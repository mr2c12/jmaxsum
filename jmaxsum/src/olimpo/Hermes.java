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
import exception.InvalidInputFileException;
import exception.PostServiceNotSetException;
import exception.VariableNotSetException;
import exception.WeightNotSetException;
import factorgraph.NodeVariable;
import hacks.ScrewerUp;
import jargs.gnu.CmdLineParser;

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
 */
public class Hermes {

    public static void main(String[] args) {

        CmdLineParser parser = new CmdLineParser();

        CmdLineParser.Option oldformat = parser.addBooleanOption("old-format");
        CmdLineParser.Option oldformatMV = parser.addIntegerOption("MV");
        CmdLineParser.Option oldformatMC = parser.addDoubleOption("MC");

        CmdLineParser.Option iterationnumber = parser.addIntegerOption('i', "iterations-number");
        CmdLineParser.Option stepbystep = parser.addBooleanOption("step-by-step");
        CmdLineParser.Option report = parser.addStringOption('R', "report");
        CmdLineParser.Option output = parser.addStringOption('O',"output");
        CmdLineParser.Option screwup = parser.addBooleanOption("screw-it-up");
        CmdLineParser.Option updateel = parser.addBooleanOption('U', "update-each-iteration");
        CmdLineParser.Option noBounded = parser.addBooleanOption("bounded-max-sum");
        CmdLineParser.Option printFactorGraph = parser.addBooleanOption('F', "print-factor-graph");
        CmdLineParser.Option time = parser.addBooleanOption('T', "time");
        CmdLineParser.Option oplus = parser.addStringOption("oplus");
        CmdLineParser.Option otimes = parser.addStringOption("otimes");
        CmdLineParser.Option solver = parser.addStringOption("solver");
        CmdLineParser.Option powerGrid = parser.addBooleanOption("is-power-grid");
        CmdLineParser.Option bms_report = parser.addBooleanOption("bms-report");
                

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }


        Boolean oldformatV = (Boolean) parser.getOptionValue(oldformat, false);
        int oldformatMVV = (Integer) parser.getOptionValue(oldformatMV, new Integer(100));
        double oldformatMCV = (Double) parser.getOptionValue(oldformatMC, new Double(10000));
        
        
        Boolean stepbystepV = (Boolean) parser.getOptionValue(stepbystep, false);
        Boolean screwupV = (Boolean) parser.getOptionValue(screwup, false);
        Boolean updateelV = (Boolean) parser.getOptionValue(updateel, false);
        Boolean boundedV = (Boolean) parser.getOptionValue(noBounded, false);
        Boolean printFactorGraphV = (Boolean) parser.getOptionValue(printFactorGraph, false);
        Boolean timeV = (Boolean) parser.getOptionValue(time, false);
        
        Boolean powerGridV = (Boolean) parser.getOptionValue(powerGrid, false);
        // what if no report? -> null!
        String reportV = (String) parser.getOptionValue(report);
        String outputV = (String) parser.getOptionValue(output);
        
        Integer iterationsV = (Integer) parser.getOptionValue(iterationnumber, new Integer(10));

        String oplusV = (String) parser.getOptionValue(oplus, "max");
        String otimesV = (String) parser.getOptionValue(otimes, "sum");
        String solverV = (String) parser.getOptionValue(solver, "athena");
        Boolean bmsrV = (Boolean) parser.getOptionValue(bms_report);

        String[] otherArgs = parser.getRemainingArgs();
        String filepath = "";
        try {
            if ((otherArgs.length == 1) && (new File(otherArgs[0]).exists())) {
                filepath = otherArgs[0];

            } else {
                throw new Exception("Invalid file or unrecognized argument/s!\n\n");
            }

            if (!((oplusV.equalsIgnoreCase("max") || oplusV.equalsIgnoreCase("min")))) {
                throw new Exception("Invalid value for OPlus!\n\n");
            }
            if (!( (otimesV.equalsIgnoreCase("sum")) || (otimesV.equalsIgnoreCase("prod")))) {
                throw new Exception("Invalid value for OTimes!\n\n");
            }
            if (!((solverV.equalsIgnoreCase("athena")) || (solverV.equalsIgnoreCase("eris")))) {
                throw new Exception("Invalid solver!\n\n");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }


        /*System.out.println("old format is "+ oldformatV);
        System.out.println("iterations number is "+ iterationsV);
        System.out.println("step by step is "+ stepbystepV);
        System.out.println("report is " + reportV);
        System.out.println("input file: "+filepath);*/




        try {

            long startTime = System.currentTimeMillis();

            /*COP_Instance original_cop = Cerberus.getInstanceFromFile(filepath, oldformatV);
            PowerGrid pg = new PowerGrid(args[1]);*/

            COP_Instance original_cop;
            if (powerGridV){
                PowerGrid pg = new PowerGrid(filepath);
                original_cop = pg.getCopM();
            }
            else {
                original_cop = Cerberus.getInstanceFromFile(filepath, oldformatV, oldformatMVV,oldformatMCV);
            }


            COP_Instance cop;
            InstanceCloner ic = null;
            if (screwupV || boundedV){
                 ic = new InstanceCloner(original_cop);
                 cop = ic.getClonedInstance();
            }
            else {
                cop = original_cop;
            }


            ScrewerUp screwerup = null;

            if (screwupV) {
                screwerup = new ScrewerUp(cop);
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
            if (solverV.equalsIgnoreCase("athena")){
                core = new Athena(cop, oplusV, otimesV);
            }
            else if (solverV.equalsIgnoreCase("eris")){
                core = new Eris(oplusV, cop);
            }

            core.setIterationsNumber(iterationsV);

            core.setStepbystep(stepbystepV);
            core.setUpdateOnlyAtEnd(!stepbystepV);

            core.setUpdateOnlyAtEnd(!updateelV);

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
                    if (BMax.sanityCheckOnSolution(cop.actualValue(), original_cop.actualValue())) {
                        finale += "OK\n";
                    } else {
                        finale += "ERROR\n";
                    }
                } catch (VariableNotSetException e1) {
                    System.out.println("Unable to get the Approximation Ratio: at least one variable is not set");
                } catch (WeightNotSetException e2) {
                    System.out.println(e2);
                }

                if (bmsrV != null){
                    System.out.println("bounded report="+
                            cop.getAgentsNumber()+","+
                            cop.getDensity()+","+
                            cop.actualValue()+","+
                            original_cop.actualValue()+","+
                            (original_cop.actualValue()+BMax.getB())
                            );
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
           
            
            if(outputV!=null){
            	String outString = "";
            	for (NodeVariable nv : original_cop.getNodevariables()) {
    				outString += nv.getId();
    				outString += " ";
    				try {
    					outString += nv.getStateArgument();
    				} catch (VariableNotSetException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    				outString += "\n";
    			}
                try {
                	System.out.println(outString);
                    BufferedWriter out = new BufferedWriter(new FileWriter(outputV));
                    out.write(outString);
                    out.close();
                } catch (IOException ex) {
                    System.out.println("Sorry but I'm unable to write the output to the file "+outputV);
                }
            	
            }

            
            

        } catch (IllegalArgumentException iaex) {
            System.out.println(iaex.getMessage());
        } catch (PostServiceNotSetException pse) {
            System.out.println("Fatal problem in the process of initialization: no PostService set!");
        } catch (InvalidInputFileException iife) {
            System.out.println("The given file is not a valid input for this program!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void printUsage() {
        String usage = "Usage: java -jar maxSum.jar [-options] inputfile\n";
        usage += "where inputfile is the path of the file containing an instance of a COP problem\n";
        usage += "and where options include:\n";
        usage += "\t--solver\t\tthe solver method, can be \"athena\" for MaxSum or \"eris\" for Simulated Annealing (default: \"athena\")\n";
        usage += "\t--oplus\t\tthe oplus operator, can be \"max\" or \"min\" (default: \"max\")\n";
        usage += "\t--otimes\tthe otimes operator, only \"sum\" available at the moment (default: \"sum\")\n";

        usage += "\t--old-format\tif the input file does not specify function id and agent\n";
        usage += "\t--is-power-grid\tif the input file represent a power grid\n";
        usage += "\t--step-by-step\tat each iteration the execution pause and wait for ENTER to be pressed\n";
        usage += "\t--iterations-number <n> | -i <n>\n\t\tset the number of iterations of the algorithm\n";
        usage += "\t--update-each-iteration <n> | -U <n>\n\t\tprint the utility value and variables value at every iteration\n";
        usage += "\t--print-factor-graph | -F\n\t\tprint both version of factor graph, original and after the bounded-max-sum phase\n";
        usage += "\t--screw-it-up\tto use the preferences-on-values hack\n";
        usage += "\t--bounded-max-sum\tuse the Bounded Max Sum phase\n";
        usage += "\t--time | -T\n\t\tprint total time usage\n";
        usage += "\t--report <file>\twrite the report of the execution on <file>";
        usage += "\t--bms-report\twrite the short report for bounded phase";

        System.out.println(usage);
    }
}
