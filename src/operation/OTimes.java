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
import java.util.LinkedList;
import messages.MessageQ;
import messages.MessageR;

// TODO: check if it's easy to change
// TODO: implement a correct actualValue() method in COP_instance

/**
 * This interface provides methods for oplus operator
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public interface OTimes {

    public MessageQ otimes(NodeVariable sender, NodeFunction receiver, MessageR m1, MessageR m2);

    public MessageQ otimes(NodeVariable sender, NodeFunction receiver, LinkedList<MessageR> messagelist);

    public MessageQ otimes(NodeVariable sender, NodeFunction receiver, MessageR[] messagearray);

    public MessageQ nullMessage(NodeVariable sender, NodeFunction receiver, int size);


}
