package VotingSystem;

import java.util.ArrayList;

public class Tree {
    protected Node root;
    public void addNode(Node root, Node newNode){

    }
    public void deleteNode(Node root, Node node){}
    public void insert(int [] ballot){

    }
    public void transferVotes(int index, Node root){

    }
    public ArrayList<Node> getPreorder(Node root){
        ArrayList<Node> preOrder = new ArrayList<Node>();
        preOrder.add(root);
        return preOrder;
    }

    public ArrayList<Node> getNodes(int index){
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node node = root;
        while(root != null){
            nodes.add(root);
            node = root.children[index];
        }
        return nodes;
    }


}
