package fr.umlv.masterPilot;


import fr.umlv.masterPilot.parser.handler.LevelHandler;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.SpaceshipFactory;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.star.Star;
import fr.umlv.masterPilot.star.StarFactory;
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
 * This is the motor of the game
 * it launch and end  the game
 * create and destroy his character
 * Created by emmanuel on 06/12/13.
 */
public class MasterPilotMotor implements KeyMotionObservable {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    // OBSERVER LIST
    private final List<KeyMotionObserver> observerList = new ArrayList<>();

    private final float timeStep = 1.0f / 6f;
    private final int velocityIterations = 6;
    private final int positionIterations = 3;
    private int gameTiming;

    /**
     * this will launch the game
     * @param context
     * @param levelFile this is a path to the xml level config
     */
    public void launchGame(ApplicationContext context, String levelFile) {

        /**
         * Load the level.
         */
        final LevelHandler levelParser = new LevelHandler();
        ;
        try {
            // Get the current directory from which the user work
            StringBuffer accessFileName = new StringBuffer();
            accessFileName.append(System.getProperty("user.dir")).append(File.separator).append(levelFile);
            InputStream fichier = new FileInputStream(new File(accessFileName.toString()));
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
            gameTiming = Integer.valueOf("500");

            populatedWorld(masterPilotWorld, context, levelParser);
            run(masterPilotWorld, context);
        });
    }

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
                                ApplicationContext context, LevelHandler levelParser) {

        /**
         * Get datas from the xml.
         */

        // Data about the timer
        int timer = levelParser.getTime().getTimer();

        // Data about wave
        short waveNumber = levelParser.getWave().getWaveEnemyNumber();

        // Datas about bombs and megaBombs.
        short bombPercentage = levelParser.getBomb().getBombPercentage();
        short megaBombPercentage = levelParser.getMegaBomb().getMegaBombPercentage();

        // Datas about planets.
        short planetPercentage = levelParser.getPlanet().getTotalPercentage();
        short radiusMin = levelParser.getPlanet().getRadiusMin();
        short radiusMax = levelParser.getPlanet().getRadiusMax();
        int densityMin = levelParser.getPlanet().getDensityMin();
        int densityMax = levelParser.getPlanet().getDensityMax();

        // Datas about enemies.
        int enemyNumber = levelParser.getEnemy().getTotalEnemyNumber();
        int tieNumber = levelParser.getEnemy().getTieNumber();
        int cruiserNumber = levelParser.getEnemy().getcruiserNumber();
        int squadronNumber = levelParser.getEnemy().getSquadronNumber();
        int invaderNumber = levelParser.getEnemy().getInvaderNumber();
        int spaceBallNumber = levelParser.getEnemy().getSpaceBallNumber();

        /**
         * Get number from the datas percentages.
         */
        int minimum = 1;
        int maximum = 100;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i;

        // About bombs and megabombs        
        i = rn.nextInt() % n;
        int bombNumber = bombPercentage * (minimum + i) / 100;
        i = rn.nextInt() % n;
        int megaBombNumber = megaBombPercentage * (minimum + i) / 100;

        // About the planets
        minimum = 1;
        maximum = 8; // because we can't have more than 8 planet on a surface.
        i = rn.nextInt() % n;

        int planetNumber = planetPercentage * (minimum + i) / 100;

        minimum = radiusMin;
        maximum = radiusMax;
        i = rn.nextInt() % n;
        int radius = minimum + i;

        minimum = densityMin;
        maximum = densityMax;
        i = rn.nextInt() % n;
        int density = minimum + i;

        /**
         * Create planets and add the planets to the manager
         */

        /**
         * Put some planets
         */
        createRandomStar(masterPilotWorld, planetPercentage);
        /**
         * Put enemies 's first wave.
         */


        SpaceshipFactory factory = new SpaceshipFactory(masterPilotWorld);
        Hero h = factory.createHero(0, 0, MasterPilotWorld.MODE.CHEAT);

        this.addObserver(h);


    }

    /**
     * this will generate a certain number of Star according to the given percentage
     *
     * @param masterPilotWorld
     * @param percentage
     */
    private void createRandomStar(MasterPilotWorld masterPilotWorld, int percentage) {
        StarFactory startFactory = new StarFactory(masterPilotWorld);
/**
 * caculate number of star
 */


        int pc = (int) ((0.08 * percentage)) + 1;


        int tc = pc;


        int tour = 0;


        for (int i = -(WIDTH / 2) * 10; i <= 0; i = i + 280) {

            if (tour % 3 == 0) {
                tc = pc;

            }

            for (int p = (HEIGHT / 2) * 10; p >= 0; p = p - 180) {
                int rInterval = 2 + (int) (Math.random() * ((pc - 2) + 1));


                if (rInterval % pc == 0 && tc >= 0) {
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
                    startFactory.createStar(i, p, Color.yellow);
                    tc--;

                }


            }

            tour++;
        }

        System.out.println("THIS MAP CONTAINS " + masterPilotWorld.getStarList().size() + "  star");
    }

    /**
     * main loop of the game
     *
     *
     * @param masterPilotWorld : This is the masterpilote world
     * @param context
     */
    private void run(MasterPilotWorld masterPilotWorld,
                     ApplicationContext context) {
        long beforeTime, afterTime, timeDiff, sleepTime;

        beforeTime = System.nanoTime();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                gameTiming--;

            }
        }, 1000, 1000);


        for (; ; ) {

            masterPilotWorld.getWorld().step(timeStep, velocityIterations,
                    positionIterations);

            destroyCharacter(masterPilotWorld);

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

                // TODO use this to center view place it in proper place
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
                masterPilotWorld.drawFrameworkEnd(true, WIDTH / 2, HEIGHT / 2);
                return;
            }
            beforeTime = System.nanoTime();
        }
    }

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


    private void generateEnemies(int tieNumber, int cruiserNumber,
                                 int squadronNumber, int invaderNumber,
                                 int spaceBallNumber,
                                 Hero hero,
                                 MasterPilotWorld masterPilotWorld) {
        int number = 0;

        /**
         * Generate x position
         */
        float x_min = hero.getBody().getPosition().x - 450;
        float x_max = hero.getBody().getPosition().x + 450;
        Random x_rand = new Random();
        float x_mod = x_max - x_min + 1;
        float x_pos;

        /**
         * Generate y position
         */
        float y_min = hero.getBody().getPosition().y + 350;
        float y_max = hero.getBody().getPosition().y + 500;
        Random y_rand = new Random();
        float y_mod = y_max - y_min + 1;
        float y_pos;

        SpaceshipFactory enemyFactory = new SpaceshipFactory(masterPilotWorld);

        // Generate tie 
        number = tieNumber;
        while (number != 0) {

            x_pos = x_rand.nextFloat() % x_mod;
            y_pos = y_rand.nextFloat() % y_mod;

            // Check if the tie position is not on a planet
            for (Star planet : masterPilotWorld.getStarList()) {

                // If its not, generate a tie.
                if (x_pos < planet.getBody().getPosition().x - 50f && x_pos > planet.getBody().getPosition().x + 50) {
                    if (y_pos < planet.getBody().getPosition().y - 50f && y_pos > planet.getBody().getPosition().y + 50) {
                        enemyFactory.createEnemy("TIE", (int) x_pos, (int) y_pos, hero);
                        --number;
                    }
                }
            }
        }

        //Generate cruiser
        number = cruiserNumber;
        while (number != 0) {

            x_pos = x_rand.nextFloat() % x_mod;
            y_pos = y_rand.nextFloat() % y_mod;

            // Check if the cruiser position is not on a planet
            for (Star planet : masterPilotWorld.getStarList()) {

                // If its not generate a cruiser.
                if (x_pos < planet.getBody().getPosition().x - 50f && x_pos > planet.getBody().getPosition().x + 50) {
                    if (y_pos < planet.getBody().getPosition().y - 50f && y_pos > planet.getBody().getPosition().y + 50) {
                        enemyFactory.createEnemy("CRUISER", (int) x_pos, (int) y_pos, hero);
                        --number;
                    }
                }
            }
        }

        //Generate squadron
        number = squadronNumber;
        while (number != 0) {

            x_pos = x_rand.nextFloat() % x_mod;
            y_pos = y_rand.nextFloat() % y_mod;

            // Check if the squadron position is not on a planet
            for (Star planet : masterPilotWorld.getStarList()) {

                // If its not generate a squadron.
                if (x_pos < planet.getBody().getPosition().x - 50f && x_pos > planet.getBody().getPosition().x + 50) {
                    if (y_pos < planet.getBody().getPosition().y - 50f && y_pos > planet.getBody().getPosition().y + 50) {
                        enemyFactory.createEnemy("SQUADRON", (int) x_pos, (int) y_pos, hero);
                        --number;
                    }
                }
            }
        }

        //Generate invader
        number = invaderNumber;
        while (number != 0) {

            x_pos = x_rand.nextFloat() % x_mod;
            y_pos = y_rand.nextFloat() % y_mod;

            // Check if the invader position is not on a planet
            for (Star planet : masterPilotWorld.getStarList()) {

                // If its not generate an invader.
                if (x_pos < planet.getBody().getPosition().x - 50f && x_pos > planet.getBody().getPosition().x + 50) {
                    if (y_pos < planet.getBody().getPosition().y - 50f && y_pos > planet.getBody().getPosition().y + 50) {
                        enemyFactory.createEnemy("INVADER", (int) x_pos, (int) y_pos, hero);
                        --number;
                    }
                }
            }
        }

        //Generate squadron
        number = spaceBallNumber;
        while (number != 0) {

            x_pos = x_rand.nextFloat() % x_mod;
            y_pos = y_rand.nextFloat() % y_mod;

            // Check if the spaceball position is not on a planet
            for (Star planet : masterPilotWorld.getStarList()) {

                // If its not generate a spaceBall.
                if (x_pos < planet.getBody().getPosition().x - 50f && x_pos > planet.getBody().getPosition().x + 50) {
                    if (y_pos < planet.getBody().getPosition().y - 50f && y_pos > planet.getBody().getPosition().y + 50) {
                        enemyFactory.createEnemy("SPACEBALL", (int) x_pos, (int) y_pos, hero);
                        --number;
                    }
                }
            }
        }
    }


}
