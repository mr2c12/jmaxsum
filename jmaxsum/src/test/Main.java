/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import boundedMaxSum.ClonedMSInstance;
import boundedMaxSum.InstanceCloner;
import hacks.ScrewerUp;
import maxsum.MS_COP_Instance;
import misc.Utils;
import system.COP_Instance;
import olimpo.Cerbero;
import system.Core;

/**
 *
 * @author mik
 */
public class Main {

    static final int debug = test.DebugVerbosity.debugMain;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Started");

        try {

            String path = "/home/mik/NetBeansProjects/maxSum/test43.cop2";
            if (debug>=1) {
                    String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Main: creating the COP instance from " + path);
                    System.out.println("---------------------------------------");
            }
            COP_Instance cop = Cerbero.getInstanceFromFile(path);
            //COP_Instance cop = Cerbero.getInstanceFromFile("/home/mik/NetBeansProjects/maxSum/problem.cop2",true);

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

            //ClonedMSInstance cop2 = new ClonedMSInstance((MS_COP_Instance) cop);

            InstanceCloner ic = new InstanceCloner(cop);
            COP_Instance cop2 = ic.getClonedInstance();

            System.out.println("Cloned instance value:\n"+cop2.toTestString());

            System.out.println("------------------------------------------------");

            Core core = new Core(cop2);

            core.setIterationsNumber(50);
            core.setStepbystep(false);

            core.solve();

            core.conclude();

            ic.setOriginalVariablesValues();
            System.out.println("original value(ic)= " + ic.getActualOriginalValue());
            System.out.println("original value(cop)= " + cop.actualValue());

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

            Core core = new Core(cop);

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
