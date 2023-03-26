package Testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import VotingSystem.CPL;
import VotingSystem.IR;
import VotingSystem.RunElection;

public class RunElectionTests {
    
    private RunElection election;
    private File file;
    private FileWriter audit;
    
    @BeforeEach
    public void setup() throws IOException {
        URL path = RunElection.class.getResource("/test.csv");
        file = new File(path.getFile());
        audit = mock(FileWriter.class);
        election = new RunElection(file.getName());
    }
    
    @Test
    public void testGenerateAuditFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String expectedIRName = "IR" + now.format(formatter) + ".txt";
        String expectedCPLName = "CPL" + now.format(formatter) + ".txt";
        
        String actualIRName = election.generateAuditFileName("IR");
        String actualCPLName = election.generateAuditFileName("CPL");
        
        assertEquals(expectedIRName, actualIRName);
        assertEquals(expectedCPLName, actualCPLName);
    }
    
    @Test
    public void testParseElectType() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String expectedType = br.readLine();
        br.close();
        
        String actualType = election.parseElectType();
        
        assertEquals(expectedType, actualType);
    }
    
    @Test
    public void testRunIR() throws IOException {
        IR ir = mock(IR.class);
        election.runIR();
    }
    
    @Test
    public void testRunCPL() throws IOException {
        CPL cpl = mock(CPL.class);
        election.runCPL();
    }
    
    @Test
    public void testStartWithValidFile() throws IOException {
        RunElection spyElection = org.mockito.Mockito.spy(election);
        
        spyElection.start();
        
        org.mockito.Mockito.verify(spyElection).runIR();
    }
    
    @Test
    public void testStartWithInvalidFile() throws IOException {
        election = new RunElection("invalid.csv");
        RunElection spyElection = org.mockito.Mockito.spy(election);
        
        assertThrows(NullPointerException.class, () -> {
            spyElection.start();
        });
    }
}

