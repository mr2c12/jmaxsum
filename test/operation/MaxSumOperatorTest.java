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

import exception.FunctionNotPresentException;
import exception.InvalidInputFileException;
import messages.MessageFactoryArrayDouble;
import messages.MessageFactory;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.TabularFunction;

import java.util.LinkedList;

import messages.MailMan;
import messages.MessageQ;
import messages.MessageQArrayDouble;
import messages.MessageR;
import messages.MessageRArrayDouble;
import messages.PostService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import system.COP_Instance;
import olimpo.Cerberus;
import static org.junit.Assert.*;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class MaxSumOperatorTest {

    static PostService postservice;
    static MSumOperator maxsumoperator;
    static MessageFactory mfactory;
    static Cerberus cerbero;
    static COP_Instance cop;
    static MSumOperator instance;
    static OPlus oplus;
    static OTimes otimes;


    public MaxSumOperatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        postservice = new MailMan();


        mfactory = new MessageFactoryArrayDouble();

        MaxSumOperatorTest.maxsumoperator = new MSumOperator(new OTimes_MaxSum(mfactory), new OPlus_MaxSum(mfactory));

        cerbero = new Cerberus();
        try {
            cop = cerbero.getInstanceFromFile("/home/mik/NetBeansProjects/maxSum/paper.cop2");

            otimes = new OTimes_MaxSum(mfactory);
            oplus = new OPlus_MaxSum(mfactory);


            instance = new MSumOperator(otimes, oplus);
        } catch (InvalidInputFileException ex) {
            // do nothing
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of computeAlpha method, of class MaxSumOperator.
     */
    @Test
    public void testComputeAlpha() {
        try {
            System.out.println("computeAlpha");
            LinkedList<MessageR> params = new LinkedList<MessageR>();
            double[] r1 = {1, 2, 3};
            params.add(new MessageRArrayDouble(null, null, r1));
            double[] r2 = {-1, -2, -3};
            params.add(new MessageRArrayDouble(null, null, r2));
            double[] r3 = {10, 15, 2};
            params.add(new MessageRArrayDouble(null, null, r3));
            double expResult = -9.0;
            double result = instance.computeAlpha(NodeVariable.getNodeVariable(0), NodeFunction.getNodeFunction(0), params);
            assertEquals(expResult, result, 0.0);
        } catch (FunctionNotPresentException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Test of computeQ method, of class MaxSumOperator.
     */
    @Test
    public void testComputeQ() {
        System.out.println("computeQ");
        NodeVariable x = null;
        double alpha = -9.0;
        LinkedList<MessageR> params = new LinkedList<MessageR>();
        double[] r1 = {1,2,3};
        params.add(new MessageRArrayDouble(null, null, r1));
        double[] r2 = {1,2,3};
        params.add(new MessageRArrayDouble(null, null, r2));
        double[] r3 = {10,15,2};
        params.add(new MessageRArrayDouble(null, null, r3));
        double[] res = {3,10,-1};
        MessageQ expResult = new MessageQArrayDouble(null, null, res);
        MessageQ result = instance.computeQ(null, null, alpha, params);
        //assertEquals(expResult, result);
        /*System.out.println("exp res="+ expResult.toString());
        System.out.println("act res="+ result.toString());
        */boolean equals = result.equals(expResult);
        //System.out.println("Il confronto vale "+equals);
        assertEquals(equals, true);
    }





    /**
     * Test of setOplus method, of class MaxSumOperator.
     */
    @Test
    public void testSetOplus() {
        System.out.println("setOplus");
        OPlus oplus = null;
        MSumOperator instance = MaxSumOperatorTest.maxsumoperator;
        instance.setOplus(oplus);
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOtimes method, of class MaxSumOperator.
     */
    @Test
    public void testSetOtimes() {
        System.out.println("setOtimes");
        OTimes otimes = null;
        MSumOperator instance = MaxSumOperatorTest.maxsumoperator;
        instance.setOtimes(otimes);
        fail("The test case is a prototype.");
    }


    /**
     * Test of updateQ method, of class MaxSumOperator.
     */
    @Test
    public void testUpdateQ() {
        System.out.println("updateQ");
        NodeVariable x = null;
        NodeFunction f = null;
        MSumOperator instance = MaxSumOperatorTest.maxsumoperator;
        instance.updateQ(x, f, postservice);
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateR method, of class MaxSumOperator.
     */
    //@Test
    public void testUpdateR() {
        System.out.println("updateR");
        NodeFunction f = null;
        NodeVariable x = null;
        MSumOperator instance = MaxSumOperatorTest.maxsumoperator;
        instance.updateR(f, x, postservice);
        fail("The test case is a prototype.");
    }

}