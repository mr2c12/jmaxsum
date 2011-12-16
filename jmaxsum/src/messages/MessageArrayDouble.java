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

import factorgraph.Node;
import misc.Utils;

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class MessageArrayDouble implements Message{

    protected MessageContent message;
    protected Node sender;
    protected Node receiver;

    final static int debug = test.DebugVerbosity.debugMessageArrayDouble;

    public MessageContent getMessage() {
        return message;
    }

    public void setMessage(MessageContent message) {
        this.message = message;
    }

    public MessageArrayDouble(Node sender, Node receiver, int size, double value) {
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "creating with sender "+sender+" receiver "+receiver);
                System.out.println("---------------------------------------");
        }
        this.sender = sender;
        this.receiver = receiver;
        this.message = new MessageContentArrayDouble(size, value);
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "created with sender "+this.sender+" receiver "+this.receiver);
                System.out.println("---------------------------------------");
        }

    }

    public MessageArrayDouble(Node sender, Node receiver, double[] message) {
        Double[] m = new Double[message.length];
        //problem! does not work!
        //System.arraycopy(message, 0, m, 0, message.length);
        for (int index= 0; index < message.length; index++) {
            m[index] = message[index];
        }
        this.sender = sender;
        this.receiver = receiver;
        this.message = new MessageContentArrayDouble(m);
    }

    public MessageArrayDouble(Node sender, Node receiver, Double[] message) {
        if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "creating with sender "+sender+" receiver "+receiver);
            System.out.println("---------------------------------------");
        }
        this.sender = sender;
        this.receiver = receiver;
        this.message = new MessageContentArrayDouble(message);
        if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "created with sender "+this.sender+" receiver "+this.receiver);
            System.out.println("---------------------------------------");
        }
    }

    public MessageArrayDouble(Node sender, Node receiver, MessageContent mc){

        this.sender = sender;
        this.receiver = receiver;
        // create a new one
        this.message = mc.clone();

        if (debug>=3) {
            String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
            String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
            System.out.println("---------------------------------------");
            System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "created with sender "+this.sender+" receiver "+this.receiver);
            System.out.println("---------------------------------------");
        }

    }
   

    public double getValue(int position) {
        return this.message.getValue(position);
    }

    public void setValue(int position, double value){
        this.message.setValue(position, value);
    }

    public int size(){
        return this.message.size();
    }

    @Override
    public String toString(){
        return "Message from "+ this.sender + " to " + this.receiver + " = " + this.message.stringContent();
                //Utils.toString(this.message);
    }

    public boolean equals(Message m) {
        return this.message.equals(m.getMessage());
    }

    public boolean equalContent(Message other) {
        return this.message.equals(other.getMessage());
    }

    public void setSender(Node n) {
        this.sender = n;
    }

    public void setReceiver(Node n) {
        this.receiver = n;
    }

    public Node getReceiver() {
        return receiver;
    }

    public Node getSender() {
        return sender;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageArrayDouble other = (MessageArrayDouble) obj;
        if (this.message != other.message && (this.message == null || !this.message.equals(other.message))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }


    

}
