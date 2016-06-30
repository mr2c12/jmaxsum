package misc;

import exception.LengthMismatchException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


/**
 * Utility class.
 * @author Michele Roncalli
 */
public class Utils {

    final static int debug = test.DebugVerbosity.debugUtils;

    /**
     * Sum array
     * @param a1 first array
     * @param a2 second array
     * @return an array where at position i is stored the sum of a1[i] and a2[i]
     * @throws IllegalArgumentException if at a1 or a2 is null, or if their length is different.
     */
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

    /**
     * Build a String representing the array
     * @param a array of Object to turn into a String
     * @return String representation of the array.
     */
    public static String toString(Object[] a){
        StringBuilder array = new StringBuilder();
        array.append("[ ");
        for (int i = 0; i < a.length - 1; i++) {
            array.append(a[i]).append(" ,");
        }
        array.append(a[a.length - 1]).append("]");
        return array.toString();
    }
    public static String toString(double[] a){
        StringBuilder array = new StringBuilder();
        array.append("[ ");
        for (int i = 0; i < a.length - 1; i++) {
            array.append(a[i]).append(" ,");
        }
        array.append(a[a.length - 1]).append("]");
        return array.toString();
    }

    public static String toString(Double[] a){
        StringBuilder array = new StringBuilder();
        array.append("[ ");
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] != null){
                StringBuilder append = array.append(a[i] + " ,");
            } else {
                StringBuilder append = array.append("null,");
            }
        }
        StringBuilder append = array.append(a[a.length - 1] + "]");
        return array.toString();
    }


    public static String toString(int[] a){
        StringBuilder array = new StringBuilder();
        array.append("[ ");
        for (int i = 0; i < a.length - 1; i++) {
            array.append(a[i]).append(" ,");
        }
        array.append(a[a.length - 1]).append("]");
        return array.toString();
    }


    /**
     * VERY IMPORTANT METHOD.<br/>
     * It is used a lot of times in the whole project.<br/>
     * This is an algorithm that produce all the possible arrays in a given range.<br/>
     * e.g. let max={1,2,2,1} an array where max[i] is the number of values of i-th cell of aray to create.<br/>
     * So, we want this lines of code to create:<br/>
     * [0,0,0,0]<br/>
     * [0,0,1,0]<br/>
     * [0,1,0,0]<br/>
     * [0,1,1,0]<br/>
     * @param v the array to create.
     * @param max the array of maximum values for each position.
     */
    public static void enumerate(int[] v, int[] max){
        int imax = v.length-1;
        int i=imax;
        int quanti = 0;
        while(i>=0){


            while ( v[i] < max[i] - 1 ) {
                //System.out.println(Utils.toString(v));
                // HERE v IS THE ARRAY!

                System.out.print("F ");
                for (int index= 0; index < v.length; index++) {
                    System.out.print(v[index]+ " ");
                    
                }
                System.out.print((int)(Math.random()*100)+"\n");


                quanti++;
                v[i]++;
                for (int j = i+1; j <= imax; j++) {
                    v[j]=0;
                }
                i=imax;

            }

            i--;

        }
        //System.out.println(Utils.toString(v));
        // HERE v IS THE ARRAY!
        System.out.print("F ");
        for (int index= 0; index < v.length; index++) {
            System.out.print(v[index]+ " ");

        }
        System.out.print((int)(Math.random()*100)+"\n");

        quanti++;
        System.out.println("prodotti "+quanti+ " array");
    }


    /**
     * @see Utils#enumerate(int[], int[]) 
     */
    public static void enumerate(int[] max){
        Utils.enumerate(new int[max.length], max);
    }


    /**
     * Simple method that store a String into a file. Like the putfilecontent() php function.
     * @param string to be saved.
     * @param file where to write.
     * @throws IOException if any problem accessing the file occurs.
     */
    public static void stringToFile(String string, String file) throws IOException{
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Writing:\n"+string+"\nin\n"+file);
                System.out.println("---------------------------------------");
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(string);
            out.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * It manages two arrays and produces a new one, using the rule:
     *
     * new[i] = array1[i] + operation * array2[i]
     *
     * @param operation an integer, should be 1 for sum or -1 for difference
     * @param a1 the first array
     * @param a2 the second array
     * @return the new array
     * @throws LengthMismatchException if a1 has a different lenght wrt a2
     * @throws NullPointerException if a1 (or a2) has a null value
     */
    public static Double[] opArray (int operation, Double[] a1, Double[] a2) throws LengthMismatchException, NullPointerException{
       if (a1.length != a2.length){
           throw new LengthMismatchException();
       }
       Double[] res = new Double[a1.length];
       for(int index = 0; index < a1.length; index++){
           if (a1[index] == null){
               throw new NullPointerException("Null found in a1");
           }
           if (a2[index] == null){
               throw new NullPointerException("Null found in a2");
           }
           res[index] = a1[index] + ( a2[index]*operation);
       }
       return res;
    }

    public static double[] opArray (int operation, double[] a1, double[] a2) throws LengthMismatchException, NullPointerException{
       if (a1.length != a2.length){
           throw new LengthMismatchException();
       }
       double[] res = new double[a1.length];
       for(int index = 0; index < a1.length; index++){

           res[index] = a1[index] + ( a2[index]*operation);
       }
       return res;
    }

    /**
     * Implementation of modern variation of Fisher–Yates shuffle algorithm
     * @param a array to be shuffled
     * @return a shuffled
     */
    public static Object[] shuffleArrayFY(Object[] a){
        /*
         * To shuffle an array a of n elements (indices 0..n-1):
          for i from n − 1 downto 1 do
               j ← random integer with 0 ≤ j ≤ i
               exchange a[j] and a[i]
         */
        int n = a.length;
        Random rnd = new Random();
        int j;
        Object tmp;
        for (int i = n-1; n>=1; n--){
            j = rnd.nextInt(i+1);
            tmp = a[j];
            a[j] = a[i];
            a[i] = tmp;
        }
        return a;
    }
}
