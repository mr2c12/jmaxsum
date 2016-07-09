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
import java.io.IOException;
import java.util.ArrayList;
import powerGrid.PowerGrid;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class GridInstanceCreator {

    public static void main(String[] args) {

               
        //createNInstances(n, numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
        /*int[] Ma = {200, 1000, 2000, 10000, 20000, 100000};
        int[] na = {100, 100, 100, 100, 100, 20};*/


        int[] Ma = {2};
        int[] na = {1};
        double xmeanbase = 0.29;
        double modxmean;
        double modxmean_startvalue=0;

        if (args.length >= 1){
            Ma[0] = Integer.parseInt(args[0]);
        }
        if ( args.length >= 2){
            na[0] = Integer.parseInt(args[1]);
        }
        if ( args.length >= 3){
            modxmean_startvalue = Double.parseDouble(args[2]);
        }
        int n;
        int M;
        double xmean;
        double delta = 0.2;
        int numberOfLoadsForGenerator = 3;
        int R = 2;
        long time = System.currentTimeMillis();
        long oldtime = time;
        String path;
        //System.out.println("[" + time + "ms] Begin.");
        try {
            Runtime.getRuntime().exec("mkdir ./report");


            for (int index = 0; index < Ma.length; index++) {

                M = Ma[index];
                Runtime.getRuntime().exec("mkdir -p ./report/"+M);

                for (modxmean = modxmean_startvalue; modxmean < 11; modxmean++){

                    xmean = xmeanbase + (modxmean/1000.0);


                    Runtime.getRuntime().exec("mkdir -p ./report/"+M+"/"+xmean);

                    for (n = 0; n < na[index]; n++) {
                        time = System.currentTimeMillis();
                        System.out.println("[" + (time-oldtime)/1000.0 + "s] M="+M+" xmean="+xmean+" iteration="+(n+1)+"/"+(na[index]));
                        oldtime=time;
                        // create n instances
                        path = "./report/" + M + "/" + xmean + "/" + n + ".pg";
                        //System.out.println("Starting creating istance.");
                        createInstanceAndSave(1, M, numberOfLoadsForGenerator, R, xmean, delta, path);
                        //System.out.println("Istance created.");
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void createNInstances(int core, int n, int numberOfGenerators, int numberOfLoadsForGenerator, int R, double xmean, double delta) {
        ArrayList<PowerGrid> pgList = new ArrayList<PowerGrid>();
        PowerGrid tempInstance;
        while (n > 0) {
            try {
                //System.out.println("Attempt to create instance " + n);
                tempInstance = new PowerGrid(core, numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
                pgList.add(tempInstance);
                n--;
                //System.out.println("Created instance");
                //System.out.println(tempInstance.toStringFile());

            } catch (NoMoreGeneratorsException ex) {
                System.out.println("No more generators! Retrying!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void createInstanceAndSave(int core, int numberOfGenerators, int numberOfLoadsForGenerator, int R, double xmean, double delta, String path) {
        PowerGrid tempInstance;

        try {
            //System.out.println("New object PowerGrid");
            tempInstance = new PowerGrid(core, numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
            //System.out.println("New object PowerGrid ready, saving..");
            tempInstance.saveToFile(path);
            //System.out.println("New object PowerGrid saved,");


        } catch (NoMoreGeneratorsException ex) {
            System.out.println("No more generators! Retrying!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
