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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Hash table with 2 key for an element.
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class TwoKeysHashtable<K1, K2, TV>  {
    
    private HashMap<K1, HashMap<K2, TV>> bigtable;

    public TwoKeysHashtable(){
        this.bigtable = new HashMap<K1, HashMap<K2, TV>>();
    }

    public boolean containsKey(K1 k1, K2 k2){
        if (!this.bigtable.containsKey(k1)) {
            return false;
        }
        else
            return this.bigtable.get(k1).containsKey(k2);
    }

    public TV put (K1 k1, K2 k2, TV tv) {
        if (!this.bigtable.containsKey(k1)) {
            this.bigtable.put(k1, new HashMap<K2, TV>());
        }
        return (this.bigtable.get(k1)).put(k2, tv);
    }

    public TV get(K1 k1, K2 k2){
        if (!this.containsKey(k1, k2)){
            return null;
        }
        else {
            return (this.bigtable.get(k1)).get(k2);
        }
    }

    public int size(){
        int size = 0;
        Iterator<K1> it1 = this.bigtable.keySet().iterator();
        while (it1.hasNext()) {
            K1 k1 = it1.next();
            Iterator<K2> it2 = this.bigtable.get(k1).keySet().iterator();
            while (it2.hasNext()) {
                K2 k2 = it2.next();
                size++;
            }

        }

        return size;
    }

    public void clear(){
        this.bigtable = new HashMap<K1, HashMap<K2, TV>>();
    }

    public boolean isEmpty(){
        return this.isEmpty();
    }

    public Set<K1> firstKeySet(){
        return bigtable.keySet();
    }

    public Set<K2> secondKeySet(K1 firstKey){
        return this.bigtable.get(firstKey).keySet();
    }
}
