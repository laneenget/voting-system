package Testing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import VotingSystem.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.fail;

public class IRtests {
    RunElection runElection;
    IR ir;
    String[] candidateNames = {"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};
    IR irInitialize;
    IR irMajority1;
    IR irMajority2;
    IR irMajority3;
    IR irReassignNoneEliminated1;
    IR irReassign1;
    IR irReassign2;
    IR irEliminate1;
    IR irEliminate2;
    IR irConductAlgorithm1;
    IR irConductAlgorithm2;
    IR irConductAlgorithm3;
    IR irConductAlgorithm4;
    IR irConductAlgorithm5;
    IR irConductAlgorithm6;

    @Before
    public void setUp() throws IOException {
        runElection = new RunElection("test.txt");

        FileReader input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRHeader1.csv");
        ir = new IR(input, null);
        ir.parseHeader();

        irInitialize = new IR(input, null);
        irInitialize.setCandidates(new Candidate[4]);
        irInitialize.initializeCandidates(candidateNames);

        FileReader majority1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRMajority1.csv");
        // Candidate A ballots : [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots:  [2, 1]  [0, 1]
        irMajority1 = new IR(majority1Input, null);
        irMajority1.parseHeader();
        irMajority1.processFile();

        FileReader majority2Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRMajority2.csv");
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        irMajority2 = new IR(majority2Input, null);
        irMajority2.parseHeader();
        irMajority2.processFile();

        FileReader majority3Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRMajority3.csv");
        // Inserted ballots for Candidate A: [1, 0, 0]  [1, 2, 3]
        // Inserted ballots for Candidate B: [2, 1, 3]  [3, 1, 2]
        // Inserted ballots for Candidate C: [3, 2, 1]  [2, 3, 1]   [0, 0, 1]   [2, 0, 1]
        irMajority3 = new IR(majority3Input, null);
        irMajority3.parseHeader();
        irMajority3.processFile();

        FileReader reassignNoneEliminated1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRReassignNoneEliminated1.csv");
        // Candidate A ballots: [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots: [2, 1]  [0, 1]
        FileWriter reassignNoneEliminated1Audit = new FileWriter("Audit_IRReassignNoneEliminated1.txt");
        irReassignNoneEliminated1 = new IR(reassignNoneEliminated1Input, reassignNoneEliminated1Audit);
        irReassignNoneEliminated1.parseHeader();
        irReassignNoneEliminated1.processFile();

        FileReader reassign1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRReassign1.csv");
        // Candidate A ballots: [1,2,3]  [1,0,2]  [1,3,2]  [1,0,0]
        // Candidate B ballots: [0,1,0]  [0,1,2]  [2,1,3]  [3,1,2]
        // Candidate C ballots: [0,0,1]  [2,3,1]  [2,0,1]  [3,0,1]
        irReassign1 = new IR(reassign1Input, null);
        irReassign1.parseHeader();
        irReassign1.processFile();

        FileReader eliminate1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IREliminate1.csv");
        // Same as reassign2Input
        irEliminate1 = new IR(eliminate1Input, null);
        irEliminate1.parseHeader();
        irEliminate1.processFile();
        irEliminate1.eliminateCandidate(0);
        irEliminate1.eliminateCandidate(1);

        FileReader reassign2Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRReassign2.csv");
        irReassign2 = new IR(reassign2Input, null);
        irReassign2.parseHeader();
        irReassign2.processFile();
        irReassign2.getCandidates()[2].setEliminated(true);
        irReassign2.getCandidates()[4].setEliminated(true);

        FileReader eliminate2Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IREliminate2.csv");
        irEliminate2 = new IR(eliminate2Input, null);
        irEliminate2.parseHeader();
        irEliminate2.processFile();
        irEliminate2.getCandidates()[2].setEliminated(true);
        irEliminate2.getCandidates()[4].setEliminated(true);
        irEliminate2.eliminateCandidate(0);
    }

    @Test
    public void runTests(){
        Assert.assertTrue("test.txt" == runElection.filename);
    }

    @Test
    public void test_initialize_candidates_1(){
        Assert.assertTrue(irInitialize.getCandidates()[0].getName().equals("Rosen"));
    }
    @Test
    public void test_initialize_candidates_2(){
        Assert.assertTrue(irInitialize.getCandidates()[0].getParty().equals("(D)"));
    }

    @Test
    public void test_initialize_candidates_3(){
        Assert.assertTrue(irInitialize.getCandidates()[3].getName().equals("Royce"));
    }
    @Test
    public void test_initialize_candidates_4(){
        Assert.assertTrue(irInitialize.getCandidates()[3].getParty().equals("(L)"));
    }

    @Test
    public void test_majority_candidate_1() {
        // Input: Two Candidates
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        // Candidate A total first-place votes: 4
        // Candidate B total first-place votes: 2
        // Non-Zero vote total

        Assert.assertTrue(irMajority1.majorityCandidate() == irMajority1.getCandidates()[0]);
    }

    @Test
    public void test_majority_candidate_2() {
        // Testing when exact 50-50
        // Input: Two Candidates
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        // Candidate A total first-place votes: 2
        // Candidate B total first-place votes: 2
        // Non-Zero vote total

        Assert.assertTrue(irMajority2.majorityCandidate() == null);
    }

    @Test
    public void test_majority_candidate_3() {
        // Input: Three Candidates
        // Inserted ballots for Candidate A: [1, 0, 0]  [1, 2, 3]
        // Inserted ballots for Candidate B: [2, 1, 3]  [3, 1, 2]
        // Inserted ballots for Candidate C: [3, 2, 1]  [2, 3, 1]   [0, 0, 1]   [2, 0, 1]
        // Candidate A total first-place votes: 2
        // Candidate B total first-place votes: 2
        // Candidate C total first-place votes: 4
        // Nobody wins majority
        Assert.assertTrue(irMajority3.majorityCandidate() == null);
    }

    @Test
    public void test_parseHeader_1() {
        Assert.assertTrue(ir.getCurNumCandidates() == 4);
    }

    @Test
    public void test_parseHeader_2() {
        Assert.assertTrue(ir.getNumBallots() == 6);
    }

    @Test
    public void test_parseHeader_3() {
        Assert.assertTrue(ir.getCandidates()[2].getName().equals("Chou"));
    }

    @Test
    public void test_reassignVotesNoneEliminated_1() {
        // Simple case where no candidates have yet been eliminated
        // Reassign Candidate A ballots to Candidate B ballots
        // Candidate A ballots:          [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots:          [2, 1]  [0, 1]
        // Inserting:                            [0, 1]  [0, 1]
        // (end) Candidate B Tree:       [2, 1]  [0, 1]  [0, 1]  [0, 1]
        // BTree1 numVotes: 4
        Node BNode = irReassignNoneEliminated1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[1];
        ArrayList<ArrayList<Integer>> toInsert = irReassignNoneEliminated1
                .getCandidates()[0]
                .getBallots()
                .getBallots(BNode);
        irReassignNoneEliminated1.reassignVotesNoneEliminated(toInsert, 1);
        Assert.assertTrue(irReassignNoneEliminated1.getCandidates()[1].getNumVotes() == 4);
    }

    @Test
    public void test_reassignVotes_1() {
        // Intermediate case where there is one previously eliminated candidate
        // Testing double reassignment
        // ORIGINAL ballots:
        // Candidate A ballots: [1,2,3]  [1,0,2]  [1,3,2]  [1,0,0]
        // Candidate B ballots: [0,1,0]  [0,1,2]  [2,1,3]  [3,1,2]
        // Candidate C ballots: [0,0,1]  [2,3,1]  [2,0,1]  [2,0,1]
        //                      [0,1,2]  <--- when getting 2nd place for B
        //                               [0,0,1]  [0,2,1]  <--- when getting 2nd place for C

        // First, eliminate A. State of Candidate B, Candidate C after:
        // Candidate B ballots: [0,1,0] *[0,1,2] [2,1,3] [3,1,2]     [0,1,2]*
        // Candidate C ballots: [0,0,1] [2,3,1] [2,0,1] [2,0,1]     [0,0,1] [0,2,1]
        // Next, eliminate B. State of Candidate C after:
        // Candidate C ballots: [0,0,1] [2,3,1] [2,0,1] [2,0,1]     [0,0,1] [0,2,1]     [0,0,1] [0,0,1] [0,0,1] [0,0,1]
        // Candidate C numVotes = 10
        //                              [0,0,1]         [2,0,1]     [0,0,1] <-- when getting 2nd place C nodes
        //                                      [1,0,2]                     <-- when getting 2nd place A nodes
        ArrayList<Integer> eliminated = new ArrayList<>();
        Node BNode_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[1];
        ArrayList<ArrayList<Integer>> toInsert_B_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()               // A Tree
                .getBallots(BNode_wave1);   // Should be the B ballot subtree

        Node CNode_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert_C_wave1 = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getBallots(CNode_wave1);
        irReassign1.reassignVotesNoneEliminated(toInsert_B_wave1, 1);
        irReassign1.reassignVotesNoneEliminated(toInsert_C_wave1, 2);
        irReassign1.getCandidates()[0].setEliminated(true);
        eliminated.add(0);
        // SECOND Reassignment wave (reassign B to C)
        Node CNode_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert_C_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getBallots(CNode_wave2);
        Node ANode_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getRoot()
                .getChildren()[0];
        ArrayList<ArrayList<Integer>> toInsert_A_wave2 = irReassign1
                .getCandidates()[1]
                .getBallots()
                .getBallots(ANode_wave2);

        irReassign1.reassignVotes(eliminated, toInsert_C_wave2);
        irReassign1.reassignVotes(eliminated, (toInsert_A_wave2));
        Assert.assertTrue(irReassign1.getCandidates()[2].getNumVotes() == 10);
    }

    @Test
    public void test_eliminate_1() {
        // This tests out the exact same election as test_reassignVotes_2
        // Except directly through elimination
        Assert.assertTrue(irEliminate1.getCandidates()[2].getNumVotes() == 10);
    }

    @Test
    public void test_reassignVotes_2() {
        // Test reassignment with multiple eliminated candidates
        // Five candidates total
        // Candidate A ballots:     [1,3,2,5,4] [1,0,4,3,2]
        // Candidate B ballots:     [0,1,0,0,0]
        // Previously eliminated:   Candidates in indices 2, 4
        // Reassign A to B:         [0,2,1,4,3]
        // B afterwards:            [0,1,0,0,0] [0,1,0,2,0]
        ArrayList<Integer> eliminated = new ArrayList<>();
        eliminated.add(2);
        eliminated.add(4);
        Node ballotNode = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getBallots(ballotNode);
        irReassign2.reassignVotes(eliminated, toInsert);
        Assert.assertTrue(irReassign2.getCandidates()[1].getNumVotes() == 2);
    }

    @Test
    public void test_eliminate_2() {
        Assert.assertTrue(irEliminate2.getCandidates()[1].getNumVotes() == 2);
    }

   @Test
   public void test_conductAlgorithm_1() {
        // Simple case where there are 2 candidates
        // Candidate A has 2 votes, Candidate B has 0 votes -- Candidate A wins
        // TODO: Maybe this test can simply look at the output of the audit file
       FileReader conductAlgorithm1Input = null;
       try {
           conductAlgorithm1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRConductAlgorithm1.csv");
       } catch (FileNotFoundException e) {
           throw new RuntimeException(e);
       }
       FileWriter conductAlgorithm1Audit = null;
       try {
           conductAlgorithm1Audit = new FileWriter("Project1/VotingSystem/Testing/IRTestsResources/Audit_IRConductAlgorithm1.txt");
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       irConductAlgorithm1 = new IR(conductAlgorithm1Input, conductAlgorithm1Audit);
       irConductAlgorithm1.parseHeader();
       irConductAlgorithm1.processFile();
       irConductAlgorithm1.conductAlgorithm();
   }
    @Test
    public void test_conductAlgorithm_2() {
        // Test where there are no ties -- there is no immediate majority
        // A candidate must be eliminated to determine the winner
        // Simple election with 3 candidates -- Candidate B wins
        // Candidate A: [1,0,0] [1,2,3] [1,3,2]
        // Candidate B: [0,1,2] [2,1,3] [0,1,0] [0,1,2]
        // Candidate C: [0,2,1] [0,2,1]
        // After C is eliminated:
        // Candidate A: [1,0,0] [1,2,3] [1,3,2]
        // Candidate B: [0,1,2] [2,1,3] [0,1,0] [0,1,2] [0,0,1] [0,0,1]
        // Candidate B has 6/9 votes ---> has majority ---> wins

        FileReader conductAlgorithm2Input = null;
        try {
            conductAlgorithm2Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRConductAlgorithm2.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileWriter conductAlgorithm2Audit = null;
        try {
            conductAlgorithm2Audit = new FileWriter("Project1/VotingSystem/Testing/IRTestsResources/Audit_IRConductAlgorithm2.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        irConductAlgorithm2 = new IR(conductAlgorithm2Input, conductAlgorithm2Audit);
        irConductAlgorithm2.parseHeader();
        irConductAlgorithm2.processFile();
        irConductAlgorithm2.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_3() {
        // NOTE: This test ONLY works while in development
        // As we need to change the breakTie method to return a predetermined number
        // In order to predict exactly who the winner will be every time this test is run.
        // Test where there is one tie, and there are three candidates -- B should win, A & C tied -> A eliminated
        // For purposes of testing, the first candidate in the tied list will be eliminated
        // Candidate A: [1,0,0] [1,2,3]
        // Candidate B: [3,1,2] [2,1,3] [0,1,0] [2,1,0]
        // Candidate C: [0,0,1] [2,0,1]
        // After A gets eliminated ->
        // Candidate B: [3,1,2] [2,1,3] [0,1,0] [2,1,0]    [0,1,2]
        // Candidate C: [0,0,1] [2,0,1]
        // B wins with 5 votes
        FileReader conductAlgorithm3Input = null;
        try {
            conductAlgorithm3Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRConductAlgorithm3.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileWriter conductAlgorithm3Audit = null;
        try {
            conductAlgorithm3Audit = new FileWriter("Project1/VotingSystem/Testing/IRTestsResources/Audit_IRConductAlgorithm3.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        irConductAlgorithm3 = new IR(conductAlgorithm3Input, conductAlgorithm3Audit);
        irConductAlgorithm3.parseHeader();
        irConductAlgorithm3.processFile();
        irConductAlgorithm3.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_4() {
        // Test where there are multiple ties for elimination
        // First, tie between B & C & D -> B Eliminated
        // Next, tie between C & D -> C Eliminated
        // D will win the majority compared to A
        // For purposes of testing, the first candidate in the tied list will be eliminated
        // Candidate A: [1,0,0,0] [1,2,3,4] [1,2,3,4] [1,2,3,4] [1,0,0,0] [1,0,0,0]
        // Candidate B: [0,1,0,0] [3,1,2,0] [0,1,0,0] [3,1,4,2]
        // Candidate C: [2,0,1,0] [2,0,1,0] [2,0,1,3] [2,0,1,3]
        // Candidate D: [4,3,2,1] [0,2,0,1] [2,0,0,1] [2,3,0,1]
        // After B is eliminated:
        // Candidate A: [1,0,0,0] [1,2,3,4] [1,2,3,4] [1,2,3,4] [1,0,0,0] [1,0,0,0]
        // Candidate C: [2,0,1,0] [2,0,1,0] [2,0,1,3] [2,0,1,3] *[2,0,1,0]*
        // Candidate D: [4,3,2,1] [0,2,0,1] [2,0,0,1] [2,3,0,1] *[2,0,3,1]*
        // After C is eliminated:
        // Candidate A: [1,0,0,0] [1,2,3,4] [1,2,3,4] [1,2,3,4] [1,0,0,0] [1,0,0,0]
        //             *[1,0,0,0] [1,0,0,0] [1,0,0,2] [1,0,0,2] [1,0,0,0]*
        // Candidate D: [4,3,2,1] [0,2,0,1] [2,0,0,1] [2,3,0,1] [2,0,3,1]
        // Candidate A wins with 11 votes (total votes: 16)
        FileReader conductAlgorithm4Input = null;
        try {
            conductAlgorithm4Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRConductAlgorithm4.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileWriter conductAlgorithm4Audit = null;
        try {
            conductAlgorithm4Audit = new FileWriter("Project1/VotingSystem/Testing/IRTestsResources/Audit_IRConductAlgorithm4.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        irConductAlgorithm4 = new IR(conductAlgorithm4Input, conductAlgorithm4Audit);
        irConductAlgorithm4.parseHeader();
        irConductAlgorithm4.processFile();
        irConductAlgorithm4.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_5() {
        // Test where POPULARITY decides the winner
        // Candidate A: [1,2] [1,0] [1,2]
        // Candidate B: [2,1] [2,1]
        // A wins
        FileReader conductAlgorithm5Input = null;
        try {
            conductAlgorithm5Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRConductAlgorithm5.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileWriter conductAlgorithm5Audit = null;
        try {
            conductAlgorithm5Audit = new FileWriter("Project1/VotingSystem/Testing/IRTestsResources/Audit_IRConductAlgorithm5.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        irConductAlgorithm5 = new IR(conductAlgorithm5Input, conductAlgorithm5Audit);
        irConductAlgorithm5.parseHeader();
        irConductAlgorithm5.processFile();
        irConductAlgorithm5.conductAlgorithm();
    }

    @Test
    public void test_conductAlgorithm_6() {
        // test ballots.csv
        FileReader conductAlgorithm6Input = null;
        try {
            conductAlgorithm6Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/ballots.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileWriter conductAlgorithm6Audit = null;
        try {
            conductAlgorithm6Audit = new FileWriter("Project1/VotingSystem/Testing/IRTestsResources/Audit_IRConductAlgorithm6.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        irConductAlgorithm6 = new IR(conductAlgorithm6Input, conductAlgorithm6Audit);
        irConductAlgorithm6.parseHeader();
        irConductAlgorithm6.processFile();
        irConductAlgorithm6.conductAlgorithm();
    }


}
