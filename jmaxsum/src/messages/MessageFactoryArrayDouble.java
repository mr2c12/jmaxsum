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
public class MessageFactoryArrayDouble implements MessageFactory{

    final static int debug = test.DebugVerbosity.debugMessageFactoryArrayDouble;

    public MessageQ getMessageQ(NodeVariable sender, NodeFunction receiver, double[] content) {
        return new MessageQArrayDouble(sender, receiver, content);
    }

    public MessageR getMessageR(NodeFunction sender, NodeVariable receiver, double[] content) {
        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "creating messager from "+sender+" to "+receiver);
                System.out.println("---------------------------------------");
        }
        return new MessageRArrayDouble(sender, receiver, content);
    }

}
