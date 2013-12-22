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
    private boolean inEnemyNumber, inTieNumber, inCruiserNumber,
            inSquadronNumber;
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
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Bomb":
            bomb = new Bomb();
            inBomb = true;
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "MegaBomb":
            megaBomb = new Megabomb();
            inMegaBomb = true;
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Wave":
            wave = new Wave();
            inWave = true;
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Enemy":
            enemy = new Enemy();
            inEnemy = true;
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Planet":
            planet = new Planet();
            inPlanet = true;
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
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
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TotalBombPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                bomb.setBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            inBombPercentage = true;
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TotalMegabombPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                megaBomb.setMegaBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            inMegaBombPercentage = true;
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TotalWaveNumber":
            try {
                short enemyNumber = Short.parseShort(attributes.getValue("id"));
                wave.setWaveEnemyNumber(enemyNumber);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TotalEnemyNumber":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                enemy.setTotalEnemyNumber(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TieNumber":
            try {
                short tiePercentage = Short.parseShort(attributes.getValue("id"));
                enemy.setTieNumber(tiePercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "CruiserNumber":
            try {
                short cruiserPercentage = Short.parseShort(attributes.getValue("id"));
                enemy.setCruiserNumber(cruiserPercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "SquadronNumber":
            try {
                short squadronPercentage = Short.parseShort(attributes.getValue("id"));
                enemy.setSquadronNumber(squadronPercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;
            
        case "TotalPlanetPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                planet.setTotalPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "RadiusMin":
            try {
                short radiusmin = Short.parseShort(attributes.getValue("id"));
                planet.setRadiusMin(radiusmin);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;
            
        case "RadiusMax":
            try {
                short radiusmax = Short.parseShort(attributes.getValue("id"));
                planet.setRadiusMax(radiusmax);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "DensityMin":
            try {
                int densitymin = Short.parseShort(attributes.getValue("id"));
                planet.setDensityMin(densitymin);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "DensityMax":
            try {
                int densitymax = Integer.parseInt(attributes.getValue("id"));
                planet.setDensityMax(densitymax);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
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
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Bomb":
            //bomb = null;
            inBomb = false;
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "MegaBomb":
            //megaBomb = null;
            inMegaBomb = false;
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Wave":
            //wave = null;
            inWave = false;
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Enemy":
            //enemy = null;
            inEnemy = false;
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Planet":
            //planet = null;
            inPlanet = false;
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Timer":
            //time = null;
            inTimer = false;
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "TotalBombPercentage":
            inBombPercentage = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalMegabombPercentage":
            inMegaBombPercentage = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalWaveNumber":
            inWaveNumber = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalEnemyNumber":
            inEnemyNumber = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TieNumber":
            inTieNumber = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "CruiserNumber":
            inCruiserNumber = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "SquadronNumber":
            inSquadronNumber = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalPlanetPercentage":
            inPlanetPercentage = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "RadiusMin":
            inRadiusMin = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "RadiusMax":
            inRadiusMax = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "DensityMin":
            inDensityMin = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "DensityMax":
            inDensityMax = false;
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
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

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    public Megabomb getMegaBomb() {
        return megaBomb;
    }

    public void setMegaBomb(Megabomb megaBomb) {
        this.megaBomb = megaBomb;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Wave getWave() {
        return wave;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Timer getTime() {
        return time;
    }

    public void setTime(Timer time) {
        this.time = time;
    }    
}

