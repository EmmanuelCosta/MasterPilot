package fr.umlv.masterPilot.parser.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        LevelHandler gestionnaire = null;
        try {

            // lecture d'un fichier XML avec un DefaultHandler
            InputStream fichier = new FileInputStream(new File("/home/sybille/java/MasterPilot/MasterPilot/Level1.xml"));
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
