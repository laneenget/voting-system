package VotingSystem;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.security.SecureRandom;


public abstract class Election {
    protected FileWriter audit;
    protected FileReader input;

    abstract public void processFile();
    /**
     * This method is used to break ties in the election. It takes in an arraylist of candidates
     * and the number of candidates to eliminate. It then randomly selects candidates to eliminate
     * @param candidates ArrayList of candidates to break the tie
     * @param numToEliminate number of candidates to eliminate
     * @return int[] of the candidates to eliminate
     */
    public int [] breakTie(ArrayList<Integer> candidates, int numToEliminate){
        if(candidates.size() <= numToEliminate){
            return candidates.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        }
        int[] eliminated = new int[numToEliminate];
        int n = numToEliminate;
        ArrayList<Integer> c = new ArrayList<Integer>(candidates);
        while(n > 0){
            SecureRandom rand = new SecureRandom();
            int index = rand.nextInt(c.size());
            eliminated[n - 1] = c.get(index);
            c.remove(index);
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
