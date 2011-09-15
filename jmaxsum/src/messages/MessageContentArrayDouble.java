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

import misc.Utils;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class MessageContentArrayDouble extends  MessageContent{

    protected double[] message;

    public MessageContentArrayDouble(double[] message){
        this.message = new double[message.length];
        System.arraycopy(message, 0, this.message, 0, message.length);
    }

     public MessageContentArrayDouble(int size, double value) {
        this.message = new double[size];
        for (int i = 0; i < size; i++) {
            this.message[i] = value;
        }
    }

    public int size() {
        return this.message.length;
    }

    public double getValue(int position) {
        return this.message[position];
    }

    public void setValue(int position, double value) {
        this.message[position] = value;
    }

    @Override
    public String toString(){
        return Utils.toString(this.message);
    }

    @Override
    public MessageContent clone() {
        double[] newmessage = new double[this.message.length];
        System.arraycopy(this.message, 0, newmessage, 0, this.message.length);
        return new MessageContentArrayDouble(newmessage);
    }

}
