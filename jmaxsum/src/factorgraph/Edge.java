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

package factorgraph;

import misc.TwoKeysHashtable;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class Edge {

    protected NodeFunction source;
    protected NodeVariable dest;

    public static TwoKeysHashtable<NodeFunction, NodeVariable, Edge> edgeList = new TwoKeysHashtable<NodeFunction, NodeVariable, Edge>();

    @Override
    public String toString() {
        return "Edge{" + "source=" + source + "dest=" + dest + '}';
    }

    public static Edge getEdge(NodeFunction source, NodeVariable dest) {
        if (Edge.edgeList.containsKey(source, dest)){
            return Edge.edgeList.get(source, dest);
        }
        else {
            Edge newEdge = new Edge(source, dest);
            Edge.edgeList.put(source, dest, newEdge);
            return newEdge;
        }
    }

    private Edge(NodeFunction source, NodeVariable dest) {
        this.source = source;
        this.dest = dest;
    }



    public NodeVariable getDest() {
        return dest;
    }

    public void setDest(NodeVariable dest) {
        this.dest = dest;
    }

    public NodeFunction getSource() {
        return source;
    }

    public void setSource(NodeFunction source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
            return false;
        }
        if (this.dest != other.dest && (this.dest == null || !this.dest.equals(other.dest))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 23 * hash + (this.dest != null ? this.dest.hashCode() : 0);
        return hash;
    }



    



}
