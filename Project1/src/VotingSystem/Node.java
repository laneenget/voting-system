package VotingSystem;

import java.util.ArrayList;

public class Node {
    protected int numVotes;
    protected Node [] children;
    protected int curDepth;
    protected int index;
    protected Node parent;
    protected boolean hasChildren = false;
    protected ArrayList<Integer> ballot = null;

    // Testing purposes
    public Node[] getChildren() {
        return this.children;
    }
}
