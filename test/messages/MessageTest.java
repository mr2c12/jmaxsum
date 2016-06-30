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
public class MessageTest {

    public MessageTest() {
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
     * Test of size method, of class Message.
     */
    //@Test
    public void testSize() {
        System.out.println("size");
        Message instance = new MessageImpl();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);

    }

    /**
     * Test of getValue method, of class Message.
     */
    //@Test
    public void testGetValue() {
        System.out.println("getValue");
        int position = 0;
        Message instance = new MessageImpl();
        double expResult = 0.0;
        double result = instance.getValue(position);
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of setValue method, of class Message.
     */
    //@Test
    public void testSetValue() {
        System.out.println("setValue");
        int position = 0;
        double value = 0.0;
        Message instance = new MessageImpl();
        instance.setValue(position, value);

    }

    /**
     * Test of equals method, of class Message.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        double[] content = {1.0,2.0};
        MessageQArrayDouble qa12 = new MessageQArrayDouble(null, null, content);

        double[] array = {2.0,2.0};
        MessageQArrayDouble qa22 = new MessageQArrayDouble(null, null, array);

        assertEquals(false, qa12.equals(qa22));

        MessageRArrayDouble ra22 = new MessageRArrayDouble(null, null, array);

        assertEquals(false, qa22.equals(ra22));

        MessageQArrayDouble qb22 = new MessageQArrayDouble(null, null, array);

        assertEquals(true, qb22.equals(qa22));
    }

    /**
     * Test of getMessage method, of class Message.
     */
    //@Test
    public void testGetMessage() {


    }

    /**
     * Test of setMessage method, of class Message.
     */
    //@Test
    public void testSetMessage() {
        System.out.println("setMessage");
        MessageContent message = null;
        Message instance = new MessageImpl();
        instance.setMessage(message);

    }

    /**
     * Test of equalContent method, of class Message.
     */
    @Test
    public void testEqualContent() {
        double[] array = {2.0,2.0};

        MessageQArrayDouble qa22 = new MessageQArrayDouble(null, null, array);

        MessageRArrayDouble ra22 = new MessageRArrayDouble(null, null, array);

        assertEquals(true, qa22.equalContent(ra22));
    }

    /**
     * Test of setSender method, of class Message.
     */
    //@Test
    public void testSetSender() {
        System.out.println("setSender");
        Node n = null;
        Message instance = new MessageImpl();
        instance.setSender(n);

    }

    /**
     * Test of setReceiver method, of class Message.
     */
    //@Test
    public void testSetReceiver() {
        System.out.println("setReceiver");
        Node n = null;
        Message instance = new MessageImpl();
        instance.setReceiver(n);

    }

    /**
     * Test of getReceiver method, of class Message.
     */
    //@Test
    public void testGetReceiver() {
        System.out.println("getReceiver");
        Message instance = new MessageImpl();
        Node expResult = null;
        Node result = instance.getReceiver();
        assertEquals(expResult, result);

    }

    /**
     * Test of getSender method, of class Message.
     */
    //@Test
    public void testGetSender() {
        System.out.println("getSender");
        Message instance = new MessageImpl();
        Node expResult = null;
        Node result = instance.getSender();
        assertEquals(expResult, result);

    }

    public class MessageImpl implements Message {

        public int size() {
            return 0;
        }

        public double getValue(int position) {
            return 0.0;
        }

        public void setValue(int position, double value) {
        }

        public boolean equals(Message m) {
            return false;
        }

        public MessageContent getMessage() {
            return null;
        }

        public void setMessage(MessageContent message) {
        }

        public boolean equalContent(Message other) {
            return false;
        }

        public void setSender(Node n) {
        }

        public void setReceiver(Node n) {
        }

        public Node getReceiver() {
            return null;
        }

        public Node getSender() {
            return null;
        }
    }

}