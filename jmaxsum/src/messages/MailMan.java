

package messages;

import exception.ReceiverNullException;
import exception.SenderNullException;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class implements the postService in local mode
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

    public void sendQMessage(NodeVariable x, NodeFunction f, MessageQ value) {
        this.qmessages.setValue(x, f, value);
    }

    public void sendRMessage(NodeFunction f, NodeVariable x, MessageR value) {
        this.rmessages.setValue(f, x, value);
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

    public LinkedList<MessageR> getMessageRToX(NodeVariable x) {
        return this.rmessages.getMessageRToX(x);
    }
}
