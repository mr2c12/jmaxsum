

package messages;

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class implements the Post Service in local mode.
 * @author Michele Roncalli <roncallim@gmail.com>
 */
public class MailMan implements PostService{

    private QMessageList qmessages;
    private RMessageList rmessages;
    private HashMap<NodeVariable, MessageContent> zmessages;

    public MailMan() {
        this.qmessages = new QMessageList();
        this.rmessages = new RMessageList();
        this.zmessages = new HashMap<NodeVariable, MessageContent>();

    }





    public void setMessagesList(QMessageList qmessages, RMessageList rmessages){
        this.qmessages = qmessages;
        this.rmessages = rmessages;
    }

    public boolean sendQMessage(NodeVariable x, NodeFunction f, MessageQ value) {
        return this.qmessages.setValue(x, f, value);
    }

    public boolean sendRMessage(NodeFunction f, NodeVariable x, MessageR value) {
        return this.rmessages.setValue(f, x, value);
    }

    public MessageQ readQMessage(NodeVariable x, NodeFunction f) {
        return this.qmessages.getValue(x, f);
    }

    public MessageR readRMessage(NodeFunction f, NodeVariable x) {
        return this.rmessages.getValue(f, x);
    }

    public MessageContent readZMessage(NodeVariable x) {
        return this.zmessages.get(x);
    }

    public void setZMessage(NodeVariable x, MessageContent mc) {
        this.zmessages.put(x, mc);
    }

    /**
     * List of message R addressed to x
     * @param x receiver of R-messages
     * @return the list of R-messages
     */
    public LinkedList<MessageR> getMessageRToX(NodeVariable x) {
        return this.rmessages.getMessageRToX(x);
    }
}
