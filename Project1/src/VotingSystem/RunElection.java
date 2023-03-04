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

    public void start(){System.out.println("Do stuff");}
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
