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

package misc;

import factorgraph.Node;
import java.util.Collection;
import java.util.HashMap;

/**
 * Disjoint sets of type Node manager.<br/>
 * Used in Bounded Max Sum when executing Kruskal algorithm.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class DisjointSetNode {

    protected static HashMap<Node, DisjointSet<Node>> map;// = new HashMap<Node, DisjointSet<Node>>();

    public static void initDS(Collection<Node> nodelist){
        map = new HashMap<Node, DisjointSet<Node>>();
        for (Node e : nodelist){
            DisjointSetNode.map.put(
                    e,
                    new DisjointSet<Node>(e)
                    );
        }
    }

    public static boolean sameDS(Node e1, Node e2){
        return (
                map.get(e1).equals(map.get(e2))
                );
    }

    public static void union(Node e1, Node e2){
        if (!sameDS(e1,e2)){
            // merge
            //map.put(e1, map.get(e1).union(map.get(e2)));
            //map.put(e2, map.get(e1));


            //all nodes in map.get(e2) and map.get(e1) have to point to u.
            DisjointSet<Node> u = map.get(e1).union(map.get(e2));
            for (Node e : u.getElements()) { // e1 and e2 are included in u
                 map.put(e, u);
            }
        }
    }

    public static int size(){
        return map.size();
    }

    
    public static String toTestString(){
        String string = "";
        for (Node key : map.keySet()){
            string += "[ "+key+"]\t->\t"+map.get(key)+"\n";
        }


        return string;
    }

}
