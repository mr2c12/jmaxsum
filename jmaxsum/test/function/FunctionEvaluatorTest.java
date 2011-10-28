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

package function;

import misc.Utils;
import exception.InvalidInputFileException;
import factorgraph.NodeFunction;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import messages.MessageQ;
import olimpo.Cerbero;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import system.COP_Instance;
import static org.junit.Assert.*;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class FunctionEvaluatorTest {

    static FunctionEvaluator instance;

    public FunctionEvaluatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        COP_Instance cop = null;
        try {
            cop = Cerbero.getInstanceFromFile("/home/mik/NetBeansProjects/jMaxSumSVN/bounded_simple.cop2");
        } catch (InvalidInputFileException ex) {
            ex.printStackTrace();
        }
        for (NodeFunction f : cop.getNodefunctions()) {
            if (f.id() == 1){
                System.out.println("Found f: "+f);
                instance = f.getFunction();
            }

        }

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp()  {
        
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class FunctionEvaluator.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        /*int[] args_pos = new int[3];
        NodeArgument[] params = instance.functionArgument(args_pos);*/

        NodeArgument[] params = {NodeArgument.getNodeArgument("0"),NodeArgument.getNodeArgument("0"),NodeArgument.getNodeArgument("0")};

        double expResult = 3.0;
        double result = instance.evaluate(params);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of parametersNumber method, of class FunctionEvaluator.
     */
    @Test
    public void testParametersNumber() {
        System.out.println("parametersNumber");
        int expResult = 3;
        int result = instance.parametersNumber();
        assertEquals(expResult, result);
    }




    /**
     * Test of functionArgument method, of class FunctionEvaluator.
     */
    @Test
    public void testFunctionArgument() {
        System.out.println("functionArgument");
        int[] argumentsNumber = {0,0,0};
        
        NodeArgument[] expResult = {NodeArgument.getNodeArgument("0"), NodeArgument.getNodeArgument("0"), NodeArgument.getNodeArgument("0") };
        NodeArgument[] result = instance.functionArgument(argumentsNumber);
        System.out.println("Original: "+Utils.toString(expResult));
        System.out.println("Result: "+Utils.toString(result));
        assertEquals(Utils.toString(expResult),Utils.toString(result));
    }

    /**
     * Test of maxFfixedX method, of class FunctionEvaluator.
     */
    @Test
    public void testMaxFfixedX() throws Exception {
        System.out.println("maxFfixedX");
        NodeVariable x = NodeVariable.getNodeVariable(2);
        System.out.println("Found variable "+x);
        double[] expResult = {7,5,8,9};
        double[] result = instance.maxFfixedX(x);
        System.out.println("Result: "+Utils.toString(result));
        assertEquals(Utils.toString(result),Utils.toString(expResult));
    }

    

}