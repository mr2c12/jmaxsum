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

package messages;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public abstract class MessageContent {

    public abstract int size();

    public abstract Double getValue(int position);

    public abstract void setValue(int position, Double value);

    public abstract MessageContent clone();

    @Override
    public boolean equals(Object mc){
        if ((mc == null)|| !(mc instanceof MessageContent)){
            return false;
        }
        if (this.size() != ((MessageContent)mc).size()){
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            //if (this.getValue(i) != ((MessageContent)mc).getValue(i)){
            if (!this.getValue(i).equals( ((MessageContent)mc).getValue(i) )){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.size(); i++) {
            hash += (this.getValue(i).hashCode());
        }
        return hash;
    }

    public String stringContent(){
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < this.size(); i++) {
            if (this.getValue(i) == null){
                s.append("null").append(",");
            } else {
                s.append(this.getValue(i)).append(",");
            }
        }
        s.append("]");
        return s.toString();
    }

}
