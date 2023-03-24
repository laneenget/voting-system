package VotingSystem;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.SecureRandom;


public abstract class Election {
    protected FileWriter audit;
    protected FileReader input;

    abstract public void processFile();
    public int [] breakTie(ArrayList<Integer> candidates, int numToEliminate){
        if(candidates.size() <= numToEliminate){
            Throw(new Exception("Error, not enough candidates to eliminate"));
            return candidates;
        }
        int[] eliminated = new int[numToEliminate];
        int n = numToEliminate;
        while(n > 0){
            SecureRandom rand = new SecureRandom();
            int index = rand.nextInt(candidates.size());
            eliminated[n - 1] = candidates.get(index);
            candidates.remove(index);
            n--;
        }
        return eliminated;
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
