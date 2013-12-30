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



    private class GameSpec{
        private final int tieNumber;
        private final int cruiserNumber;
        private final int squadronNumber;
        private final int invaderNumber;
        private final int spaceBallNumber;
        private final int vogueNumber;
        private final int timer;

        private GameSpec(int tieNumber, int cruiserNumber, int squadronNumber, int invaderNumber, int spaceBallNumber, int vogueNumber, int timer) {
            this.tieNumber = tieNumber;
            this.cruiserNumber = cruiserNumber;
            this.squadronNumber = squadronNumber;
            this.invaderNumber = invaderNumber;
            this.spaceBallNumber = spaceBallNumber;
            this.vogueNumber = vogueNumber;
            this.timer = timer;
        }

        public int getTimer() {
            return timer;
        }

        public int getTieNumber() {
            return tieNumber;
        }

        public int getWaveNumber() {
            return vogueNumber;
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
    public void launchGame(ApplicationContext context, String levelFile, String mode) {


        /**
         * Load the level.
         */
        final LevelHandler levelParser = new LevelHandler();

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


            populatedWorld(masterPilotWorld, context, levelParser, mode);

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
                                ApplicationContext context, LevelHandler levelParser, String mode) {

        /**
         * Get datas from the xml.
         */

        // Data about the timer
        int timer = levelParser.getTime().getTimer();
        //set timer
      //  gameTiming = timer;
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

        /**
         * Initialize the game mode.
         */
        Hero h = null;
        switch (mode) {
            case "-h":
                h = factory.createHero(0, 0, MasterPilotWorld.MODE.HARDCORE);
                break;

            case "-c":
                h = factory.createHero(0, 0, MasterPilotWorld.MODE.CHEAT);
                break;

            default:
                throw new IllegalArgumentException("The specified argument for the game mode is not valid.\n Please enter -c for the cheat mode. \n Or enter -h for the hardcore mode.");
        }

        this.addObserver(h);


        GameSpec gameSpec = new GameSpec(tieNumber, cruiserNumber, squadronNumber, invaderNumber, spaceBallNumber, waveNumber, timer);
//
        run(masterPilotWorld, context,gameSpec);
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

        System.out.println("THIS MAP CONTAINS " + masterPilotWorld.getStarList().size() + "  star");
    }

    /**
     * main loop of the game
     *
     * @param masterPilotWorld : This is the masterpilot world
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
        createEnnemyWave(masterPilotWorld, gameSpec.getTieNumber(), gameSpec.getCruiserNumber(),
                gameSpec.getSquadronNumber(), gameSpec.getInvaderNumber(), gameSpec.getSpaceBallNumber());



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

                //  use this to center view place it in proper place
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
                if(wave > 0){
                    createEnnemyWave(masterPilotWorld, gameSpec.getTieNumber(), gameSpec.getCruiserNumber(),
                            gameSpec.getSquadronNumber(), gameSpec.getInvaderNumber(), gameSpec.getSpaceBallNumber());

                    wave--;
                }else {

                // If there is no waves again
                // YOU WIN
                masterPilotWorld.drawFrameworkEnd(true, WIDTH / 2, HEIGHT / 2);
                return;
                }
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

    public void createEnnemyWave(MasterPilotWorld masterPilotWorld, Integer... ennemyTab) {
        Body bodyHero = masterPilotWorld.getBodyHero();
        Hero hero = masterPilotWorld.getHero();
        int x =(int) bodyHero.getPosition().x;
        int y =(int) bodyHero.getPosition().y;

        int size = ennemyTab.length;
        int compt = 0;

        int init_x = 0;
        int init_y = 0;
        int direction;
        SpaceshipFactory enemyFactory = new SpaceshipFactory(masterPilotWorld);


        for (int i : ennemyTab) {
            switch (compt) {
                case 0:
                    //TIE
                    init_x = x + 300;
                    init_y = y + 300;

                     direction =0;
                    while( i > 0){
                        if(direction %4 == 0){
                            enemyFactory.createEnemy("TIE",init_x+50*i,init_y,hero);
                        }else if(direction %4 == 1){
                            enemyFactory.createEnemy("TIE",init_x-50*i,init_y,hero);
                        }else if(direction %4 == 2){
                            enemyFactory.createEnemy("TIE",init_x-50*i,init_y+50,hero);
                        }else{
                            enemyFactory.createEnemy("TIE",init_x+50*i,init_y-50,hero);
                        }
                        i--;
                        direction++;
                    }

                    break;
                case 1:
                    //CRUISER
                    init_x = x + 300;
                    init_y = y + 300;

                     direction =0;
                    while( i > 0){
                        if(direction %4 == 0){
                            enemyFactory.createEnemy("CRUISER",init_x+70*i,init_y,hero);
                        }else if(direction %4 == 1){
                            enemyFactory.createEnemy("CRUISER",init_x-70*i,-init_y,hero);
                        }else if(direction %4 == 2){
                            enemyFactory.createEnemy("CRUISER",init_x-70*i,-init_y+70,hero);
                        }else{
                            enemyFactory.createEnemy("CRUISER",init_x+70*i,init_y-70,hero);
                        }
                        i--;
                        direction++;
                    }
                    break;
                case 2:
                    init_x = x + 200;
                    init_y = y + 250;

                    direction =0;
                    while( i > 0){
                        if(direction %4 == 0){
                            enemyFactory.createEnemy("SQUADRON",init_x+30*i,init_y,hero);
                        }else if(direction %4 == 1){
                            enemyFactory.createEnemy("SQUADRON",init_x-30*i,-init_y,hero);
                        }else if(direction %4 == 2){
                            enemyFactory.createEnemy("SQUADRON",init_x-30*i,-init_y+30,hero);
                        }else{
                            enemyFactory.createEnemy("SQUADRON",init_x+30*i,init_y-30,hero);
                        }
                        i--;
                        direction++;
                    }
                    break;
                case 3:
                    init_x = x + 200;
                    init_y = y + 250;

                    direction =0;
                    while( i > 0){
                        if(direction %4 == 0){
                            enemyFactory.createEnemy("INVADER",init_x+150*i,init_y,hero);
                        }else if(direction %4 == 1){
                            enemyFactory.createEnemy("INVADER",init_x-150*i,-init_y,hero);
                        }else if(direction %4 == 2){
                            enemyFactory.createEnemy("INVADER",init_x-150*i,-init_y+100,hero);
                        }else{
                            enemyFactory.createEnemy("INVADER",init_x+150*i,init_y-100,hero);
                        }
                        i--;
                        direction++;
                    }
                    break;
                case 4:
                    init_x = x + 200;
                    init_y = y + 250;

                    direction =0;
                    while( i > 0){
                        if(direction %4 == 0){
                            enemyFactory.createEnemy("SPACEBALL",init_x+100*i,init_y,hero);
                        }else if(direction %4 == 1){
                            enemyFactory.createEnemy("SPACEBALL",init_x-100*i,-init_y,hero);
                        }else if(direction %4 == 2){
                            enemyFactory.createEnemy("SPACEBALL",init_x-100*i,-init_y+80,hero);
                        }else{
                            enemyFactory.createEnemy("SPACEBALL",init_x+100*i,init_y-80,hero);
                        }
                        i--;
                        direction++;
                    }
                    break;
                case 5:
                    break;
                default:
                    break;

            }
            compt++;

        }


    }

    private void generateEnemies(int tieNumber, int cruiserNumber,
                                 int squadronNumber, int invaderNumber,
                                 int spaceBallNumber,
                                 Hero hero,
                                 MasterPilotWorld masterPilotWorld) {

        generateEnemy("TIE", tieNumber, hero, masterPilotWorld);
        generateEnemy("CRUISER", cruiserNumber, hero, masterPilotWorld);
        generateEnemy("SQUADRON", squadronNumber, hero, masterPilotWorld);
        generateEnemy("INVADER", invaderNumber, hero, masterPilotWorld);
        generateEnemy("SPACEBALL", spaceBallNumber, hero, masterPilotWorld);
    }

    private void generateEnemy(String type, int number, Hero hero, MasterPilotWorld masterPilotWorld) {
        /**
         * Generate x position
         */
        float x_min = hero.getBody().getPosition().x - WIDTH;
        float x_max = hero.getBody().getPosition().x + WIDTH;
        Random x_rand = new Random();
        float x_mod = x_max - x_min + 1;
        float x_pos;

        /**
         * Generate y position
         */
        float y_min = hero.getBody().getPosition().y;
        float y_max = hero.getBody().getPosition().y + HEIGHT + 200;
        Random y_rand = new Random();
        float y_mod = y_max - y_min + 1;
        float y_pos;

        SpaceshipFactory enemyFactory = new SpaceshipFactory(masterPilotWorld);

        // Generate enemy
        while (number > 0) {
            x_pos = x_rand.nextFloat() % x_mod;
            y_pos = y_rand.nextFloat() % y_mod;

            // Check if the tie position is not on a planet
            for (Star planet : masterPilotWorld.getStarList()) {

                // If its not, generate a tie.
                if (x_pos < planet.getBody().getPosition().x - 150f && x_pos > planet.getBody().getPosition().x + 50) {
                    if (y_pos < planet.getBody().getPosition().y - 150f && y_pos > planet.getBody().getPosition().y + 50) {
                        enemyFactory.createEnemy(type, (int) x_pos, (int) y_pos, hero);
                        --number;
                    }
                } else {
                    enemyFactory.createEnemy(type, (int) x_pos, (int) y_pos + 150, hero);
                    --number;
                }
            }
        }
    }


}
