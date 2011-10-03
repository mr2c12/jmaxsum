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

package function;
import factorgraph.NodeArgument;
import factorgraph.NodeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;



/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class TabularFunction extends FunctionEvaluator{



    protected HashMap<String, Double> costTable;

    public TabularFunction() {
        this.costTable = new HashMap<String, Double>();
    }

    public void addParametersCost(NodeArgument[] params, double cost){
        StringBuilder key = new StringBuilder();
        key.append("");
        for (int i = 0; i < params.length; i++) {
            key.append(params[i].toString()).append(";");
        }
        this.costTable.put(key.toString(), cost);
        
        // set the min and the max
        if (this.minCost == null || cost < this.minCost) {
            this.minCost = cost;
        }
        if (this.maxCost == null || cost > this.maxCost) {
            this.maxCost = cost;
        }
    }

    @Override
    public double evaluate(NodeArgument[] params) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            key.append(params[i].toString()).append(";");
        }
        return this.costTable.get(key.toString());
    }

    public int entryNumber(){
        return this.costTable.size();
    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        StringBuilder append = string.append("Function evaluator with " + this.entryNumber() + " entries\n");
        string.append("NodeVariable used: ");
        Iterator<NodeVariable> it = this.parameters.iterator();
        while (it.hasNext()) {
            NodeVariable nodeVariable = it.next();
            StringBuilder append1 = string.append(nodeVariable + " ");
        }
        string.append("\n");
        /*Iterator<String> keyit = this.costTable.keySet().iterator();
        while (keyit.hasNext()) {
            String key = keyit.next();
            string += "["+key+"] "+this.costTable.get(key)+"\n";
        }*/
        HashMap<NodeArgument[], Double> table = this.getParametersCost();
        Iterator<NodeArgument[]> ita = table.keySet().iterator();
        while (ita.hasNext()) {
            NodeArgument[] nodeArguments = ita.next();
            string.append("[ ");
            for (int i = 0; i < nodeArguments.length; i++) {
                string.append(nodeArguments[i]).append(" ");
            }
            string.append("] ").append(table.get(nodeArguments)).append("\n");
        }

        return string.toString();
    }

    public HashMap<NodeArgument[], Double> getParametersCost() {
        HashMap<NodeArgument[], Double> table = new HashMap<NodeArgument[], Double>();
        Iterator<String> its = this.costTable.keySet().iterator();
        String string;
        StringTokenizer t;
        LinkedList<NodeArgument> argumentlist;
        while (its.hasNext()) {
            string = its.next();
            t = new StringTokenizer(string, ";");
            argumentlist = new LinkedList<NodeArgument>();
            while(t.hasMoreTokens()){
                argumentlist.add(NodeArgument.getNodeArgument(t.nextToken()));
            }
            NodeArgument[] array = argumentlist.toArray(new NodeArgument[0]);
            table.put( array ,this.costTable.get(string) );
        }


        return table;
    }

    public String toStringForFile() {
        StringBuilder string = new StringBuilder();
        HashMap<NodeArgument[], Double> table = this.getParametersCost();
        Iterator<NodeArgument[]> ita = table.keySet().iterator();
        while (ita.hasNext()) {
            NodeArgument[] nodeArguments = ita.next();
            string.append("F ");
            for (int i = 0; i < nodeArguments.length; i++) {
                string.append(nodeArguments[i]).append(" ");
            }
            string.append(table.get(nodeArguments)).append("\n");
        }

        return string.toString();
    }

    @Override
    public ArrayList<Double> getCostValues() {
        return new ArrayList<Double>(this.costTable.values());
    }


}
