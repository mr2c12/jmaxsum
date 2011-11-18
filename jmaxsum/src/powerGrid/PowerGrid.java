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

import exception.InitializatedException;
import exception.NoMoreGeneratorsException;
import exception.UnInitializatedException;
import function.CO2Simple;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;
import misc.Utils;
import test.DebugVerbosity;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class PowerGrid {

    private static final int debug = DebugVerbosity.debugPowerGrid;
    HashSet<Generator> generators;
    HashSet<Load> loads;

    private PowerGrid() {
        Load.resetLoads();
        Generator.resetGenerators();
        
        this.generators = new HashSet<Generator>();
        this.loads = new HashSet<Load>();

    }

    public PowerGrid(String file) throws InitializatedException, FileNotFoundException, IOException{
        this();
        this.initFromFile(file);
    }

    public PowerGrid(int numberOfGenerators, int numberOfLoadsForGenerator, int R) throws IllegalArgumentException, NoMoreGeneratorsException {
        this();
        this.initRandom(numberOfGenerators, numberOfLoadsForGenerator, R);
    }

    /**
     * Create a random instance using the parameters
     * @param numberOfGenerators M
     * @param numberOfLoadsForGenerator D
     * @param R
     */
    public void initRandom(int numberOfGenerators, int numberOfLoadsForGenerator, int R) throws IllegalArgumentException, NoMoreGeneratorsException {
        if (R > numberOfLoadsForGenerator) {
            throw new IllegalArgumentException("R can't be greater than the number of loads for each generator!");
        }
        Random rnd = new Random();
        Generator gen;
        Load ld;

        // create the generators
        for (int i_gen = 0; i_gen < numberOfGenerators; i_gen++) {
            gen = Generator.getNextGenerator(rnd.nextDouble());
            this.generators.add(gen);

            // create CO2
            gen.setCo2(
                    new CO2Simple(gen.getPower(),
                    1 + rnd.nextInt(5)));


            // create D loads for each generator
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



        for (Generator g : this.generators) {

            // pick R random loads from it
            LinkedList<Load> temp_load_list = new LinkedList<Load>(g.getLoads());
            for (int i = 0; i < R; i++) {
                loads_to_link.add(
                        temp_load_list.remove(
                        rnd.nextInt(temp_load_list.size())));
            }

            if (g.howManyLoads() <= numberOfLoadsForGenerator + R) {
                generators_that_admit_another_link.add(g);
            }


        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Loads to link: "+loads_to_link.size());
                for (Load l : loads_to_link){
                    System.out.println(l);
                }
                System.out.println("Generators to link: "+generators_that_admit_another_link.size());
                for (Generator g : generators_that_admit_another_link){
                    System.out.println(g);
                }
                System.out.println("---------------------------------------");
        }



        while (!loads_to_link.isEmpty()) {
            load_to_link = loads_to_link.remove(
                    rnd.nextInt(
                    loads_to_link.size()));
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "load_to_link selected=" + load_to_link);
                System.out.println("---------------------------------------");
            }

            do {

                if (generators_that_admit_another_link.size()==1 && generators_that_admit_another_link.get(0).hasLoad(load_to_link)){
                    throw new NoMoreGeneratorsException();
                }

                position = rnd.nextInt(generators_that_admit_another_link.size());
                generator_to_link = generators_that_admit_another_link.get(position);

                if (debug >= 3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "generator selected=" + generator_to_link + " while list has "+ generators_that_admit_another_link.size()+ " elements");
                    System.out.println("---------------------------------------");
                }

            } while (generator_to_link.hasLoad(load_to_link));

            load_to_link.addGenerator(generator_to_link);
            generator_to_link.addLoad(load_to_link);

            if (generator_to_link.howManyLoads() == numberOfLoadsForGenerator + R) {
                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Remove "+generator_to_link+ " with "+generator_to_link.howManyLoads()+" loads: "+generator_to_link.getLoadsList());
                        System.out.println("---------------------------------------");
                }
                generators_that_admit_another_link.remove(position);
                //generators_that_admit_another_link.remove(generator_to_link);
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

    public String toStringFile() throws UnInitializatedException {
        if (this.generators == null || this.loads == null) {
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "UnInitialized instance of Power Grid");
                System.out.println("---------------------------------------");
            }
            throw new UnInitializatedException();
        }
        String stringFile = "";
        for (Generator git : this.getGenerators()) {
            stringFile += "G " + git.getId() + " " + git.getPower();
            for (Load git_l : git.getLoads()) {
                stringFile += " " + git_l.getId();
            }
            CO2Simple co = (CO2Simple) git.getCo2();
            stringFile += "\nS " + co.getMult() + " " + co.getMaxPower() + "\n";

        }

        for (Load lit : this.getLoads()) {
            stringFile += "L " + lit.getId() + " " + lit.getRequiredPower();
            for (Generator lit_g : lit.getGenerators()) {
                stringFile += " " + lit_g.getId();
            }
            stringFile += "\n";
        }

        return stringFile;
    }

    public void saveToFile(String file) throws IOException, UnInitializatedException {
        Utils.stringToFile(this.toStringFile(), file);
    }

    public void initFromFile(String file) throws InitializatedException, FileNotFoundException, IOException {
        if (this.generators == null || this.loads == null) {
            throw new InitializatedException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
        String line = in.readLine();

        HashMap<Generator, LinkedList<Integer>> gen_loads = new HashMap<Generator, LinkedList<Integer>>();
        HashMap<Load, LinkedList<Integer>> load_gens = new HashMap<Load, LinkedList<Integer>>();

        StringTokenizer t = null;
        String token = null;
        Generator gen = null;
        Load load = null;
        CO2Simple co;
        int id;
        double power, mult;
        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Beginning parse of file " + file);
            System.out.println("---------------------------------------");
        }
        while (line != null) {
            t = new StringTokenizer(line);
            if (t.hasMoreTokens()) {
                token = t.nextToken();
                if (token.equals("G")) {
                    id = Integer.parseInt(t.nextToken());
                    power = Double.parseDouble(t.nextToken());
                    gen = Generator.getGenerator(id, power);
                    this.generators.add(gen);
                    while (t.hasMoreTokens()) {
                        if (!gen_loads.containsKey(gen)) {
                            gen_loads.put(gen, new LinkedList<Integer>());
                        }
                        gen_loads.get(gen).add(Integer.parseInt(t.nextToken()));
                    }

                } else if (token.equals("S")) {
                    mult = Double.parseDouble(t.nextToken());
                    power = Double.parseDouble(t.nextToken());
                    co = new CO2Simple(power, mult);
                    gen.setCo2(co);
                } else if (token.equals("L")) {
                    id = Integer.parseInt(t.nextToken());
                    power = Double.parseDouble(t.nextToken());
                    load = Load.getLoad(id, power);
                    this.loads.add(load);
                    while (t.hasMoreTokens()) {
                        if (!load_gens.containsKey(load)) {
                            load_gens.put(load, new LinkedList<Integer>());
                        }
                        load_gens.get(load).add(Integer.parseInt(t.nextToken()));
                    }

                }
            }
            line = in.readLine();
        }
        in.close();


        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Report.\nGenerators:");
            for (Generator git : gen_loads.keySet()) {
                    System.out.print("<"+git+"> ");
                for (Integer idt : gen_loads.get(git)) {
                    
                    System.out.print(Load.getLoad(idt, 1));
                }
                System.out.println("");
            }
            System.out.println("---------------------------------------");
        }



        if (debug >= 3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Parse finished. Ready to link.");
            System.out.println("---------------------------------------");
        }

        for (Generator git : gen_loads.keySet()) {
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Links for geerator " + git);
                System.out.println("---------------------------------------");
            }
            for (Integer idt : gen_loads.get(git)) {
                git.addLoad(
                        Load.getLoad(idt, 1));
            }
        }

        for (Load lit : load_gens.keySet()) {
            if (debug >= 3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: " + dclass + " method: " + dmethod + "] " + "Links for load " + lit);
                System.out.println("---------------------------------------");
            }
            for (Integer idt : load_gens.get(lit)) {
                lit.addGenerator(
                        Generator.getGenerator(idt, 1));
            }
        }
    }
}
