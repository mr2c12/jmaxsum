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
     * Test of enumerate method, of class Utils.
     */
    //@Test
    public void testEnumerate_intArr() {
        System.out.println("enumerate");
        int[] max = {2,2,2,2,2};
        Utils.enumerate(max);

    }


    /**
     * Test of shuffleArrayFY method, of class Utils.
     */
    @Test
    public void testShuffleArrayFY() {
        System.out.println("shuffleArrayFY");
        Object[] a = {0,1,2,3,4,5,6,7,8,9};
        Object[] original_a = {0,1,2,3,4,5,6,7,8,9};
        Object[] result = Utils.shuffleArrayFY(a);

        assertArrayEquals(a, result);
        System.out.println("a is:"+Utils.toString(a));

    }





}