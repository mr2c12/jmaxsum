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
package olimpo;

import exception.NoMoreValuesException;
import exception.ParameterNotFoundException;
import exception.ResultOkException;
import exception.VariableNotSetException;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import misc.Utils;
import operation.Solver;
import system.COP_Instance;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Eris implements Solver{

    private COP_Instance cop;
    private String op = "max";
    private ArrayList<NodeVariable> variables;
    private ArrayList<NodeFunction> functions;
    double costo;
    int passi = -1;
    long inizio, fine;
    // massimo numero di passi
    private int kMax = 10000;
    // parametri per il simulated annealing
    private double T = 300;
    private double costValue = Double.NaN;
    private double haltValue = Double.NaN;
    private boolean pleaseReport = false;
    private String reportpath = "";
    private String report = "";
    private boolean stepbystep = false;
    private boolean updateOnlyAtEnd = true;
    private boolean time = false;

    public Eris(String op, COP_Instance cop) throws ParameterNotFoundException {

        if (op.equalsIgnoreCase("max")){
            this.op = "max";
        }
        else if (op.equalsIgnoreCase("min")){
            this.op = "min";
        }
        else {
            throw new ParameterNotFoundException("Unknown operation: "+op);
        }

        this.cop = cop;

        // TODO: copy only variables with domain > 1
        this.variables = new ArrayList<NodeVariable>(this.cop.getNodevariables());
        this.functions = new ArrayList<NodeFunction>(this.cop.getNodefunctions());
    }







    public void pleaseReport(String file) {

        this.pleaseReport = true;
        this.reportpath = file;

    }

    public void randomInit() {
        for (NodeVariable x : this.variables) {
            try {
                x.setAnotherRandomValidValue();
            } catch (NoMoreValuesException ex) {
                // do nothing
            }
        }
        try {
            this.costo = this.cop.actualValue();
        } catch (VariableNotSetException ex) {
            ex.printStackTrace();
        }
        this.passi = -1;
    }

    private NodeVariable randomNodo() {

        Random rnd = new Random();

        return this.variables.get(rnd.nextInt(this.variables.size()));
    }

    private double update(double temperatura, int k) {
        return (this.T * Math.exp(-k / (8 * this.T)));
    }

    public void solve() {

        // inizializza random
        this.randomInit();
        this.inizio = System.currentTimeMillis();

        // costo
        double costoPrecedente = this.costo;
        int k = 0;
        int mink = 0;
        NodeVariable nodo = null;
        boolean ripeti = false;
        int valorePrecedente = -1;
        double delta = 0;
        double probab = -1;
        double temperatura = this.T;
        Random rnd = new Random();
        String status;
        boolean ffFound = false;
        this.report += "max_iterations_number=" + this.kMax + "\n";





        try {
            //while (temperatura > this.TLow) {
            while (k <= this.kMax) {

                do {
                    try {
                        ripeti = false;
                        nodo = this.randomNodo();

                        valorePrecedente = nodo.getStateIndex();


                        nodo.setAnotherRandomValidValue();


                    } catch (NoMoreValuesException ex) {
                        //} catch (Exception ex) {
                        ripeti = true;
                    }
                } while (ripeti);
                try {
                    this.costo = this.cop.actualValue();
                } catch (VariableNotSetException ex) {
                    this.costo = 0;
                }

                if (checkValue(delta)){
                    throw new ResultOkException();
                }

                // TODO: INIZIALIZZA CORRETTAMENTE COSTO
                if (this.op.equalsIgnoreCase("max")) {
                    delta = costoPrecedente - this.costo;
                } else if (this.op.equalsIgnoreCase("min")) {
                    delta = this.costo - costoPrecedente;
                }

                if (delta <= 0) {

                    // aggiorna
                    costoPrecedente = this.costo;
                    // aggiorno
                    this.costValue = this.costo;
                    mink = k;
                } else {

                    // aggiorno?
                    probab = Math.exp(-delta / temperatura);
                    if (rnd.nextDouble() < probab) {
                        // aggiorno
                        // aggiorno
                        costoPrecedente = this.costo;
                        // aggiorno il minimo
                        // non devo aggiornare
                    } else {
                        // non aggiorno
                        nodo.setStateIndex(valorePrecedente);
                        this.costo = costoPrecedente;
                    }
                }
                // ripetere senza aggiornare la temperatura

                // è ora di aggiornare
                k++;
                temperatura = update(temperatura, k);

                status = "iteration_" + k + "=" + this.cop.status();


                if (!this.updateOnlyAtEnd) {
                    if (this.pleaseReport) {
                        this.report += status + "\n";
                    } else {
                        System.out.println(status);
                    }
                }

                if (this.stepbystep) {
                    System.out.print("Iteration "+(k)+"/"+ this.kMax+" completed, press enter to continue");
                    try {
                        System.in.read();
                    } catch (IOException ex) {
                        //skip
                    }
                    System.out.println("");
                }

            }
        } catch (ResultOkException rex){
            ffFound = true;
        }

        this.fine = System.currentTimeMillis();
        this.passi = k-1;


        status = "final=" + this.cop.status();


        if (!this.updateOnlyAtEnd) {
            if (this.pleaseReport) {
                
            } else {
                System.out.println(status);
            }
        }

        this.report += status + "\n";
        this.report += "total time [ms]="+(fine-inizio)+"\n";
        
        this.report += "latest value got at iteration="+mink+"\n";
        this.report += "total number of iteration="+passi+"\n";
        this.report += "fixed point found=";
        if (ffFound){
            this.report += "Y";
        }
        else {
            this.report += "N";
        }
        this.report +="\n";
        if (this.pleaseReport) {
            try {
                Utils.stringToFile(this.report, this.reportpath);
            } catch (IOException ex) {
                System.out.println("Sorry but I'm unable to write the report to the file "+this.reportpath);
            }
        }
        else {
            System.out.println(report);
        }


    }


    private boolean checkValue(double value){
        if (this.op.equalsIgnoreCase("max")){
            return value >= this.haltValue;
        }
        else if (this.op.equalsIgnoreCase("min")){
            return value <= this.haltValue;
        }
        else {
            //nothing to do!
            return false;
        }
    }

    public void setIterationsNumber(int n) {
        this.kMax = n;
    }

    public void setStepbystep(boolean stepbystep) {
        this.stepbystep = stepbystep;
    }

    public void setUpdateOnlyAtEnd(boolean updateOnlyAtEnd) {
        this.updateOnlyAtEnd = updateOnlyAtEnd;
    }
}
