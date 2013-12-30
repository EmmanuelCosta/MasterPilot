package fr.umlv.masterPilot;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.parser.handler.LevelHandler;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.SpaceShipFactory;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.star.StarFactory;
import fr.umlv.masterPilot.world.BombFactory;
import fr.umlv.masterPilot.world.KeyMotionObservable;
import fr.umlv.masterPilot.world.KeyMotionObserver;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import fr.umlv.zen3.ApplicationContext;
import fr.umlv.zen3.KeyboardEvent;
import org.jbox2d.dynamics.Body;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * This is the motor of the game it launch and end the game create and destroy
 * his character Created by emmanuel on 06/12/13.
 */
public class MasterPilotMotor implements KeyMotionObservable {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final List<KeyMotionObserver> observerList = new ArrayList<>();

    private final float timeStep = 1.0f / 6f;
    private final int velocityIterations = 6;
    private final int positionIterations = 3;
    private int gameTiming;

    /**
     * this class contains the main specification of a MasterPilot Session of
     * game like time of the game, number of ennemy by type, number of wave
     */
    private class GameSpec {
        private final int tieNumber;
        private final int cruiserNumber;
        private final int squadronNumber;
        private final int invaderNumber;
        private final int spaceBallNumber;
        private final int waveNumber;
        private final int timer;
        private final int bombPercentage;
        private final int megaBombPercentage;

        private GameSpec(int tieNumber, int cruiserNumber, int squadronNumber,
                         int invaderNumber, int spaceBallNumber, int waveNumber,
                         int timer, int bombPercentage, int megaBombPercentage) {
            this.tieNumber = tieNumber;
            this.cruiserNumber = cruiserNumber;
            this.squadronNumber = squadronNumber;
            this.invaderNumber = invaderNumber;
            this.spaceBallNumber = spaceBallNumber;
            this.waveNumber = waveNumber;
            this.timer = timer;
            this.bombPercentage = bombPercentage;
            this.megaBombPercentage = megaBombPercentage;
        }

        public int getBombPercentage() {
            return bombPercentage;
        }

        public int getMegaBombPercentage() {
            return megaBombPercentage;
        }

        public int getTimer() {
            return timer;
        }

        public int getTieNumber() {
            return tieNumber;
        }

        public int getWaveNumber() {
            return waveNumber;
        }

        public int getSpaceBallNumber() {
            return spaceBallNumber;
        }

        public int getInvaderNumber() {
            return invaderNumber;
        }

        public int getSquadronNumber() {
            return squadronNumber;
        }

        public int getCruiserNumber() {
            return cruiserNumber;
        }
    }

    /**
     * this is use for launching the game from the levelFile given
     * 
     * @param context
     *            : The context which handle a graphic objects for rendreng of
     *            the game
     * @param levelFile
     *            : path to the level file config
     * @param mode
     *            : mode of game { CHEAT OR HARDCORE}
     */
    public void launchGame(ApplicationContext context, String levelFile,
            String mode) {

        /**
         * Load the level.
         */
        final LevelHandler levelParser = new LevelHandler();

        try {
            // Get the current directory from which the user work
            StringBuffer accessFileName = new StringBuffer();
            accessFileName.append(System.getProperty("user.dir"))
                    .append(File.separator).append(levelFile);
            InputStream fichier = new FileInputStream(new File(
                    accessFileName.toString()));
            levelParser.parse(fichier);

        } catch (ParserConfigurationException pce) {
            System.out.println("Parser configuration error");
            System.out.println("Error when calling newSAXParser()");

        } catch (SAXException se) {
            System.out.println("Parsing error");
            System.out.println("Error when calling parse()");

        } catch (IOException ioe) {
            System.out.println("Input/Output error");
            System.out.println("Error when calling parse()");
        }

        context.render(graphics -> {
            MasterPilotWorld masterPilotWorld = initPlateform(graphics);

            populatedWorld(masterPilotWorld, context, levelParser, mode);

        });
    }

    /**
     * this will create the main plateform of the game
     * 
     * @param graphics
     *            : the Graphics2D objects use for rendering purpose
     * @return MasterPiloteWorld
     */
    private MasterPilotWorld initPlateform(Graphics2D graphics) {
        // Create Master Pilote
        MasterPilotWorld mp = new MasterPilotWorld(graphics);
        mp.initPlateForm(WIDTH, HEIGHT);

        // SET REFERENTIEL
        mp.setLandMark(WIDTH / 2, HEIGHT / 2);

        return mp;
    }

    /**
     * call this to populate the world
     * 
     * @param masterPilotWorld
     * @param context
     */

    private void populatedWorld(MasterPilotWorld masterPilotWorld,
            ApplicationContext context, LevelHandler levelParser, String mode) {

        /**
         * Get datas from the xml.
         */

        // Data about the timer
        int timer = levelParser.getTime().getTimer();

        // Data about wave
        short waveNumber = levelParser.getWave().getWaveEnemyNumber();

        // Datas about planets.
        short planetPercentage = levelParser.getPlanet().getTotalPercentage();

        // Datas about enemies.
        int tieNumber = levelParser.getEnemy().getTieNumber();
        int cruiserNumber = levelParser.getEnemy().getcruiserNumber();
        int squadronNumber = levelParser.getEnemy().getSquadronNumber();
        int invaderNumber = levelParser.getEnemy().getInvaderNumber();
        int spaceBallNumber = levelParser.getEnemy().getSpaceBallNumber();

        int bombPercentage = levelParser.getBomb().getBombPercentage();
        short megaBombPercentage = levelParser.getMegaBomb().getMegaBombPercentage();
        /**
         * Put some planets
         */
        createRandomStar(masterPilotWorld, planetPercentage); 

        /**
         * Initialize the game mode.
         */
        SpaceShipFactory factory = new SpaceShipFactory(masterPilotWorld);
        
        Hero h = null;
        switch (mode) {
        case "-h":
            h = factory.createHero(0, 0, MasterPilotWorld.MODE.HARDCORE);
            break;

        case "-c":
            h = factory.createHero(0, 0, MasterPilotWorld.MODE.CHEAT);
            break;

        default:
            throw new IllegalArgumentException(
                    "The specified argument for the game mode is not valid.\n Please enter -c for the cheat mode. \n Or enter -h for the hardcore mode.");
        }

        this.addObserver(h);

        GameSpec gameSpec = new GameSpec(tieNumber, cruiserNumber,
                squadronNumber, invaderNumber, spaceBallNumber, waveNumber,
                timer, bombPercentage, megaBombPercentage);
        run(masterPilotWorld, context, gameSpec);
    }

    /**
     * this will generate a certain number of Star according to the given
     * percentage
     * 
     * @param masterPilotWorld
     * @param percentage
     */
    private void createRandomStar(MasterPilotWorld masterPilotWorld,
            int percentage) {
        StarFactory startFactory = new StarFactory(masterPilotWorld);

        /**
         * caculate number of star
         */
        int pc = (int) ((0.08 * percentage)) + 1;
        int tc = pc;
        int tour = 0;

        // masterPilotWorld.addToBombManager(empBomb.getBody(), empBomb);
        for (int i = -(WIDTH / 2) * 10; i <= 0; i = i + 280) {
            if (tour % 3 == 0) {
                tc = pc;
            }

            for (int p = (HEIGHT / 2) * 10; p >= 0; p = p - 180) {
                int rInterval = 2 + (int) (Math.random() * ((pc - 2) + 1));

                if (rInterval % pc == 0 && tc >= 0) {
                    if (Math.abs(i) > 100 || Math.abs(p) > 100)
                        startFactory.createStar(i, p, Color.yellow);
                    tc--;
                }
            }
            tour++;
        }

        tour = 0;
        for (int i = -(WIDTH / 2) * 10; i <= 0; i = i + 280) {
            if (tour % 3 == 0) {
                tc = pc;
            }

            for (int p = -(HEIGHT / 2) * 10; p <= 0; p = p + 180) {
                int rInterval = 2 + (int) (Math.random() * ((pc - 2) + 1));
                if (rInterval % pc == 0 && tc >= 0) {
                    if (Math.abs(i) > 100 || Math.abs(p) > 100)
                        startFactory.createStar(i, p, Color.yellow);
                    tc--;
                }
            }
            tour++;
        }
        tour = 0;
        for (int i = 0; i <= (WIDTH / 2) * 10; i = i + 280) {

            if (tour % 3 == 0) {
                tc = pc;
            }
            for (int p = 0; p >= -(HEIGHT / 2) * 10; p = p - 180) {
                int rInterval = 2 + (int) (Math.random() * ((pc - 2) + 1));
                if (rInterval % pc == 0 && tc >= 0) {
                    if (Math.abs(i) > 100 || Math.abs(p) > 100)
                        startFactory.createStar(i, p, Color.yellow);
                    tc--;
                }
            }
            tour++;
        }
        tour = 0;
        for (int i = 0; i <= (WIDTH / 2) * 10; i = i + 280) {

            if (tour % 3 == 0) {
                tc = pc;

            }
            for (int p = (HEIGHT / 2) * 10; p >= 0; p = p - 180) {
                int rInterval = 2 + (int) (Math.random() * ((pc - 2) + 1));

                if (rInterval % pc == 0 && tc >= 0) {
                    if (Math.abs(i) > 100 || Math.abs(p) > 100)
                        startFactory.createStar(i, p, Color.yellow);
                    tc--;
                }
            }

            tour++;
        }

        System.out.println("THIS MAP CONTAINS "
                + masterPilotWorld.getStarList().size() + "  star");
    }

    /**
     * main loop of the game
     * 
     * 
     * 
     * @param masterPilotWorld
     *            : This is the masterpilot world
     * @param context
     */
    private void run(MasterPilotWorld masterPilotWorld,
            ApplicationContext context, GameSpec gameSpec) {

        long beforeTime, afterTime, timeDiff, sleepTime;

        beforeTime = System.nanoTime();

        Timer timer = new Timer();

        gameTiming = gameSpec.getTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                gameTiming--;

            }
        }, 1000, 1000);

        int wave = gameSpec.getWaveNumber();
        wave--;
        createEnnemyWave(masterPilotWorld, gameSpec.getTieNumber(),
                gameSpec.getCruiserNumber(), gameSpec.getSquadronNumber(),
                gameSpec.getInvaderNumber(), gameSpec.getSpaceBallNumber());

        int lastNumber=masterPilotWorld.getEnemyList().size();

        for (;;) {

            masterPilotWorld.getWorld().step(timeStep, velocityIterations,
                    positionIterations);

            destroyCharacter(masterPilotWorld);

            if(lastNumber > masterPilotWorld.getEnemyList().size() && wave ==1){
                generateBomb(masterPilotWorld,gameSpec.getBombPercentage(),"BOMB");
                generateBomb(masterPilotWorld,gameSpec.getMegaBombPercentage(),"MEGABOMB");
                lastNumber=masterPilotWorld.getEnemyList().size();
            }


            KeyboardEvent keyEvent = context.pollKeyboard();

            /**
             * this for notify the hero that event have been find
             */

            if (keyEvent != null) {
                this.notifyObserver(keyEvent);
            }

            context.render(graphics -> {
                masterPilotWorld.repaint(WIDTH, HEIGHT);
                masterPilotWorld.draw();

                masterPilotWorld.drawFrameworkClock(gameTiming / 3600,
                        (gameTiming % 3600) / 60, (gameTiming % 3600) % 60);

                Body hero = masterPilotWorld.getBodyHero();

                // use this to center view place it in proper place
                masterPilotWorld.setCamera(hero.getPosition());

            });

            proccessManager(masterPilotWorld.getEnemyList(),
                    masterPilotWorld.getHero());
            afterTime = System.nanoTime();

            timeDiff = afterTime - beforeTime;
            sleepTime = (1000000000 / 60 - timeDiff) / 1000000;
            if (sleepTime > 0) {
                try {

                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                }
            }

            if (gameTiming < 0) {
                masterPilotWorld.drawFrameworkEnd(false, WIDTH / 2, HEIGHT / 2);
                return;
            } else if (masterPilotWorld.getEnemyList().isEmpty()) {
                // If there is wave again
                // Generate enemies.
                if (wave > 0) {
                    createEnnemyWave(masterPilotWorld, gameSpec.getTieNumber(),
                            gameSpec.getCruiserNumber(),
                            gameSpec.getSquadronNumber(),
                            gameSpec.getInvaderNumber(),
                            gameSpec.getSpaceBallNumber());

                    wave--;
                } else {

                    // If there is no waves again
                    // YOU WIN
                    masterPilotWorld.drawFrameworkEnd(true, WIDTH / 2,
                            HEIGHT / 2);
                    return;
                }
            }
            beforeTime = System.nanoTime();
        }
    }

    /**
     * will destroy the enemy in the world jbox2d that were destroyed in the
     * game
     * 
     * @param masterPilotWorld
     */
    private void destroyCharacter(MasterPilotWorld masterPilotWorld) {
        List<Body> destroyBody = masterPilotWorld.getDestroyBody();
        Iterator<Body> iterator = destroyBody.iterator();

        while (iterator.hasNext()) {
            Body next = iterator.next();

            iterator.remove();
            masterPilotWorld.getWorld().destroyBody(next);
        }

    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    @Override
    public void addObserver(KeyMotionObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void notifyObserver(KeyboardEvent keyEvent) {
        for (KeyMotionObserver k : observerList) {
            k.onKeyPressed(keyEvent);
        }
    }

    @Override
    public void delObserver(KeyMotionObserver observer) {
        observerList.remove(observer);
    }

    private void proccessManager(List<SpaceShip> spaceShips, Hero hero) {
        for (SpaceShip space : spaceShips) {
            space.doMove();
        }
    }

    /**
     * this will generate a bomb accornding to the given percentage
     *
     * @param masterPilotWorld
     * @param bombPercentage
     * @param type
     */
    public void generateBomb(MasterPilotWorld masterPilotWorld,int bombPercentage,String type){
        Body bodyHero = masterPilotWorld.getBodyHero();

        int x = (int) bodyHero.getPosition().x;
        int y = (int) bodyHero.getPosition().y;

        int rand = new Random().nextInt();

        rand= Math.abs(rand)%100;

        BombFactory bombFactory = new BombFactory(masterPilotWorld);

        if(rand <= bombPercentage){

            switch (type){
                case "BOMB":
                    bombFactory.createBomb(x+200,y-50, Bomb.BombType.BOMB);
                    break;
                case "MEGABOMB":
                    bombFactory.createBomb(x-350,y+50, Bomb.BombType.MEGABOMB);
                    break;
                default :
                    break;
            }
        }
    }
    /**
     * this will create in the world ennemies accroding to the given ennemyTab
     * each indices of tab concern a specific type of ennemy in the world
     * 
     * look at the correlation table below : [0] : TIE [1]: CRUISER [2] :
     * SQUADRON [3] : INVADER [4] : SPACEBALL
     * 
     * @param masterPilotWorld
     *            the world of the game
     * @param ennemyTab
     *            number of attempt ennemy in the game per wave
     */
    public void createEnnemyWave(MasterPilotWorld masterPilotWorld,
            Integer... ennemyTab) {
        Body bodyHero = masterPilotWorld.getBodyHero();
        Hero hero = masterPilotWorld.getHero();
        int x = (int) bodyHero.getPosition().x;
        int y = (int) bodyHero.getPosition().y;
        int compt = 0;
        int init_x = 0;
        int init_y = 0;
        int direction;
        String type;
        SpaceShipFactory enemyFactory = new SpaceShipFactory(masterPilotWorld);

        for (int i : ennemyTab) {
            switch (compt) {
            case 0:
                // TIE
                init_x = x + 300;
                init_y = y + 300;

                direction = 0;
                type = "TIE";
                while (i > 0) {
                    if (direction % 4 == 0) {
                        enemyFactory.createEnemy(type, init_x + 50 * i, init_y,
                                hero);
                    } else if (direction % 4 == 1) {
                        enemyFactory.createEnemy(type, init_x - 50 * i, init_y,
                                hero);
                    } else if (direction % 4 == 2) {
                        enemyFactory.createEnemy(type, init_x - 50 * i,
                                init_y + 50, hero);
                    } else {
                        enemyFactory.createEnemy(type, init_x + 50 * i,
                                init_y - 50, hero);
                    }
                    i--;
                    direction++;
                }

                break;
            
            case 1:
                // CRUISER
                init_x = x + 300;
                init_y = y + 300;

                direction = 0;
                type = "CRUISER";
                while (i > 0) {
                    if (direction % 4 == 0) {
                        enemyFactory.createEnemy(type, init_x + 70 * i, init_y,
                                hero);
                    } else if (direction % 4 == 1) {
                        enemyFactory.createEnemy(type, init_x - 70 * i,
                                -init_y, hero);
                    } else if (direction % 4 == 2) {
                        enemyFactory.createEnemy(type, init_x - 70 * i,
                                -init_y + 70, hero);
                    } else {
                        enemyFactory.createEnemy(type, init_x + 70 * i,
                                init_y - 70, hero);
                    }
                    i--;
                    direction++;
                }
                break;
            
            case 2:
                init_x = x + 200;
                init_y = y + 250;

                direction = 0;
                type = "SQUADRON";
                while (i > 0) {
                    if (direction % 4 == 0) {
                        enemyFactory.createEnemy(type, init_x + 30 * i, init_y,
                                hero);
                    } else if (direction % 4 == 1) {
                        enemyFactory.createEnemy(type, init_x - 30 * i,
                                -init_y, hero);
                    } else if (direction % 4 == 2) {
                        enemyFactory.createEnemy(type, init_x - 30 * i,
                                -init_y + 30, hero);
                    } else {
                        enemyFactory.createEnemy(type, init_x + 30 * i,
                                init_y - 30, hero);
                    }
                    i--;
                    direction++;
                }
                break;
           
            case 3:
                init_x = x + 200;
                init_y = y + 250;

                direction = 0;
                type = "INVADER";
                while (i > 0) {
                    if (direction % 4 == 0) {
                        enemyFactory.createEnemy(type, init_x + 150 * i,
                                init_y, hero);
                    } else if (direction % 4 == 1) {
                        enemyFactory.createEnemy(type, init_x - 150 * i,
                                -init_y, hero);
                    } else if (direction % 4 == 2) {
                        enemyFactory.createEnemy(type, init_x - 150 * i,
                                -init_y + 100, hero);
                    } else {
                        enemyFactory.createEnemy(type, init_x + 150 * i,
                                init_y - 100, hero);
                    }
                    i--;
                    direction++;
                }
                break;
            
            case 4:
                init_x = x + 200;
                init_y = y + 250;

                direction = 0;
                type = "SPACEBALL";
                while (i > 0) {
                    if (direction % 4 == 0) {
                        enemyFactory.createEnemy(type, init_x + 100 * i,
                                init_y, hero);
                    } else if (direction % 4 == 1) {
                        enemyFactory.createEnemy(type, init_x - 100 * i,
                                -init_y, hero);
                    } else if (direction % 4 == 2) {
                        enemyFactory.createEnemy(type, init_x - 100 * i,
                                -init_y + 80, hero);
                    } else {
                        enemyFactory.createEnemy(type, init_x + 100 * i,
                                init_y - 80, hero);
                    }
                    i--;
                    direction++;
                }
                break;
            default:
                break;
            }
            compt++;
        }
    }


}
