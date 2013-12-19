package fr.umlv.masterPilot.parser.xml;

public class Planet {
    private short totalPercentage;
    private short radiusMin;
    private short radiusMax;
    private short densityMin;
    private short densityMax;
    
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
    
    public short getDensityMin() {
        return densityMin;
    }
    
    public short getDensityMax() {
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
    
    public void setDensityMin(short densityMin) { 
        this.densityMin = densityMin;
    }
    
    public void setDensityMax(short densityMax) { 
        this.densityMax = densityMax;
    }

/*------------------------------------------------------------------------------------------------*/
    
    public String toString(){
        return new StringBuffer("Total enemy's percentage").append(totalPercentage).append(", ")
            .append("Radius minimum : ").append(radiusMin).append(", ")
            .append("Radius maximum : ").append(radiusMax).append(", ")
            .append("Density minimum : ").append(densityMin).append(", ")
            .append("Density maximum : ").append(densityMax).append(", ")
            .toString();
    }
}
