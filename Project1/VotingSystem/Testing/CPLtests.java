package Testing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import VotingSystem.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import VotingSystem.RunElection;

import static org.junit.Assert.fail;

public class CPLtests {
    RunElection runElection;
    CPL cpl = new CPL(null, null);
    String[] parties = ["Democratic", "Republican", "New Wave", "Reform", "Green", "Independent"];
    String[] demCandidates = ["Foster", "Volz", "Pike"];
    String[] repCandidates = ["Green", "Xu", "Wang"];
    String[] nwCandidates = ["Jacks", "Rosen"];
    String[] refCandidates = ["McClure", "Berg"];
    String[] greenCandidates = ["Zheng", "Melvin"];
    String[] indCandidates = ["Peters"];

    @Before
    public void setUp() {
        
    }
}
