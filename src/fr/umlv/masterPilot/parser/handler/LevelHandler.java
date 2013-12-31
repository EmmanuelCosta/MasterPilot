package fr.umlv.masterPilot.parser.handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
/**
 * @author Sybille
 * Handler that allows to parse the xml file. 
 */

public class LevelHandler extends DefaultHandler {

    // Results of our parsing.
    private Bomb bomb;
    private Megabomb megaBomb;
    private Enemy enemy;
    private Wave wave;
    private Planet planet;
    private Timer time;

    public LevelHandler() {
        super();
    }

    public void parse(InputStream input) throws ParserConfigurationException,
            SAXException, IOException {
        
        // Creation of the SAX parser.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();

        //Launch the parsing giving the stream and the "handler"
        parser.parse(input, this);
    }

    public void parse(String filename) throws FileNotFoundException,
            ParserConfigurationException, SAXException, IOException {
        parse(new FileInputStream(filename));
    }

    // Detect the beginning of a tag.
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        switch (localName) {
        case "Level":
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Bomb":
            bomb = new Bomb();
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "MegaBomb":
            megaBomb = new Megabomb();
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Wave":
            wave = new Wave();
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Enemy":
            enemy = new Enemy();
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "Planet":
            planet = new Planet();
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
            System.out.println("Start: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TotalBombPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                bomb.setBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TotalMegabombPercentage":
            try {
                short percentage = Short.parseShort(attributes.getValue("id"));
                megaBomb.setMegaBombPercentage(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
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
                int percentage = Integer.parseInt(attributes.getValue("id"));
                enemy.setTotalEnemyNumber(percentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "TieNumber":
            try {
                int tiePercentage = Integer.parseInt(attributes.getValue("id"));
                enemy.setTieNumber(tiePercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "CruiserNumber":
            try {
                int cruiserPercentage = Integer.parseInt(attributes.getValue("id"));
                enemy.setCruiserNumber(cruiserPercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;

        case "SquadronNumber":
            try {
                int squadronPercentage = Integer.parseInt(attributes.getValue("id"));
                enemy.setSquadronNumber(squadronPercentage);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;
            
        case "SpaceBallNumber":
            try {
                int spaceBallNumber = Integer.parseInt(attributes.getValue("id"));
                enemy.setSpaceBallNumber(spaceBallNumber);
            } catch (Exception e) {
                throw new SAXException(e);
            }
            System.out.println("\tStart: " + "<" +localName + ">" + attributes.getValue("id"));
            break;
            
        case "InvaderNumber":
            try {
                short invaderNumber = Short.parseShort(attributes.getValue("id"));
                enemy.setInvaderNumber(invaderNumber);
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

    // Detect the end of a tag
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        
        switch (localName) {
        case "Level":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Bomb":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "MegaBomb":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Wave":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Enemy":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Planet":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "Timer":
            System.out.println("End: "+ "<\\" + localName + ">\n");
            break;

        case "TotalBombPercentage":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalMegabombPercentage":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalWaveNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalEnemyNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TieNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "CruiserNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "SquadronNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;
            
        case "SpaceBallNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;
            
        case "InvaderNumber":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "TotalPlanetPercentage":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "RadiusMin":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "RadiusMax":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "DensityMin":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        case "DensityMax":
            System.out.println("\tEnd: "+ "<\\" + localName + ">\n");
            break;

        default:
            throw new SAXException("Unknown tag: " + localName);
        }
        
    }

    // Begin parsing
    public void startDocument() throws SAXException {
        System.out.println("Begining of parsing");
    }

    // End parsing
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

