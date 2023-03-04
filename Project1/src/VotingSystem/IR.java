package VotingSystem;

import java.io.FileReader;
import java.io.FileWriter;

public class IR extends Election{
    private Candidate [] candidates;
    private int numBallots;
    private FileWriter audit;
    private FileReader input;
    private int curNumCandidates;

    public IR(FileReader input, FileWriter audit){
        this.input = input;
        this.audit = audit;
    }
    public void eliminateCandidate(int index){}
    public void reassignVotes(Candidate candidate){}
    public boolean isMajority(){return false;}
    public void processFile(){}
    public void printResults(){}
    public void parseHeader(){}
    public void conductAlgorithm(){}
}
