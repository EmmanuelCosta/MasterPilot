package fr.umlv.masterPilot.parser.xml;

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
                .toString();
        }
}
