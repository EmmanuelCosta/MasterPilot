package fr.umlv.masterPilot.parser.xml;

public class Enemy {
    private int totalEnemyNumber;
    private int tieNumber;
    private int cruiserNumber;
    private int squadronNumber;
    private int spaceBallNumber;
    private int invaderNumber;
    
/*------------------------------------------------------------------------------------------------*/
    
    public int getTotalEnemyNumber() {
        return totalEnemyNumber;
    }
    
    public int getTieNumber() {
        return tieNumber;
    }
    
    public int getcruiserNumber() {
        return cruiserNumber;
    }
    
    public int getSquadronNumber() {
        return squadronNumber;
    }
    
    public int getSpaceBallNumber() {
        return spaceBallNumber;
    }
    
    public int getInvaderNumber() {
        return invaderNumber;
    }
    
/*------------------------------------------------------------------------------------------------*/
        
    public void setTotalEnemyNumber(int totalPercentage) { 
        this.totalEnemyNumber = totalPercentage;
    }
    
    public void setTieNumber(int tiePercentage) { 
        this.tieNumber = tiePercentage;
    }
    
    public void setCruiserNumber(int cruiserPercentage) { 
        this.cruiserNumber = cruiserPercentage;
    }
    
    public void setSquadronNumber(int squadronPercentage) { 
        this.squadronNumber = squadronPercentage;
    }
    
    public void setSpaceBallNumber(int spaceBallNumber) { 
        this.spaceBallNumber = spaceBallNumber;
    }
  
    public void setInvaderNumber(int invaderNumber) { 
        this.invaderNumber = invaderNumber;
    }
    
    

/*------------------------------------------------------------------------------------------------*/
    
    public String toString(){
        return new StringBuffer("Total enemy's number: ").append(totalEnemyNumber).append("\n")
            .append("TIE's number : ").append(tieNumber).append("\n")
            .append("Cruiser's number : ").append(cruiserNumber).append("\n")
            .append("Squadron's number : ").append(squadronNumber).append("\n")
            .append("SpaceBall's number : ").append(spaceBallNumber).append("\n")
            .append("Invader's number : ").append(invaderNumber).append("\n")
            .toString();
    }
}
