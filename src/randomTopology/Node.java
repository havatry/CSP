package randomTopology;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Joshua A. Campbell
 *
 * Represents a node in a graph.
 * 
 * OverView:
 * 		该类在github上复制来的，主要是给本包中的Grap类做辅助的
 */
public class Node
{
    private int identifier;
    // Holds the Identifiers of this nodes neighbors.
    private Set<Integer> neighbors;

    /**
     * Constructes a node with the given identifier.
     * Identifiers should be unique.
     */
    public Node(int identifier)
    {
        this.identifier = identifier;
        neighbors = new LinkedHashSet<Integer>();
    }

    /**
     * Adds the given identifier to the list of neighbors.
     * There is some auto-boxing that goes on here (int to Integer), but 
     * I thought that this should be too bad for sparse graphs.
     */
    public void addEdgeTo(int neighbor)
    {
        neighbors.add(neighbor);
    }

    /**
     * Removes the given identifier from the list of neighbors.
     * There is some auto-boxing that goes on here..
     */
    public void removeEdgeTo(Integer neighbor)
    {
        neighbors.remove(neighbor);
    }

    /**
     * Returns the identifier wrapped in an Integer.
     */
    public Integer getIdentifier()
    {
        return new Integer(this.identifier);
    }

    /**
     * Returns the list of neighbors.
     */
    public Set<Integer> getNeighbors()
    {
        return neighbors;
    }

}
