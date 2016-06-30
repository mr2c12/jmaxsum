package factorgraph;

import exception.OutOfNodeNumberException;
import java.util.Set;

/**
 * Interface for a node of factor graph
 * @author mik
 */
public interface Node {

    public void addNeighbour(Node n);

    public Set<? extends Node> getNeighbour();

    public String stringOfNeighbour();

    @Override
    public boolean equals(Object o);

    public int getId();

    @Override
    public int hashCode();

    public Node getClone() throws OutOfNodeNumberException;

}
