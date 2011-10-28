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

package factorgraph;

import function.FunctionEvaluator;
import java.util.ArrayList;
import java.util.HashMap;
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
public class EdgeTest {

    static NodeFunction source;
    static NodeVariable dest;
    static Edge instance;

    public EdgeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        source = NodeFunction.putNodeFunction(1, null);
        dest = NodeVariable.getNodeVariable(1);
        instance = Edge.getEdge(EdgeTest.source, EdgeTest.dest);
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
     * Test of getDest method, of class Edge.
     */
    @Test
    public void testGetDest() {
        System.out.println("getDest");
        Node result = instance.getDest();
        assertEquals(result, NodeVariable.getNodeVariable(1));
        
    }



    /**
     * Test of getSource method, of class Edge.
     */
    @Test
    public void testGetSource() {
        System.out.println("getSource");
        Node result = instance.getSource();
        assertEquals(NodeFunction.putNodeFunction(1, null), result);
    }

    /**
     * Test of equals method, of class Edge.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Edge instance2 = Edge.getEdge(source, dest);
        boolean expResult = true;
        boolean result = instance.equals(instance2);
        assertEquals(expResult, result);
    }



}