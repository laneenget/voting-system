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
        RunElection newElec = new RunElection("france.csv");
        newElec.start();
    }

    @Test
    public void testIR(){
        RunElection newElec = new RunElection("ballot.csv");
        newElec.start();
    }
}
