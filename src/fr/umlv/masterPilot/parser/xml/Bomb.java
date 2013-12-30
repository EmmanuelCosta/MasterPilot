package fr.umlv.masterPilot.parser.xml;

/**
 * @author Sybille
 * Object containing informations about the xml parsing 
 */
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
            .append("\n")
            .toString();
    }
}
