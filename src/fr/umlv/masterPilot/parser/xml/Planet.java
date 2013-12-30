package fr.umlv.masterPilot.parser.xml;

/**
 * @author Sybille
 * Object containing informations about the xml parsing 
 */
public class Planet {
    private short totalPercentage;
    private short radiusMin;
    private short radiusMax;
    private int densityMin;
    private int densityMax;
    
/*------------------------------------------------------------------------------------------------*/
    
    public short getTotalPercentage() {
        return totalPercentage;
    }
    
    public short getRadiusMin() {
        return radiusMin;
    }
    
    public short getRadiusMax() {
        return radiusMax;
    }
    
    public int getDensityMin() {
        return densityMin;
    }
    
    public int getDensityMax() {
        return densityMax;
    }
    
/*------------------------------------------------------------------------------------------------*/
        
    public void setTotalPercentage(short totalPercentage) { 
        this.totalPercentage = totalPercentage;
    }
    
    public void setRadiusMin(short radiusMin) { 
        this.radiusMin = radiusMin;
    }
    
    public void setRadiusMax(short radiusMax) { 
        this.radiusMax = radiusMax;
    }
    
    public void setDensityMin(int densityMin) { 
        this.densityMin = densityMin;
    }
    
    public void setDensityMax(int densityMax) { 
        this.densityMax = densityMax;
    }

/*------------------------------------------------------------------------------------------------*/
    
    public String toString(){
        return new StringBuffer("Total planeet percentage: ").append(totalPercentage).append("\n")
            .append("Radius minimum : ").append(radiusMin).append("\n")
            .append("Radius maximum : ").append(radiusMax).append("\n")
            .append("Density minimum : ").append(densityMin).append("\n")
            .append("Density maximum : ").append(densityMax).append("\n")
            .toString();
    }
}
