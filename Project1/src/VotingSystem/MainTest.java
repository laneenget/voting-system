package VotingSystem;

import java.io.*;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.fail;
/*
 * This class is used to test the RunElection class
 * Through which the entire system functionality
 * is tested.
 * Results are validated via command line
 * output and validating the creation of
 * an accurate audit file.
 */
public class Maintests {
    


    @Test
    public void testCPL(){
        // Create new RunElection object with "france.csv" as the file name
        RunElection newElec = new RunElection("france.csv");
        // Call start method of election
        newElec.start();
    }

    @Test
    public void testIR(){
        // Create new RunElection object with "ballot.csv" as the file name
        RunElection newElec = new RunElection("ballot.csv");
        // Call start method of election
        newElec.start();
    }
}
