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
        StringBuilder string = new StringBuilder();
        string.append("Agents number: ").append(this.agents.size()).append("\n");
        Iterator<Agent> ita = this.agents.iterator();
        while (ita.hasNext()) {
            Agent agent = ita.next();
            string.append("- ").append(agent).append("\n");
            string.append("\twith variables:\n");
            // nodevariable
            Iterator<NodeVariable> itv = agent.getVariables().iterator();
            while (itv.hasNext()) {
                NodeVariable nodeVariable = itv.next();
                string.append("\t- ").append(nodeVariable).append(" with neighbours: ").append(nodeVariable.stringOfNeighbour()).append("\n");
            }
            // nodefunction
            string.append("\twith function:\n");
            Iterator<NodeFunction> itf = agent.getFunctions().iterator();
            while (itf.hasNext()) {
                NodeFunction nodeFunction = itf.next();
                string.append("\t- ").append(nodeFunction).append(" with neighbours: ").append(nodeFunction.stringOfNeighbour()).append("\n");
                string.append("\t\twith function evaluator:\n");
                string.append(nodeFunction.getFunction());
            }

        }

        return string.toString();
    }


    public String toStringFile(){
        StringBuilder string = new StringBuilder();

        HashMap<NodeVariable, Integer> nodevariable_agent = new HashMap<NodeVariable, Integer>();
        HashMap<NodeFunction, Integer> nodefunction_agent = new HashMap<NodeFunction, Integer>();

        Iterator<NodeVariable> itnv;
        Iterator<NodeFunction> itnf;
        FunctionEvaluator fe;

        // AGENTS

        Iterator<Agent> ita = this.agents.iterator();
        while (ita.hasNext()) {
            Agent agent = ita.next();
            string.append("AGENT ").append(agent.id()).append("\n");

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
            string.append("VARIABLE ").append(nodeVariable.id()).append(" ").append(nodevariable_agent.get(nodeVariable)).append(" ").append(nodeVariable.size()).append("\n");
        }

        // FUNCTIONS

        itnf = this.nodefunctions.iterator();
        while (itnf.hasNext()) {
            NodeFunction nodeFunction = itnf.next();
            string.append("CONSTRAINT ").append(nodeFunction.id()).append(" ").append(nodefunction_agent.get(nodeFunction)).append(" ");

            // variables id
            fe = nodeFunction.getFunction();
            for (int i = 0; i < fe.parametersNumber(); i++) {
                string.append(fe.getParameter(i).id() + " ");
            }
            string.append("\n");

            // variables values and function cost
            string.append(fe.toStringForFile());
            
        }




        return string.toString();
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
