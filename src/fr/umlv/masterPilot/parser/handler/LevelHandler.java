package fr.umlv.masterPilot.parser.handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.umlv.masterPilot.parser.xml.Bomb;
import fr.umlv.masterPilot.parser.xml.Enemy;
import fr.umlv.masterPilot.parser.xml.Megabomb;
import fr.umlv.masterPilot.parser.xml.Planet;
import fr.umlv.masterPilot.parser.xml.Timer;
import fr.umlv.masterPilot.parser.xml.Wave;

public class LevelHandler extends DefaultHandler {

    // résultats de notre parsing>
    private Bomb bomb;
    private Megabomb megaBomb;
    private Enemy enemy;
    private Wave wave;
    private Planet planet;
    private Timer time;

    // flags nous indiquant la position du parseur
    private boolean inLevel, inBomb, inMegaBomb, inEnemy, inWave, inPlanet,
            inTimer;
    private boolean inMegaBombPercentage, inBombPercentage;
    private boolean inWaveNumber;
    private boolean inEnemyPercentage, inTiePercentage, inCruiserPercentage,
            inSquadronPercentage;
    private boolean inDensityMax, inDensityMin, inRadiusMax, inRadiusMin,
            inPlanetPercentage;

    // buffer nous permettant de récupérer les données
    private StringBuffer buffer;

    public LevelHandler() {
        super();
    }

    public void parse(InputStream input) throws ParserConfigurationException,
            SAXException, IOException {
        // creation du parser SAX
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();

        // lancement du parsing en précisant le flux et le "handler"
        // l'instance qui implémente les méthodes appelées par le parser
        // dans notre cas: this
        parser.parse(input, this);
    }

    public void parse(String filename) throws FileNotFoundException,
            ParserConfigurationException, SAXException, IOException {
        parse(new FileInputStream(filename));
    }

    // détection d'ouverture de balise
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        switch (localName) {
        case "Level":
            inLevel = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "Bomb":
            bomb = new Bomb();
            inBomb = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "MegaBomb":
            megaBomb = new Megabomb();
            inMegaBomb = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "Wave":
            wave = new Wave();
            inWave = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "Enemy":
            enemy = new Enemy();
            inEnemy = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "Planet":
            planet = new Planet();
            inPlanet = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;
            
        case "Timer":
            time = new Timer();
            try {
                int chrono = Short.parseShort(attributes.getValue("id"));
                time.setTimer(chrono);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            inTimer = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "TotalBombPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                bomb.setBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            inBombPercentage = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "TotalMegabombPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                megaBomb.setMegaBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            inMegaBombPercentage = true;
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "TotalWaveNumber":
            try {
                short enemyNumber = Short.parseShort(attributes.getValue("id"));
                wave.setWaveEnemyNumber(enemyNumber);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "TotalEnemyPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                bomb.setBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "TiePercentage":
            try {
                short tiePercentage = Short.parseShort(attributes.getValue("id"));
                enemy.setTiePercentage(tiePercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "CruiserPercentage":
            try {
                short cruiserPercentage = Short.parseShort(attributes.getValue("id"));
                enemy.setCruiserPercentage(cruiserPercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "SquadronPercentage":
            try {
                short squadronPercentage = Short.parseShort(attributes.getValue("id"));
                enemy.setSquadronPercentage(squadronPercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;
            
        case "TotalPlanetPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                planet.setTotalPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "RadiusMin":
            try {
                short radiusmin = Short.parseShort(attributes.getValue("id"));
                planet.setRadiusMin(radiusmin);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;
            
        case "RadiusMax":
            try {
                short radiusmax = Short.parseShort(attributes.getValue("id"));
                planet.setRadiusMin(radiusmax);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "DensityMin":
            try {
                short densitymin = Short.parseShort(attributes.getValue("id"));
                planet.setRadiusMin(densitymin);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;

        case "DensityMax":
            try {
                short densitymax = Short.parseShort(attributes.getValue("id"));
                planet.setRadiusMin(densitymax);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("Start: "+localName + " " + attributes.getValue("id"));
            break;
            
        default:
            throw new SAXException();

        }
    }

    // détection fin de balise
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        
        switch (localName) {
        case "Level":
            inLevel = false;
            System.out.println("End: "+localName);
            break;

        case "Bomb":
            //bomb = null;
            inBomb = false;
            System.out.println("End: "+localName);
            break;

        case "MegaBomb":
            //megaBomb = null;
            inMegaBomb = false;
            System.out.println("End: "+localName);
            break;

        case "Wave":
            //wave = null;
            inWave = false;
            System.out.println("End: "+localName);
            break;

        case "Enemy":
            //enemy = null;
            inEnemy = false;
            System.out.println("End: "+localName);
            break;

        case "Planet":
            //planet = null;
            inPlanet = false;
            System.out.println("End: "+localName);
            break;

        case "Timer":
            //time = null;
            inTimer = false;
            System.out.println("End: "+localName);
            break;

        case "TotalBombPercentage":
            inBombPercentage = false;
            System.out.println("End: "+localName);
            break;

        case "TotalMegabombPercentage":
            inMegaBombPercentage = false;
            System.out.println("End: "+localName);
            break;

        case "TotalWaveNumber":
            inWaveNumber = false;
            System.out.println("End: "+localName);
            break;

        case "TotalEnemyPercentage":
            inEnemyPercentage = false;
            System.out.println("End: "+localName);
            break;

        case "TiePercentage":
            inTiePercentage = false;
            System.out.println("End: "+localName);
            break;

        case "CruiserPercentage":
            inCruiserPercentage = false;
            System.out.println("End: "+localName);
            break;

        case "SquadronPercentage":
            inSquadronPercentage = false;
            System.out.println("End: "+localName);
            break;

        case "TotalPlanetPercentage":
            inPlanetPercentage = false;
            System.out.println("End: "+localName);
            break;

        case "RadiusMin":
            inRadiusMin = false;
            System.out.println("End: "+localName);
            break;

        case "RadiusMax":
            inRadiusMax = false;
            System.out.println("End: "+localName);
            break;

        case "DensityMin":
            inDensityMin = false;
            System.out.println("End: "+localName);
            break;

        case "DensityMax":
            inDensityMax = false;
            System.out.println("End: "+localName);
            break;

        default:
            throw new SAXException("Unknown tag: " + localName);
        }
        
    }

    // début du parsing
    public void startDocument() throws SAXException {
        System.out.println("Beginning of parsing");
    }

    // fin du parsing
    public void endDocument() throws SAXException {
        System.out.println("End of parsing");
        System.out.println("Result of parsing");

        System.out.println(bomb.toString());
        System.out.println(megaBomb.toString());
        System.out.println(enemy.toString());
        System.out.println(wave.toString());
        System.out.println(planet.toString());
        System.out.println(time.toString());
    }
}
