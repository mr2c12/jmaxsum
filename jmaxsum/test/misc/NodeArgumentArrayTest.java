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

import factorgraph.NodeArgument;
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
public class NodeArgumentArrayTest {

    public NodeArgumentArrayTest() {
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
     * Test of equals method, of class NodeArgumentArray.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        NodeArgument[] params = new NodeArgument[3];
        params[0] = NodeArgument.getNodeArgument(1);
        params[1] = NodeArgument.getNodeArgument(0);
        params[2] = NodeArgument.getNodeArgument(0);

        NodeArgument[] params2 = new NodeArgument[3];
        params2[0] = NodeArgument.getNodeArgument(1);
        params2[1] = NodeArgument.getNodeArgument(0);
        params2[2] = NodeArgument.getNodeArgument(0);
        
        NodeArgumentArray instance = NodeArgumentArray.getNodeArgumentArray(params);
        NodeArgumentArray instance2 = NodeArgumentArray.getNodeArgumentArray(params2);
        boolean expResult = true;
        boolean result = instance.equals(instance2);
        assertEquals(expResult, result);

        NodeArgument[] params3 = new NodeArgument[3];
        params3[0] = NodeArgument.getNodeArgument(1);
        params3[1] = NodeArgument.getNodeArgument(0);
        params3[2] = NodeArgument.getNodeArgument(1);
        NodeArgumentArray instance3 = NodeArgumentArray.getNodeArgumentArray(params3);
        expResult = false;


        System.out.println("params: "+Utils.toString(params));
        params[1] = NodeArgument.getNodeArgument(1);
        System.out.println("params: "+Utils.toString(params));
        NodeArgumentArray instance1new = NodeArgumentArray.getNodeArgumentArray(params);


        
        System.out.println("Size of map: "+NodeArgumentArray.numberOfNodeArgumentArray());

        result = instance.equals(instance3);
        assertEquals(expResult, result);
        result = instance2.equals(instance3);
        assertEquals(expResult, result);

        result = instance.equals(instance1new);
        assertEquals(false, result);

    }

    /**
     * Test of hashCode method, of class NodeArgumentArray.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        NodeArgument[] params = new NodeArgument[3];
        params[0] = NodeArgument.getNodeArgument(1);
        params[1] = NodeArgument.getNodeArgument(0);
        params[2] = NodeArgument.getNodeArgument(0);

        NodeArgument[] params2 = new NodeArgument[3];
        params2[0] = NodeArgument.getNodeArgument(1);
        params2[1] = NodeArgument.getNodeArgument(0);
        params2[2] = NodeArgument.getNodeArgument(0);

        NodeArgumentArray instance = NodeArgumentArray.getNodeArgumentArray(params);
        NodeArgumentArray instance2 =  NodeArgumentArray.getNodeArgumentArray(params2);
        assertEquals(instance.hashCode(), instance2.hashCode());

        params[0] = NodeArgument.getNodeArgument(0);
        NodeArgumentArray instance1new = NodeArgumentArray.getNodeArgumentArray(params);
        boolean result = (instance.hashCode() == instance1new.hashCode());
        assertEquals(false, (instance.hashCode() == instance1new.hashCode()));

    }

}