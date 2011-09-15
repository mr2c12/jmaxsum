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

package system;

import exception.VariableNotSetException;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import function.FunctionEvaluator;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import maxsum.Agent;
import messages.PostService;
import operation.Operator;

/**
 * Instance of COP problem.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public abstract class COP_Instance {

    protected HashSet<NodeVariable> nodevariables;
    protected HashSet<NodeFunction> nodefunctions;
    protected HashSet<NodeArgument> nodeargumens;
    protected HashSet<Agent> agents;


    public COP_Instance() {
        nodevariables = new HashSet<NodeVariable>();
        nodefunctions = new HashSet<NodeFunction>();
        nodeargumens = new HashSet<NodeArgument>();
        agents = new HashSet<Agent>();
    }

    public COP_Instance(HashSet<NodeVariable> nodevariables, HashSet<NodeFunction> nodefunctions, HashSet<NodeArgument> nodeargumens, HashSet<Agent> agents) {
        this.nodevariables = nodevariables;
        this.nodefunctions = nodefunctions;
        this.nodeargumens = nodeargumens;
        this.agents = agents;
    }

    public HashSet<Agent> getAgents() {
        return agents;
    }

    public void setAgents(HashSet<Agent> agents) {
        this.agents = agents;
    }

    public HashSet<NodeArgument> getNodeargumens() {
        return nodeargumens;
    }

    public void setNodeargumens(HashSet<NodeArgument> nodeargumens) {
        this.nodeargumens = nodeargumens;
    }

    public HashSet<NodeFunction> getNodefunctions() {
        return nodefunctions;
    }

    public void setNodefunctions(HashSet<NodeFunction> nodefunctions) {
        this.nodefunctions = nodefunctions;
    }

    public HashSet<NodeVariable> getNodevariables() {
        return nodevariables;
    }

    public void setNodevariables(HashSet<NodeVariable> nodevariables) {
        this.nodevariables = nodevariables;
    }

    public String toTestString(){
        String string = "";
        string += "Agents number: " + this.agents.size()+"\n";
        Iterator<Agent> ita = this.agents.iterator();
        while (ita.hasNext()) {
            Agent agent = ita.next();
            string += "- " + agent+"\n";
            string += "\twith variables:\n";
            // nodevariable
            Iterator<NodeVariable> itv = agent.getVariables().iterator();
            while (itv.hasNext()) {
                NodeVariable nodeVariable = itv.next();
                string += "\t- "+nodeVariable+" with neighbours: "+nodeVariable.stringOfNeighbour()+"\n";
            }
            // nodefunction
            string += "\twith function:\n";
            Iterator<NodeFunction> itf = agent.getFunctions().iterator();
            while (itf.hasNext()) {
                NodeFunction nodeFunction = itf.next();
                string += "\t- "+nodeFunction+" with neighbours: "+nodeFunction.stringOfNeighbour()+"\n";
                string += "\t\twith function evaluator:\n";
                string += nodeFunction.getFunction();
            }

        }

        return string;
    }


    public String toStringFile(){
        String string = "";

        HashMap<NodeVariable, Integer> nodevariable_agent = new HashMap<NodeVariable, Integer>();
        HashMap<NodeFunction, Integer> nodefunction_agent = new HashMap<NodeFunction, Integer>();

        Iterator<NodeVariable> itnv;
        Iterator<NodeFunction> itnf;
        FunctionEvaluator fe;

        // AGENTS

        Iterator<Agent> ita = this.agents.iterator();
        while (ita.hasNext()) {
            Agent agent = ita.next();
            string += "AGENT " + agent.id()+"\n";

            itnv = agent.getVariables().iterator();
            while (itnv.hasNext()) {
                NodeVariable nodeVariable = itnv.next();
                nodevariable_agent.put(nodeVariable, agent.id());
            }

            itnf = agent.getFunctions().iterator();
            while (itnf.hasNext()) {
                NodeFunction nodeFunction = itnf.next();
                nodefunction_agent.put(nodeFunction, agent.id());
            }

        }

        // VARIABLES

        itnv = this.nodevariables.iterator();
        while (itnv.hasNext()) {
            NodeVariable nodeVariable = itnv.next();
            string += "VARIABLE "+ nodeVariable.id() +
                    // agent id
                    " " + nodevariable_agent.get(nodeVariable) +
                    // number of values
                    " " + nodeVariable.size()
                    + "\n";
        }

        // FUNCTIONS

        itnf = this.nodefunctions.iterator();
        while (itnf.hasNext()) {
            NodeFunction nodeFunction = itnf.next();
            string += "CONSTRAINT "+ nodeFunction.id() +
                    // agent id
                    " " + nodefunction_agent.get(nodeFunction) + " ";

            // variables id
            fe = nodeFunction.getFunction();
            for (int i = 0; i < fe.parametersNumber(); i++) {
                string += fe.getParameter(i).id() + " ";
            }
            string += "\n";

            // variables values and function cost
            string += fe.toStringForFile();
            
        }




        return string;
    }

    public void toFile(String path){

        FileOutputStream of;

        try {
            of = new FileOutputStream(path);
            of.write(this.toStringFile().getBytes());
            of.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    void setPostService(PostService ps) {
        Iterator<Agent> it = this.agents.iterator();
        while (it.hasNext()) {
            Agent agent = it.next();
            agent.setPostservice(ps);
        }
    }

    void setOperator(Operator op) {
        Iterator<Agent> it = this.agents.iterator();
        while (it.hasNext()) {
            Agent agent = it.next();
            agent.setOp(op);
        }
    }

    void setPostServiceAndOperator(PostService ps, Operator op) {
        Iterator<Agent> it = this.agents.iterator();
        while (it.hasNext()) {
            Agent agent = it.next();
            agent.setPostservice(ps);
            agent.setOp(op);
        }
    }


    public abstract double actualValue() throws VariableNotSetException;


}
