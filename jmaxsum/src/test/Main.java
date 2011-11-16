/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import boundedMaxSum.InstanceCloner;
import hacks.ScrewerUp;
import maxsum.MS_COP_Instance;
import misc.Utils;
import system.COP_Instance;
import olimpo.Cerberus;
import olimpo.Athena;
import powerGrid.PowerGrid;

/**
 * Class used for test executions.
 * @author Michele Roncalli
 */
public class Main {

    static final int debug = test.DebugVerbosity.debugMain;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Started");

        /*PowerGrid pg = new PowerGrid();
        pg.initRandom(4, 3, 1);*/

        

        try {

            String[] paths = {
                "/home/mik/NetBeansProjects/maxSum/copTest.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/input.cop2",
                //"/home/mik/NetBeansProjects/maxSum/paper.cop2",
                //"/home/mik/NetBeansProjects/maxSum/paper_multi.cop2",
                //"/home/mik/NetBeansProjects/maxSum/simpleTest.cop2",
                //"/home/mik/NetBeansProjects/maxSum/test43.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/test43_mod.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/bounded_simple.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/infinity_test3.cop2",
                //"/home/mik/NetBeansProjects/jMaxSumSVN/test_infinity_2.cop2",
                //"/home/mik/Documenti/univr/Ragionamento Automatico/stage/report/DCOPProblem/Node200Domain3InducedWidth2Den2.0e-2-30.dcop"
            };

            for (String path : paths){

                if (debug>=1) {
                        String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Main: creating the COP instance from " + path);
                        System.out.println("---------------------------------------");
                }
                COP_Instance cop = Cerberus.getInstanceFromFile(path,false);
                //COP_Instance cop = Cerberus.getInstanceFromFile("/home/mik/NetBeansProjects/maxSum/problem.cop2",true);

                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "original instance is: " + cop.toTestString());
                        System.out.println("---------------------------------------");
                }

                System.out.println("Original instance value:\n"+cop.toTestString());

                System.out.println("------------------------------------------------");
                
                //COP_Instance cop2 = new ClonedMSInstance((MS_COP_Instance) cop);

                

                /*
                InstanceCloner ic = new InstanceCloner(cop);
                COP_Instance cop2 = ic.getClonedInstance();
                

                System.out.println("Cloned instance value:\n"+cop2.toTestString());

                System.out.println("------------------------------------------------");
                */

                
                Athena core = new Athena(cop,"max", "sum");
                core.setIterationsNumber(5);
                core.setStepbystep(true);
                core.setUpdateOnlyAtEnd(false);
                core.solve();
                core.conclude();

                /*
                Athena core2 = new Athena(cop2,"max", "sum");
                core2.setIterationsNumber(500);
                core2.setStepbystep(false);
                core2.solve();
                core2.conclude();
                */
                /*
                ClonedMSInstance cop3 = new ClonedMSInstance((MS_COP_Instance) cop);
                Athena core3 = new Athena(cop3);
                core3.setIterationsNumber(500);
                core3.setStepbystep(false);
                core3.solve();
                core3.conclude();
                 System.out.println("original value(cop3)= " + cop3.actualValue());
                */


                //ic.setOriginalVariablesValues();
                //System.out.println("original value(ic)= " + ic.getActualOriginalValue());
                System.out.println("original value(cop)= " + cop.actualValue());
                /*System.out.println("original value(cop2)= " + cop2.actualValue());

                ic.setOriginalVariablesValues();*/
                System.out.println("cop new status: "+cop.status());

                //System.out.println("original renewed value(cop)= " + cop.actualValue());

                //System.out.println(ic.testString());


                


                /*if (cop.actualValue() == cop2.actualValue()){
                    System.out.println("OK on "+path);
                }
                else {
                    System.out.println("FAIL on "+path);
                }*/
            }
            /*
            ScrewerUp su = new ScrewerUp(cop);

            cop = su.screwItUp();

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "screwed instance is: "+ cop.toTestString());
                    System.out.println("---------------------------------------");
            }

            Athena core = new Athena(cop);

            core.setIterationsNumber(5);
            core.setStepbystep(false);
            String report = "reports/cop_";
            report += System.currentTimeMillis();
            report += ".report";
            core.pleaseReport(report);

            core.solve();

            cop = su.fixItUp();

            core.conclude();


            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "fixed instance is: "+ cop.toTestString());
                    System.out.println("---------------------------------------");
            }*/


        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Finish in " + ((System.currentTimeMillis() - startTime)/(double)1000) + " s");
    }


    

}
