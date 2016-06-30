package operation;

import factorgraph.NodeFunction;
import factorgraph.NodeVariable;
import messages.PostService;

/**
 * This interface force to implement all the needed method for computing all possible MaxSum variants.
 * @author Michele Roncalli < roncallim at gmail dot com >
 */
public interface Operator {

    public boolean updateQ(NodeVariable x, NodeFunction f, PostService ps);

    public boolean updateR(NodeFunction f, NodeVariable x, PostService ps);

    public void updateZ(NodeVariable x, PostService ps);

    public int argOfInterestOfZ(NodeVariable x, PostService ps);

}
