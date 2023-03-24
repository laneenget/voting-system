package VotingSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Arrays;
public class IR extends Election{
    private Candidate [] candidates;
    private int numBallots;
    private BufferedReader br;
    private int curNumCandidates;

    /**
     * This is the constructor for IR.
     * @param input A FileReader that represents the election input file to process
     * @param audit A FileWriter that represents the audit file to write to
     */
    public IR(FileReader input, FileWriter audit, BufferedReader br){
        super.input = input;
        super.audit = audit;
        this.br = br;
    }

    /**
     * Eliminates a single candidate and handles the distribution of ballots from the eliminated candidate.
     * @param index an int that represents the index of the candidate to eliminate.
     */
    public void eliminateCandidate(int index){
        int i;
        Tree eliminatedTree = this.candidates[index].getBallots();
        String[] eliminatingAnnouncement = {"Commence transferring of to-be eliminated candidate " +
                this.candidates[index].getName() + " votes to other Candidates"};
        writeToAudit(eliminatingAnnouncement);

        // Make a list of eliminated candidates
        ArrayList<Integer> eliminated = new ArrayList<>();
        for (i = 0; i < this.candidates.length; i++) {
            if (this.candidates[i].isEliminated()) {
                eliminated.add(i);
            }
        }
        if (eliminatedTree.root.children != null) {
            for (i = 0; i < eliminatedTree.root.children.length; i++) {
                Node secondPlace = eliminatedTree.root.children[i];
                if (secondPlace != null) {
                    ArrayList<ArrayList<Integer>> ballots = eliminatedTree.getBallots(secondPlace);
                    if (eliminated.size() == 0) {
                        reassignVotesNoneEliminated(ballots, i);
                    }
                    else {
                        reassignVotes(eliminated, ballots);
                    }
                }
            }
        }
        String[] eliminatedAnnouncement = {"\nCandidate " + this.candidates[index].getName()
                + " has been eliminated."};
        writeToAudit(eliminatedAnnouncement);
        this.candidates[index].setEliminated(true);
        this.curNumCandidates -= 1;
    }

    /**
     * Handles reassignment of votes when no candidate has been eliminated thus far in the election.
     * @param ballots an ArrayList<ArrayList<Integer>> of ballots to insert.
     * @param index an int that represents the index of the candidate to transfer the votes to.
     */
    public void reassignVotesNoneEliminated(ArrayList<ArrayList<Integer>> ballots, int index) {
        // When there are no eliminated candidates yet, we do not need to mutate the ballot
        // Instead, the ballots may all be directly inserted into the corresponding candidate's tree
        Tree toInsert = this.candidates[index].getBallots();
        int i;
        for (i = 0; i < ballots.size(); i++) {
            ArrayList<Integer> curBallot = ballots.get(i);
            // Ensure that trailing 1's from insertion do not remain
            if (curBallot.size() > this.candidates.length) {
                curBallot.remove(curBallot.size()-1);
            }
            String ballotRepresentation = curBallot.toString();
            String[] transferAnnouncement = {"Transferring ballot " + ballotRepresentation +
                    " to candidate " + this.candidates[index].getName()};
            writeToAudit(transferAnnouncement);
            toInsert.insert(curBallot);
        }
    }

    /**
     * Handles the reassignment of votes when at least one candidate has been eliminated already.
     * @param eliminated an ArrayList<Integer> that represents the indices of the previously eliminated candidates.
     * @param ballots an ArrayList<ArrayList<Integer>> that represents all the ballots to transfer.
     */
    public void reassignVotes(ArrayList<Integer> eliminated, ArrayList<ArrayList<Integer>> ballots){
        int i;
        HashMap<Integer, Integer> ballotsMap = new HashMap<>();
        int j;
        int k;
        int curElimCount;
        // Loop over the ballots
        for (i = 0; i < ballots.size(); i++) {
            ballotsMap.clear();
            curElimCount = 0;
            ArrayList<Integer> curBallot = ballots.get(i);
            if (curBallot.size() > this.candidates.length) {
                curBallot.remove(curBallot.size()-1);
            }
            // Make a list of eliminated candidate ranks
            ArrayList<Integer> eliminatedRanks = new ArrayList<>();
            for (k = 0; k < eliminated.size(); k++) {
                if (curBallot.get(eliminated.get(k)) != 0) {
                    eliminatedRanks.add(curBallot.get(eliminated.get(k)));
                }
            }
            // Sort the list such that the eliminated ranks are sorted in increasing order
            eliminatedRanks.sort(null);
            // Fill in the ballotsMap -- Each rank between 1 and the number of candidates in the election
            // is mapped to a value that represents how many eliminated candidates precede them in the race
            for (j = 1; j < this.candidates.length + 1; j++) {
                if (curElimCount < eliminatedRanks.size() && j == eliminatedRanks.get(curElimCount)) {
                    curElimCount += 1;
                } else {
                    ballotsMap.put(j, curElimCount);
                }
            }
            // Mutate the ballot, subtracting by how many eliminated candidates there were
            // before each rank, referring to the ballotsMap to calculate the difference
            // This is to ensure that no ballots with eliminated candidates ranked are inserted
            for (j = 0; j < curBallot.size(); j++) {
                if (this.candidates[j].isEliminated() || curBallot.get(j) <= 0) {
                    curBallot.set(j, 0);
                } else {
                    curBallot.set(j, curBallot.get(j) - ballotsMap.get(curBallot.get(j)));
                }
            }
            // Insert into the Tree of the candidate that corresponds to the new number 1 ranking
            // It's possible to end up with a ballot with all 0s, so only insert if a 1 exists in ballot
            int indexToInsert = curBallot.indexOf(1);
            if (indexToInsert != -1) {
                // Write this transfer of the ballot to the audit file
                String ballotRepresentation = curBallot.toString();
                String[] transferAnnouncement = {"Transferring ballot " + ballotRepresentation +
                        " to candidate " + this.candidates[indexToInsert].getName()};
                writeToAudit(transferAnnouncement);
                Tree toInsert = this.candidates[indexToInsert].getBallots();
                toInsert.insert(curBallot);
            }
        }
    }

    /**
     * Determines the candidate with a majority.
     * @return null if no candidate has a majority, or the Candidate object that does.
     */
    public Candidate majorityCandidate(){
        int i;
        int totalVotes = 0;
        int curMaxVotes = Integer.MIN_VALUE;
        Candidate curCandidate = null;
        Candidate highestCandidate = null;
        // Get the total number of votes and the max votes a candidate has to see if there's a majority
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
        // If there is no majority, reset the returned value back to null.
        if (curMaxVotes <= totalVotes / 2) {
            highestCandidate = null;
        }
        return highestCandidate;
    }
    public void processFile(){
        String [] nextBallot;
        String ballotString;
        try {
            while ((ballotString = br.readLine()) != null) {
                nextBallot = ballotString.split(",",-1);
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
        }
        catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void printResults(){
        System.out.println("-------------------------   ELECTION RESULTS   -------------------------");
        System.out.println("Total number of ballots: " + this.numBallots);
        System.out.println("Final Vote Totals after candidate elimination and ballot transfers:");
        Arrays.sort(candidates, Comparator.comparingInt(Candidate::getNumVotes).reversed());
        for(Candidate c : candidates){
            System.out.println(c.getName() + ": " + c.getNumVotes());
        }
        Candidate winner = candidates[0];
        System.out.println("-----------------------------------------------------------------------\n\n");
        System.out.println("The winner is: " + winner.getName() + " with " + winner.getNumVotes() + " votes.");
        System.out.printf("%s received %.2f%% of the votes.\n\n", winner.getName(), (double)winner.getNumVotes() / (double)this.numBallots * 100);



    }

    /**
     * Parses the header of the input and stores election and candidate information.
     */
    public void parseHeader() {
        // Read the first 4 lines, which is the header, of the input file
        int i;
        String nextRecord;
        for (i = 0; i < 3; i++) {
            try {
                nextRecord = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // read the number of candidates
            if (i == 0) {
                int numCandidates = Integer.parseInt(nextRecord);
                this.candidates = new Candidate[numCandidates];
                this.curNumCandidates = numCandidates;
            }
            // create the candidate objects
            else if (i == 1) {
                String[] candidates = nextRecord.split(", ");
                initializeCandidates(candidates);
            }
            // set the numBallots field
            else if (i == 2) {
                this.numBallots = Integer.parseInt(nextRecord);
            }
        }
        String[] headerInformation = {"The header has been read. There are: " +
                this.curNumCandidates + " candidates at the start of the election, " +
                this.numBallots + " number of ballots in this election."};
        writeToAudit(headerInformation);
    }

    /**
     * Initializes the candidates array with initialized Candidate objects.
     * @param candidatesList a String array of candidate names and parties.
     */
    public void initializeCandidates(String[] candidatesList) {
        String[] initializationAnnouncement = {"Commence initialization of candidate information: "};
        writeToAudit(initializationAnnouncement);
        int i;
        for (i = 0; i < candidatesList.length; i++) {
            candidatesList[i] = candidatesList[i].trim();
            // Split the string by spaces, as the first word is the name and the second is the party
            String[] candidateInfo = candidatesList[i].split(" ");
            Tree newTree = new Tree(i, this.curNumCandidates);
            Candidate newCandidate = new Candidate(candidateInfo[0], candidateInfo[1], newTree);
            this.candidates[i] = newCandidate;
            String[] candidateRecord = {"Candidate " + i + " name: " + candidateInfo[0] +
                    " Party: " + candidateInfo[1]};
            writeToAudit(candidateRecord);
        }
    }

    /**
     * Conducts the IR algorithm. Determines the winner of the election, deciding if there are ties and who
     * to eliminate if necessary.
     */
    public void conductAlgorithm() {
        int smallestVotes;
        int toEliminate;
        Candidate winner;
        // Continue conducting eliminations as long as there is no majority and there are more than two candidates.
        while ((winner = majorityCandidate()) == null && this.curNumCandidates > 2) {
            // Find the index of the candidate to eliminate
            ArrayList<Integer> tiedCandidates = new ArrayList<>();
            smallestVotes = Integer.MAX_VALUE;
            for (int i = 0; i < this.candidates.length; i++) {
                if (!candidates[i].isEliminated() && candidates[i].getNumVotes() < smallestVotes) {
                    tiedCandidates.clear();
                    tiedCandidates.add(i);
                    smallestVotes = candidates[i].getNumVotes();
                } else if (!candidates[i].isEliminated() && candidates[i].getNumVotes() == smallestVotes) {
                    tiedCandidates.add(i);
                }
            }
            if (tiedCandidates.size() > 1) {
                // There is more than one candidate with the fewest number of votes
                // Write tie status to audit file
                String[] tieAnnouncement = {"There is currently a tie between candidates: "};
                writeToAudit(tieAnnouncement);
                String[] tiedCandidatesNames = new String[tiedCandidates.size()];
                for (int i = 0; i < tiedCandidates.size(); i++) {
                    tiedCandidatesNames[i] = this.candidates[tiedCandidates.get(i)].getName() + " ";
                }
                writeToAudit(tiedCandidatesNames);
                toEliminate = tiedCandidates.get(breakTie(tiedCandidates.size(), 1) - 1);
                String[] tieBreakResult = {"Result of tie break: " +
                        this.candidates[toEliminate].getName()};
                writeToAudit(tieBreakResult);
            } else {
                // There is only one candidate with the fewest number of votes
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
        String winnerAnnouncement = "Candidate " + winner.getName() + " has won with " +
                winner.getNumVotes() + " votes.";
        String[] announcement = {winnerAnnouncement};
        writeToAudit(announcement);
        try {
            if (this.audit != null) {
                audit.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // TODO: change file permissions for audit file
    }

    // testing purposes
    public Candidate[] getCandidates() {return this.candidates;}
    public void setCandidates(Candidate[] candidates) {this.candidates = candidates;}

    public int getCurNumCandidates() {return this.curNumCandidates;}

    public int getNumBallots() {return this.numBallots;}
}
