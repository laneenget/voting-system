package VotingSystem;

public class Main {

    public static void main(String args []) {
        String holder = args[0];
        RunElection newElection = new RunElection(holder);
        newElection.start();

    }
}
