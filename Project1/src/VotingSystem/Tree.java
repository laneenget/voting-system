package VotingSystem;

import java.util.ArrayList;

public class Tree {
    protected Node root;
    protected boolean isInitialized;
    protected int numCandidates;


    Tree(int index, int numCandidates){
        root = new Node();
        this.numCandidates = numCandidates;
        root.index = index;
        root.numVotes = 0;
        root.curDepth = 0;
        root.parent = null;
        isInitialized = true;
        root.hasChildren = false;
    }
    public Node initalizeNode(){
        Node node = new Node();
        node.curDepth = 0;
        node.index = 0;
        return node;

    }
    public ArrayList<ArrayList<Integer>> getNodes(Node node){
        ArrayList<ArrayList<Integer>> nodes = new ArrayList<ArrayList<Integer>>();
        Node hold = node;
        if(hold.curDepth > 0 && hold.hasChildren == false){

            nodes.add(hold.ballot);
            return nodes;
        }
        else{
            for(int i = 0; i < hold.children.length; i++){
                if(hold.children[i] != null){
                    nodes.addAll(getNodes(hold.children[i]));
                }
            }
            if(hold.curDepth > 0){
                nodes.add(hold.ballot);
            }
            return nodes;
        }
    }
    public int[][] getBallots(Node node){
        ArrayList<ArrayList<Integer>> nodes = getNodes(node);
        int[][] ballots = new int[node.numVotes][numCandidates];
        int numBallots = 0;
        for(int i = 0; i < nodes.size(); i++){
            ArrayList<Integer> hold = nodes.get(i);
            int count = hold.get(hold.size() - 1);
            for(int j = 0; j < count; j++){
                for(int k = 0; k < hold.size() - 1; k++){
                    ballots[numBallots][k] = hold.get(k);
                }
                numBallots++;
            }
        }
        return ballots;


    }
    public void insert(int [] ballot){
        Node hold = root;
        Node temp;
        hold.numVotes += 1;
        int i = 0;
        int j = 0;
        while (j < ballot.length - 1){
            if(ballot[i] == j + 2){
                i=0;
                hold.hasChildren = true;
                temp = hold;
                hold.children = new Node[numCandidates];
                hold.children[ballot[i]] = initalizeNode();
                hold = hold.children[ballot[i]];
                hold.numVotes += 1;
                hold.parent = temp;
                hold.curDepth = j + 1;
                hold.index = ballot[i];
                j++;
                break;
            }
            else if(ballot[i] == 0){
                j++;
            }
            i++;
        }
        if(hold.ballot == null){
            hold.ballot = new ArrayList<Integer>();
            for(int k = 0; k < ballot.length; k++){
                hold.ballot.add(ballot[k]);
            }
            hold.ballot.add(1);
        }
        else {
            int x = hold.ballot.get(hold.ballot.size() - 1);
            x += 1;
            hold.ballot.set(hold.ballot.size() - 1, x);
        }


    }


}
