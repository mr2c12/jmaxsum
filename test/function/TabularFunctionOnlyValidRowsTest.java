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

import java.util.Arrays;
import messages.MessageQArrayDouble;
import exception.InvalidInputFileException;
import olimpo.Cerberus;
import system.COP_Instance;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashMap;
import messages.MessageQ;
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
public class TabularFunctionOnlyValidRowsTest {

    static TabularFunctionOnlyValidRows instance;

    public TabularFunctionOnlyValidRowsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        NodeVariable.getNodeVariable(1).addValue(NodeArgument.getNodeArgument(0));
        NodeVariable.getNodeVariable(1).addValue(NodeArgument.getNodeArgument(1));
        NodeVariable.getNodeVariable(2).addValue(NodeArgument.getNodeArgument(0));
        NodeVariable.getNodeVariable(2).addValue(NodeArgument.getNodeArgument(1));
        NodeVariable.getNodeVariable(3).addValue(NodeArgument.getNodeArgument(0));
        NodeVariable.getNodeVariable(3).addValue(NodeArgument.getNodeArgument(1));

        instance = new TabularFunctionOnlyValidRows(Double.NEGATIVE_INFINITY);

        instance.addParameter(NodeVariable.getNodeVariable(1));
        instance.addParameter(NodeVariable.getNodeVariable(2));
        instance.addParameter(NodeVariable.getNodeVariable(3));

        NodeArgument[] params = new NodeArgument[3];
        params[0] = NodeArgument.getNodeArgument(1);
        params[1] = NodeArgument.getNodeArgument(0);
        params[2] = NodeArgument.getNodeArgument(0);
        instance.addParametersCost(params, 0);
        params[0] = NodeArgument.getNodeArgument(0);
        params[1] = NodeArgument.getNodeArgument(1);
        params[2] = NodeArgument.getNodeArgument(0);
        instance.addParametersCost(params, 0);
        params[0] = NodeArgument.getNodeArgument(0);
        params[1] = NodeArgument.getNodeArgument(0);
        params[2] = NodeArgument.getNodeArgument(1);
        instance.addParametersCost(params, 0);

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
     * Test of evaluate method, of class TabularFunctionOnlyValidRows.
     */
    @Test
    public void testEvaluate() {

        System.out.println("evaluate");
        
        System.out.println("Function is:\n"+instance.toStringForFile());

        NodeArgument[] params = new NodeArgument[3];
        params[0] = NodeArgument.getNodeArgument(1);
        params[1] = NodeArgument.getNodeArgument(0);
        params[2] = NodeArgument.getNodeArgument(0);

        NodeArgument[] params2 = new NodeArgument[3];
        params2[0] = NodeArgument.getNodeArgument(1);
        params2[1] = NodeArgument.getNodeArgument(0);
        params2[2] = NodeArgument.getNodeArgument(0);

        System.out.println("Param arrays are "+ Arrays.equals(params, params2));

        double expResult = 0.0;
        double result = instance.evaluate(params);
        assertEquals(expResult, result, 0.0);


        params[0] = NodeArgument.getNodeArgument(1);
        params[1] = NodeArgument.getNodeArgument(1);
        params[2] = NodeArgument.getNodeArgument(0);

        expResult = Double.NEGATIVE_INFINITY;
        result = instance.evaluate(params);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of maxminWRT method, of class TabularFunctionOnlyValidRows.
     */
    @Test
    public void testMaxminWRT() throws Exception {
        System.out.println("maxminWRT");

        System.out.println("This function has parameters:");
        for (NodeVariable par : instance.getParameters()){
            System.out.println("- "+par);
        }
        System.out.println("Function\n"+instance.toStringForFile());
        String op = "max";
        NodeVariable x = NodeVariable.getNodeVariable(1);
        HashMap<NodeVariable, MessageQ> modifierTable = null;
        
        double[] expResult = {0.0,0.0};
        double[] result = instance.maxminWRT(op, x, modifierTable);
        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i],expResult[i],1E-5) ;
            
        }


        modifierTable = new HashMap<NodeVariable, MessageQ>();

        double[] message = {1.0,20.0};

        modifierTable.put(
                NodeVariable.getNodeVariable(2),
                new MessageQArrayDouble(x, null, message)
                );

        double[] message2 = {15.0,10.0};

        modifierTable.put(
                NodeVariable.getNodeVariable(3),
                new MessageQArrayDouble(x, null, message2)
                );

        expResult[0] = 35;
        expResult[1] = 16;
        result = instance.maxminWRT(op, x, modifierTable);
        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i],expResult[i],1E-5) ;

        }

        expResult[0] = 11;
        expResult[1] = 16;
        instance.setOtherValue(Double.POSITIVE_INFINITY);
        result = instance.maxminWRT("min", x, modifierTable);
        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i],expResult[i],1E-5) ;

        }

    }

}