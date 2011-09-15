package factorgraph;

import java.util.HashMap;

/**
 * A possible value of a NodeVariable
 * @author mik
 */
public class NodeArgument {

    Object value = null;

    private static HashMap<Integer, NodeArgument> table = new HashMap<Integer, NodeArgument>();

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

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString(){
        return this.value.toString();
    }

    public boolean equals(NodeArgument n){
        return this.getValue().equals(n.getValue());
    }

    public int hashCode(){
        return this.value.hashCode();
    }

}
