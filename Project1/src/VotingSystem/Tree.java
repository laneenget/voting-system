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
        ArrayList<Integer> ballot = new ArrayList<Integer>();
        Node hold = node;
        System.out.println(hold.hasChildren);
        if(hold.curDepth > 0 && hold.hasChildren == false){
            ballot = hold.ballot;
            nodes.add(ballot);
        }
        else{
            for(int i = 0; i < numCandidates; i++){
                if(hold.children[i] != null){
                    nodes.addAll(getNodes(hold.children[i]));
                }
            }
            if(hold.curDepth > 0 && hold.ballot != null){
                ballot = hold.ballot;
                nodes.add(ballot);
            }
        }
        return nodes;
    }
    public ArrayList<ArrayList<Integer>> getBallots(Node node){
        ArrayList<ArrayList<Integer>> nodes = getNodes(node);
        ArrayList<ArrayList<Integer>> returnBals = new ArrayList<ArrayList<Integer>>();
        int numBallots = 0;
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int size = nodes.size();
        System.out.println(size + " is size");
        for(int i = 0; i < nodes.size(); i++){
            ArrayList<Integer> hold = nodes.get(i);
            System.out.println(hold.size());
            int size2 = hold.size();
            int count = hold.get(size2 - 1);
            hold.remove(size2 - 1);
            temp.clear();
            for(int k = 0; k < size2 - 1; k++){
                temp.add(hold.get(k));
            }
            for(int j = 0; j < count; j++){
                System.out.println(temp.size());
                System.out.println("73");
                returnBals.add(temp);
                numBallots++;
            }
        
        }
        System.out.println(numBallots + " is numBallots");
        return returnBals;


    }
    public void insert(ArrayList<Integer> ballot){
        Node hold = root;
        hold.curDepth = 0;
        hold.numVotes += 1;
        int index = 0;
        int rank = 2;
        int size = ballot.size();
        for(int i = 0; i < size - 1; i++){
            index = ballot.indexOf(rank);
            if(index == -1){
                break;
            }
            else{
                if(hold.hasChildren == false){
                    hold.children = new Node[numCandidates];
                    hold.hasChildren = true;
                    System.out.println("89");
                    System.out.println("New node " + index + " has Children");
                }
                if(hold.children[index] == null){
                    hold.children[index] = initalizeNode();
                }
                hold = hold.children[index];
                hold.curDepth = i + 1;
                rank++;   
            }

        }

        if(hold.ballot == null){
            hold.ballot = new ArrayList<Integer>();
            hold.ballot = ballot;

            hold.ballot.add(1);
            System.out.println(hold.ballot.size());
            System.out.println("100");
        }
        else {
            int x = hold.ballot.get(hold.ballot.size() - 1);
            x += 1;
            System.out.println(hold.ballot.size());
            System.out.println("104");
            hold.ballot.set(hold.ballot.size() - 1, x);
        }


    }
}
