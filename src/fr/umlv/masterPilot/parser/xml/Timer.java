package fr.umlv.masterPilot.parser.xml;

/**
 * @author Sybille
 * Object containing informations about the xml parsing 
 */
public class Timer {
        private int time;
        
        public int getTimer() {
            return time;
        }
        
        public void setTimer(int time) { 
            this.time = time;
        }

        public String toString(){
            return new StringBuffer("Timer : ")
                .append(time)
                .append("\n")
                .toString();
        }
}
