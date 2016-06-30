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

package messages;

import factorgraph.Node;
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
public class MessageArrayDoubleTest {

    public MessageArrayDoubleTest() {
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
     * Test of getMessage method, of class MessageArrayDouble.
     */
    @Test
    public void testGetMessage() {
        System.out.println("getMessage");
        Double[] content = new Double[2];//{1.0,2.0};
        content[0]=1.0;
        content[1]=2.0;
        MessageContentArrayDouble mc = new MessageContentArrayDouble(content);
        MessageArrayDouble instance = new MessageArrayDouble(null, null, mc);
        MessageContent expResult = null;
        MessageContent result = instance.getMessage();
        assertEquals(expResult, result);

    }

    /**
     * Test of setMessage method, of class MessageArrayDouble.
     */
    @Test
    public void testSetMessage() {
        System.out.println("setMessage");
        MessageContent message = null;
        MessageArrayDouble instance = null;
        instance.setMessage(message);

    }

    /**
     * Test of getValue method, of class MessageArrayDouble.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        int position = 0;
        MessageArrayDouble instance = null;
        double expResult = 0.0;
        double result = instance.getValue(position);
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of setValue method, of class MessageArrayDouble.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        int position = 0;
        double value = 0.0;
        MessageArrayDouble instance = null;
        instance.setValue(position, value);

    }

    /**
     * Test of size method, of class MessageArrayDouble.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        MessageArrayDouble instance = null;
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);

    }

    /**
     * Test of toString method, of class MessageArrayDouble.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        MessageArrayDouble instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);

    }

    /**
     * Test of equals method, of class MessageArrayDouble.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Message m = null;
        MessageArrayDouble instance = null;
        boolean expResult = false;
        boolean result = instance.equals(m);
        assertEquals(expResult, result);

    }

    /**
     * Test of equalContent method, of class MessageArrayDouble.
     */
    @Test
    public void testEqualContent() {
        System.out.println("equalContent");
        Message other = null;
        MessageArrayDouble instance = null;
        boolean expResult = false;
        boolean result = instance.equalContent(other);
        assertEquals(expResult, result);

    }

    /**
     * Test of setSender method, of class MessageArrayDouble.
     */
    @Test
    public void testSetSender() {
        System.out.println("setSender");
        Node n = null;
        MessageArrayDouble instance = null;
        instance.setSender(n);

    }

    /**
     * Test of setReceiver method, of class MessageArrayDouble.
     */
    @Test
    public void testSetReceiver() {
        System.out.println("setReceiver");
        Node n = null;
        MessageArrayDouble instance = null;
        instance.setReceiver(n);

    }

    /**
     * Test of getReceiver method, of class MessageArrayDouble.
     */
    @Test
    public void testGetReceiver() {
        System.out.println("getReceiver");
        MessageArrayDouble instance = null;
        Node expResult = null;
        Node result = instance.getReceiver();
        assertEquals(expResult, result);

    }

    /**
     * Test of getSender method, of class MessageArrayDouble.
     */
    @Test
    public void testGetSender() {
        System.out.println("getSender");
        MessageArrayDouble instance = null;
        Node expResult = null;
        Node result = instance.getSender();
        assertEquals(expResult, result);

    }

}