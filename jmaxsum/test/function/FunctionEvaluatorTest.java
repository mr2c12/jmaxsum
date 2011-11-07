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

import java.util.LinkedList;
import java.util.Collection;
import misc.Utils;
import exception.InvalidInputFileException;
import factorgraph.NodeFunction;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import messages.MessageQ;
import olimpo.Cerberus;
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
            cop = Cerberus.getInstanceFromFile("/home/mik/NetBeansProjects/jMaxSumSVN/bounded_simple.cop2");
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
    //@Test
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
    //@Test
    public void testParametersNumber() {
        System.out.println("parametersNumber");
        int expResult = 3;
        int result = instance.parametersNumber();
        assertEquals(expResult, result);
    }




    /**
     * Test of functionArgument method, of class FunctionEvaluator.
     */
    //@Test
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
    //@Test
    public void testMaxFfixedX() throws Exception {
        System.out.println("maxFfixedX");
        NodeVariable x = NodeVariable.getNodeVariable(2);
        System.out.println("Found variable "+x);
        double[] expResult = {7,5,8,9};
        double[] result = instance.maxFfixedX(x);
        System.out.println("Result: "+Utils.toString(result));
        assertEquals(Utils.toString(result),Utils.toString(expResult));
    }

    /**
     * Test of removeArgs method, of class FunctionEvaluator.
     */
    @Test
    public void testRemoveArgs() {
        System.out.println("removeArgs");


        NodeVariable.resetIds();
        NodeFunction.resetIds();
        NodeArgument.resetIds();


        COP_Instance cop = null;
        try {
            cop = Cerberus.getInstanceFromFile("/home/mik/NetBeansProjects/jMaxSumSVN/bounded_arity5.cop2");
        } catch (InvalidInputFileException ex) {
            ex.printStackTrace();
        }
        for (NodeFunction f : cop.getNodefunctions()) {
            if (f.id() == 4){
                System.out.println("Found f: "+f);
                instance = f.getFunction();
            }

        }
        System.out.println("Function selected:\n"+instance);

        LinkedList<NodeVariable> args = new LinkedList<NodeVariable>();
        args.add(NodeVariable.getNodeVariable(1));
        args.add(NodeVariable.getNodeVariable(5));
        args.add(NodeVariable.getNodeVariable(2));
        args.add(NodeVariable.getNodeVariable(2));
        args.add(NodeVariable.getNodeVariable(13));
        
        instance.removeArgs(args);
        
        NodeArgument[] arguments = {
            NodeArgument.getNodeArgument("0"),
            NodeArgument.getNodeArgument("0")
        };
        assertEquals(instance.evaluate(arguments), 2.0, 0.0);

        arguments[0]=NodeArgument.getNodeArgument("0");
        arguments[1]=NodeArgument.getNodeArgument("1");
        assertEquals(instance.evaluate(arguments), 3.0, 0.0);

        arguments[0]=NodeArgument.getNodeArgument("1");
        arguments[1]=NodeArgument.getNodeArgument("0");
        assertEquals(instance.evaluate(arguments), 43.0, 0.0);

        arguments[0]=NodeArgument.getNodeArgument("1");
        arguments[1]=NodeArgument.getNodeArgument("1");
        assertEquals(instance.evaluate(arguments), 15.0, 0.0);


        for (NodeFunction f : cop.getNodefunctions()) {
            if (f.id() == 2){
                System.out.println("Found f: "+f);
                instance = f.getFunction();
            }

        }
        System.out.println("Function selected:\n"+instance);

         args = new LinkedList<NodeVariable>();
        args.add(NodeVariable.getNodeVariable(1));
        args.add(NodeVariable.getNodeVariable(5));
        args.add(NodeVariable.getNodeVariable(2));
        args.add(NodeVariable.getNodeVariable(2));
        args.add(NodeVariable.getNodeVariable(13));

        instance.removeArgs(args);

        NodeArgument[] arguments2 = {
            NodeArgument.getNodeArgument("0")
        };

        assertEquals(instance.evaluate(arguments2), 8.0, 0.0);

        arguments2[0]=NodeArgument.getNodeArgument("1");

        assertEquals(instance.evaluate(arguments2), 2.0, 0.0);

    }

    public class FunctionEvaluatorImpl extends FunctionEvaluator {

        public double evaluate(NodeArgument[] params) {
            return 0.0;
        }

        public void addParametersCost(NodeArgument[] params, double cost) {
        }

        public void clearCosts() {
        }

        public HashMap<NodeArgument[], Double> getParametersCost() {
            return null;
        }

        public String toStringForFile() {
            return "";
        }

        public int entryNumber() {
            return 0;
        }

        public ArrayList<Double> getCostValues() {
            return null;
        }

        public FunctionEvaluator getClone() {
            return null;
        }
    }

    

}