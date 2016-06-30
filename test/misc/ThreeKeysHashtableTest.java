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
public class ThreeKeysHashtableTest {

    public ThreeKeysHashtableTest() {
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
     * Test of containsKey method, of class ThreeKeysHashtable.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");
        ThreeKeysHashtable<String, String, String, Integer> instance = new ThreeKeysHashtable<String, String, String, Integer>();
        boolean expResult = true;
        String k1, k2, k3;
        k1="1";
        k2="2";
        k3="3";
        Integer v = 4;
        instance.put(k1, k2, k3, v);
        boolean result = instance.containsKey(k1, k2, k3);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class ThreeKeysHashtable.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        ThreeKeysHashtable<String, String, String, Integer> instance = new ThreeKeysHashtable<String, String, String, Integer>();
        boolean expResult = true;
        String k1, k2, k3;
        k1="1";
        k2="2";
        k3="3";
        Integer v = 4;
        instance.put(k1, k2, k3, v);
        assertTrue(true);
    }

    /**
     * Test of get method, of class ThreeKeysHashtable.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        ThreeKeysHashtable<String, String, String, Integer> instance = new ThreeKeysHashtable<String, String, String, Integer>();
        boolean expResult = true;
        String k1, k2, k3;
        k1="1";
        k2="2";
        k3="3";
        Integer v = 4;
        instance.put(k1, k2, k3, v);
        Integer result = instance.get(k1, k2, k3);
        assertEquals(v, result);
    }

    /**
     * Test of size method, of class ThreeKeysHashtable.
     */
    @Test
    public void testSize() {
        System.out.println("get");
        ThreeKeysHashtable<String, String, String, Integer> instance = new ThreeKeysHashtable<String, String, String, Integer>();
        
        String k1, k2, k3;
        k1="1";
        k2="2";
        k3="3";
        Integer v = 4;
        instance.put(k1, k2, k3, v);
        instance.put(k1, "1", k3, v);
        instance.put(k1, k2, "1", v);
        int expResult = 3;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class ThreeKeysHashtable.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        ThreeKeysHashtable<String, String, String, Integer> instance = new ThreeKeysHashtable<String, String, String, Integer>();

        String k1, k2, k3;
        k1="1";
        k2="2";
        k3="3";
        Integer v = 4;
        instance.put(k1, k2, k3, v);
        instance.put(k1, "1", k3, v);
        instance.put(k1, k2, "1", v);
        instance.clear();
        assertTrue(instance.size() == 0);
    }

    /**
     * Test of isEmpty method, of class ThreeKeysHashtable.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        System.out.println("clear");
        ThreeKeysHashtable<String, String, String, Integer> instance = new ThreeKeysHashtable<String, String, String, Integer>();

        String k1, k2, k3;
        k1="1";
        k2="2";
        k3="3";
        Integer v = 4;
        instance.put(k1, k2, k3, v);
        instance.put(k1, "1", k3, v);
        instance.put(k1, k2, "1", v);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

}