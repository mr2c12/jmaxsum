package factorgraph;

import java.util.HashSet;

/**
 * Interface for a node of factor graph
 * @author mik
 */
public interface Node {

    public void addNeighbour(Node n);

    public HashSet getNeighbour();

    public String stringOfNeighbour();

    @Override
    public boolean equals(Object o);

    public int getId();

    @Override
    public int hashCode();


}
