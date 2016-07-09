/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;


/**
 *
 * @author mik
 */
public class QMessageList extends MessageList{

    public QMessageList(){
        super();
    }


    public boolean setValue(NodeVariable v, NodeFunction f, MessageQ value){
        return super.setValue(v, f, value);
    }

    public MessageQ getValue(NodeVariable v, NodeFunction f){
        return (MessageQ) super.getValue(v, f);
    }
}
