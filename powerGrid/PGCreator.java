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
package powerGrid;

import function.CO2Simple;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class PGCreator implements Runnable {

    public static final int debug = 0;
    int numberOfGenerators;
    int numberOfLoadsForGenerator;
    int R;
    double xmean;
    double delta;
    HashSet<Generator> generators;
    HashSet<Load> loads;
    boolean isAlive;
    LinkedList<Generator> generators_that_admit_another_link;
    LinkedList<Load> loads_to_link;

    public PGCreator(int numberOfGenerators, int numberOfLoadsForGenerator, int R, double xmean, double delta, HashSet<Generator> generators, HashSet<Load> loads, LinkedList<Generator> generators_that_admit_another_link, LinkedList<Load> loads_to_link) {
        this.numberOfGenerators = numberOfGenerators;
        this.numberOfLoadsForGenerator = numberOfLoadsForGenerator;
        this.R = R;
        this.xmean = xmean;
        this.delta = delta;
        this.generators = generators;
        this.loads = loads;

        this.generators_that_admit_another_link = generators_that_admit_another_link;
        this.loads_to_link = loads_to_link;
    }

    @Override
    public void run() {

        this.isAlive = true;

        Random rnd = new Random();
        Generator gen;
        Load ld;

        for (int i_gen = 0; i_gen < numberOfGenerators; i_gen++) {
            gen = Generator.getNextGenerator(1);
            this.generators.add(gen);

            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Created generator " + i_gen);
                System.out.println("---------------------------------------");
            }

            // create CO2
            //fixme
            gen.setCo2(
                    new CO2Simple(gen.getPower(), 1 + rnd.nextInt(5)) //new CO2Simple(1, 1)
                    );

            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "created co2 for " + i_gen);
                System.out.println("---------------------------------------");
            }


            int numberOfLoadsToLinkAdded = 0;

            // create D loads for each generator
            for (int id_load = 0; id_load < numberOfLoadsForGenerator; id_load++) {

                //ld = Load.getNextLoad(rnd.nextDouble());
                ld = Load.getNextLoad(
                        (rnd.nextDouble() * delta) + (xmean - (delta / 2)));

                if (ld == null){
                    System.out.println("ld is null!");
                    System.exit(-1);
                }


                this.loads.add(ld);


                if (numberOfLoadsToLinkAdded >= R) {
                    // nothing, all done
                } else {
                    if ((numberOfLoadsForGenerator - id_load) == (R - numberOfLoadsToLinkAdded)) {
                        loads_to_link.add(ld);
                        numberOfLoadsToLinkAdded++;
                    } else if ((numberOfLoadsForGenerator - id_load) > (R - numberOfLoadsToLinkAdded)) {
                        if (rnd.nextInt(2) == 1) {
                            loads_to_link.add(ld);
                            numberOfLoadsToLinkAdded++;
                        }
                        else {
                            //not add
                        }
                    }
                }


                // link 'em all!
                ld.addGenerator(gen);
                gen.addLoad(ld);

                if (debug >= 3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "created load " + id_load + " for generator " + i_gen);
                    System.out.println("---------------------------------------");
                }
            }


            /*LinkedList<Load> temp_load_list = new LinkedList<Load>(gen.getLoads());
            for (int i = 0; i < R; i++) {
                loads_to_link.add(
                        temp_load_list.remove(
                        rnd.nextInt(temp_load_list.size())));
            }

            if (gen.howManyLoads() <= numberOfLoadsForGenerator + R) {
                generators_that_admit_another_link.add(gen);
            }*/

            generators_that_admit_another_link.add(gen);


            // all loads ready, next generator..
        }

        this.isAlive = false;
    }

    
    boolean isAlive() {
        return this.isAlive;
    }
}
