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

import factorgraph.Edge;
import java.util.Collection;
import java.util.HashMap;

/**
 * Disjoint sets of type Edge manager.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class DisjointSetEdge {

    protected static HashMap<Edge, DisjointSet<Edge>> map = new HashMap<Edge, DisjointSet<Edge>>();

    public static void initDS(Collection<Edge> edgelist){
        for (Edge e : edgelist){
            DisjointSetEdge.map.put(
                    e,
                    new DisjointSet<Edge>(e)
                    );
        }
    }

    public static boolean sameDS(Edge e1, Edge e2){
        return (
                map.get(e1).equals(map.get(e2))
                );
    }

    public static void union(Edge e1, Edge e2){
        if (!sameDS(e1,e2)){
            // merge
            map.put(e1, map.get(e1).union(map.get(e2)));
            map.put(e2, map.get(e1));
        }
    }

    public static int size(){
        return map.size();
    }

    
    public static String toTestString(){
        String string = "";
        for (Edge key : map.keySet()){
            string += "[ "+key+"]\t->\t"+map.get(key)+"\n";
        }


        return string;
    }

}
