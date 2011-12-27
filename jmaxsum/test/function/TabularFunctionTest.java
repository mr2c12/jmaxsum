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

import exception.ParameterNotFoundException;
import java.util.Iterator;
import messages.MessageQ;
import factorgraph.NodeVariable;
import java.util.HashMap;
import factorgraph.NodeArgument;
import messages.MessageQArrayDouble;
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
public class TabularFunctionTest {

    public TabularFunction instance;
    public HashMap<NodeVariable, MessageQ> modifierTable;
    public NodeVariable x1;
    public NodeVariable x2;
    public NodeVariable x3;

    public TabularFunctionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new TabularFunction();
        x1 = NodeVariable.getNodeVariable(1);
        x1.addValue(NodeArgument.getNodeArgument("x"));
        x1.addValue(NodeArgument.getNodeArgument("y"));
        x2 = NodeVariable.getNodeVariable(2);
        x2.addValue(NodeArgument.getNodeArgument("t"));
        x2.addValue(NodeArgument.getNodeArgument("f"));
        x3 = NodeVariable.getNodeVariable(3);
        x3.addValue(NodeArgument.getNodeArgument("a"));
        x3.addValue(NodeArgument.getNodeArgument("b"));
        x3.addValue(NodeArgument.getNodeArgument("c"));
        instance.addParameter(x1);
        instance.addParameter(x2);
        instance.addParameter(x3);
        Object[][] params3d = {
            {"x","t","a",5.0},
            {"x","t","b",6.0},
            {"x","t","c",7.0},
            {"x","f","a",1.0},
            {"x","f","b",-1.0},
            {"x","f","c",2.0},
            {"y","t","a",0.0},
            {"y","t","b",3.0},
            {"y","t","c",-3.0},
            {"y","f","a",4.0},
            {"y","f","b",5.0},
            {"y","f","c",4.0}
        };
        for (int i = 0; i < params3d.length; i++) {
            NodeArgument n1 = NodeArgument.getNodeArgument(params3d[i][0]);
            NodeArgument n2 = NodeArgument.getNodeArgument(params3d[i][1]);
            NodeArgument n3 = NodeArgument.getNodeArgument(params3d[i][2]);
            double cost = (Double)params3d[i][3];
            NodeArgument[] params = {n1,n2,n3};
            instance.addParametersCost(params, cost);
        }
        modifierTable = new HashMap<NodeVariable, MessageQ>();
        
        double[] message1 = {2,1};
        modifierTable.put(x1, new MessageQArrayDouble(null, null, message1));
        double[] message2 = {-3,-4};
        modifierTable.put(x2, new MessageQArrayDouble(null, null, message2));
        double[] message3 = {3,5,-7};
        modifierTable.put(x3, new MessageQArrayDouble(null, null, message3));

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class TabularFunction.
     */
    //@Test
    public void testAdd() {
        System.out.println("add");
        NodeArgument[] params =  {NodeArgument.getNodeArgument("1"),
        NodeArgument.getNodeArgument("2"),NodeArgument.getNodeArgument("3")};
            //new NodeArgument("1"),new NodeArgument("2"),new NodeArgument("3")};
        double cost = 0.0;
        //TabularFunction instance = new TabularFunction();
        instance.addParametersCost(params, cost);
        assertEquals(true, instance.entryNumber() == 1);

    }

    /**
     * Test of evaluate method, of class TabularFunction.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        NodeArgument n1 = NodeArgument.getNodeArgument("y");
        NodeArgument n2 = NodeArgument.getNodeArgument("t");
        NodeArgument n3 = NodeArgument.getNodeArgument("c");
        NodeArgument[] params = {n1,n2,n3};
        double expResult = -3.0;
        double result = instance.evaluate(params);
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of parametersNumber method, of class FunctionEvaluatorMaxSum.
     */
    @Test
    public void testParametersNumber() {
        System.out.println("parametersNumber");
        //FunctionEvaluatorMaxSum instance = new FunctionEvaluatorMaxSumImpl();
        int expResult = 3;
        int result = instance.parametersNumber();
        assertEquals(expResult, result);;
    }

    /**
     * Test of getParameter method, of class FunctionEvaluatorMaxSum.
     */
    @Test
    public void testGetParameter() {
        System.out.println("getParameter");
        int index = 1;
        //FunctionEvaluatorMaxSum instance = new FunctionEvaluatorMaxSumImpl();
        NodeVariable expResult = x2;
        NodeVariable result = instance.getParameter(index);
        assertEquals(expResult, result);
    }

    /**
     * Test of getParameterPosition method, of class FunctionEvaluatorMaxSum.
     */
    @Test
    public void testGetParameterPosition() {
        System.out.println("getParameterPosition");
        NodeVariable x = x3;
        //FunctionEvaluatorMaxSum instance = new FunctionEvaluatorMaxSumImpl();
        int expResult = 2;
        try {
            int result = instance.getParameterPosition(x);
            assertEquals(expResult, result);
        } catch (ParameterNotFoundException ex){
            fail("Exception raised: "+ex);
        }
        
    }


    @Test
    public void testEvaluateMod() {
        System.out.println("evaluateMod");



        NodeArgument n1 = NodeArgument.getNodeArgument("y");
        NodeArgument n2 = NodeArgument.getNodeArgument("t");
        NodeArgument n3 = NodeArgument.getNodeArgument("c");
        NodeArgument[] params = {n1,n2,n3};
        
        double expResult = -12.0;
        System.out.println("Dimensione tabella: " + instance.entryNumber());



        /*Iterator<String> it = instance.costTable.keySet().iterator();
        while (it.hasNext()) {
            String string = it.next();
            System.out.println("trovata chiave "+ string);
        }*/

        //System.out.println("n1 è uguale a n2? " + n1.equals(n2));
        
        //NodeArgument[] params, HashMap<NodeVariable, MessageQ> modifierTable
        double result = instance.evaluateMod(params, modifierTable);
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of addParametersCost method, of class TabularFunction.
     */
    @Test
    public void testAddParametersCost() {
        System.out.println("addParametersCost");
        Object[][] params3d = {
            {"x","t","a",12.0},
            {"x","t","b",6.0},
            {"x","t","c",7.0},
            {"x","f","a",1.0},
            {"x","f","b",-1.0},
            {"x","f","c",2.0},
            {"y","t","a",0.0},
            {"y","t","b",3.0},
            {"y","t","c",-3.0},
            {"y","f","a",4.0},
            {"y","f","b",5.0},
            {"y","f","c",4.0}
        };
        int i = 0;
        NodeArgument n1 = NodeArgument.getNodeArgument(params3d[i][0]);
        NodeArgument n2 = NodeArgument.getNodeArgument(params3d[i][1]);
        NodeArgument n3 = NodeArgument.getNodeArgument(params3d[i][2]);
        double cost = (Double)params3d[i][3];
        NodeArgument[] params = {n1,n2,n3};
        instance.addParametersCost(params, cost);
        double result = instance.evaluate(params);
        assertEquals(12.0, result, 0.0);
        
    }

    /**
     * Test of entryNumber method, of class TabularFunction.
     */
    @Test
    public void testEntryNumber() {
        System.out.println("entryNumber");
        int expResult = 12;
        int result = instance.entryNumber();
        assertEquals(expResult, result);
     }

    /**
     * Test of toString method, of class TabularFunction.
     */
    @Test
    public void testToString() {
        System.out.println("toString");


        String result = instance.toString();
        System.out.println("Instance is:\n"+result);
        assertTrue(true);
    }

    /**
     * Test of getParametersCost method, of class TabularFunction.
     */
    /*@Test
    public void testGetParametersCost() {
        System.out.println("getParametersCost");
        HashMap<NodeArgument[], Double> expResult = null;
        HashMap<NodeArgument[], Double> result = instance.getParametersCost();
        Iterator<NodeArgument[]> itna = result.keySet().iterator();
        while (itna.hasNext()) {
            NodeArgument[] nodeArguments = itna.next();
            System.out.print("Node arguments: ");
            for (int i = 0; i < nodeArguments.length; i++) {
                System.out.print(nodeArguments[i]+" ");
            }
            System.out.println("-> cost: "+ result.get(nodeArguments));

        }
        assertTrue(true);
    }*/

    /**
     * Test of toStringForFile method, of class TabularFunction.
     */
    @Test
    public void testToStringForFile() {
        System.out.println("toStringForFile");
        String result = instance.toStringForFile();
        System.out.println(result);
        assertTrue(true);
    }

}