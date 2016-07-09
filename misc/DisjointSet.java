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
 * Class for creating DisjointSet of type T.<br/>
 * Provide standard methods find and union (but please take a look at UNION implementation).
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class DisjointSet<T> {



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

    /**
     * Note that:<br/>
     * ds1.union(ds2) return the disjoint set of ds1 merging ds2.
     * @param set2 the second disjoint set
     * @return the merged disjoint set.
     */
    public DisjointSet<T> union(DisjointSet<T> set2){
        if (!(this.find().equals(set2.find()))){
            if (this.size()<set2.size()){
                set2.elements.addAll(this.elements);
                this.representant = set2.getRepresentant();
                this.elements = set2.getElements();
                return set2;
            }
            else {
                this.elements.addAll(set2.elements);
                set2.setRepresentant(this.getRepresentant());
                set2.setElements(this.getElements());
                return this;
            }
        }
        return this;

    }

    /**
     * More formal-correct implementation of UNION
     * @param set1 first disjoint set
     * @param set2 second disjoint set
     */
    @SuppressWarnings("unchecked")
    public static void union(DisjointSet set1, DisjointSet set2) {
        if (!(set1.find().equals(set2.find()))){
            if (set1.size()<set2.size()){
                set2.getElements().addAll(set1.getElements());
                /*set1.setRepresentant(set2.getRepresentant());
                set1.setElements(set2.getElements());*/
                set1 = set2;
            }
            else {
                set1.getElements().addAll(set2.getElements());
                /*set1.setRepresentant(set2.getRepresentant());
                set1.setElements(set2.getElements());*/
                set2 = set1;
            }
        }
    }

    public void setElements(LinkedList<T> elements) {
        this.elements = elements;
    }

    public void setRepresentant(T representant) {
        this.representant = representant;
    }

    @Override
    public String toString() {
        return "DisjointSet { [representant=" + representant  + "] elements=" + elements + '}';
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        // next line makes compiler complain for unsafe check
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


    public T getRepresentant(){
        return this.representant;
    }

    public LinkedList<T> getElements(){
        return this.elements;
    }


}
