package fr.umlv.masterPilot.parser.xml;

public class Level {

    private int id;
    private short enemyPercentage, planetPercentage, waveEnemyNumber;

    public Level(){}

/*------------------------------------------------------------------------------------------------*/
    
    public int getId() {
        return id;
    }
    
    public short getEnemyPercentage() {
        return enemyPercentage;
    }
    
    public short getPlanetPercentage() {
        return planetPercentage;
    }
    
    public short getWaveEnemyNumber() {
        return waveEnemyNumber;
    }
  
    
/*------------------------------------------------------------------------------------------------*/
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setEnemyPercentage(short enemyPercentage) {
        this.enemyPercentage = enemyPercentage;
    }
    
    public void setPlanetPercentage(short planetPercentage) { 
        this.planetPercentage = planetPercentage;
    }
    
    public void setWaveEnemyNumber(short waveEnemyNumber) { 
        this.waveEnemyNumber = waveEnemyNumber;
    }

    public String toString(){
        return new StringBuffer("Enemy percentage : ").append(enemyPercentage).append(", ")
            .append("Planet Percentage : ").append(planetPercentage).append(", ")
            .append("Number of enemy's wave : ").append(waveEnemyNumber)
            .toString();
    }
    
}
