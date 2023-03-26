package VotingSystem;

import java.io.*;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.fail;
public class Maintests {
    


    @Test
    public void testCPL(){
        // Please make sure france.csv is actually in the same directory as RunElection.java
        // Before running this test
        RunElection newElec = new RunElection("france.csv");
        newElec.start();
    }

    @Test
    public void testIR(){
        // Please make sure ballots.csv is actually in the same directory as RunElection.java
        // Before running this test
        RunElection newElec = new RunElection("ballots.csv");
        newElec.start();
    }
}
