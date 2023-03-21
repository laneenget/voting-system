package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;

public class RunElection {
    public String filename;
    private FileReader input;
    private FileWriter audit;
    private BufferedReader br;

    public void start(){
        System.out.println("Do stuff");
        Tree tree = new Tree(3, 5);
        ArrayList<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        tree.insert(test);
        ArrayList<Integer> testbal = new ArrayList<Integer>();
        testbal.add(3);
        testbal.add(1);
        testbal.add(2);
        testbal.add(5);
        testbal.add(4);
        tree.insert(testbal);
        tree.insert(test);
        ArrayList<ArrayList<Integer>> ballots = tree.getBallots(tree.root);
        ArrayList<ArrayList<Integer>> ballots2 = tree.getBallots(tree.root);
        ArrayList<ArrayList<Integer>> ballots3 = tree.getBallots(tree.root);
        for(int i = 0; i < ballots.size(); i++){
            System.out.println(ballots.get(i));
        }
        System.out.println("After1)");
        for(int i = 0; i < ballots2.size(); i++){
            System.out.println(ballots2.get(i));
        }
        System.out.println("After2)");
        for(int i = 0; i < ballots3.size(); i++){
            System.out.println(ballots3.get(i));
        }
        System.out.println("After3)");


}
    public RunElection(String filename){
        this.filename = filename;
    }
    public void promptUser(String prompt){
        System.out.println(prompt);
    }
    public String generateAuditFileName(String elecType){
        Date d1 = new Date();
        return elecType + d1 + ".txt";
    }
    public String parseElectType(){
        String line = "";
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;

    }
    public void runIR(){}
    public void runCPL(){}
}
