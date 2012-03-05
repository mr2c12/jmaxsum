/*
 *  Copyright (C) 2012 Michele Roncalli <roncallim at gmail dot com>
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

package system;

import factorgraph.NodeVariable;
import exception.InitializatedException;
import exception.ResultOkException;
import exception.UnInitializatedException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import java.io.FileNotFoundException;
import java.io.IOException;
import maxsum.Relaxable_MS_COP_Instance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import powerGrid.PowerGrid;
import static org.junit.Assert.*;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class WalkGridTest {

    public WalkGridTest() {
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
     * Test of randomInit method, of class WalkGrid.
     */
    @Test
    public void testRandomInit() {
        try {
            System.out.println("randomInit");
            PowerGrid pg = new PowerGrid("/home/mik/NetBeansProjects/jMaxSumSVN/pgtest.pg");
            Relaxable_MS_COP_Instance cop = pg.getCopMnotInfNoCo2();
            WalkGrid instance = new WalkGrid(cop);
            instance.randomInit();
            System.out.println("cop initializated in:\n"+cop.status());
            
        } catch (UnInitializatedException ex) {
            ex.printStackTrace();
        } catch (InitializatedException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test of getOverloadedGenerator method, of class WalkGrid.
     */
    @Test
    public void testGetOverloadedGenerator() throws Exception {
        System.out.println("getOverloadedGenerator");
        PowerGrid pg = new PowerGrid("/home/mik/NetBeansProjects/jMaxSumSVN/pgtest.pg");
        Relaxable_MS_COP_Instance cop = pg.getCopMnotInfNoCo2();
        WalkGrid instance = new WalkGrid(cop);

        int uno = 1;
        int due = 2;

        NodeVariable.getNodeVariable(0).setStateArgument(
                NodeArgument.getNodeArgument(uno)
                );
        NodeVariable.getNodeVariable(1).setStateArgument(
                NodeArgument.getNodeArgument(uno)
                );
        NodeVariable.getNodeVariable(2).setStateArgument(
                NodeArgument.getNodeArgument(uno)
                );
        NodeVariable.getNodeVariable(3).setStateArgument(
                NodeArgument.getNodeArgument(due)
                );
        NodeVariable.getNodeVariable(4).setStateArgument(
                NodeArgument.getNodeArgument(due)
                );
        NodeVariable.getNodeVariable(5).setStateArgument(
                NodeArgument.getNodeArgument(due)
                );

        System.out.println("cop set to:\n"+cop.status());



        NodeFunction expResult = NodeFunction.getNodeFunction(2);
        NodeFunction result = instance.getOverloadedGenerator();

        System.out.println("expected: "+expResult.toString()+"\nresult: "+result.toString());

        assertEquals(expResult, result);


        NodeVariable.getNodeVariable(0).setStateArgument(
                NodeArgument.getNodeArgument(uno)
                );
        NodeVariable.getNodeVariable(1).setStateArgument(
                NodeArgument.getNodeArgument(uno)
                );
        NodeVariable.getNodeVariable(2).setStateArgument(
                NodeArgument.getNodeArgument(due)
                );
        NodeVariable.getNodeVariable(3).setStateArgument(
                NodeArgument.getNodeArgument(due)
                );
        NodeVariable.getNodeVariable(4).setStateArgument(
                NodeArgument.getNodeArgument(uno)
                );
        NodeVariable.getNodeVariable(5).setStateArgument(
                NodeArgument.getNodeArgument(due)
                );

        System.out.println("cop set to:\n"+cop.status());

        boolean ok = false;
        try {
            result = instance.getOverloadedGenerator();
        }
        catch (ResultOkException ex){
            ok = true;
        }

        assertEquals(true, ok);

        

    }


    /**
     * Test of solve method, of class WalkGrid.
     */
    @Test
    public void testSolve() throws Exception {
        System.out.println("solve");
        //PowerGrid pg = new PowerGrid("/home/mik/NetBeansProjects/jMaxSumSVN/pgtest.pg");

        PowerGrid pg = new PowerGrid("/home/mik/Documenti/univr/Ragionamento Automatico/stage/report/200/0.29/2.pg");

        Relaxable_MS_COP_Instance cop = pg.getCopMnotInfNoCo2();
        WalkGrid instance = new WalkGrid(cop);
        instance.solve();
        System.out.println("After solve, istance is:"+cop.status());
        System.out.println("Relaxed cost is:"+cop.actualRelaxedValue());
    }

}