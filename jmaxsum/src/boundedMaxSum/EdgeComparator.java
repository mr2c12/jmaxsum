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

package boundedMaxSum;

import factorgraph.Edge;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.Comparator;
import java.util.HashMap;
import misc.TwoKeysHashtable;

/**
 * Class implementing Comparator, to be used in a priority queue of edges.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class EdgeComparator implements Comparator<Edge>{

    protected HashMap<Edge, Double> weightTable;

    public EdgeComparator(HashMap<Edge, Double> weightTable) {
        this.weightTable = weightTable;
    }


/**
 * Compares its two arguments for order. Returns a negative integer, zero, or a
 * positive integer as the first argument is less than, equal to, or greater
 * than the second.
 * @param o1 first edge
 * @param o2 second edge
 * @return the comparison
 */
    public int compare(Edge o1, Edge o2) {
        double w1 = this.weightTable.get(o1);
        double w2 = this.weightTable.get(o2);
        if (w1 == w2) {
            return 0;
        }
        else if (w1>w2){
            return 1;
        }
        else {
            return -1;
        }
    }

}
