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

import java.util.ArrayList;
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
public class NodeVariableTest {
        NodeVariable instance;
        NodeArgument n1;
        NodeArgument n2;

    public NodeVariableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = NodeVariable.getNodeVariable(1);
                //new NodeVariable();
        n1 = NodeArgument.getNodeArgument("x");
        instance.addValue(n1);
        n2 = NodeArgument.getNodeArgument("y");
        instance.addValue(n2);
    }

    @After
    public void tearDown() {
        instance.clearValues();
    }


    /**
     * Test of addValue method, of class NodeVariable.
     */
    @Test
    public void testAddValue() {
        System.out.println("addValue");
        NodeArgument v = NodeArgument.getNodeArgument("z");
        
        instance.addValue(v);

        assertEquals(3, instance.size());
    }

    /**
     * Test of getValues method, of class NodeVariable.
     */
    @Test
    public void testGetValues() {
        System.out.println("getValues");
        ArrayList<NodeArgument> expResult = new ArrayList<NodeArgument>();
        expResult.add(NodeArgument.getNodeArgument("x"));
        expResult.add(NodeArgument.getNodeArgument("y"));
        ArrayList<NodeArgument> result = instance.getValues();
        assertEquals(expResult, result);
    }

    /**
     * Test of addNeighbour method, of class NodeVariable.
     */
    //@Test
    public void testAddNeighbour() {
        System.out.println("addNeighbour");
        Node x = null;
        NodeVariable instance = NodeVariable.getNodeVariable(1);
        instance.addNeighbour(x);
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class NodeVariable.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        //NodeVariable instance = new NodeVariable();
        int expResult = 2;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of numberOfArgument method, of class NodeVariable.
     */
    @Test
    public void testNumberOfArgument() {
        System.out.println("numberOfArgument");
        NodeArgument node = NodeArgument.getNodeArgument("y");
        //NodeVariable instance = new NodeVariable();
        int expResult = 1;
        int result = instance.numberOfArgument(node);
        assertEquals(expResult, result);
    }

    /**
     * Test of getArgument method, of class NodeVariable.
     */
    @Test
    public void testGetArgument() {
        System.out.println("getArgument");
        int index = 1;
        //NodeVariable instance = new NodeVariable();
        NodeArgument expResult = n2;
        NodeArgument result = instance.getArgument(index);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNeighbour method, of class NodeVariable.
     */
    //@Test
    public void testGetNeighbour() {
        System.out.println("getNeighbour");
        NodeVariable instance = NodeVariable.getNodeVariable(1);
        HashSet expResult = null;
        HashSet result = instance.getNeighbour();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

}