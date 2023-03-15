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
    public ArrayList<Integer> getPath(Node node){
        Node hold = node;
        ArrayList<Integer> path = new ArrayList<Integer>();
        while(hold.parent != null){
            path.add(node.index);
            if(hold.parent != null){
                hold = node.parent;
            }
            else {
                return path;
            }
        }
        return path;
    }
    public ArrayList<ArrayList<ArrayList<Integer>>> getNodes(Node node){
        ArrayList<ArrayList<ArrayList<Integer>>> nodes = new ArrayList<ArrayList<ArrayList<Integer>>>();
        if(node.curDepth > 0 && node.hasChildren == false){
            ArrayList<ArrayList<Integer>> hold = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> hold2 = new ArrayList<Integer>();
            hold2.add(node.curDepth);
            hold.add(getPath(node));
            hold2.add(node.numVotes);
            hold.add(hold2);
            nodes.add(hold);
            return nodes;
        }
        else{
            for(int i = 0; i < node.children.length; i++){
                node.numVotes -= node.children[i].numVotes;
                nodes.addAll(getNodes(node.children[i]));
            }
            ArrayList<ArrayList<Integer>> hold = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> hold2 = new ArrayList<Integer>();
            if(node.curDepth > 0){
                hold2.add(node.curDepth);
                hold.add(getPath(node));
                hold2.add(node.numVotes);
                hold.add(hold2);
                nodes.add(hold);
            }
            return nodes;
        }
    }
    public int[][] getBallots(Node node){
        ArrayList<ArrayList<ArrayList<Integer>>> nodes = getNodes(node);
        int[][] ballots = new int[node.numVotes][numCandidates];
        int numBallots = 0;
        for(int i = 0; i < nodes.size(); i++){
            ArrayList<ArrayList<Integer>> hold = nodes.get(i);
            int count = hold.get(1).get(1);
            for(int j = 0; j < count; j++){
                for(int k = 0; k < hold.get(0).size(); k++){
                    ballots[numBallots][hold.get(0).get(k)] = hold.get(1).get(0);
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

    }


}
