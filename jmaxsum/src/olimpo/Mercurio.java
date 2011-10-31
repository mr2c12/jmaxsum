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

import boundedMaxSum.InstanceCloner;
import exception.InvalidInputFileException;
import exception.PostServiceNotSetException;
import hacks.ScrewerUp;
import jargs.gnu.CmdLineParser;
import java.io.File;
import system.COP_Instance;
import system.Core;

/**
 * Command-line control interface to maxsum
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Mercurio {


    public static void main( String[] args ) {

        CmdLineParser parser = new CmdLineParser();
        
        CmdLineParser.Option oldformat = parser.addBooleanOption("old-format");
        CmdLineParser.Option iterationnumber = parser.addIntegerOption('i', "iterations-number");
        CmdLineParser.Option stepbystep = parser.addBooleanOption("step-by-step");
        CmdLineParser.Option report = parser.addStringOption('R', "report");
        CmdLineParser.Option screwup = parser.addBooleanOption("screw-it-up");
        CmdLineParser.Option updateel = parser.addBooleanOption('U',"update-each-iteration");

        try {
            parser.parse(args);
        }
        catch ( CmdLineParser.OptionException e ) {
            System.err.println(e.getMessage());
            printUsage();
            System.exit(2);
        }

        
        Boolean oldformatV = (Boolean)parser.getOptionValue(oldformat,false);
        Boolean stepbystepV = (Boolean)parser.getOptionValue(stepbystep,false);
        Boolean screwupV = (Boolean)parser.getOptionValue(screwup,false);
        Boolean updateelV = (Boolean)parser.getOptionValue(updateel,false);
        // what if no report? -> null!
        String reportV = (String)parser.getOptionValue(report);

        Integer iterationsV = (Integer)parser.getOptionValue(iterationnumber, new Integer(10));

        String[] otherArgs = parser.getRemainingArgs();
        String filepath ="";
        try {
            if ((otherArgs.length == 1) && (new File(otherArgs[0]).exists())){
                filepath = otherArgs[0];
            }
            else {
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

            COP_Instance original_cop = Cerbero.getInstanceFromFile(filepath,oldformatV);

            InstanceCloner ic = new InstanceCloner(original_cop);
            COP_Instance cop = ic.getClonedInstance();

            ScrewerUp screwerup = null;
            
            if(screwupV){
                screwerup = new ScrewerUp(cop);
                cop = screwerup.screwItUp();
            }

            // time for BoundedMaxSum to do his best!

            Core core = new Core(cop);

            core.setIterationsNumber(iterationsV);

            core.setStepbystep(stepbystepV);

            core.setUpdateOnlyAtEnd(!updateelV);

            if (reportV != null){
                core.pleaseReport(reportV);
            }

            core.solve();

            /*if(screwupV){
                cop = screwerup.fixItUp();
            }*/
            
            core.conclude();

            ic.setOriginalVariablesValues();

            System.out.println("Conclusion on original cop:\n"+cop.status());

        } catch (PostServiceNotSetException pse) {
            System.out.println("Fatal problem in the process of initialization: no PostService set!");
        } catch (InvalidInputFileException iife) {
            System.out.println("The given file is not a valid input for this program!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    public static void printUsage(){
        String usage = "Usage: java -jar maxSum.jar [-options] inputfile\n";
        usage += "where inputfile is the path of the file containing an instance of a COP problem\n";
        usage += "and where options include:\n";
        usage += "\t--old-format\tif the input file does not specify function id and agent\n";
        usage += "\t--step-by-step\tat each iteration the execution pause and wait for ENTER to be pressed\n";
        usage += "\t--iterations-number <n> | -i <n>\n\t\tset the number of iterations of the algorithm\n";
        usage += "\t--update-each-iteration <n> | -U <n>\n\t\tprint the utility value and variables value at every iteration\n";
        usage += "\t--screw-it-up\tto use the preferences-on-values hack\n";
        usage += "\t--report <file>\twrite the report of the execution on <file>";

        System.out.println(usage);
    }
}
