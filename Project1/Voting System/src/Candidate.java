public class Candidate {
    private String name;
    private String party;
    private Tree ballots;
    private boolean eliminated;

    public Candidate(String name, String party, Tree ballots){
        this.name = name;
        this.party = party;
        this.ballots = ballots;
        eliminated = false;
    }

    public String getName(){return name;}
    public String getParty(){return party;}
    public int getNumVotes(){return ballots.root.numVotes;}
    public boolean isEliminated(){return eliminated;}
    public void setEliminated(boolean out){
        eliminated = out;
    }
    public Tree getBallots(){return ballots;}

}
