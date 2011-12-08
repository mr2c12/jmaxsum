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

import java.util.Arrays;
import misc.Utils;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class MessageContentArrayDouble extends  MessageContent{

    protected Double[] message;
    public static double epsilon=1E-5;
    final static int debug = test.DebugVerbosity.debugMessageContentMessageArrayDouble;

    public MessageContentArrayDouble(Double[] message){
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "message required to be created: "+Utils.toString(message));
                System.out.println("---------------------------------------");
        }
        this.message = new Double[message.length];
        System.arraycopy(message, 0, this.message, 0, message.length);
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "message created: "+Utils.toString(this.message));
                System.out.println("---------------------------------------");
        }
    }

     public MessageContentArrayDouble(int size, double value) {
        this.message = new Double[size];
        for (int i = 0; i < size; i++) {
            this.message[i] = value;
        }
    }

    public int size() {
        return this.message.length;
    }

    public Double getValue(int position) {
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "get "+position+" value of "+Utils.toString(message));
                System.out.println("---------------------------------------");
        }
        return this.message[position];
    }

    public void setValue(int position, Double value) {
        this.message[position] = value;
    }

    @Override
    public String toString(){
        return Utils.toString(this.message);
    }

    @Override
    public MessageContent clone() {
        Double[] newmessage = new Double[this.message.length];
        System.arraycopy(this.message, 0, newmessage, 0, this.message.length);
        return new MessageContentArrayDouble(newmessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageContentArrayDouble other = (MessageContentArrayDouble) obj;

        for (int i = 0; i < message.length; i++) {
            if (Math.abs(message[i]-other.message[i]) >= MessageContentArrayDouble.epsilon) {
                return false;
            }

        }

        return true;
    }





}
