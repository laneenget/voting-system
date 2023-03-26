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
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
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
    public void setUp() throws IOException {
        String path = System.getProperty("user.dir");
        file = new File(path, "/Project1/VotingSystem/Testing/RunElectionTest.csv");
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
    public void testParseElectType_IR() throws IOException {
        election.input = new FileReader(file);
        String actualType = election.parseElectType();
        String expectedType = "IR";
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseElectType_CPL() throws IOException {
        File f = new File("RunElectionTestCPL.csv");
        election.input = new FileReader(f);
        String actualType = election.parseElectType();
        String expectedType = "CPL";
        assertEquals(expectedType, actualType);
    }
    
    @Test
    public void testRunIR() throws IOException {
        IR ir = mock(IR.class);
        file = new File("RunElectionTests1.csv");
        election.input = new FileReader(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        election.br = br;
        br.readLine();
        File mockAudit = new File(path, "/Project1/VotingSystem/Testing/Audit_RunElectionTests1.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election.audit = fw;
        election.runIR();
    }
    
    @Test
    public void testRunCPL() throws IOException {
        CPL cpl = mock(CPL.class);
        file = new File("RunElectionTest2.csv");
        election.input = new FileReader(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
        election.br = br;
        br.readLine();
        File mockAudit = new File("Audit_RunElectionTest2.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election.audit = fw;
        election.runCPL();
    }
    
    @Test
    public void testStartWithValidFile() throws IOException {
        file = new File("testStartWithValidFile.csv");
        election = new RunElection(file.getName());
        election.input(new FileReader(file));
        File mockAudit = new File("Audit_testStartWithValidFile.csv");
        FileWriter fw = new FileWriter(mockAudit);
        election.audit(fw);
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        election.br = br;
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

