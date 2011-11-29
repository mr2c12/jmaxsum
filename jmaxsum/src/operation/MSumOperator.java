package operation;

import function.FunctionEvaluator;
import factorgraph.NodeArgument;
import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import messages.MessageContent;
import messages.MessageQ;
import messages.MessageR;
import messages.MessageRArrayDouble;
import messages.PostService;
import messages.RMessageList;
import misc.Utils;

/**
 * This class implements all the necessary methods to perform a correct execution of MaxSum or MinSum, depending on OTimes and OPlus
 * @author Michele Roncalli < roncallim at gmail dot com >
 */
public class MSumOperator implements Operator{

    OTimes otimes;
    OPlus oplus;

    final static int debug = test.DebugVerbosity.debugMSumOperator;

    public MSumOperator(OTimes otimes, OPlus oplus) {
        this.otimes = otimes;
        this.oplus = oplus;
    }

    
    
    
    public void setOplus(OPlus oplus) {
        this.oplus = oplus;
    }

    public void setOtimes(OTimes otimes) {
        this.otimes = otimes;
    }


    /**
     * Compute the alpha, the normalization factor
     * @param params array of <b>only</b> the q-messages used in the computation of alpha
     * @return the normalization factor
     */
    public double computeAlpha(NodeVariable sender, NodeFunction receiver, LinkedList<MessageR> params) {
        if (params == null){
            return 0.0;
        }
        if (params.size()==0) {
            // what is the length of the array?
            return 0.0;
        }
        MessageQ acc = this.otimes.otimes(sender, receiver, params);

        double alpha = acc.getValue(0);
        for (int i = 1; i < acc.size(); i++) {
            alpha = alpha + acc.getValue(i);
        }
        alpha *= -1.0;
        alpha /= acc.size();
        return alpha;
    }

    /**
     * It compute the q-message, given the normalization factor alpha and the list of r-messages
     * @param alpha the normalization factor
     * @param rmessages list of rmessages (aggregated in the computation)
     * @return the q-message, or a <b>null</b> value if the list of rmessages is empty
     */
    public MessageQ computeQ(NodeVariable sender, NodeFunction receiver, double alpha, LinkedList<MessageR> rmessages) {
        if (rmessages.size() == 0){
            return null;
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Ready to prepare messageq with sender "+sender + " and receiver " + receiver);
                System.out.println("---------------------------------------");
        }

        MessageQ acc = this.otimes.otimes(sender, receiver, rmessages);

        for (int i = 0; i < acc.size(); i++) {
            acc.setValue(i, acc.getValue(i) + alpha);
        }
        return acc;
    }



    /**
     * Summarize the rmessages
     * @param rmessages array of <b>only</> r-messages to be used in the computation of Z
     * @return the Z array
     */
    public MessageContent computeZ(NodeVariable x, LinkedList<MessageR> rmessages) {

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Computing Z for "+x+" with "+rmessages.size()+" messages");
                System.out.println("---------------------------------------");
        }


        if (rmessages.size() == 0) {
            return this.otimes.nullMessage(x, null, x.size()).getMessage();
        }
        else {
            return this.otimes.otimes(x, null,rmessages).getMessage();
        }
    }
    
    

    public boolean updateQ(NodeVariable x, NodeFunction f, PostService postservice) {
        NodeFunction function = null;

        LinkedList<MessageR> rmessages = new LinkedList<MessageR>();

        // R from other functions f' to v
        //Iterator<NodeFunction> iterator = this.variableToFunctions.get(v).iterator();
        Iterator<NodeFunction> iterator = x.getNeighbour().iterator();
        // every new iterator value is a new index in M(i)
        while (iterator.hasNext()){
            function = iterator.next();
            if (!(function.equals(f))) {
                //rmessages.add(postservice.readRMessage(f, x));

                if (postservice.readRMessage(function, x) != null) {
                    // if there's a message in (f,x) add it to the list
                    rmessages.add(postservice.readRMessage(function, x));
                }



            }
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + " computeq from "+x+" to "+f);
                System.out.print("rmessages contains: ");
                for (MessageR mr : rmessages){
                    System.out.print(mr + "(from "+mr.getSender()+" to "+mr.getReceiver() +") ");
                }
                System.out.println("");
                System.out.println("---------------------------------------");
        }

        MessageQ messageq = this.computeQ(x, f, this.computeAlpha(x, f, rmessages), rmessages);
        if (messageq == null){
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Building up messageR and messageQ");
                    MessageR mr = this.oplus.nullMessage(f, x, x.size());
                    System.out.println("Created messager " + mr + " from "+ mr.getSender()+ " (it should be "+f+") to " +mr.getReceiver()+ " (it should be "+x+")");
                    messageq = mr.toMessageQ();
                    System.out.println("---------------------------------------");
            }
            
            //messageq = this.oplus.nullMessage(f, x, x.size()).toMessageQ();
            messageq = this.otimes.nullMessage(x, function, x.size());
        }
        
        return postservice.sendQMessage(x, f, messageq);
    }

    public boolean updateR(NodeFunction f, NodeVariable x, PostService postservice) {
        NodeVariable variable = null;

        LinkedList<MessageQ> qmessages = new LinkedList<MessageQ>();
        

                // R from other functions f' to v
        //Iterator<NodeFunction> iterator = this.variableToFunctions.get(v).iterator();
        Iterator<NodeVariable> iterator = f.getNeighbour().iterator();
        // every new iterator value is a new index in M(i)
        while (iterator.hasNext()){
            variable = iterator.next();
            //if (variable != x) {
            if (!variable.equals(x)){
                //qmessages.add(this.mailMan.readQMessage(v, f));
                //modifierTable.put(f.getFunction().getParameterPosition(v), this.mailMan.readQMessage(v, f));
                if (postservice.readQMessage(variable, f) != null){
                    qmessages.add(postservice.readQMessage(variable, f));
                }
            }
        }

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[1].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[1].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "contains the following "+qmessages.size()+" qmessages:");
                Iterator<MessageQ> itq = qmessages.iterator();
                while (itq.hasNext()) {
                MessageQ messageQ = itq.next();
                    System.out.println("MessageQ "+messageQ + "  and sender "+messageQ.getSender());
                }
                System.out.println("---------------------------------------");
        }


        MessageR messager = this.oplus.oplus(f, x, f.getFunction(), qmessages);

        if (debug>=3) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "MessageR created: "+messager);
                System.out.println("---------------------------------------");
        }

        return postservice.sendRMessage(f, x, messager);
    }

    public void updateZ(NodeVariable x, PostService ps) {
        ps.setZMessage(x, this.computeZ(
                    x,
                    ps.getMessageRToX(x)
                ));

        if (debug>=1) {
                String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                System.out.println("---------------------------------------");
                System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "Z-message updated for "+x+" "+ ps.readZMessage(x));
                System.out.println("---------------------------------------");
        }


    }

    /**
     * Implementation of arg max of Z
     * @param x the NodeVariable
     * @param ps the Post Service
     * @return the position of the arg max
     */
    public int argOfInterestOfZ(NodeVariable x, PostService ps) {
        /*return this.computeZIndex(
                ps.readZMessage(x)
                );*/
        return this.oplus.argOfInterestOfZ(
                ps.readZMessage(x)
                );
    }


}
