package factorgraph;

import java.util.HashMap;

/**
 * A possible value of a NodeVariable
 * @author mik
 */
public class NodeArgument {

    Object value = null;

    /**
     * Static table to keep a unique instance of a given argument.
     */
    private static HashMap<Integer, NodeArgument> table = new HashMap<Integer, NodeArgument>();

    /**
     *
     * @param value
     * @return the NodeArgument with requested value, that could be retrieved from the static map or created brand new it doesn't exist yet.
     */
    public static NodeArgument getNodeArgument(Object value){
        if (!(NodeArgument.table.containsKey(value.hashCode()))){
            NodeArgument.table.put(value.hashCode(), new NodeArgument(value));
        }
        return NodeArgument.table.get(value.hashCode());
    }


    private NodeArgument(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    // change it and you'll break the static map
    private void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value.toString();
    }

    @Override
    public boolean equals(Object n){
        if (!(n instanceof NodeArgument)){
            return false;
        }
        else {
            return this.getValue().equals(((NodeArgument)n).getValue());
        }
    }

    @Override
    public int hashCode(){
        return this.value.hashCode();
    }

    public static void resetIds(){
        NodeArgument.table = new HashMap<Integer, NodeArgument>();
    }

}
