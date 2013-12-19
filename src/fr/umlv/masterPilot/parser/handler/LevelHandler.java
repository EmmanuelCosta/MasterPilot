package fr.umlv.masterPilot.parser.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.umlv.masterPilot.parser.xml.Bomb;
import fr.umlv.masterPilot.parser.xml.Enemy;
import fr.umlv.masterPilot.parser.xml.Level;
import fr.umlv.masterPilot.parser.xml.Megabomb;
import fr.umlv.masterPilot.parser.xml.Planet;
import fr.umlv.masterPilot.parser.xml.Wave;

public class LevelHandler extends DefaultHandler {
    
//    //résultats de notre parsing>
//    private Bomb bomb;
//    private Megabomb megaBomb;
//    private Enemy enemy;
//    private Wave wave;
//    private Planet planet;
//
//    //flags nous indiquant la position du parseur
//    private boolean inLevel, inBomb, inMegaBomb, inEnemy, inWave, inPlanet;
//
//    //buffer nous permettant de récupérer les données
//    private StringBuffer buffer;
//
//    //détection d'ouverture de balise
//    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
//        if(qName.equals("Level")) {
//            inLevel = true;
//        } else {
//
//            buffer = new StringBuffer();
//
//            switch(qName) {
//                case "Bomb" :
//                    bomb = new Bomb();
//                    inBomb = true;
//                    break;
//
//                case "MegaBomb":
//                    megaBomb = new Megabomb();
//                    inMegaBomb = true;
//                    break;
//
//                case "Wave":
//                    wave = new Wave();
//                    inWave = true;
//                    break;
//
//                case "Enemy":
//                    enemy = new Enemy();
//                    inEnemy = true;
//                    break;
//
//                case "Planet":
//                    planet = new Planet();
//                    inPlanet = true;
//                    break;
//
//                default:
//                    switch(qName) {
//                        case "TotalbombPercentage":
//                            try {
//                                short percentage = Short.parseShort(attributes                                        .getValue("id"));
//                                bomb.setBombPercentage(percentage);
//                            } catch (Exception e) {
//                                throw new SAXException(e);
//                            }
//                            break;
//                        case "TotalMegabombPercentage":
//                            try {
//                                short percentage = Short.parseShort(attributes                                        .getValue("id"));
//                                megaBomb.setMegaBombPercentage(percentage);
//                            } catch (Exception e) {
//                                throw new SAXException(e);
//                            }
//                            break;
//                        case "TotalWaveNumber":
//                            try {
//                                short enemyNumber = Short.parseShort(attributes                                        .getValue("id"));
//                                wave.setWaveEnemyNumber(enemyNumber);
//                            } catch (Exception e) {
//                                throw new SAXException(e);
//                            }
//                            break;
//                        case "TotalEnemyPercentage":
//                            try {
//                                short percentage = Short.parseShort(attributes                                        .getValue("id"));
//                                bomb.setBombPercentage(percentage);
//                            } catch (Exception e) {
//                                throw new SAXException(e);
//                            }
//                            break;
//                        case "TiePercentage":
//                            break;
//                        case "CruiserPercentage":
//                            break;
//                        case "SquadronPercentage":
//                            break;
//                        case "TotalPlanetPercentage":
//                            break;
//                        case "RadiusMin":
//                            break;
//                        case "RadiusMax":
//                            break;
//                        case "DensityMin":
//                            break;
//                        case "DensityMax":
//                            break;
//                    }
//                    break;
//            }
//        }
//    }
//
//    //détection fin de balise
//    public void endElement(String uri, String localName, String qName)
//            throws SAXException{
//        switch(qName) {
//
//        case "Level":
//            inLevels = false;
//            levels.add(level);
//            level = null;
//            inLevel = false;
//            break;
//
//        case "EnemyPercentage":
//            level.setEnemyPercentage(Integer.buffer.toString());
//            buffer = null;
//            inNom = false;
//            break;
//
//        case "PlanetPercentage":
//            personne.setPrenom(buffer.toString());
//            buffer = null;
//            inPrenom = false;
//            break;
//
//        case "WaveNumber":
//            personne.setAdresse(buffer.toString());
//            buffer = null;
//            inAdresse = false;
//            break;
//
//        default:
//            //erreur, on peut lever une exception
//            throw new SAXException("Unknown tag " + qName);
//        }
//    }
//    //détection de caractères
//    public void characters(char[] ch,int start, int length)
//            throws SAXException{
//        String lecture = new String(ch,start,length);
//        if(buffer != null) buffer.append(lecture);
//    }
//    //début du parsing
//    public void startDocument() throws SAXException {
//        System.out.println("Début du parsing");
//    }
//    //fin du parsing
//    public void endDocument() throws SAXException {
//        System.out.println("Fin du parsing");
//        System.out.println("Resultats du parsing");
//        for(Personne p : annuaire){
//            System.out.println(p);
//        }
//    }
}
