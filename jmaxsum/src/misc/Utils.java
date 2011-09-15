package misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Utility class.
 * @author mik
 */
public class Utils {


    public static double[] sumArray(double[] a1, double[] a2) throws IllegalArgumentException{
        if (a1 == null && a2 == null){
            throw new IllegalArgumentException("Given array size mismatch");
        }
        else if (a1 == null) {
            a1 = new double[a2.length];
        }
        else if (a2 == null) {
            a2 = new double[a1.length];
        }
        if (a1.length != a2.length) {
            throw new IllegalArgumentException("Given array size mismatch");
        }
        double[] res = new double[a1.length];
        for (int i = 0; i < a1.length; i++) {
            res[i] = ( a1[i] + a2[i]);
        }

        return res;
    }


    public static String toString(double[] a){
        String array = "[ ";
        for (int i = 0; i < a.length - 1; i++) {
            array += a[i]+" ,";
        }
        array += a[a.length-1]+"]";
        return array;
    }

    public static String toString(Double[] a){
        String array = "[ ";
        for (int i = 0; i < a.length - 1; i++) {
            array += a[i]+" ,";
        }
        array += a[a.length-1]+"]";
        return array;
    }


    public static String toString(int[] a){
        String array = "[ ";
        for (int i = 0; i < a.length - 1; i++) {
            array += a[i]+" ,";
        }
        array += a[a.length-1]+"]";
        return array;
    }


    public static void enumerate(int[] v, int[] max){
        int imax = v.length-1;
        int i=imax;
        int quanti = 0;
        while(i>=0){


            while ( v[i] < max[i] - 1 ) {
                System.out.println(Utils.toString(v));
                quanti++;
                v[i]++;
                for (int j = i+1; j <= imax; j++) {
                    v[j]=0;
                }
                i=imax;

            }

            i--;

        }
        System.out.println(Utils.toString(v));
        quanti++;
        System.out.println("prodotti "+quanti+ " array");
    }



    public static void enumerate(int[] max){
        Utils.enumerate(new int[max.length], max);
    }


    public static void stringToFile(String string, String file) throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(string);
        out.close();
    }
}
