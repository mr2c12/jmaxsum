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

import java.util.HashSet;
import factorgraph.Edge;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
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
public class DisjointSetTest {

    static DisjointSet<Edge> instance,i1,i2,i3;
    static Edge e1,e2,e3;

    public DisjointSetTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        e1 = Edge.getEdge(
                NodeFunction.putNodeFunction(1, null),
                NodeVariable.getNodeVariable(1)
                );
        e2 = Edge.getEdge(
                NodeFunction.putNodeFunction(1, null),
                NodeVariable.getNodeVariable(2)
                );
        e3 = Edge.getEdge(
                NodeFunction.putNodeFunction(2, null),
                NodeVariable.getNodeVariable(2)
                );
        i1 = new DisjointSet<Edge>(e1);
        instance = new DisjointSet<Edge>(e1);
        i2 = new DisjointSet<Edge>(e2);
        i3 = new DisjointSet<Edge>(e3);
        /*HashSet<Edge> set = new HashSet<Edge>();
        set.add(e1);
        set.add(e2);
        set.add(e3);
        instance = new DisjointSet<Edge>*/
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
     * Test of equals method, of class DisjointSet.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        boolean expResult = true;
        boolean result = instance.equals(i1);
        assertEquals(expResult, result);
        
    }


    /**
     * Test of find method, of class DisjointSet.
     */
    @Test
    public void testFind() {
        System.out.println("find");
        boolean res = i1.find().equals(e1);
        assertTrue(res);
    }

    /**
     * Test of size method, of class DisjointSet.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        
        int expResult = 1;
        int result = instance.size();
        System.out.println("Instance size: "+result +"\ninstance: "+instance);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of union method, of class DisjointSet.
     */
    @Test
    public void testUnion() {
        System.out.println("union");
        System.out.println("union i1");
        DisjointSet resultDS = instance.union(i1);
        System.out.println("instance "+instance);
        System.out.println("i1 "+i1);
        System.out.println("resultDS"+resultDS);
        System.out.println("union i2");
        resultDS = instance.union(i2);
        System.out.println("instance "+instance);
        System.out.println("i2 "+i2);
        System.out.println("resultDS"+resultDS);
        System.out.println("union i3");
        resultDS = instance.union(i3);
        System.out.println("instance "+instance);
        System.out.println("i3 "+i3);
        System.out.println("resultDS"+resultDS);

        assertEquals(3, instance.size());
        
    }

    /**
     * Test of toString method, of class DisjointSet.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        System.out.println(instance.toString());
        assertTrue(true);
    }

}