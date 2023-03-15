package VotingSystem.Testing;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import VotingSystem.RunElection;

import static org.junit.Assert.fail;

public class Tests {
    RunElection runElection;
    @Before
    public void setUp() {
        runElection = new RunElection("test.txt");
    }

    @Test
    public void runTests(){
        Assert.assertTrue("test.txt" == runElection.filename);
    }
    
}
