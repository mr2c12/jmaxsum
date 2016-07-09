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

import exception.InvalidInputFileException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.TabularFunction;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import maxsum.Agent;
import maxsum.MS_COP_Instance;
import system.COP_Instance;

/**
 * Cerberus is a multi-headed hound (usually three-headed) which guards the gates of the Underworld, to prevent those who have crossed the river Styx from ever escaping.<br/>
 * <br/>
 * The input module.<br/>
 * It can read a file and take out an instance.<br/>
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Cerberus {

    public static final int debug = test.DebugVerbosity.debugCerbero;


    //COP_Instance instance;

    /**
     * Read a file and create an instance.
     * @param fname path to instance file
     * @return the instance
     */
    public static COP_Instance getInstanceFromFile(String fname) throws InvalidInputFileException {
        return getInstanceFromFile(fname, false,0,0);
    }

    /**
     * Read a file and create an instance.
     *
     * @param fname path to instance file
     * @param oldFormat if true, use the original format from UK
     * @param MaxValue maximum values across all functions
     * @param MultConst scale factor 
     * @return the instance
     */
    public static COP_Instance getInstanceFromFile(String fname, boolean oldFormat, int MaxValue, double MultConst) throws InvalidInputFileException{
        HashSet<NodeVariable> nodevariables = new HashSet<NodeVariable>();
        HashSet<NodeFunction> nodefunctions = new HashSet<NodeFunction>();
        HashSet<NodeArgument> nodeargumens = new HashSet<NodeArgument>();
        HashSet<Agent> agents = new HashSet<Agent>();



        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fname)));
            String line = in.readLine();

            Agent agent = null;
            NodeVariable nodevariable = null;
            NodeFunction nodefunction = null;
            //TabularFunction functionevaluator = null;
            StringTokenizer t = null;
            String token = null;
            String costString;
            int agent_id, variable_id, variable_to_agent, number_of_argument, function_id = -1, variable_to_function = -1, function_to_agent = -1;
            LinkedList<NodeVariable> argumentsOfFunction = null;
            NodeArgument[] arguments = null;

            while (line != null) {
                t = new StringTokenizer(line);
                if (t.hasMoreTokens()){
                    token = t.nextToken();
                    if (token.equals("AGENT")) {
                        agent_id = Integer.parseInt(t.nextToken());
                        if(debug>=3){
                            System.out.println("Agent id: "+agent_id);
                        }
                        agent = Agent.getAgent(agent_id);

                        agents.add(agent);


                    }

                    if (token.equals("VARIABLE")) {
                        variable_id = Integer.parseInt(t.nextToken());
                        if(debug>=3){
                            System.out.println("Variable id: "+variable_id);
                        }
                        variable_to_agent = Integer.parseInt(t.nextToken());
                        if(debug>=3){
                            System.out.println("Variable to agent id: "+variable_to_agent);
                        }
                        number_of_argument = Integer.parseInt(t.nextToken());
                        if(debug>=3){
                            System.out.println("Variable number of argument: "+ number_of_argument);
                        }

                        nodevariable = NodeVariable.getNodeVariable(variable_id);

                        nodevariables.add(nodevariable);

                        //agent.addNodeVariable(nodevariable);
                        Agent.getAgent(variable_to_agent).addNodeVariable(nodevariable);


                        // add the possible values
                        nodevariable.addIntegerValues(number_of_argument);



                    }
                    
                    if (token.equals("CONSTRAINT")) {
                        if (oldFormat == true) {
                            function_id++;
                        }
                        else {
                            function_id = Integer.parseInt(t.nextToken());
                        }
                        // tabular function
                        nodefunction = NodeFunction.putNodeFunction(function_id, new TabularFunction());
                        nodefunctions.add(nodefunction);
                        if(debug>=3){
                            System.out.println("nodefunction of id: "+function_id);
                        }


                        
                        // add to the current agent this function
                        if (oldFormat == true) {
                            agent.addNodeFunction(nodefunction);
                        }
                        else {
                            function_to_agent = Integer.parseInt(t.nextToken());
                            Agent.getAgent(function_to_agent).addNodeFunction(nodefunction);
                        }

                        argumentsOfFunction = new LinkedList<NodeVariable>();

                        while(t.hasMoreTokens()){
                            variable_to_function = Integer.parseInt(t.nextToken());
                            argumentsOfFunction.add( NodeVariable.getNodeVariable(variable_to_function));
                            if(debug>=3){
                                System.out.println("Function "+function_id+ " has parameter "+ variable_to_function);
                            }


                            // add this nodefunction to the actual nodevariable's neighbour
                            NodeVariable.getNodeVariable(variable_to_function).addNeighbour(nodefunction);
                            // viceversa
                            nodefunction.addNeighbour(NodeVariable.getNodeVariable(variable_to_function));
                            

                        }

                        //ok, here we have function_id and all the parameters it needs

                        /*functionevaluator = new TabularFunction();

                        nodefunction.setFunction(functionevaluator);

                        Iterator<NodeVariable> itv = argumentsOfFunction.iterator();
                        while (itv.hasNext()) {
                            NodeVariable nodeVariable = itv.next();
                            functionevaluator.addParameter(nodeVariable);
                        }*/

                        // function evaluator created

                    }

                    if (token.equals("F")) {
                        arguments = new NodeArgument[nodefunction.getFunction().parametersNumber()];
                        for (int i = 0; i < nodefunction.getFunction().parametersNumber(); i++) {
                            //arguments[i] = NodeArgument.getNodeArgument(Integer.parseInt(t.nextToken()));
                            //FIXME integer or string?!
                            arguments[i] = NodeArgument.getNodeArgument(Integer.parseInt(t.nextToken()));
                        }
                        /*cost = (Integer.parseInt(t.nextToken()));
                        nodefunction.getFunction().addParametersCost(arguments, cost);*/

                        // check for infinite
                        costString = t.nextToken();
                        if (costString.equalsIgnoreCase("inf") || costString.equalsIgnoreCase("+inf")){
                            // positive infinity
                            nodefunction.getFunction().addParametersCost(arguments,
                                    Double.POSITIVE_INFINITY
                                    );
                        }
                        else if (costString.equalsIgnoreCase("-inf")) {
                            // negative infinity
                            nodefunction.getFunction().addParametersCost(arguments,
                                    Double.NEGATIVE_INFINITY
                                    );
                        }
                        else {
                        	if (oldFormat){
	                        	Integer x = Integer.parseInt(costString);
	                        	double value = MaxValue - (x/MultConst); 
	                        	nodefunction.getFunction().addParametersCost(arguments,value);
                        	} else {
                                nodefunction.getFunction().addParametersCost(arguments,
                                Double.parseDouble(costString)
                                );                        		
                        	}
                        }


                    }
                }
                line = in.readLine();
            }
            in.close();
        }catch (IOException e) {
            throw new InvalidInputFileException();
        }catch (Exception e) {
            throw new InvalidInputFileException();
        }

        if(debug>=3){
            System.out.println("NodeFunction:");
            Iterator<NodeFunction> itf = nodefunctions.iterator();
            while (itf.hasNext()) {
                NodeFunction nodeFunction = itf.next();
                System.out.println("Function "+nodeFunction+ " uses the function evaluator:\n"+nodeFunction.getFunction());
            }
        }

        return new MS_COP_Instance(nodevariables, nodefunctions, agents);
    }

}
