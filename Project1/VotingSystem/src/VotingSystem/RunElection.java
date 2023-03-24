package VotingSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import java.util.ArrayList;
import java.util.*;

public class RunElection {
    public String filename;
    private FileReader input;
    private FileWriter audit;
    private BufferedReader br;

    public void start(){
        boolean validFile = false;
        while (!validFile) {
            validFile = true;
            if(filename == null){
                promptUser("Enter the name of the file containing the election data: ");
                Scanner sc = new Scanner(System.in);

                filename = sc.nextLine();
            }
            try {
                URL path = RunElection.class.getResource(filename);
                File f = new File(path.getFile());
                input = new FileReader(f);
            } catch (Exception e) {
                validFile = false;
                promptUser("Error, File not found" + 
                    "\nPlease ensure the file is in the same directory as the program\n" +
                    "The expected input is filename.csv, please try again");
                filename = null;
            }
        }
        String elecType = parseElectType();
        String auditFileName = generateAuditFileName(elecType);
        try {
            String path = System.getProperty("user.dir");
            File f = new File(path, auditFileName);
            audit = new FileWriter(f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(elecType.equals("IR")){
            runIR();
        }
        else if(elecType.equals("CPL")){
            runCPL();
        }
        else{
            promptUser("FILE READ ERROR");
            return;
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
        String d2 = "dummy";
        return elecType + d2 + ".txt";
    }
    public String parseElectType(){
        String line = "";
        this.br = new BufferedReader(input);
        try {
            line = br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return line;

    }
    public void runIR(){
        IR Election = new IR(input, audit, br);
        Election.parseHeader();
        Election.processFile();
        Election.conductAlgorithm();
        Election.printResults();
    }
    public void runCPL(){}
}
