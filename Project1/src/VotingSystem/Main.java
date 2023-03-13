package VotingSystem;

public class Main {

    public static void main(String args []) {
        String holder = " ";
        if(args.length > 0){
            holder = args[0];
        }   
        RunElection newElection = new RunElection(holder);
        newElection.start();

    }
}
