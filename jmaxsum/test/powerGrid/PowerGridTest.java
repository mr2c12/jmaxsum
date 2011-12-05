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
import exception.PostServiceNotSetException;
import exception.UnInitializatedException;
import exception.VariableNotSetException;
import java.util.Random;
import java.util.HashSet;
import olimpo.Athena;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class PowerGridTest {

    public PowerGridTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of initRandom method, of class PowerGrid.
     */
    @Test
    public void testInitRandom() {
        try {
            System.out.println("initRandom");
            int numberOfGenerators = 5;
            int numberOfLoadsForGenerator = 3;
            int R = 2;
            double xmean = 0.2;
            double delta = 0.2;
            PowerGrid instance = new PowerGrid(numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
            System.out.println("Instance created:\n" + instance.toStringFile());
            for (Generator g : instance.getGenerators()) {
                assertEquals(g.howManyLoads(), R + numberOfLoadsForGenerator);
            }
        } catch (InitializatedException ex) {
            ex.printStackTrace();
        } catch (UnInitializatedException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (NoMoreGeneratorsException ex) {
            System.out.println("No more generators, sorry!");
        }
    }

    /**
     * Test of saveToFile method, of class PowerGrid.
     */
    //@Test
    public void testSaveToFile() throws Exception {
        System.out.println("saveToFile");
        String file = "/home/mik/Documenti/univr/Ragionamento Automatico/stage/powerGrid_instances/";
        Random rnd = new Random();
        file += rnd.nextInt(10000);
        file += ".pg";
        int numberOfGenerators = 5;
        int numberOfLoadsForGenerator = 3;
        int R = 2;
        double xmean = 0.5;
        double delta = 0.1;
        PowerGrid instance = new PowerGrid(numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);
        instance.saveToFile(file);

    }

    /**
     * Test of initFromFile method, of class PowerGrid.
     */
    //@Test
    public void testInitFromFile() throws Exception {
        System.out.println("initFromFile");
        String file = "/home/mik/Documenti/univr/Ragionamento Automatico/stage/powerGrid_instances/598.pg";
        PowerGrid instance = new PowerGrid(file);
        System.out.println("string of parsed file:\n" + instance.toStringFile());
    }

    //@Test
    public void testGetCop() throws PostServiceNotSetException {
        System.out.println("getCop");
        try {
            int numberOfGenerators = 3;
            int numberOfLoadsForGenerator = 4;
            int R = 1;
            double xmean = 0.5;
            double delta = 0.1;
            PowerGrid instance = new PowerGrid(numberOfGenerators, numberOfLoadsForGenerator, R, xmean, delta);

            System.out.println("Instance to string:\n" + instance.toStringFile());

            System.out.println("Instance is initializated: " + instance.isInitializated());

            // note: toStringFile does NOT work for these NodeArguments.
            System.out.println("COP:\n" + instance.getCop().toStringFile());

            Athena solver = new Athena(instance.getCop(), "min", "sum");
            solver.setIterationsNumber(100);
            solver.solve();
            solver.conclude();
            System.out.println("Value: " + instance.getCop().actualValue());


        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal argument Exception");
        } catch (NoMoreGeneratorsException ex) {
            System.out.println("No more generators Exception");
        } catch (InitializatedException ex) {
            System.out.println("Initializated Exception");
        } catch (UnInitializatedException ex) {
            System.out.println("UnInitializated Exception");
        } catch (VariableNotSetException ex) {
            System.out.println("Variable not set");
        }

    }
}
