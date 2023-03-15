package VotingSystem;

import java.util.ArrayList;

public class Tree {
    protected Node root;
    protected boolean isInitialized = false;
    protected int numCandidates;


    Tree(int index, int numCandidates){
        root = new Node();
        this.numCandidates = numCandidates;
        root.index = index;
        root.numVotes = 0;
        root.curDepth = 0;
        root.parent = null;
        isInitialized = true;
        for(int i = 0; i < numCandidates; i++){
            if(i != root.index){
                root.children[i] = new Node();
                root.children[i].index = i;
                root.children[i].numVotes = 0;
                root.children[i].curDepth = 1;
            }
        }
        root.hasChildren = true;
    }
    public ArrayList<ArrayList<Integer>> getNodes(Node node){
        ArrayList<ArrayList<Integer>> nodes = new ArrayList<ArrayList<Integer>>();
        Node hold = node;
        if(node.curDepth > 0 && node.hasChildren == false){

            nodes.add(hold.ballot);
            return nodes;
        }
        else{
            for(int i = 0; i < node.children.length; i++){
                nodes.addAll(getNodes(node.children[i]));
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
                ballots[numBallots][j] = hold.get(j);
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
            i++;
            if(ballot[i] == j + 2){
                hold.hasChildren = true;
                temp = hold;
                hold = hold.children[ballot[i]];
                hold.numVotes += 1;
                hold.parent = temp;
                hold.curDepth = i + 1;
                hold.index = ballot[i];
                j++;
                break;
            }
            else if(ballot[i] == 0){
                j++;
            }
        }
        if(hold.ballot == null){
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
