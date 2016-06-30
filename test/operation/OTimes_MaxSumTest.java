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

package operation;

import java.util.LinkedList;
import messages.MessageFactoryArrayDouble;
import messages.MessageQ;
import messages.MessageQArrayDouble;
import messages.MessageR;
import messages.MessageRArrayDouble;
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
public class OTimes_MaxSumTest {

    public OTimes_MaxSumTest() {
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
     * Test of otimes method, of class OTimes_MaxSum.
     */
    @Test
    public void testOtimes_MessageR_MessageR() {
        System.out.println("otimes");
        MessageR m1 = new MessageRArrayDouble(null, null, 2,2);
        MessageR m2 = new MessageRArrayDouble(null, null, 2,4);
        MessageFactoryArrayDouble mf = new MessageFactoryArrayDouble();
        OTimes_MaxSum instance = new OTimes_MaxSum(mf);
        MessageQ expResult = new MessageRArrayDouble(null, null, 2,6).toMessageQ();

        MessageQ result = instance.otimes(null, null, m1, m2);
        System.out.println("m1:"+m1+"\nm2:"+m2+"\nresult:"+result+"\nexpresult:"+expResult);
        assertTrue(expResult.equalContent(result));
        //assertEquals(expResult, result);
    }

    /**
     * Test of nullMessage method, of class OTimes_MaxSum.
     */
    @Test
    public void testNullMessage() {
        System.out.println("nullMessage");
        int size = 3;
        MessageFactoryArrayDouble mf = new MessageFactoryArrayDouble();
        OTimes_MaxSum instance = new OTimes_MaxSum(mf);
        MessageQ expResult = new MessageRArrayDouble(null, null, 3,0).toMessageQ();
        MessageQ result = instance.nullMessage(null, null, size);
        assertTrue(expResult.equalContent(result));
    }

    /**
     * Test of otimes method, of class OTimes_MaxSum.
     */
    @Test
    public void testOtimes_LinkedList() {
        System.out.println("otimes");
        LinkedList<MessageR> messagelist = new LinkedList<MessageR>();

        MessageR m1 = new MessageRArrayDouble(null, null, 3,2);
        MessageR m2 = new MessageRArrayDouble(null, null, 3,1);
        double[] content = {1,2,3.5};
        MessageR m3 = new MessageRArrayDouble(null, null, content);

        messagelist.add(m1);
        messagelist.add(m2);
        messagelist.add(m3);

        MessageFactoryArrayDouble mf = new MessageFactoryArrayDouble();
        OTimes_MaxSum instance = new OTimes_MaxSum(mf);
        double[] contentres = {4,5,6.5};
        MessageQ expResult = new MessageQArrayDouble(null, null, contentres);
        MessageQ result = instance.otimes(null, null, messagelist);
        assertTrue(expResult.equalContent(result));
    }

    /**
     * Test of otimes method, of class OTimes_MaxSum.
     */
    @Test
    public void testOtimes_MessageRArr() {
        System.out.println("otimes");
        MessageR[] messagearray = new MessageR[3];
        MessageR m1 = new MessageRArrayDouble(null, null, 3,2);
        MessageR m2 = new MessageRArrayDouble(null, null, 3,1);
        double[] content = {1,2,3.5};
        MessageR m3 = new MessageRArrayDouble(null, null, content);

        messagearray[0] = m1;
        messagearray[1] = m2;
        messagearray[2] = m3;

        MessageFactoryArrayDouble mf = new MessageFactoryArrayDouble();
        OTimes_MaxSum instance = new OTimes_MaxSum(mf);
        double[] contentres = {4,5,6.5};
        MessageQ expResult = new MessageQArrayDouble(null, null, contentres);
        MessageQ result = instance.otimes(null, null, messagearray);
        assertTrue(expResult.equalContent(result));
    }

}