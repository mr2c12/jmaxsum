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

import exception.NoMoreGeneratorsException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import test.DebugVerbosity;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class PowerGrid {

    private static final int debug = DebugVerbosity.debugPowerGrid;
    HashSet<Generator> generators;
    HashSet<Load> loads;

    public PowerGrid() {
        this.generators = new HashSet<Generator>();
        this.loads = new HashSet<Load>();
    }

    /**
     * Create a random instance using the parameters
     * @param numberOfGenerators M
     * @param numberOfLoadsForGenerator D
     * @param R
     */
    public void initRandom(int numberOfGenerators, int numberOfLoadsForGenerator, int R) throws IllegalArgumentException {
        if (R > numberOfLoadsForGenerator) {
            throw new IllegalArgumentException("R can't be greater than the number of loads for each generator!");
        }
        Random rnd = new Random();
        Generator gen;
        Load ld;

        for (int i_gen = 0; i_gen < numberOfGenerators; i_gen++) {
            gen = Generator.getNextGenerator(rnd.nextDouble());
            this.generators.add(gen);

            for (int id_load = 0; id_load < numberOfLoadsForGenerator; id_load++) {
                ld = Load.getNextLoad(rnd.nextDouble());
                this.loads.add(ld);

                // link 'em all!
                ld.addGenerator(gen);
                gen.addLoad(ld);
            }
        }

        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Created " + this.generators.size() + " generators.");
            for (Generator g : this.generators) {
                System.out.println(g + "\n" + g.getLoadsList());
            }
            System.out.println("---------------------------------------");
        }



        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Created " + this.loads.size() + " loads.");
            for (Load l : this.loads) {
                System.out.println(l + "\n" + l.getGeneratorsList());
            }
            System.out.println("---------------------------------------");
        }

        // phase 2: R

        //HashSet<Generator> generators_biggerable = (HashSet<Generator>) this.generators.clone();
        LinkedList<Generator> generators_that_admit_another_link = new LinkedList<Generator>(this.generators);

        LinkedList<Load> loads_where_pick_up_R;
        Load load_to_link;
        Generator generator_to_link;
        int position;

        try {

            LinkedList<HashSet<Load>> list_of_loads_set = new LinkedList<HashSet<Load>>();

            // TODO: error!
            for (Generator it_gen : this.generators) {
                list_of_loads_set.add(it_gen.getLoads());
            }

            // TODO: ERROR
            /*for (Generator it_gen : this.generators) {
                loads_where_pick_up_R = new LinkedList<Load>(it_gen.getLoads());*/
            for (HashSet<Load> set : list_of_loads_set ){
                loads_where_pick_up_R = new LinkedList<Load>(set);
                // pick R loads from its
                for (int r = 0; r < R; r++) {
                    position = rnd.nextInt(loads_where_pick_up_R.size());
                    load_to_link = loads_where_pick_up_R.remove(position);

                    if (debug >= 3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "trying to link " + load_to_link);
                        System.out.println("There are " + generators_that_admit_another_link.size() + " more generators available");
                        System.out.println("---------------------------------------");
                    }

                    do {
                        if (generators_that_admit_another_link.isEmpty()) {
                            throw new NoMoreGeneratorsException();
                        }
                        // choose a valid Generator to link to load to link
                        position = rnd.nextInt(generators_that_admit_another_link.size());
                        generator_to_link = generators_that_admit_another_link.remove(position);
                    } while (
                            (generator_to_link.howManyLoads() > numberOfLoadsForGenerator + R)
                            ||
                            (generator_to_link.hasLoad(load_to_link))
                            )
                            ;

                    if (debug>=3) {
                            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                            System.out.println("---------------------------------------");
                            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "now linking " + load_to_link + " with "+generator_to_link);
                            System.out.println("---------------------------------------");
                    }

                    // now link 'em
                    load_to_link.addGenerator(generator_to_link);
                    generator_to_link.addLoad(load_to_link);



                }
            }

        } catch (NoMoreGeneratorsException ex) {
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Exception generated");
                System.out.println("---------------------------------------");
            }
            // finish!
        }

        // and now we have:
        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Created " + this.generators.size() + " generators.");
            for (Generator g : this.generators) {
                System.out.println(g + "\n" + g.getLoadsList());
            }
            System.out.println("---------------------------------------");
        }



        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Created " + this.loads.size() + " loads.");
            for (Load l : this.loads) {
                System.out.println(l + "\n" + l.getGeneratorsList());
            }
            System.out.println("---------------------------------------");
        }

    }
}
