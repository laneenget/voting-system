package Testing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import VotingSystem.*;
//import VotingSystem.Tree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//import VotingSystem.RunElection;

import static org.junit.Assert.fail;

public class IRtests {
    RunElection runElection;
    IR ir;
    String[] candidateNames = {"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};

    IR irMajority1;
    IR irMajority2;
    IR irMajority3;
    IR irReassign1;
    IR irReassign2;
    IR SAVEUS;

    @Before
    public void setUp() {
        runElection = new RunElection("test.txt");
        // Todo: add separate ir for initialize tests
        // Tests for initializing candidate
        //ir.setCandidates(new Candidate[4]);
//        ir.initializeCandidates(candidateNames);
        FileReader input;
        try {
            input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRHeader1.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ir = new IR(input, null);
        ir.parseHeader();

        FileReader majority1Input;
        try {
            majority1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRMajority1.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Candidate A ballots : [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots:  [2, 1]  [0, 1]
        irMajority1 = new IR(majority1Input, null);
        irMajority1.parseHeader();
        irMajority1.processFile();

        FileReader majority2Input;
        try {
            majority2Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRMajority2.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        irMajority2 = new IR(majority2Input, null);
        irMajority2.parseHeader();
        irMajority2.processFile();

        FileReader majority3Input;
        try {
            majority3Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRMajority3.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Inserted ballots for Candidate A: [1, 0, 0]  [1, 2, 3]
        // Inserted ballots for Candidate B: [2, 1, 3]  [3, 1, 2]
        // Inserted ballots for Candidate C: [3, 2, 1]  [2, 3, 1]   [0, 0, 1]   [2, 0, 1]
        irMajority3 = new IR(majority3Input, null);
        irMajority3.parseHeader();
        irMajority3.processFile();

        FileReader reassign1Input;
        try {
            reassign1Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRReassign1.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Candidate A ballots: [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots: [2, 1]  [0, 1]
        FileWriter reassign1InputAudit;
        try {
            reassign1InputAudit = new FileWriter("reassign2InputAudit.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        irReassign1 = new IR(reassign1Input, reassign1InputAudit);
        irReassign1.parseHeader();
        irReassign1.processFile();

        FileReader reassign2Input;
        try {
            reassign2Input = new FileReader("Project1/VotingSystem/Testing/IRTestsResources/IRReassign2.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Candidate A ballots: [1,2,3]  [1,0,2]  [1,3,2]  [1,0,0]
        // Candidate B ballots: [0,1,0]  [0,1,2]  [2,1,3]  [3,1,2]
        // Candidate C ballots: [0,0,1]  [2,3,1]  [2,0,1]  [3,0,1]
        irReassign2 = new IR(reassign2Input, null);
        irReassign2.parseHeader();
        irReassign2.processFile();

    }

    @Test
    public void runTests(){
        Assert.assertTrue("test.txt" == runElection.filename);
    }

    @Test
    public void test_initialize_candidates_1(){
        Assert.assertTrue(ir.getCandidates()[0].getName().equals("Rosen"));
    }
    @Test
    public void test_initialize_candidates_2(){
        Assert.assertTrue(ir.getCandidates()[0].getParty().equals("(D)"));
    }

    @Test
    public void test_initialize_candidates_3(){
        Assert.assertTrue(ir.getCandidates()[3].getName().equals("Royce"));
    }
    @Test
    public void test_initialize_candidates_4(){
        Assert.assertTrue(ir.getCandidates()[3].getParty().equals("(L)"));
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
    public void test_reassignVotes_1() {
        // Simple case where no candidates have yet been eliminated
        // Reassign Candidate A ballots to Candidate B ballots
        // Candidate A ballots:          [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Candidate B ballots:          [2, 1]  [0, 1]
        // Inserting:                            [0, 1]  [0, 1]
        // (end) Candidate B Tree:       [2, 1]  [0, 1]  [0, 1]  [0, 1]
        // BTree1 numVotes: 4
        Tree A = irReassign1.getCandidates()[0].getBallots();
        ArrayList<ArrayList<Integer>> allBallots = A.getBallots(A.getRoot());
        for (int i = 0; i < allBallots.size(); i++) {
            for (int j = 0; j < allBallots.get(i).size(); j++) {
                System.out.print(allBallots.get(i).get(j));
            }
            System.out.println("End ballot, reassign1");
        }
        System.out.println();
        Node BNode = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[1];
        ArrayList<ArrayList<Integer>> toInsert = irReassign1
                .getCandidates()[0]
                .getBallots()
                .getBallots(BNode);
        irReassign1.reassignVotes(toInsert, 1);
        Assert.assertTrue(irReassign1.getCandidates()[1].getNumVotes() == 4);
    }

    @Test
    public void test_reassignVotes_2() {
        // Intermediate case where there is one previously eliminated candidate
        // Testing double reassignment
        // ORIGINAL ballots:
        // Candidate A ballots: [1,2,3]  [1,0,2]  [1,3,2]  [1,0,0]
        // Candidate B ballots: [0,1,0]  [0,1,2]  [2,1,3]  [3,1,2]
        // Candidate C ballots: [0,0,1]  [2,3,1]  [2,0,1]  [3,0,1]
        //                      [0,1,2]  <--- when getting 2nd place for B
        //                               [0,0,1]  [0,2,1]  <--- when getting 2nd place for C

        // First, eliminate A. State of Candidate B, Candidate C after:
        // Candidate B ballots: [0,1,0] *[0,1,2] [2,1,3] [3,1,2]     [0,1,2]*
        // Candidate C ballots: [0,0,1] [2,3,1] [2,0,1] [3,0,1]     [0,0,1] [0,2,1]
        // Next, eliminate B. State of Candidate C after:
        // Candidate C ballots: [0,0,1] [2,3,1] [2,0,1] [3,0,1]     [0,0,1] [0,2,1]     [0,0,1] [0,0,1] [0,0,1] [0,0,1]
        // Candidate C numVotes = 10
        //                              [0,0,1]         [2,0,1]     [0,0,1] <-- when getting 2nd place C nodes
        //                                      [1,0,2]                     <-- when getting 2nd place A nodes

        // FIRST Reassignment wave
        Tree B = irReassign2.getCandidates()[1].getBallots();
        ArrayList<ArrayList<Integer>> allBallots = B.getBallots(B.getRoot());
        for (int i = 0; i < allBallots.size(); i++) {
            for (int j = 0; j < allBallots.get(i).size(); j++) {
                System.out.print(allBallots.get(i).get(j));
            }
            System.out.println("End ballot, here");
        }
        // Doesn't seem to include 010, but that's ok
        System.out.println();
        Node BNode_wave1 = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[1];
        ArrayList<ArrayList<Integer>> toInsert_B_wave1 = irReassign2
                .getCandidates()[0]
                .getBallots()               // A Tree
                .getBallots(BNode_wave1);   // Should be the B ballot subtree
        for (int i = 0; i < toInsert_B_wave1.size(); i++) {
            for (int j = 0; j < toInsert_B_wave1.get(i).size(); j++) {
                System.out.print(toInsert_B_wave1.get(i).get(j) + " ");
            }
            System.out.println("checking B nodes from A's Tree");
        }

        Node CNode_wave1 = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert_C_wave1 = irReassign2
                .getCandidates()[0]
                .getBallots()
                .getBallots(CNode_wave1);
        irReassign2.reassignVotes(toInsert_B_wave1, 1);
        irReassign2.reassignVotes(toInsert_C_wave1, 2);
        irReassign2.getCandidates()[0].setEliminated(true);

        // SECOND Reassignment wave (reassign B to C)
        Node CNode_wave2 = irReassign2
                .getCandidates()[1]
                .getBallots()
                .getRoot()
                .getChildren()[2];
        ArrayList<ArrayList<Integer>> toInsert_C_wave2 = irReassign2
                .getCandidates()[1]
                .getBallots()
                .getBallots(CNode_wave2);
        for (int i = 0; i < toInsert_C_wave2.size(); i++) {
            for (int j = 0; j < toInsert_C_wave2.get(i).size(); j++) {
                System.out.print(toInsert_C_wave2.get(i).get(j) + " ");
            }
            System.out.println("checking C nodes from B's Tree");
        }


        irReassign2.reassignVotes(toInsert_C_wave2, 2);
        System.out.println(irReassign2.getCandidates()[2].getNumVotes());
        Assert.assertTrue(irReassign2.getCandidates()[2].getNumVotes() == 10);

    }
}
