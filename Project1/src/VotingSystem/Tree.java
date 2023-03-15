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
        }
        else{
            for(int i = 0; i < hold.children.length; i++){
                if(hold.children[i] != null){
                    System.out.println("39");
                    nodes.addAll(getNodes(hold.children[i]));
                }
            }
            if(hold.curDepth > 0){
                nodes.add(hold.ballot);
            }
        }
        System.out.println(nodes.size());
        return nodes;
    }
    public int[][] getBallots(Node node){
        ArrayList<ArrayList<Integer>> nodes = getNodes(node);
        int[][] ballots = new int[node.numVotes][numCandidates];
        int numBallots = 0;
        for(int i = 0; i < nodes.size(); i++){
            System.out.println(nodes.size());
            ArrayList<Integer> hold = nodes.get(i);
            int count = hold.get(hold.size() - 1);
            for(int j = 0; j < count; j++){
                for(int k = 0; k < hold.size() - 1; k++){
                    ballots[numBallots][k] = hold.get(k);
                    System.out.println("test");
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
        int curr = 0;
        while (i < ballot.length){
            if(j == ballot.length){
                break;
            }
            if(ballot[i] == curr + 2){
                j++;
                i=0;
                hold.hasChildren = true;
                temp = hold;
                hold.children = new Node[numCandidates];
                hold.children[ballot[i]] = initalizeNode();
                hold = hold.children[ballot[i]];
                hold.numVotes += 1;
                hold.parent = temp;
                hold.curDepth = curr + 1;
                hold.index = ballot[i];
                curr++;
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
