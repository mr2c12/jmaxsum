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

package boundedMaxSum;

import exception.NodeTypeException;
import exception.WeightNotSetException;
import factorgraph.NodeVariable;
import factorgraph.NodeFunction;
import exception.InvalidInputFileException;
import java.util.PriorityQueue;
import olimpo.Cerberus;
import system.COP_Instance;
import factorgraph.Edge;
import factorgraph.FactorGraph;
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
public class BoundedMaxSumTest {

    static BoundedMaxSum instance;

    
    public BoundedMaxSumTest() {


    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        COP_Instance cop = null;
        try {
            cop = Cerberus.getInstanceFromFile("/home/mik/NetBeansProjects/jMaxSumSVN/bounded_simple.cop2");
        } catch (InvalidInputFileException ex) {
            ex.printStackTrace();
        }
        instance = new BoundedMaxSum(cop.getFactorgraph());

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
     * Test of weightTheEdge method, of class BoundedMaxSum.
     */
    @Test
    public void testWeightTheEdge() throws Exception {
        System.out.println("weightTheEdge");
        /*instance.weightTheEdge(
                this.instance.factorgraph.
                );*/

        String result = "";
        Edge e;
        boolean ok = true;
        double w;

        int[] f_id = {1,1,1,2,2,3};
        int[] x_id = {1,2,3,2,3,1};
        double[] weight = {7,4,5,8,13,1};

        for (int index= 0; index < f_id.length; index++) {
            e  = Edge.getEdge(
                            NodeFunction.getNodeFunction(f_id[index]),
                            NodeVariable.getNodeVariable(x_id[index])
                );
            instance.weightTheEdge(e);
            w = instance.getFactorgraph().getWeight(e);
            if (w==weight[index]){
                result+="Edge "+e+" has CORRECT weight ("+w+")\n";
            }
            else {
                result+="Edge "+e+" has WRONG weight ("+w+")\n";
                ok = false;
            }
        }
        

        /*
        // w11 == 7
        e  = Edge.getEdge(
                            NodeFunction.getNodeFunction(1),
                            NodeVariable.getNodeVariable(1)
                );
        instance.weightTheEdge(e);
        w = instance.getFactorgraph().getWeight(e);
        if (w==7){
            result="Edge "+e+" has CORRECT weight";
        }
        else {
            ok = false;
        }


        // w12 == 4
        e  = Edge.getEdge(
                            NodeFunction.getNodeFunction(1),
                            NodeVariable.getNodeVariable(4)
                );
        instance.weightTheEdge(e);
        w = instance.getFactorgraph().getWeight(e);
        if (w==4){
            result="Edge "+e+" has CORRECT weight";
        }
        else {
            ok = false;
        }*/



        System.out.println(result);
        assertEquals(ok, true);
    }

    /**
     * Test of getEdgeQueue method, of class BoundedMaxSum.
     */
    @Test
    public void testGetEdgeQueue() {
        System.out.println("getEdgeQueue");
        PriorityQueue<Edge> result = instance.getEdgeQueue(-1);
        int pos = 1;
        System.out.println("Queue has "+result.size()+" elements.");
        while(result.size()>0){
            try {
                System.out.println("[1] " + result.peek() + "w=" + instance.getFactorgraph().getWeight(result.poll()));
            } catch (WeightNotSetException ex) {
                ex.printStackTrace();
            }
        }
        
    }

    /**
     * Test of letsBound method, of class BoundedMaxSum.
     */
    @Test
    public void testLetsBound() {
        System.out.println("letsBound");
        
        instance.letsBound();

    }

}