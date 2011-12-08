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

package operation;

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.Iterator;
import java.util.LinkedList;
import messages.MessageFactory;
import messages.MessageQ;
import messages.MessageR;

/**
 * Implementation of OTimes
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public class OTimes_MaxSum implements OTimes{

    private MessageFactory factory;
    private static int debug = test.DebugVerbosity.debugOTimes_MaxSum;

    public OTimes_MaxSum(MessageFactory factory) {
        this.factory = factory;
    }


    public MessageQ otimes(NodeVariable sender, NodeFunction receiver, MessageR m1, MessageR m2) {
        if (m1.size() != m2.size()) {
            throw new IllegalArgumentException();
        }

        double[] content = new double[m1.size()];
        for (int i = 0; i < content.length; i++) {
            content[i] = m1.getValue(i) + m2.getValue(i);
        }
        return this.factory.getMessageQ(sender, receiver, content);
    }

    public MessageQ nullMessage(NodeVariable sender, NodeFunction receiver, int size) {
        double[] content = new double[size];
        for (int i = 0; i < content.length; i++) {
            content[i] = 0.0;
        }
        return this.factory.getMessageQ(sender, receiver, content);
    }

    public MessageQ otimes(NodeVariable sender, NodeFunction receiver, LinkedList<MessageR> messagelist) {
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + " with "+messagelist.size()+" messages");
                System.out.println("---------------------------------------");
        }
        if (messagelist == null){
            return null;
        }
        if (messagelist.size() == 0){
            return null;
        }
        MessageR[] messagearray = new MessageR[messagelist.size()];
        Iterator<MessageR> itr = messagelist.iterator();
        int index = 0;
        while (itr.hasNext()) {
            MessageR messageR = itr.next();
            messagearray[index] = messageR;

            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "At position "+index+ " there is the message " + messageR);
                    System.out.println("---------------------------------------");
            }


            index ++;
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Array created with "+ messagearray.length + " values");
                System.out.println("---------------------------------------");
        }


        return this.otimes(sender, receiver, messagearray);
    }

    public MessageQ otimes(NodeVariable sender, NodeFunction receiver, MessageR[] messagearray) {
        if (messagearray == null)
            return null;
        if (messagearray.length == 0)
            return null;

        int dimension = messagearray[0].size();
        double[] content = new double[dimension];
        for (int i = 0; i < messagearray.length; i++) {
            if (messagearray[i].size()!= dimension) {
                throw new IllegalArgumentException();
            }
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "On array "+messagearray[i]);
                    System.out.println("---------------------------------------");
            }
            for (int j = 0; j < messagearray[i].size(); j++) {

                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Loop: j="+j);
                        System.out.println("---------------------------------------");
                }

                // TODO: check!
                /*if (content[j] == null){
                    content[j] = messagearray[i].getValue(j);
                } else {*/
                    content[j]+=messagearray[i].getValue(j);
                //}

            }
        }
        return this.factory.getMessageQ(sender, receiver, content);
    }

}
