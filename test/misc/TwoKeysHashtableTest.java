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
public class TwoKeysHashtableTest {

    public TwoKeysHashtableTest() {
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
     * Test of containsKey method, of class TwoKeysHashtable.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");
        String k1 = "k1";
        Integer k2 = 2;
        TwoKeysHashtable<String, Integer, Float> instance = new TwoKeysHashtable<String, Integer, Float>();
        boolean expResult = true;
        instance.put(k1, 2, new Float(10.0));
        boolean result = instance.containsKey(k1, k2);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class TwoKeysHashtable.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        String k1 = "k1";
        Integer k2 = 2;
        TwoKeysHashtable<String, Integer, Float> instance = new TwoKeysHashtable<String, Integer, Float>();
        Float value = new Float(10.0);
        instance.put(k1, 2, new Float(10.0));
        Float retVal = instance.get(k1, k2);
        assertEquals(value, retVal);

    }

    /**
     * Test of get method, of class TwoKeysHashtable.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        String k1 = "k1";
        Integer k2 = 2;
        TwoKeysHashtable<String, Integer, Float> instance = new TwoKeysHashtable<String, Integer, Float>();
        Float value = new Float(10.0);
        instance.put(k1, 2, new Float(10.0));
        Float retVal = instance.get(k1, k2);
        assertEquals(value, retVal);
    }

    /**
     * Test of size method, of class TwoKeysHashtable.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        TwoKeysHashtable<String, Integer, Float> instance = new TwoKeysHashtable<String, Integer, Float>();
        instance.put("a", 1, new Float(2.0));
        instance.put("b", 2, new Float(2.0));
        instance.put("a", 3, new Float(2.0));
        int expResult = 3;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class TwoKeysHashtable.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        TwoKeysHashtable<String, Integer, Float> instance = new TwoKeysHashtable<String, Integer, Float>();
        instance.put("a", 1, new Float(2.0));
        instance.put("b", 2, new Float(2.0));
        instance.put("a", 3, new Float(2.0));
        instance.clear();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class TwoKeysHashtable.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        TwoKeysHashtable<String, Integer, Float> instance = new TwoKeysHashtable<String, Integer, Float>();
        instance.put("a", 1, new Float(2.0));
        instance.put("b", 2, new Float(2.0));
        instance.put("a", 3, new Float(2.0));
        instance.clear();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
    }

}