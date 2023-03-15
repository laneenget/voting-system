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
            for(int i = 0; i < numCandidates; i++){
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
    public ArrayList<ArrayList<Integer>> getBallots(Node node){
        ArrayList<ArrayList<Integer>> nodes = getNodes(node);
        ArrayList<ArrayList<Integer>> returnBals = new ArrayList<ArrayList<Integer>>();
        int numBallots = 0;
        for(int i = 0; i < nodes.size(); i++){
            System.out.println(nodes.size());
            ArrayList<Integer> hold = nodes.get(i);
            int count = hold.get(hold.size() - 1);
            for(int j = 0; j < count; j++){
                for(int k = 0; k < hold.size() - 1; k++){
                    returnBals.get(numBallots).add(hold.get(k));
                    System.out.println("test");
                }
                numBallots++;
            }
        }
        return returnBals;


    }
    public void insert(ArrayList<Integer> ballot){
        Node hold = root;
        Node temp;
        hold.numVotes += 1;
        int index = 0;
        int rank = 2;
        for(int i = 0; i < ballot.size(); i++){
            index = ballot.indexOf(rank);
            if(index == -1){
                break;
            }
            else{
                hold.hasChildren = true;
                hold.children = new Node[numCandidates];
                temp = hold;
                hold.children[index] = initalizeNode();
                hold = hold.children[index];
                rank++;   
            }

        }
        if(hold.ballot == null){
            hold.ballot = ballot;

            hold.ballot.add(1);
        }
        else {
            int x = hold.ballot.get(hold.ballot.size() - 1);
            x += 1;
            hold.ballot.set(hold.ballot.size() - 1, x);
        }


    }


}
