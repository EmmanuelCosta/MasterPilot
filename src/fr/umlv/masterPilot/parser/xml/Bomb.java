package fr.umlv.masterPilot.parser.xml;

public class Bomb {
    private short bombPercentage;
    
    public short getBombPercentage() {
        return bombPercentage;
    }
    
    public void setBombPercentage(short bombPercentage) { 
        this.bombPercentage = bombPercentage;
    }

    public String toString(){
        return new StringBuffer("Percentage's bomb : ")
            .append(bombPercentage)
            .toString();
    }
}
