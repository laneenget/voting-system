package VotingSystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.*;

public class RunElection {
    public String filename;
    private FileReader input;
    private FileWriter audit;
    private BufferedReader br;

    public void start(){
        System.out.println("Do stuff");
        boolean validFile = false;
        while (!validFile) {
            validFile = true;
            promptUser("Enter the name of the file containing the election data: ");
            Scanner sc = new Scanner(System.in);
            filename = sc.nextLine();
            try {
                input = new FileReader(filename);
            } catch (FileNotFoundException e) {
                validFile = false;
                promptUser("Error, File not found" + 
                    "\nPlease ensure the file is in the same directory as the program\n" +
                    "The exected input is filename.csv, please try again");
            }
        }
        String elecType = parseElectType();
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
        return elecType + d1 + ".txt";
    }
    public String parseElectType(){
        String line = "";
        BufferedReader br = new BufferedReader(input);
        try {
            line = br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return line;

    }
    public void runIR(){}
    public void runCPL(){}
}
