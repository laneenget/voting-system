package VotingSystem;

public class Party {
    private String name;
    private String [] candidates;
    private int numVotes;
    private int seatsReceived;

    public String getName(){return name;}
    public int getNumVotes(){return numVotes;}
    public int getSeatsReceived(){return seatsReceived;}
    public String [] getCandidates(){return candidates;}
    public void setName(String name){
        this.name = name;
    }
    public void setCandidates(String [] candidates){
        this.candidates = candidates;
    }
    public void setNumVotes(int votes){
        numVotes = votes;
    }
    public void setSeatsReceived(int seats){
        seatsReceived = seats;
    }
    public void incrementVoteCount(){numVotes++;}
    public void incrementSeatCount(){seatsReceived++;}
}
