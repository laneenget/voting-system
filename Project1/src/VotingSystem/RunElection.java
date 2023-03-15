package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class RunElection {
    private String filename;
    private FileReader input;
    private FileWriter audit;
    private BufferedReader br;

    public void start(){
        System.out.println("Do stuff");
        Tree tree = new Tree(3, 5);
        int[] testbal = {3,1,5,2,4};
        tree.insert(testbal);
        int[] testbal2 = {3,1,2,5,4};
        tree.insert(testbal2);
        tree.insert(testbal);
        System.out.println(tree.root.numVotes);
        int[][] ballots = tree.getBallots(tree.root);
        for(int i = 0; i < ballots.length; i++){
            for(int j = 0; j < ballots[i].length; j++){
                System.out.print(ballots[i][j] + " ");
            }
            System.out.println();
        }

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
