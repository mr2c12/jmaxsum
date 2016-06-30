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
public class MessageQArrayDouble extends MessageArrayDouble implements MessageQ {

    static final int debug = test.DebugVerbosity.debugMessageQArrayDouble;

    public MessageQArrayDouble(NodeVariable sender, NodeFunction receiver, int size, double value){
        super(sender, receiver, size,value);
    }

    public MessageQArrayDouble(NodeVariable sender, NodeFunction receiver, double[] message) {
        super(sender, receiver, message);
    }


    public MessageQArrayDouble(NodeVariable sender, NodeFunction receiver, Double[] message) {
        super(sender, receiver, message);
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Sender " + sender + " Receiver "+receiver);
                System.out.println("---------------------------------------");
        }
    }

    MessageQArrayDouble(NodeVariable sender, NodeFunction receiver, MessageContent message) {
        super(sender, receiver, message);
    }




    public MessageR toMessageR() {
        MessageR r = new MessageRArrayDouble(this.getReceiver(), this.getSender(), this.message);
        return r;
    }

    @Override
    public boolean equals(Message m) {
        if (m instanceof MessageQ) {
            return super.equals(m);
        }
        else {
            return false;
        }
    }



    public NodeFunction getReceiver() {
        return (NodeFunction) receiver;
    }

    public void setReceiver(NodeFunction receiver) {
        this.receiver = receiver;
    }

    public NodeVariable getSender() {
        return (NodeVariable) sender;
    }

    public void setSender(NodeVariable sender) {
        this.sender = sender;
    }
}
