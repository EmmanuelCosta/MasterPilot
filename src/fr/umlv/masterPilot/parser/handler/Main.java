package fr.umlv.masterPilot.parser.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        LevelHandler gestionnaire = null;
        
        // Reading an XML file with a DefaultHandler
        try {
            
            // Get the current directory from which the user work
            StringBuffer accessFileName =  new StringBuffer() ;
            accessFileName.append(System.getProperty("user.dir")).append(File.separator).append("Level1.xml") ; 
            InputStream fichier = new FileInputStream(new File(accessFileName.toString()));
            
            gestionnaire = new LevelHandler();
            gestionnaire.parse(fichier);

        } catch (ParserConfigurationException pce) {
            System.out.println("Erreur de configuration du parseur");
            System.out.println("Lors de l'appel à newSAXParser()");
            
        } catch (SAXException se) {
            System.out.println("Erreur de parsing");
            System.out.println("Lors de l'appel à parse()");
            
        } catch (IOException ioe) {
            System.out.println("Erreur d'entrée/sortie");
            System.out.println("Lors de l'appel à parse()");
        }
        
        /**
         * Here is an example of how you can get the values in the xml document.
         */
        int time = gestionnaire.getTime().getTimer();
        short bombPercentage = gestionnaire.getBomb().getBombPercentage();
        short megaBombPercentage = gestionnaire.getMegaBomb().getMegaBombPercentage();
        short waveNumber = gestionnaire.getWave().getWaveEnemyNumber();
        // this is the same for Enemy and planet.
        System.out.println(time + " " + bombPercentage + " " + megaBombPercentage + " " + waveNumber);
    }
}
