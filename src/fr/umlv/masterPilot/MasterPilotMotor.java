package fr.umlv.masterPilot;

import fr.umlv.masterPilot.world.KeyMotionObservable;
import fr.umlv.masterPilot.world.KeyMotionObserver;
import fr.umlv.masterPilot.ship.SpaceShip;
import fr.umlv.masterPilot.ship.SpaceshipFactory;
import fr.umlv.masterPilot.ship.hero.Hero;
import fr.umlv.masterPilot.star.Star;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import fr.umlv.zen3.ApplicationContext;
import fr.umlv.zen3.KeyboardEvent;

import org.jbox2d.dynamics.Body;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by emmanuel on 06/12/13.
 */
public class MasterPilotMotor implements KeyMotionObservable {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    //OBSERVER LIST
    private final List<KeyMotionObserver> observerList = new ArrayList<>();
    //TODO MOVE IT IN MASTERPILOTE WORLD
    private final float timeStep = 1.0f / 6f;
    private final int velocityIterations = 6;
    private final int positionIterations = 3;
    private int gameTiming;



    public void launchGame(ApplicationContext context, String levelFile) {

            /**
             * gerer le xml ici
             */
        
        context.render(graphics -> {
            MasterPilotWorld masterPilotWorld = initPlateform(graphics);
            gameTiming = Integer.valueOf("500");


            populatedWorld(masterPilotWorld, context);
            run(masterPilotWorld, context);
        });
    }

    private MasterPilotWorld initPlateform(Graphics2D graphics) {
        //Create Master Pilote
        MasterPilotWorld mp = new MasterPilotWorld(graphics);
        mp.initPlateForm(WIDTH, HEIGHT);

        // SET REFERENTIEL
        mp.setLandMark(WIDTH / 2, HEIGHT / 2);

        return mp;
    }

    private void populatedWorld(MasterPilotWorld masterPilotWorld, ApplicationContext context) {
        //TODO MANAGE WITH FILE CONFIG
//
        Star star = new Star(masterPilotWorld.getWorld(), 100, 250, Color.yellow);
        star.create();

        Star star2 = new Star(masterPilotWorld.getWorld(), 100, 550, Color.GREEN);
        star2.create();

        Star star3 = new Star(masterPilotWorld.getWorld(), 400, 350, Color.RED);
        star3.create();

        Star star4 = new Star(masterPilotWorld.getWorld(), 400, 650, Color.BLUE);
        star4.create();


        Hero h = new Hero(masterPilotWorld.getWorld(), 0, 0);
        //
        h.create();

        masterPilotWorld.setHero(h);
        this.addObserver(h);

        SpaceshipFactory factory = new SpaceshipFactory(masterPilotWorld);
//        factory.createEnemy("TIE", 350, 150, h);
//
//        factory.createEnemy("TIE", 850, 750, h);
////
//        factory.createEnemy("TIE", 250, -50, h);
////
////
//        factory.createEnemy("TIE", -150, 50, h);
//
//
//
        factory.createEnemy("INVADER", 150, 50, h);

//        factory.createEnemy("TIE", 150, 50, h);
//
//        factory.createEnemy("SQUADRON", 350, 50, h);
//

        factory.createEnemy("CRUISER", -350, 50, h);
//
////
//        factory.createEnemy("SQUADRON", -20, 90, h);
//        factory.createEnemy("CRUISER", -350, 100, h);
//
//
//        factory.createEnemy("SQUADRON", -20, 90, h);
////
////
//        factory.createEnemy("TIE", 200, 90, h);
//
//        factory.createEnemy("TIE", 200, 90, h);
////
////
//        factory.createEnemy("CRUISER", -50, 90, h);

//
//        GenericBomb empBomb = new GenericBomb(masterPilotWorld.getWorld(), 70, -35, Bomb.BombType.BOMB);
//        empBomb.create();
//
//        masterPilotWorld.addToBombManager(empBomb.getBody(), empBomb);
//        empBomb = new GenericBomb(masterPilotWorld.getWorld(), 170, -35, Bomb.BombType.MEGABOMB);
//        empBomb.create();

//        masterPilotWorld.addToBombManager(empBomb.getBody(), empBomb);


        System.out.println("==========");
        for (SpaceShip space : masterPilotWorld.getEnemyList()) {
            System.out.println(space);
        }
    }

    /**
     * main loop of the game
     *
     * @param masterPilotWorld : This is the masterpilote world
     * @param context
     */
    private void run(MasterPilotWorld masterPilotWorld, ApplicationContext context) {
        long beforeTime, afterTime, timeDiff, sleepTime;

        beforeTime = System.nanoTime();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                gameTiming--;

            }
        }, 1000, 1000);

//        Thread thread = new Thread(()->{
//            for (SpaceShip space : masterPilotWorld.getEnemyList()) {
//                space.doMove();
//            }
//        });
//        thread.start();

//        Timer time = new Timer();
//        time.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                for (SpaceShip space : masterPilotWorld.getEnemyList()) {
//                space.doMove();
//            }
//
//
//            }
//        }, 500, 500);
        for (; ; ) {


            masterPilotWorld.getWorld().step(timeStep, velocityIterations, positionIterations);

            List<Body> destroyBody = masterPilotWorld.getDestroyBody();
            Iterator<Body> iterator = destroyBody.iterator();
            while (iterator.hasNext()) {
                Body next = iterator.next();

                iterator.remove();
                masterPilotWorld.getWorld().destroyBody(next);
            }


            KeyboardEvent keyEvent = context.pollKeyboard();

            /**
             * this for notify the hero
             * that event have been find
             */


            if (keyEvent != null) {
                this.notifyObserver(keyEvent);
            }


            context.render(graphics -> {
                masterPilotWorld.repaint(WIDTH, HEIGHT);
                masterPilotWorld.draw();


                masterPilotWorld.drawFrameworkClock(gameTiming / 3600, (gameTiming % 3600) / 60, (gameTiming % 3600) % 60);


                Body hero = masterPilotWorld.getBodyHero();

                //TODO use this to center view place it in proper place
                masterPilotWorld.setCamera(hero.getPosition());

            });

            proccessManager(masterPilotWorld.getEnemyList(), masterPilotWorld.getHero());
            afterTime = System.nanoTime();

            timeDiff = afterTime - beforeTime;
            sleepTime = (1000000000 / 60 - timeDiff) / 1000000;
            if (sleepTime > 0) {
                try {


                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                }
            }


            if (gameTiming < 0 ) {
                masterPilotWorld.drawFrameworkEnd(false,WIDTH/2,HEIGHT/2);
                return;
            }
            else if(masterPilotWorld.getEnemyList().isEmpty()){
                masterPilotWorld.drawFrameworkEnd(true,WIDTH/2,HEIGHT/2);
                return;
            }
            beforeTime = System.nanoTime();
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

    public void run2(MasterPilotWorld masterPilotWorld, ApplicationContext context) {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        long lastFpsTime=0;
        int fps=0;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                gameTiming--;

            }
        }, 1000, 1000);

        // keep looping round til the game ends
        while (true) {
            masterPilotWorld.getWorld().step(timeStep, velocityIterations, positionIterations);
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
             * this for notify the hero
             * that event have been find
             */


            if (keyEvent != null) {
                this.notifyObserver(keyEvent);
            }


            context.render(graphics -> {
                masterPilotWorld.repaint(WIDTH, HEIGHT);
                masterPilotWorld.draw();


                masterPilotWorld.drawFrameworkClock(gameTiming / 3600, (gameTiming % 3600) / 60, (gameTiming % 3600) % 60);


                Body hero = masterPilotWorld.getBodyHero();

                //TODO use this to center view place it in proper place
                masterPilotWorld.setCamera(hero.getPosition());

            });
            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            int sleep= (int)(lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
            System.out.println(sleep);
            if(sleep > 0){
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(gameTiming < 0){
              return;
            }
        }
    }

}
