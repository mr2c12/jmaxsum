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

/**
 *
 * @author Michele Roncalli <roncallim at gmail dot com>
 */
public interface Message {

    public int size();

    public double getValue(int position);

    public void setValue(int position, double value);

    public boolean equals(Message m);

    public MessageContent getMessage();

    public void setMessage(MessageContent message);

    public boolean equalContent(Message other);

    public void setSender(Node n);
    public void setReceiver(Node n);

    public Node getReceiver();
    public Node getSender();

}
