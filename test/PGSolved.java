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
import exception.NoMoreGeneratorsException;
import exception.PostServiceNotSetException;
import exception.UnInitializatedException;
import hacks.ScrewerUp;
import olimpo.Athena;
import powerGrid.PowerGrid;
import system.COP_Instance;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class PGSolved {

    public static void main(String[] args){
        try {
            System.out.println("Generating powergrid.");
            //PowerGrid pg = new PowerGrid(2, 3, 2, 0.35, 0.02);
            //PowerGrid pg = new PowerGrid("./dati_08_12_11/pg_200_1000_2000/1000/0.29/0.pg");//"trovalerrore.pg");
            //PowerGrid pg = new PowerGrid("/home/mik/NetBeansProjects/jMaxSumSVN/dati_08_12_11/pg_200_1000_2000/200/0.29/2.pg");
            PowerGrid pg = new PowerGrid("/home/mik/NetBeansProjects/jMaxSumSVN/232.pg");
            //System.out.println(pg.toStringFile());
            //System.out.println("Generation complete.");

            //ScrewerUp screwerup = null;

            COP_Instance cop = pg.getMCCop();

            /*pg.saveToFile("/home/mik/NetBeansProjects/jMaxSumSVN/232.pg");
            System.exit(0);*/

            System.out.println(cop.toStringFile());

            System.exit(0);
               /*screwerup = new ScrewerUp(cop);
                cop = screwerup.screwItUp();*/


            //System.out.println(cop.toStringFile());
            Athena athena = new Athena(cop, "min", "sum");

            athena.setIterationsNumber(30000);
            athena.setUpdateOnlyAtEnd(true);
            athena.setStepbystep(false);

            System.out.println("Start solver!");
            athena.solve();

            /*System.out.println("Fixing..");
            cop = screwerup.fixItUp();*/
            //System.out.println("Fixed cop:\n"+cop.toStringFile());

        } catch (PostServiceNotSetException ex) {
            ex.printStackTrace();
        } catch (UnInitializatedException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
