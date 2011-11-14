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

import java.util.HashSet;
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
        System.out.println("initRandom");
        int numberOfGenerators = 5;
        int numberOfLoadsForGenerator = 3;
        int R = 7;
        PowerGrid instance = new PowerGrid();
        instance.initRandom(numberOfGenerators, numberOfLoadsForGenerator, R);

        for (Generator g : instance.getGenerators()){
            assertEquals(g.howManyLoads(), R+numberOfLoadsForGenerator);
        }
    }

}