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


import java.util.LinkedList;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class DisjointSet<T> {

    @SuppressWarnings(value = "unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        // next line make compiler complain for unsafe check
        // but seems right to me: if obj is not instance of T
        // then line above return false
        // and cast will never be executed
        final DisjointSet<T> other = (DisjointSet<T>) obj;
        if (this.representant != other.representant && (this.representant == null || !this.representant.equals(other.representant))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.elements != null ? this.elements.hashCode() : 0);
        hash = 83 * hash + (this.representant != null ? this.representant.hashCode() : 0);
        return hash;
    }

    protected LinkedList<T> elements;
    protected T representant;

    public DisjointSet(T element){
        this.elements = new LinkedList<T>();
        this.representant = element;
        this.elements.add(element);
    }


    public T find(){
        return this.representant;
    }

    public int size(){
        return this.elements.size();
    }

    public DisjointSet<T> union(DisjointSet<T> set2){
        if (!(this.find().equals(set2.find()))){
            if (this.size()<set2.size()){
                set2.elements.addAll(this.elements);
                // TODO: update representant?
                
                return set2;
            }
            else {
                this.elements.addAll(set2.elements);
                // TODO: update representant?
                return this;
            }
        }
        return this;

    }

    @Override
    public String toString() {
        return "DisjointSet { [representant=" + representant  + "] elements=" + elements + '}';
    }




}
