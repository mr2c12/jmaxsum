package messages;


import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.LinkedList;


/**
 * Service class for managing messages
 * @author Michele Roncalli < roncallim at gmail dot com >
 */
public interface PostService {
    /**
     * Takes a message Q from x to f and it's value. It can be stored in a local enviroment or sent to the agent.
     * @param x the NodeVariable that is the origin of the message
     * @param f the NodeFunction that is the receiver of the message
     * @param value the value of the message
     * @return true if the new value is different
     */
    boolean sendQMessage(NodeVariable x, NodeFunction f, MessageQ value);
    /**
     * Takes a message R from f to x and it's value. It can be stored in a local enviroment or sent to the agent.
     * @param f the NodeFunction that is the origin of the message
     * @param x the NodeVariable that is the receiver of the message
     * @param value the value of the message
     * @return true if the new value is different
     */
    boolean sendRMessage(NodeFunction f, NodeVariable x, MessageR value);
    /**
     * Returns the value of the q-message from x to f
     * @param x NodeVariable sender
     * @param f NodeFunction receiver
     * @return the value of the message
     */
    MessageQ readQMessage(NodeVariable x, NodeFunction f);
    /**
     * Returns the value of the r-message from f to x
     * @param f NodeFunction sender
     * @param x NodeVariable receiver
     * @return the value of the message
     */
    MessageR readRMessage(NodeFunction f, NodeVariable x);


    MessageContent readZMessage(NodeVariable x);

    void setZMessage(NodeVariable x, MessageContent mc);

    public LinkedList<MessageR> getMessageRToX(NodeVariable x);

}
