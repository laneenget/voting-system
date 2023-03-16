package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    public void eliminateCandidate(int index){
        int i;
        for (i = 0; i < this.candidates.length; i++) {
            if (!this.candidates[i].isEliminated()) {
                Tree secondPlace = this.candidates[i].getBallots();
                Tree eliminatedTree = this.candidates[index].getBallots();
                ArrayList<ArrayList<Integer>> ballots = eliminatedTree.getBallots(secondPlace.root);
                reassignVotes(ballots, i);
            }
        }
        this.candidates[index].setEliminated(true);
        this.curNumCandidates -= 1;
    }
    public void reassignVotes(ArrayList<ArrayList<Integer>> ballots, int index){
        // This method will return the cleaned up, ready-to-insert ballots
        // From the eliminated candidates tree for the candidate associated
        // with index

        // (temp notes)
        // Ballot from to-eliminate Candidate A Tree
        // To insert into Candidate C Tree (As C is ranked number 2)
        // [A, B, C, D]     <-- Nodes that are represented
        // [1, 3, 2, 4]     <-- Current Rankings (the actual ballot)
        // A is eliminated
        // B was already eliminated in the past
        // See that B was eliminated before and decrement by one again --
        // Only decrement for all nodes that had a lower ranking (so higher number)
        // than the eliminated B
        // In this case, only D is decremented, not C

        // [null, null, C, D]   <--- what the children node array would look like
        // [----, ----, 1, 2]

        int j;
        int check;
        int i;

        // This Implementation should work, insert can handle
        // empty values in a ballot, and the new ballots will already
        // contain the correct rankings
        //for(i = 0; i < ballots.size(); i ++){
        //    Tree candidateTree = this.candidates[index].getBallots();
        //    candidateTree.insert(ballots.get(i));
        //
        // There's no avoiding this ugliness (crying)
        for (i = 0; i < ballots.size(); i++) {
            for (j = 1; j < ballots.get(i).size(); j++) {
                if (this.candidates[j].isEliminated()) {
                    for (check = 1; check < ballots.get(i).size(); check++) {
                        if (ballots.get(i).get(check) > ballots.get(i).get(j)) {
                            int curRank = ballots.get(i).get(check);
                            ballots.get(i).set(check, curRank - 1);
                        }
                    }
                    ballots.get(i).set(j, 0);
                    this.candidates[index].getBallots().insert(ballots.get(i));
                }
            }
        }
    }
    // It's a bit easier for isMajority to return the candidate that is majority
    // or null if no majority for audit file writing purposes (now called majorityCandidate)
    public Candidate majorityCandidate(){
        int i;
        int totalVotes = 0;
        int curMaxVotes = Integer.MIN_VALUE;
        Candidate curCandidate = null;
        Candidate highestCandidate = null;
        for (i = 0; i < this.candidates.length; i++) {
            curCandidate = candidates[i];
            if (!curCandidate.isEliminated()) {
                totalVotes += curCandidate.getNumVotes();
            }
            if (curCandidate.getNumVotes() > curMaxVotes) {
                curMaxVotes = curCandidate.getNumVotes();
                highestCandidate = curCandidate;
            }
        }
        if (curMaxVotes > totalVotes / 2) {
            highestCandidate = curCandidate;
        }
        return highestCandidate;
    }
    public void processFile(){}
    public void printResults(){}
    public void parseHeader() {
        BufferedReader br = new BufferedReader(input);
        // Read the first 4 lines, which is the header, of the input file
        int i;
        String nextRecord = "";
        for (i = 0; i < 4; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // this is rlly uglee right now im sorry
            // read the number of candidates
            if (i == 1) {
                int numCandidates = Integer.parseInt(nextRecord);
                this.candidates = new Candidate[numCandidates];
                this.curNumCandidates = numCandidates ;
            }
            // create the candidate objects
            else if (i == 2) {
                initializeCandidates(nextRecord.split(","));
            }
            else if (i == 3) {
                this.numBallots = Integer.parseInt(nextRecord);
            }
        }
    }
    public void initializeCandidates(String[] candidatesList) {
        int i;
        for (i = 0; i < candidatesList.length; i++) {
            String[] candidateInfo = candidatesList[i].split(" ");
            Candidate newCandidate = new Candidate(candidateInfo[0], candidateInfo[1], null);
            this.candidates[i] = newCandidate;
        }
    }
    public void conductAlgorithm(){
        this.parseHeader();
        int smallestVotes;
        int toEliminate;
        Candidate winner;
        while ((winner = majorityCandidate()) == null && this.curNumCandidates > 2) {
            // (Finding the candidate to eliminate could be its own method if we wanted to)
            // Find the index of the candidate to eliminate
            ArrayList<Integer> tiedCandidates = new ArrayList<>();
            smallestVotes = Integer.MAX_VALUE;
            for (int i = 0; i < this.candidates.length; i++) {
                if (candidates[i].getNumVotes() < smallestVotes) {
                    tiedCandidates.add(i);
                }
            }
            if (tiedCandidates.size() > 1) {
                toEliminate = breakTie(tiedCandidates.size() - 1);
            }
            else {
                toEliminate = tiedCandidates.get(0);
            }
            eliminateCandidate(toEliminate);
        }
        // There are 2 candidates left. Choose the winner by popularity.
        if (winner == null) {
            int first_total = -1;
            for (Candidate finalist : this.candidates) {
                if (!finalist.isEliminated()) {
                    first_total = finalist.getNumVotes();
                    winner = finalist;
                }
                else if (!finalist.isEliminated() && (finalist.getNumVotes() > first_total)) {
                    winner = finalist;
                }
            }
        }
        // A winner has been chosen.
        // format properly later (?)
        String winnerAnnouncement = "Candidate" + winner.getName() + " has won with " +
                winner.getNumVotes() + " votes.";
        writeToAudit(winnerAnnouncement);
        printResults();
        // TODO: change file permissions for audit file
    }
}
