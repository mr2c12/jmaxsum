package messages;

import java.util.HashMap;

/**
 * This class implements a list of message (either Q or R)
 * @author Michele Roncalli
 */
public class MessageList {


    final static int debug = test.DebugVerbosity.debugMessageList;
    /**
     * The hashmap contains the list of message.
     * Each message is identified by a couple of keys.
     */
    protected HashMap<Object, HashMap<Object,Message>> messages;

    public MessageList(){
        this.messages = new HashMap<Object, HashMap<Object,Message>>();
    }

    /**
     * Save the message in the position <c1,c2>.
     * If the key <c1,c2> does not exist, this method create it first.
     * @param c1 the first key
     * @param c2 the second key
     * @param value the message, can be null
     * @return true if the new message is different
     */
    public boolean setValue(Object c1, Object c2, Message value){
        boolean retVal;
        if (!(this.messages.containsKey(c1))){
            this.messages.put(c1, new HashMap<Object,Message>());
            if (debug>=3) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "entry does not exists: return false (different)");
                    System.out.println("---------------------------------------");
            }
            retVal = true;
        }
        else {
            if (debug>=3) {
                if (!value.equals(( this.messages.get(c1) ).get(c2))) {
                    String dmethod = Thread.currentThread().getStackTrace()[2].getMethodName();
                    String dclass = Thread.currentThread().getStackTrace()[2].getClassName();
                    System.out.println("---------------------------------------");
                    System.out.println("[class: "+dclass+" method: " + dmethod+ "] " + "saving "+value+" instead of "+(this.messages.get(c1) ).get(c2));
                    System.out.println("---------------------------------------");
                }
            }
            retVal = !value.equals(
                    ( this.messages.get(c1) ).get(c2)
                    );

                    
        }
        ( this.messages.get(c1) ).put(c2, value);

        return retVal;
    }

    /**
     * Get the message in the position <c1,c2>.
     * If the key <c1,c2> does not exist, this method create it first.
     * @param c1 the first key
     * @param c2 the second key
     * @return the message, can be null
     */
    public Message getValue(Object c1, Object c2){
        if (!(this.messages.containsKey(c1))){
            //this.messages.put(c1, new HashMap<Object,Message>());
            return null;
        }
        return ( this.messages.get(c1) ).get(c2);
    }

    /**
     * Overrides the method containsKey of HashMap
     * @param c1 the first key
     * @param c2 the second key
     * @return true if and only if <c1,c2> does exist
     */
    public boolean containsKey(Object c1, Object c2) {
        if (this.messages.containsKey(c1)){
            return this.messages.get(c1).containsKey(c2);
        }
        else {
            return false;
        }
    }

}
