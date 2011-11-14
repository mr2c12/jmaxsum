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
        LinkedList<Generator> generators_that_admit_another_link = new LinkedList<Generator>();

        LinkedList<Load> loads_to_link = new LinkedList<Load>();
        Load load_to_link;
        Generator generator_to_link;
        int position;



        for (Generator g: this.generators){

            // pick R random loads from it
            LinkedList<Load> temp_load_list = new LinkedList<Load>(g.getLoads());
            for (int i = 0; i < R; i++) {
                loads_to_link.add(
                        temp_load_list.remove(
                            rnd.nextInt(temp_load_list.size())
                            )
                        );
            }

            if (g.howManyLoads()<=numberOfLoadsForGenerator+R) {
                generators_that_admit_another_link.add(g);
            }


        }



        while(!loads_to_link.isEmpty()) {
            load_to_link = loads_to_link.remove(
                    rnd.nextInt(
                        loads_to_link.size()
                    ));
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "load_to_link selected="+load_to_link);
                    System.out.println("---------------------------------------");
            }
            
            do{
                position = rnd.nextInt(generators_that_admit_another_link.size());
                generator_to_link = generators_that_admit_another_link.get(position);

                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "generator selected="+generator_to_link);
                        System.out.println("---------------------------------------");
                }

            } while (generator_to_link.hasLoad(load_to_link));

            load_to_link.addGenerator(generator_to_link);
            generator_to_link.addLoad(load_to_link);

            if (generator_to_link.howManyLoads()==numberOfLoadsForGenerator+R){
                generators_that_admit_another_link.remove(position);
            }

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

    public HashSet<Generator> getGenerators() {
        return generators;
    }

    public HashSet<Load> getLoads() {
        return loads;
    }



}
