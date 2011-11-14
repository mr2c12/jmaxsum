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

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;


/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class MessageRArrayDouble extends MessageArrayDouble implements MessageR {

    final static int debug = test.DebugVerbosity.debugMessageRArrayDouble;
    
    public MessageRArrayDouble(NodeFunction sender, NodeVariable receiver, int size, double value){
        super(sender, receiver, size,value);
    }

    public MessageRArrayDouble(NodeFunction sender, NodeVariable receiver, double[] message) {
        super(sender, receiver, message);
    }

    public MessageRArrayDouble(NodeFunction sender, NodeVariable receiver, Double[] message) {
        super(sender, receiver, message);
    }

    MessageRArrayDouble(NodeFunction sender, NodeVariable receiver, MessageContent message) {
        super(sender, receiver, message);
    }


    public MessageQ toMessageQ() {
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + " reverting to Q from " +this.sender + " to " + this.receiver);
                System.out.println("---------------------------------------");
        }
        return new MessageQArrayDouble(this.getReceiver(), this.getSender(), this.message);
    }

    @Override
    public boolean equals(Message m) {
        if (m instanceof MessageR) {
            return super.equals(m);
        }
        else {
            return false;
        }
    }

    public NodeVariable getReceiver() {
        return (NodeVariable) this.receiver;
    }

    public void setReceiver(NodeVariable receiver) {
        this.receiver = receiver;
    }

    public NodeFunction getSender() {
        return (NodeFunction) this.sender;
    }

    public void setSender(NodeFunction sender) {
        this.sender = sender;
    }




}
