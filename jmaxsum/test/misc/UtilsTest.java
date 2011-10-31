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

package misc;

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
public class UtilsTest {

    public UtilsTest() {
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
     * Test of sumArray method, of class Utils.
     */
    @Test
    public void testSumArray() {
        System.out.println("sumArray");
        double[] a1 = null;
        double[] a2 = null;
        double[] expResult = null;
        double[] result = Utils.sumArray(a1, a2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Utils.
     */
    @Test
    public void testToString_ObjectArr() {
        System.out.println("toString");
        Object[] a = null;
        String expResult = "";
        String result = Utils.toString(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Utils.
     */
    @Test
    public void testToString_doubleArr() {
        System.out.println("toString");
        double[] a = null;
        String expResult = "";
        String result = Utils.toString(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Utils.
     */
    @Test
    public void testToString_DoubleArr() {
        System.out.println("toString");
        Double[] a = null;
        String expResult = "";
        String result = Utils.toString(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Utils.
     */
    @Test
    public void testToString_intArr() {
        System.out.println("toString");
        int[] a = null;
        String expResult = "";
        String result = Utils.toString(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of enumerate method, of class Utils.
     */
    @Test
    public void testEnumerate_intArr_intArr() {
        System.out.println("enumerate");
        int[] v = null;
        int[] max = null;
        Utils.enumerate(v, max);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of enumerate method, of class Utils.
     */
    @Test
    public void testEnumerate_intArr() {
        System.out.println("enumerate");
        int[] max = {2,2,2,2,2};
        Utils.enumerate(max);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stringToFile method, of class Utils.
     */
    @Test
    public void testStringToFile() throws Exception {
        System.out.println("stringToFile");
        String string = "";
        String file = "";
        Utils.stringToFile(string, file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of opArray method, of class Utils.
     */
    @Test
    public void testOpArray() throws Exception {
        System.out.println("opArray");
        int operation = 0;
        Double[] a1 = null;
        Double[] a2 = null;
        Double[] expResult = null;
        Double[] result = Utils.opArray(operation, a1, a2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}