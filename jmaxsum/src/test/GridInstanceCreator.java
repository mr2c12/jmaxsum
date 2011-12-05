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
import exception.UnInitializatedException;
import java.util.ArrayList;
import powerGrid.PowerGrid;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class GridInstanceCreator {


    public static void main(String[] args){
        //createNInstances(n, numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
        createNInstances(2,1, 100000, 3, 2, 0.28, 0.2);

    }

    public static void createNInstances(int core, int n,int numberOfGenerators, int numberOfLoadsForGenerator, int R, double xmean, double delta){
        ArrayList<PowerGrid> pgList = new ArrayList<PowerGrid>();
        PowerGrid tempInstance;
        while (n>0){
            try {
                System.out.println("Attempt to create instance "+n);
                tempInstance = new PowerGrid(core,numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
                pgList.add(tempInstance);
                n--;
                System.out.println("Created instance");
                //System.out.println(tempInstance.toStringFile());
            
            } catch (NoMoreGeneratorsException ex) {
                System.out.println("No more generators! Retrying!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

}
