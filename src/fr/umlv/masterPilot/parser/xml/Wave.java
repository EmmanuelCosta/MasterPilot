package fr.umlv.masterPilot.parser.xml;

/**
 * @author Sybille
 * Object containing informations about the xml parsing 
 */
public class Wave {
        private short waveEnemyNumber;
        
        public short getWaveEnemyNumber() {
            return waveEnemyNumber;
        }
        
        public void setWaveEnemyNumber(short waveEnemyNumber) { 
            this.waveEnemyNumber = waveEnemyNumber;
        }

        public String toString(){
            return new StringBuffer("Number of enemy's wave : ")
                .append(waveEnemyNumber)
                .append("\n")
                .toString();
        }
}
