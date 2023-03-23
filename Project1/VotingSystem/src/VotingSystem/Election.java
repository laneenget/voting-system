package VotingSystem;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;


public abstract class Election {
    protected FileWriter audit;
    protected FileReader input;

    abstract public void processFile();
    public int breakTie(int numCandidates, int numToEliminate){
//        // with just 2 ppl
//        Random random = new Random();
//        int eliminated;
//        eliminated = random.nextInt(2) + 1;
//        return eliminated;
        return 1;
    }
    abstract public void conductAlgorithm();
    abstract public void printResults();
    public void writeToAudit(String[] output){
        try {
            for (String s : output) {
                audit.write(s);
                audit.write(", ");
            }
            audit.write("\n");
        } catch (Exception e) {
            System.out.println("Error writing to audit file");
        }

    }
    abstract public void parseHeader();


}
