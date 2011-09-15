/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import hacks.ScrewerUp;
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

            core.setIterationsNumber(5);/*
            core.setStepbystep(false);
            String report = "reports/cop_";
            report += System.currentTimeMillis();
            report += ".report";
            core.pleaseReport(report);*/

            core.solve();

            cop = su.fixItUp();

            core.conclude();


            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[0].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[0].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "fixed instance is: "+ cop.toTestString());
                    System.out.println("---------------------------------------");
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Finish in " + ((System.currentTimeMillis() - startTime)/(double)1000) + " s");
    }


    

}
