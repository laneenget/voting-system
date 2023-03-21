package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

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
        Tree eliminatedTree = this.candidates[index].getBallots();
        for (i = 0; i < eliminatedTree.root.children.length; i++) {
                Node secondPlace = eliminatedTree.root.children[i];
                if (secondPlace != null) {
                    ArrayList<ArrayList<Integer>> ballots = eliminatedTree.getBallots(secondPlace);
                    reassignVotes(ballots, i);
                }
            }
        this.candidates[index].setEliminated(true);
        this.curNumCandidates -= 1;
    }
    public void reassignVotes(ArrayList<ArrayList<Integer>> ballots, int index){
        // First, make a list of the candidates eliminated so far
        ArrayList<Integer> eliminated = new ArrayList<>();
        int i;
        for (i = 0; i < this.candidates.length; i++) {
            if (this.candidates[i].isEliminated()) {
                eliminated.add(i);
            }
        }
        // Check if one other candidate (not including the candidate
        // that is currently being eliminated) has already been eliminated
        // If not, then simply insert each ballot into the corresponding Tree
        if (eliminated.size() == 0) {
            Tree toInsert = this.candidates[index].getBallots();
            for (i = 0; i < ballots.size(); i++) {
                toInsert.insert(ballots.get(i));
            }
        }
        else {
            HashMap<Integer, Integer> ballotsMap = new HashMap<>();
            int j;
            int k;
            int curElimCount;
            // Loop over the ballots
            for (i = 0; i < ballots.size(); i++) {
                ballotsMap.clear();
                curElimCount = 0;
                ArrayList<Integer> curBallot = ballots.get(i);
                // Make a list of eliminated candidate ranks
                ArrayList<Integer> eliminatedRanks = new ArrayList<>();
                for (k = 0; k < eliminated.size(); k++) {
                    if (curBallot.get(eliminated.get(k)) == 0) {
                        eliminatedRanks.add(curBallot.get(eliminated.get(k)));
                    }
                }
                // Sort the list
                eliminatedRanks.sort(null);
                // Fill in the ballotsMap
                for (j = 1; j < this.candidates.length + 1; j++) {
                    if (j == eliminatedRanks.get(curElimCount)) {
                        curElimCount += 1;
                    }
                    else {
                        ballotsMap.put(j, curElimCount);
                    }
                }
                // Mutate the ballot, subtracting by how many eliminated
                // candidates there were before each rank
                for (j = 0; j < curBallot.size(); j++) {
                    if (this.candidates[j].isEliminated()) {
                        curBallot.set(j, 0);
                    } else {
                        curBallot.set(j, curBallot.get(j) - ballotsMap.get(curBallot.get(j)));
                    }
                }
                int indexToInsert = curBallot.indexOf(1);
                Tree toInsert = this.candidates[indexToInsert].getBallots();
                toInsert.insert(curBallot);
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
        if (curMaxVotes <= totalVotes / 2) {
            highestCandidate = null;
        }
        return highestCandidate;
    }
    public void processFile(){

        CSVReader csvReader = new CSVReader(input);
        String [] nextBallot;

         try {
            while ((nextBallot = csvReader.readNext()) != null) {
                writeToAudit(nextBallot);
                ArrayList<Integer> ballot = new ArrayList<Integer>();
                int specialIndex =-1;
                for(int i = 0; i < nextBallot.length; i++){
                    int curRank = 0;
                    if(!nextBallot[i].equals("")){
                        curRank = Integer.parseInt(nextBallot[i]);
                        if(curRank == 1){
                            specialIndex = i;
                        }
                    }
                    ballot.add(curRank);
                }
                Tree candidateTree = this.candidates[specialIndex].getBallots();
                candidateTree.insert(ballot);
            }
        } catch (CsvValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void printResults(){
        System.out.println("-------------------------   ELECTION RESULTS   -------------------------");
        ArrayList<Candidate> candidatesArrayList = new ArrayList<Candidate>();
        for(int i = 0; i < this.candidates.length; i++){
            candidatesArrayList.add(this.candidates[i]);
        }
        candidatesArrayList.sort(null);
        for(int i = 0; i < candidatesArrayList.size(); i++){
            System.out.println(candidatesArrayList.get(i).getName() + ": " + candidatesArrayList.get(i).getNumVotes());
        }
        Candidate winner = candidatesArrayList.get(0);
        System.out.println("-----------------------------------------------------------------------\n\n");
        System.out.println("The winner is: " + winner.getName() + " with " + winner.getNumVotes() + " votes."); 
        System.out.printf(winner.getName() + " received " + 
        "%.2f" + "% of the votes.\n\n", (double)winner.getNumVotes() / (double)this.numBallots * 100);


    }
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
            // read the number of candidates
            if (i == 1) {
                int numCandidates = Integer.parseInt(nextRecord);
                this.candidates = new Candidate[numCandidates];
                this.curNumCandidates = numCandidates ;
            }
            // create the candidate objects
            else if (i == 2) {
                initializeCandidates(nextRecord.split(", "));
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
    public void conductAlgorithm() {
        this.parseHeader();
        this.processFile();
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
                    tiedCandidates.clear();
                    tiedCandidates.add(i);
                } else if (candidates[i].getNumVotes() == smallestVotes) {
                    tiedCandidates.add(i);
                }
            }
            if (tiedCandidates.size() > 1) {
                toEliminate = breakTie(tiedCandidates.size() - 1);
            } else {
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
                } else if (!finalist.isEliminated() && (finalist.getNumVotes() > first_total)) {
                    winner = finalist;
                }
            }
        }
        // A winner has been chosen.
        String winnerAnnouncement = "Candidate" + winner.getName() + " has won with " +
                winner.getNumVotes() + " votes.";
        String[] announcement = {winnerAnnouncement};
        writeToAudit(announcement);
        printResults();
        // TODO: change file permissions for audit file
    }

    // testing purposes
    public Candidate[] getCandidates() {return this.candidates;}
    public void setCandidates(Candidate[] candidates) {this.candidates = candidates;}

    public int getCurNumCandidates() {return this.curNumCandidates;}

    public int getNumBallots() {return this.numBallots;}
}
