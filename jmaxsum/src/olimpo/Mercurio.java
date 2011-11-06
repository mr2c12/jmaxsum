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
import hacks.ScrewerUp;
import jargs.gnu.CmdLineParser;
import java.io.File;
import java.io.IOException;
import misc.Utils;
import system.COP_Instance;

/**
 * Command-line control interface for maxsum
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Mercurio {

    public static void main(String[] args) {

        CmdLineParser parser = new CmdLineParser();

        CmdLineParser.Option oldformat = parser.addBooleanOption("old-format");
        CmdLineParser.Option iterationnumber = parser.addIntegerOption('i', "iterations-number");
        CmdLineParser.Option stepbystep = parser.addBooleanOption("step-by-step");
        CmdLineParser.Option report = parser.addStringOption('R', "report");
        CmdLineParser.Option screwup = parser.addBooleanOption("screw-it-up");
        CmdLineParser.Option updateel = parser.addBooleanOption('U', "update-each-iteration");
        CmdLineParser.Option noBounded = parser.addBooleanOption("no-bounded-max-sum");
        CmdLineParser.Option printFactorGraph = parser.addBooleanOption('F', "print-factor-graph");

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }


        Boolean oldformatV = (Boolean) parser.getOptionValue(oldformat, false);
        Boolean stepbystepV = (Boolean) parser.getOptionValue(stepbystep, false);
        Boolean screwupV = (Boolean) parser.getOptionValue(screwup, false);
        Boolean updateelV = (Boolean) parser.getOptionValue(updateel, false);
        Boolean noBoundedV = (Boolean) parser.getOptionValue(noBounded, false);
        Boolean printFactorGraphV = (Boolean) parser.getOptionValue(printFactorGraph, false);

        // what if no report? -> null!
        String reportV = (String) parser.getOptionValue(report);

        Integer iterationsV = (Integer) parser.getOptionValue(iterationnumber, new Integer(10));

        String[] otherArgs = parser.getRemainingArgs();
        String filepath = "";
        try {
            if ((otherArgs.length == 1) && (new File(otherArgs[0]).exists())) {
                filepath = otherArgs[0];
            } else {
                throw new Exception("Invalid file or unrecognized argument/s!\n\n");
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

            COP_Instance original_cop = Cerbero.getInstanceFromFile(filepath, oldformatV);

            InstanceCloner ic = new InstanceCloner(original_cop);
            COP_Instance cop = ic.getClonedInstance();


            ScrewerUp screwerup = null;

            if (screwupV) {
                screwerup = new ScrewerUp(cop);
                cop = screwerup.screwItUp();
            }
            
            BoundedMaxSum BMax = null;
            if (!noBoundedV) {
                // time for BoundedMaxSum to do his best!
                BMax = new BoundedMaxSum(cop.getFactorgraph());
                //BMax.weightTheGraph();
                BMax.letsBound();
            }

            Athena core = new Athena(cop);

            core.setIterationsNumber(iterationsV);

            core.setStepbystep(stepbystepV);

            core.setUpdateOnlyAtEnd(!updateelV);

            if (reportV != null) {
                core.pleaseReport(reportV);
            }

            core.solve();

            /*if(screwupV){
            cop = screwerup.fixItUp();
            }*/

            core.conclude();

            // set the variables value to the original instance
            ic.setOriginalVariablesValues();


            String finale = "";

            if (printFactorGraphV) {
                finale += "==========================================\n"
                        + "Original factor graph:\n" + original_cop.getFactorgraph().toString()
                        + "==========================================\n";
                if (!noBoundedV) {
                    // bounded version printed only if created
                    finale += "==========================================\n"
                            + "Bounded factor graph:\n" + cop.getFactorgraph().toString()
                            + "==========================================\n";
                }
            }

            if (!noBoundedV) {
                // bounded version printed only if created
                try {
                    finale += "Approximation Ratio: "+
                            BMax.getApproximationRatio(original_cop.actualValue(), cop.actualValue())
                            +"\n";
                } catch (VariableNotSetException e1) {
                    System.out.println("Unable to get the Approximation Ratio: at least one variable is not set");
                } catch (WeightNotSetException e2) {
                    System.out.println(e2);
                }
            }
            
            finale += "Conclusion for original cop=" + original_cop.status();

            if (reportV != null) {
                try {
                    Utils.stringToFile(finale, reportV);
                } catch (IOException ex) {
                    System.out.println("Unable to write the report to "+reportV);
                }
            } else {
                System.out.println(finale);
            }

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
        usage += "\t--old-format\tif the input file does not specify function id and agent\n";
        usage += "\t--step-by-step\tat each iteration the execution pause and wait for ENTER to be pressed\n";
        usage += "\t--iterations-number <n> | -i <n>\n\t\tset the number of iterations of the algorithm\n";
        usage += "\t--update-each-iteration <n> | -U <n>\n\t\tprint the utility value and variables value at every iteration\n";
        usage += "\t--print-factor-graph | -F\n\t\tprint both version of factor graph, original and after the bounded-max-sum phase\n";
        usage += "\t--screw-it-up\tto use the preferences-on-values hack\n";
        usage += "\t--no-bounded-max-sum\tskip the Bounded Max Sum phase\n";
        usage += "\t--report <file>\twrite the report of the execution on <file>";

        System.out.println(usage);
    }
}
