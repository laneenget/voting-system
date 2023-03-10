package VotingSystem;

import java.util.ArrayList;

public class Tree {
    protected Node root;
    protected boolean isInitialized = false;

    public void initializeTree(){
        root.numVotes = 0;
        root.curDepth = 0;
        isInitialized = true;
    }
    public void addNode(Node root, Node newNode){
        root.children[newNode.index] = newNode;
    }
    public void deleteNode(Node root, Node node){
        root.children[node.index] = null;
    }
    public void insert(int [] ballot, int curDepth){
        if(!isInitialized){
            initializeTree();
        }
        root.numVotes += 1;
        int i;
        int seek = curDepth + 2;
        for(i= 0; i <ballot.length; i++){
            if(ballot[i] == seek){
                Node node = new Node();
                node.index = i;
                node.curDepth = curDepth +1;
                Tree tree = new Tree();
                tree.root = node;
                root.children[i] = node;
                tree.insert(ballot, curDepth + 1);
            }
        }
    }
    public void transferVotes(int index, Node root){

    }
    public ArrayList<Node> getPreorder(int index, int curDepth){
        ArrayList<Node> preOrder = new ArrayList<Node>();
        Node hold = root;
        if(root.index == index && curDepth == 0){
            preOrder.add(root);
            return preOrder;
        }
        else {
            for(int i = 0; i < curDepth; i++)
                if((hold = hold.children[index]) == null){
                    ArrayList<Node> empty = new ArrayList<Node>();
                    return empty;
            };
        }
        while(hold != null){
            preOrder.add(hold);
            for(int i = 0; i<hold.children.length; i++){
                preOrder.addAll(getPreorder(index, curDepth + 1));
            }
        }
        return preOrder;
    }

    public ArrayList<Node> getNodes(int index){
        ArrayList<Node> nodes = getPreorder(index, 0);
        return nodes;
    }


}
