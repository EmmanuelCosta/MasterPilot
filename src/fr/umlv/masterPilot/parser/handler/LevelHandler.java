package fr.umlv.masterPilot.parser.handler;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import fr.umlv.masterPilot.parser.xml.Bomb;
import fr.umlv.masterPilot.parser.xml.Enemy;
import fr.umlv.masterPilot.parser.xml.Megabomb;
import fr.umlv.masterPilot.parser.xml.Planet;
import fr.umlv.masterPilot.parser.xml.Wave;

public class LevelHandler extends DefaultHandler {
    
    //résultats de notre parsing>
    private final Bomb bomb;
    private final Megabomb megaBomb;
    private final Enemy enemy;
    private final Wave wave;
    private final Planet planet;

    //flags nous indiquant la position du parseur
    private final boolean inLevel, inBomb, inMegaBomb, inEnemy, inWave, inPlanet;
    private final boolean inMegaBombPercentage, inBombPercentage;
    private final boolean inWaveNumber;
    private final boolean inEnemyPercentage, inTiePercentage, inCruiserPercentage, inSquadronPercentage;
    private final boolean inDensityMax, inDensityMin, inRadiusMax, inRadiusMin, inPlanetPercentage;  

    //buffer nous permettant de récupérer les données
    private StringBuffer buffer;

    //détection d'ouverture de balise
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        if(qName.equals("Level")) {
            inLevel = true;
        } else {

            buffer = new StringBuffer();

            switch(qName) {
                case "Bomb" :
                    bomb = new Bomb();
                    inBomb = true;
                    break;

                case "MegaBomb":
                    megaBomb = new Megabomb();
                    inMegaBomb = true;
                    break;

                case "Wave":
                    wave = new Wave();
                    inWave = true;
                    break;

                case "Enemy":
                    enemy = new Enemy();
                    inEnemy = true;
                    break;

                case "Planet":
                    planet = new Planet();
                    inPlanet = true;
                    break;

                default:
                    switch(qName) {
                        case "TotalbombPercentage":
                            inBombPercentage = true;
                            break;
                            
                        case "TotalMegabombPercentage":
                            inMegaBombPercentage = true;
                            break;
                            
                        case "TotalWaveNumber":
                            inWaveNumber = true;
                            break;
                            
                        case "TotalEnemyPercentage":
                            inEnemyPercentage = true;
                            break;
                            
                        case "TiePercentage":
                            inTiePercentage = true;
                            break;
                            
                        case "CruiserPercentage":
                            inCruiserPercentage = true;
                            break;
                            
                        case "SquadronPercentage":
                            inSquadronPercentage = true;
                            break;
                            
                        case "TotalPlanetPercentage":
                            inPlanetPercentage = true;
                            break;
                            
                        case "RadiusMin":
                            inRadiusMin = true;
                            break;
                            
                        case "RadiusMax":
                            inRadiusMax = true;
                            break;
                            
                        case "DensityMin":
                            inDensityMin = true;
                            break;
                            
                        case "DensityMax":
                            inDensityMax = true;
                            break;
                            
                        default:
                            throw new SAXException("Unknown tag: " + qName);
                    }
                    break;
            }
        }
    }

    //détection fin de balise
    public void endElement(String uri, String localName, String qName)throws SAXException{
        if(qName.equals("Level")) {
            inLevel = false;
        } else {
            buffer = new StringBuffer();

            switch(qName) {
                case "Bomb" :
                    bomb = null;
                    inBomb = false;
                    break;

                case "MegaBomb":
                    megaBomb = null;
                    inMegaBomb = false;
                    break;

                case "Wave":
                    wave = null;
                    inWave = false;
                    break;

                case "Enemy":
                    enemy = null;
                    inEnemy = false;
                    break;

                case "Planet":
                    planet = null;
                    inPlanet = true;
                    break;

                default:
                    switch(qName) {
                        case "TotalbombPercentage":
                            try {
                                short percentage = Short.parseShort(attributes.getValue("id"));                                         .getValue("id"));
                                bomb.setBombPercentage(percentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "TotalMegabombPercentage":
                            try {
                                short percentage = Short.parseShort(attributes.getValue("id"));                                         .getValue("id"));
                                megaBomb.setMegaBombPercentage(percentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "TotalWaveNumber":
                            try {
                                short enemyNumber = Short.parseShort(attributes.getValue("id"));                                         .getValue("id"));
                                wave.setWaveEnemyNumber(enemyNumber);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "TotalEnemyPercentage":
                            try {
                                short percentage = Short.parseShort(attributes.getValue("id"));                                        .getValue("id"));
                                bomb.setBombPercentage(percentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "TiePercentage":
                            try {
                                short tiePercentage = Short.parseShort(attributes.getValue("id"));                                         .getValue("id"));
                                enemy.setTiePercentage(tiePercentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "CruiserPercentage":
                            try {
                                short cruiserPercentage = Short.parseShort(attributes.getValue("id"));                                         .getValue("id"));
                                enemy.setCruiserPercentage(cruiserPercentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "SquadronPercentage":
                            try {
                                short squadronPercentage = Short.parseShort(attributes.getValue("id"));                                         .getValue("id"));
                                enemy.setSquadronPercentage(squadronPercentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "TotalPlanetPercentage":
                            try {
                                short percentage = Short.parseShort(attributes.getValue("id"));                                        .getValue("id"));
                                planet.setTotalPercentage(percentage);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "RadiusMin":
                            try {
                                short radiusmin = Short.parseShort(attributes.getValue("id"));                                        .getValue("id"));
                                planet.setRadiusMin(radiusmin);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "RadiusMax":
                            try {
                                short radiusmax = Short.parseShort(attributes.getValue("id"));                                        .getValue("id"));
                                planet.setRadiusMin(radiusmax);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "DensityMin":
                            try {
                                short densitymin = Short.parseShort(attributes.getValue("id"));                                        .getValue("id"));
                                planet.setRadiusMin(densitymin);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                        case "DensityMax":
                            try {
                                short densitymax = Short.parseShort(attributes.getValue("id"));                                        .getValue("id"));
                                planet.setRadiusMin(densitymax);
                            } catch (Exception e) {
                                throw new SAXException(e);
                            }
                            break;
                            default:
                                
                    }
                    break;
            }
        }
    }
    }
    //détection de caractères
    public void characters(char[] ch,int start, int length)
            throws SAXException{
        String lecture = new String(ch,start,length);
        if(buffer != null) buffer.append(lecture);
    }
    //début du parsing
    public void startDocument() throws SAXException {
        System.out.println("Début du parsing");
    }
    //fin du parsing
    public void endDocument() throws SAXException {
        System.out.println("Fin du parsing");
        System.out.println("Resultats du parsing");
        for(Personne p : annuaire){
            System.out.println(p);
        }
    }
}
