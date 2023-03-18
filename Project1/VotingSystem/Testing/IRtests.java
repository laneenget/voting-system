package Testing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import VotingSystem.*;
//import VotingSystem.Tree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import VotingSystem.RunElection;

import static org.junit.Assert.fail;

public class IRtests {
    RunElection runElection;
    IR ir = new IR(null, null);
    String[] candidateNames = {"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};

    Tree ATree1;
    Tree BTree1;
    Tree ATree2;
    Tree BTree2;
    Tree CTree1;
    Tree ATree3;

    @Before
    public void setUp() {
        runElection = new RunElection("test.txt");
        // Tests for initializing candidate
        ir.setCandidates(new Candidate[4]);
        ir.initializeCandidates(candidateNames);

        FileReader input;
        // try {
        //     // input = new FileReader("Project1\\VotingSystem\\Testing\\IRHeader1.csv");
        // } catch (FileNotFoundException e) {
        //     throw new RuntimeException(e);
        // }
        //ir = new IR(input, null);
        //ir.parseHeader();

        // Trees for tests
        // ATree1 ballots : [1, 0]  [1, 2]  [1, 2]  [1, 0]
        ATree1 = new Tree(0,2);
        ArrayList<Integer> ATree1List1 = new ArrayList<>();
        ATree1List1.add(1);
        ATree1List1.add(0);
        ATree1.insert(ATree1List1);
        ArrayList<Integer> ATree1List2 = new ArrayList<>();
        ATree1List2.add(1);
        ATree1List2.add(2);
        ATree1.insert(ATree1List2);
        ATree1.insert(ATree1List2);
        ATree1.insert(ATree1List1);

        // BTree1 ballots : [2, 1]  [0, 1]
        BTree1 = new Tree(1, 2);
        ArrayList<Integer> BTree1List1 = new ArrayList<>();
        BTree1List1.add(2);
        BTree1List1.add(1);
        BTree1.insert(BTree1List1);

        ArrayList<Integer> BTree1List2 = new ArrayList<>();
        BTree1List2.add(0);
        BTree1List2.add(1);
        BTree1.insert(BTree1List2);

        // ATree2 ballots : [1, 0]  [1, 2]
        ATree2 = new Tree(0, 2);
        ArrayList<Integer> ATree2List1 = new ArrayList<>();
        ATree2List1.add(1);
        ATree2List1.add(0);
        ATree2.insert(ATree2List1);

        ArrayList<Integer> ATree2List2 = new ArrayList<>();
        ATree2List2.add(1);
        ATree2List2.add(2);
        ATree2.insert(ATree2List2);

        // ATree3 ballots : [1, 0, 0]  [1, 2, 3]
        ATree3 = new Tree(0, 3);
        ArrayList<Integer> ATree3List1 = new ArrayList<>();
        ATree3List1.add(1);
        ATree3List1.add(0);
        ATree3List1.add(0);
        ATree3.insert(ATree3List1);

        ArrayList<Integer> ATree3List2 = new ArrayList<>();
        ATree3List2.add(1);
        ATree3List2.add(2);
        ATree3List2.add(3);
        ATree3.insert(ATree3List2);

        // BTree2 ballots : [2, 1, 3]  [3, 1, 2]
        BTree2 = new Tree(1, 3);
        ArrayList<Integer> BTree2List1 = new ArrayList<>();
        BTree2List1.add(2);
        BTree2List1.add(1);
        BTree2List1.add(3);
        BTree2.insert(BTree2List1);

        ArrayList<Integer> BTree2List2 = new ArrayList<>();
        BTree2List2.add(3);
        BTree2List2.add(1);
        BTree2List2.add(2);
        BTree2.insert(BTree2List2);

        // CTree1 ballots : [3, 2, 1]  [2, 3, 1]   [0, 0, 1]   [2, 0, 1]
        CTree1 = new Tree(2,3);
        ArrayList<Integer> CTree1List1 = new ArrayList<>();
        CTree1List1.add(3);
        CTree1List1.add(2);
        CTree1List1.add(1);
        CTree1.insert(CTree1List1);

        ArrayList<Integer> CTree1List2 = new ArrayList<>();
        CTree1List2.add(2);
        CTree1List2.add(3);
        CTree1List2.add(1);
        CTree1.insert(CTree1List2);

        ArrayList<Integer> CTree1List3 = new ArrayList<>();
        CTree1List3.add(0);
        CTree1List3.add(0);
        CTree1List3.add(1);
        CTree1.insert(CTree1List3);

        ArrayList<Integer> CTree1List4 = new ArrayList<>();
        CTree1List4.add(2);
        CTree1List4.add(0);
        CTree1List4.add(1);
        CTree1.insert(CTree1List4);

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

//    @Test
//    public void test_majority_candidate_1() {
//        // Input: One Candidate
//        // Simple Tree with one root node
//        Tree zeroTree = new Tree(0,1);
//        Candidate test1 = new Candidate("Zero", "(Zero)", zeroTree);
//        Candidate[] lst = {test1};
//        ir.setCandidates(lst);
//        Assert.assertTrue(ir.majorityCandidate() == null);
//    }

    @Test
    public void test_majority_candidate_2() {
        // Input: Two Candidates
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        // Candidate A total first-place votes: 4
        // Candidate B total first-place votes: 2
        // Non-Zero vote total

        Candidate test1 = new Candidate("A", "(A)", ATree1);
        Candidate test2 = new Candidate("B", "(B)", BTree1);
        Candidate[] lst = {test1, test2};
        ir.setCandidates(lst);
        Assert.assertTrue(ir.majorityCandidate() == test1);
    }

    @Test
    public void test_majority_candidate_3() {
        // Testing when exact 50-50
        // Input: Two Candidates
        // Inserted ballots for Candidate A: [1, 0]  [1, 2]
        // Inserted ballots for Candidate B: [2, 1]  [0, 1]
        // Candidate A total first-place votes: 2
        // Candidate B total first-place votes: 2
        // Non-Zero vote total

        Candidate test1 = new Candidate("A", "(A)", ATree2);
        Candidate test2 = new Candidate("B", "(B)", BTree1);
        Candidate[] lst = {test1, test2};
        ir.setCandidates(lst);
        Assert.assertTrue(ir.majorityCandidate() == null);
    }

    @Test
    public void test_majority_candidate_4() {
        // Input: Three Candidates
        // Inserted ballots for Candidate A: [1, 0, 0]  [1, 2, 3]
        // Inserted ballots for Candidate B: [2, 1, 3]  [3, 1, 2]
        // Inserted ballots for Candidate C: [3, 2, 1]  [2, 3, 1]   [0, 0, 1]   [2, 0, 1]
        // Candidate A total first-place votes: 2
        // Candidate B total first-place votes: 2
        // Candidate C total first-place votes: 4
        // Nobody wins majority

        Candidate test1 = new Candidate("A", "(A)", ATree3);
        Candidate test2 = new Candidate("B", "(B)", BTree2);
        Candidate test3 = new Candidate("C", "(C)", CTree1);
        Candidate[] lst = {test1, test2, test3};
        ir.setCandidates(lst);
        Assert.assertTrue(ir.majorityCandidate() == null);
    }

    //@Test
    // public void test_parseHeader_1() {
    //     Assert.assertTrue(ir.getCurNumCandidates() == 4);
    // }

    // @Test
    // public void test_parseHeader_2() {
    //     Assert.assertTrue(ir.getNumBallots() == 6);
    // }

    // @Test
    // public void test_parseHeader_3() {
    //     Assert.assertTrue(ir.getCandidates()[2].getName().equals("Chou"));
    // }

    @Test
    public void test_reassignVotes_1() {
        // Simple case where no candidates have yet been eliminated
        // Reassign ATree1 ballots to BTree1
        // ATree1:          [1, 0]  [1, 2]  [1, 2]  [1, 0]
        // BTree1:          [2, 1]  [0, 1]
        // (end) BTree1:    [2, 1]  [0, 1]  [0, 1]  [0, 1]
        // BTree1 numVotes: 4

        Candidate test2 = new Candidate("B", "(B)", BTree1);
        ArrayList<Integer> ballot1 = new ArrayList<>();
        ballot1.add(0);
        ballot1.add(1);
        ArrayList<Integer> ballot2 = new ArrayList<>();
        ballot2.add(0);
        ballot2.add(1);
        ArrayList<ArrayList<Integer>> toInsert = new ArrayList<>();
        toInsert.add(ballot1);
        toInsert.add(ballot2);

    }
}
