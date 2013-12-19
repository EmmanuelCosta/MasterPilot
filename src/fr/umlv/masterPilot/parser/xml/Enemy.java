package fr.umlv.masterPilot.parser.xml;

public class Enemy {
    private short totalPercentage;
    private short tiePercentage;
    private short cruiserPercentage;
    private short squadronPercentage;
    
/*------------------------------------------------------------------------------------------------*/
    
    public short getTotalPercentage() {
        return totalPercentage;
    }
    
    public short getTiePercentage() {
        return tiePercentage;
    }
    
    public short getcruiserPercentage() {
        return cruiserPercentage;
    }
    
    public short getSquadronPercentage() {
        return squadronPercentage;
    }
    
/*------------------------------------------------------------------------------------------------*/
        
    public void setTotalPercentage(short totalPercentage) { 
        this.totalPercentage = totalPercentage;
    }
    
    public void setTiePercentage(short tiePercentage) { 
        this.tiePercentage = tiePercentage;
    }
    
    public void setCruiserPercentage(short cruiserPercentage) { 
        this.cruiserPercentage = cruiserPercentage;
    }
    
    public void setSquadronPercentage(short squadronPercentage) { 
        this.squadronPercentage = squadronPercentage;
    }

/*------------------------------------------------------------------------------------------------*/
    
    public String toString(){
        return new StringBuffer("Total enemy's percentage").append(totalPercentage).append(", ")
            .append("TIE's Percentage : ").append(tiePercentage).append(", ")
            .append("Cruiser's Percentage : ").append(cruiserPercentage).append(", ")
            .append("Squadron's Percentage : ").append(squadronPercentage).append(", ")
            .toString();
    }
}
