package fr.umlv.masterPilot.parser.xml;

public class Enemy {
    private short totalEnemyNumber;
    private short tieNumber;
    private short cruiserNumber;
    private short squadronNumber;
    
/*------------------------------------------------------------------------------------------------*/
    
    public short getTotalEnemyNumber() {
        return totalEnemyNumber;
    }
    
    public short getTieNumber() {
        return tieNumber;
    }
    
    public short getcruiserNumber() {
        return cruiserNumber;
    }
    
    public short getSquadronNumber() {
        return squadronNumber;
    }
    
/*------------------------------------------------------------------------------------------------*/
        
    public void setTotalEnemyNumber(short totalPercentage) { 
        this.totalEnemyNumber = totalPercentage;
    }
    
    public void setTieNumber(short tiePercentage) { 
        this.tieNumber = tiePercentage;
    }
    
    public void setCruiserNumber(short cruiserPercentage) { 
        this.cruiserNumber = cruiserPercentage;
    }
    
    public void setSquadronNumber(short squadronPercentage) { 
        this.squadronNumber = squadronPercentage;
    }

/*------------------------------------------------------------------------------------------------*/
    
    public String toString(){
        return new StringBuffer("Total enemy's percentage: ").append(totalEnemyNumber).append("\n")
            .append("TIE's Percentage : ").append(tieNumber).append("\n")
            .append("Cruiser's Percentage : ").append(cruiserNumber).append("\n")
            .append("Squadron's Percentage : ").append(squadronNumber).append("\n")
            .toString();
    }
}
