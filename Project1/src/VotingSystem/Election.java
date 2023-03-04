package VotingSystem;

import java.io.FileReader;
import java.io.FileWriter;

public abstract class Election {
    private FileWriter audit;
    private FileReader input;

    public void processFile(){}
    public int breakTie(int numCandidates){return -1;}
    public void conductAlgorithm(){}
    public void printResults(){}
    public void writeToAudit(String output){}
    public void parseHeader(){}


}
