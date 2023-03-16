

import java.io.FileReader;
import java.io.FileWriter;

public class CPL extends Election {
    private Party[] parties;
    private int voteTotal;
    private int numSeats;
    private FileReader input;
    private FileWriter output;

    public CPL(FileReader input, FileWriter output){
        this.input = input;
        this.output = output;
    }

    public Party drawLotto(){return parties[0];}
    public void processFile(){}
    public void conductAlgorithm(){}
    public void parseHeader(){}
    public void printResults(){}
}
