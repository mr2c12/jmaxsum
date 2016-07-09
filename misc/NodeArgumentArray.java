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

import factorgraph.NodeArgument;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class NodeArgumentArray {

    // TODO: please improve: no more than one object for array
    private static HashMap<NodeArgumentArray, NodeArgumentArray> arrayMap = new HashMap<NodeArgumentArray, NodeArgumentArray>();
    private final NodeArgument[] data;

    private NodeArgumentArray(NodeArgument[] data)
    {
        if (data == null)
        {
            throw new NullPointerException();
        }
        this.data = new NodeArgument[data.length];
        for (int i = 0; i < data.length; i++) {
            NodeArgument nodeArgument = data[i];
            this.data[i] = nodeArgument;
        }
    }

    public static int numberOfNodeArgumentArray(){
        return NodeArgumentArray.arrayMap.size();
    }
    
    public static NodeArgumentArray getNodeArgumentArray(NodeArgument[] data){
        NodeArgumentArray tmp = new NodeArgumentArray(data);
        if (NodeArgumentArray.arrayMap.containsKey(tmp)){
            return NodeArgumentArray.arrayMap.get(tmp);
        }
        else {
            NodeArgumentArray.arrayMap.put(tmp, tmp);
            return tmp;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodeArgumentArray other = (NodeArgumentArray) obj;
        if (this.data.length != other.data.length) {
            return false;
        }
        else {
            for (int i = 0; i < data.length; i++) {
                if (!(data[i].equals(other.data[i]))){
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash=0;// = Arrays.hashCode(this.data);
        for (int i = 0; i < data.length; i++) {
            NodeArgument nodeArgument = data[i];
            hash += nodeArgument.hashCode();
        }
        return hash;
    }

    public NodeArgument[] getArray(){
        return this.data;
    }

    @Override
    public String toString() {
        String string = "NodeArgumentArray{" + "data=[";
        for (int i = 0; i < data.length; i++) {
            NodeArgument nodeArgument = data[i];
            string+=nodeArgument+",";
        }
        string+="]}";
        return string;
    }





}
