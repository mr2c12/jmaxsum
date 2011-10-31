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
import factorgraph.NodeVariable;
import factorgraph.NodeFunction;
import factorgraph.Edge;
import java.util.Collection;
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
public class DisjointSetEdgeTest {


    static DisjointSetTest instance;
    static DisjointSet<Edge> i1,i2,i3;
    static Edge e1,e2,e3;
    static HashSet<Edge> set;
    

    public DisjointSetEdgeTest() {
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
        i2 = new DisjointSet<Edge>(e2);
        i3 = new DisjointSet<Edge>(e3);
        set = new HashSet<Edge>();
        set.add(e1);
        set.add(e2);
        set.add(e3);

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
     * Test of initDS method, of class DisjointSetEdge.
     */
    @Test
    public void testInitDS() {
        System.out.println("initDS");
       
        DisjointSetEdge.initDS(set);
        System.out.println(DisjointSetEdge.toTestString());
        assertEquals(3, DisjointSetEdge.size());
    }

    /**
     * Test of sameDS method, of class DisjointSetEdge.
     */
    @Test
    public void testSameDS() {
        System.out.println("sameDS");
        boolean expResult = false;
        boolean result = DisjointSetEdge.sameDS(e1, e2);
        assertEquals(expResult, result);
        expResult = true;
        result = DisjointSetEdge.sameDS(e1, e1);
        assertEquals(expResult, result);
    }

    /**
     * Test of union method, of class DisjointSetEdge.
     */
    @Test
    public void testUnion() {
        System.out.println("union");

        DisjointSetEdge.union(e1, e2);
        assertEquals(true, DisjointSetEdge.sameDS(e1, e2));
        System.out.println(DisjointSetEdge.toTestString());
    }

    /**
     * Test of toString method, of class DisjointSetEdge.
     */

}