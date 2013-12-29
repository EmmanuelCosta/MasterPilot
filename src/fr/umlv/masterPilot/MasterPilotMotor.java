package fr.umlv.masterPilot;

import fr.umlv.masterPilot.bomb.Bomb;
import fr.umlv.masterPilot.bomb.GenericBomb;

import fr.umlv.masterPilot.parser.handler.LevelHandler;
import fr.umlv.masterPilot.star.StarFactory;
import fr.umlv.masterPilot.world.KeyMotionObservable;
import fr.umlv.masterPilot.world.KeyMotionObserver;

import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.SpaceshipFactory;
import fr.umlv.masterPilot.ship.hero.Hero;

import fr.umlv.masterPilot.world.MasterPilotWorld;
import fr.umlv.zen3.ApplicationContext;
import fr.umlv.zen3.KeyboardEvent;
import org.jbox2d.dynamics.Body;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by emmanuel on 06/12/13.
 */
public class MasterPilotMotor implements KeyMotionObservable {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    // OBSERVER LIST
    private final List<KeyMotionObserver> observerList = new ArrayList<>();

    private final float timeStep = 1.0f / 6f;
    private final int velocityIterations = 6;
    private final int positionIterations = 3;
    private int gameTiming;


    public void launchGame(ApplicationContext context, String levelFile) {

        /**
         * Load the level.
         */
        final LevelHandler levelParser = new LevelHandler();;
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
        int bombNumber =  bombPercentage * (minimum + i) / 100;
        i = rn.nextInt() % n;
        int megaBombNumber = megaBombPercentage * (minimum + i) / 100;
        
        // About the planets
        minimum = 1;
        maximum = 8; // because we can't have more than 8 planet on a surface.
        i = rn.nextInt() % n;
        int planetNumber =  planetPercentage * (minimum + i) / 100;
        
        minimum = radiusMin;
        maximum = radiusMax;
        i = rn.nextInt() % n;
        int  radius = minimum + i;
        
        minimum = densityMin;
        maximum = densityMax;
        i = rn.nextInt() % n;
        int  density = minimum + i;
        
        /**
         * Create planets and add the planets to the manager
         */
        
        /**
         * Put some planets
         */

        /**
         * Put enemies 's first wave.
         */
        
        // TODO MANAGE WITH FILE CONFIG
        StarFactory startFactory = new StarFactory(masterPilotWorld);
        startFactory.createStar(100, 250, Color.yellow);
        startFactory.createStar(100, 550, Color.GREEN);
        startFactory.createStar(400, 350, Color.RED);
        startFactory.createStar(400, 650, Color.BLUE);


        SpaceshipFactory factory = new SpaceshipFactory(masterPilotWorld);
        Hero h = factory.createHero(0, 0, MasterPilotWorld.MODE.CHEAT);

        this.addObserver(h);

        factory.createEnemy("INVADER", 150, 50, h);

        factory.createEnemy("TIE", 150, 50, h);

        factory.createEnemy("SQUADRON", 350, 50, h);

        factory.createEnemy("CRUISER", -350, 50, h);

        //
        factory.createEnemy("SQUADRON", -20, 90, h);
        factory.createEnemy("CRUISER", -350, 100, h);

        factory.createEnemy("SPACEBALL", -20, 90, h);
        // //
        // //
        factory.createEnemy("TIE", 200, 90, h);
        //
        factory.createEnemy("TIE", 200, 90, h);
        //
        //
        factory.createEnemy("CRUISER", -50, 90, h);

        //
        GenericBomb empBomb = new GenericBomb(masterPilotWorld.getWorld(), 70,
                -35, Bomb.BombType.BOMB);
        empBomb.create();

        masterPilotWorld.addToBombManager(empBomb.getBody(), empBomb);
        empBomb = new GenericBomb(masterPilotWorld.getWorld(), 170, -35,
                Bomb.BombType.MEGABOMB);
        empBomb.create();
        masterPilotWorld.addToBombManager(empBomb.getBody(), empBomb);

        // masterPilotWorld.addToBombManager(empBomb.getBody(), empBomb);


    }

    private void createRandomStar(MasterPilotWorld masterPilotWorld, int nb) {
        StarFactory startFactory = new StarFactory(masterPilotWorld);


int pc=(int)((0.8)%10+1);
        System.out.println(pc);
        for(int i=-(WIDTH/2)*2;i<=(WIDTH/2)*2;i=i+280){
nb=5;
            for(int p=-(HEIGHT/2)*2;p<=(HEIGHT/2)*2;p=p+180){
                int rInterval = 0 + (int)(Math.random() * ((5 - 0) + 1));
//               sout
                System.out.println(rInterval);
               if(nb == 0)
                   break;
                if(rInterval== 4){

                     startFactory.createStar(i, p, Color.yellow);
                }
                nb--;
                pc++;
            }
        }

//        for (int i = 0; i < 1; i++,w++) {
////            for (int j = -2; j < 2; j++,w++) {
//                int al =  200 + (int)(Math.random() * ((400 - 200) + 1)) ;
//               int rInterval = 8+(int)(Math.random() * ((16 - 8) + 1));
//                if (w % 4 == 0) {
//                    startFactory.createStar((WIDTH / rInterval) + al , (HEIGHT / rInterval) - al, Color.yellow);
//                } else if (w % 4 == 1) {
//                    startFactory.createStar((WIDTH / rInterval) + al , -(HEIGHT / rInterval) + al, Color.RED);
//                }else if(w % 4 == 2){
//                    startFactory.createStar(-(WIDTH / rInterval) - al, -(HEIGHT / rInterval) + al , Color.BLUE);
//                }else{
//                    startFactory.createStar(-(WIDTH / rInterval) - al , (HEIGHT / rInterval) - al , Color.WHITE);
//                }
//            }
//        }
    }

    /**
     * main loop of the game
     * 
     * @param masterPilotWorld
     *            : This is the masterpilote world
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

        // Thread thread = new Thread(()->{
        // for (SpaceShip space : masterPilotWorld.getEnemyList()) {
        // space.doMove();
        // }
        // });
        // thread.start();

        // Timer time = new Timer();
        // time.schedule(new TimerTask() {
        // @Override
        // public void run() {
        // for (SpaceShip space : masterPilotWorld.getEnemyList()) {
        // space.doMove();
        // }
        //
        //
        // }
        // }, 500, 500);
        for (;;) {

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

    public void run2(MasterPilotWorld masterPilotWorld,
            ApplicationContext context) {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        long lastFpsTime = 0;
        int fps = 0;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                gameTiming--;

            }
        }, 1000, 1000);

        // keep looping round til the game ends
        while (true) {
            masterPilotWorld.getWorld().step(timeStep, velocityIterations,
                    positionIterations);
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double) OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000) {
                System.out.println("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }

            List<Body> destroyBody = masterPilotWorld.getDestroyBody();
            Iterator<Body> iterator = destroyBody.iterator();
            while (iterator.hasNext()) {
                Body next = iterator.next();

                iterator.remove();
                masterPilotWorld.getWorld().destroyBody(next);
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

                // TODO use this to center view place it in proper place
                masterPilotWorld.setCamera(hero.getPosition());

            });
            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            // remember this is in ms, whereas our lastLoopTime etc. vars are in
            // ns.

            int sleep = (int) (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
            System.out.println(sleep);
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (gameTiming < 0) {
                return;
            }
        }
    }

}
