/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author mik
 */
public class RMessageList extends MessageList{

    final static int debug = test.DebugVerbosity.debugRMessageList;

    public RMessageList(){
        super();
    }


    public boolean setValue(NodeFunction f, NodeVariable v,  MessageR value){
        return super.setValue(f, v, value);
    }

    public MessageR getValue(NodeFunction f, NodeVariable v){
        return (MessageR) super.getValue(f, v);
    }

    public LinkedList<MessageR> getMessageRToX(NodeVariable x){
        LinkedList<MessageR> rmessages = new LinkedList<MessageR>();
        Iterator<Object> itf = super.messages.keySet().iterator();
        while (itf.hasNext()) {
            Object object = itf.next();
            //Iterator<Object> itx = super.messages.get(object).keySet().iterator();
            if ( super.messages.get(object).containsKey(x)) {
                rmessages.add((MessageR) super.messages.get(object).get(x));

                if (debug>=3) {
                        String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                        String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                        System.out.println("---------------------------------------");
                        MessageR m = (MessageR) super.messages.get(object).get(x);
                        System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "looking for to "+x+", chosen message from "+m.getSender()+" to "+m.getReceiver());
                        System.out.println("---------------------------------------");
                }


            }

        }

        return rmessages;
    }

}
