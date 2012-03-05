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

package test;

import exception.InitializatedException;
import exception.PostServiceNotSetException;
import exception.UnInitializatedException;
import java.io.FileNotFoundException;
import java.io.IOException;
import olimpo.Athena;
import powerGrid.PowerGrid;
import system.SARecycler;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class PGToData {

    public static void main (String[] args){
        if (args.length < 3 ){
            System.out.println("Please enter instance type, instance file and report path.");
            System.exit(0);
        }
        try {
            PowerGrid pg = new PowerGrid(args[1]);

            if (args[0].equalsIgnoreCase("sarecycler")){
                //SARecycler sa = new SARecycler(10, 25000, pg.getCopM(), args[2], "modinf");
                
                int kmax = Integer.valueOf(args[3]);
                double tmax = Double.valueOf(args[4]);
                String report = args[2];
                SARecycler sa = new SARecycler(20, kmax, pg.getCopM(), report,"modinf", tmax);
            }
            else if (args[0].equalsIgnoreCase("sarecyclernoinf")){


                int kmax = Integer.valueOf(args[3]);
                double tmax = Double.valueOf(args[4]);
                int repetition = Integer.valueOf(args[5]);
                String report = args[2];
                System.out.println("Running SARecycler for "+repetition+" repetition, with kmax="+kmax+", tmax="+tmax+", report: "+report);
                SARecycler sa = new SARecycler(repetition, kmax, pg.getCopM(), report,"modinf", tmax);
            }
            else {
                Athena athena = new Athena(pg.getCopM(), "min", "sum");
                athena.pleaseReport(args[2]);
                athena.setUpdateOnlyAtEnd(true);
                athena.setIterationsNumber(300);
                athena.solve();
            }

            
            
            
        } catch (PostServiceNotSetException ex) {
            ex.printStackTrace();
        } catch (UnInitializatedException ex) {
            ex.printStackTrace();
        } catch (InitializatedException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
