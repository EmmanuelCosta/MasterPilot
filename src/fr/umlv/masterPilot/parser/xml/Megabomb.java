package fr.umlv.masterPilot.parser.xml;

/**
 * @author Sybille
 * Object containing informations about the xml parsing 
 */
public class Megabomb {
    private short megaBombPercentage;
    
    public short getMegaBombPercentage() {
        return megaBombPercentage;
    }
    
    public void setMegaBombPercentage(short bombPercentage) { 
        this.megaBombPercentage = bombPercentage;
    }

    public String toString(){
        return new StringBuffer("Percentage's megabomb : ")
            .append(megaBombPercentage)
            .append("\n")
            .toString();
    }
}
