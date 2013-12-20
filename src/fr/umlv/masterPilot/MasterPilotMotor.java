package fr.umlv.masterPilot;


import fr.umlv.masterPilot.Interface.Bomb;
import fr.umlv.masterPilot.Interface.KeyMotionObservable;
import fr.umlv.masterPilot.Interface.KeyMotionObserver;
import fr.umlv.masterPilot.Interface.SpaceShip;
import fr.umlv.masterPilot.bomb.GenericBomb;
import fr.umlv.masterPilot.hero.Hero;
import fr.umlv.masterPilot.star.Star;
import fr.umlv.masterPilot.world.MasterPilot;
import fr.umlv.masterPilot.world.SpaceshipFactory;
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

    public void launchGame(ApplicationContext context) {

        context.render(graphics -> {
            MasterPilot masterPilot = initPlateform(graphics);
               gameTiming=Integer.valueOf("500");



            populatedWorld(masterPilot, context);
            run(masterPilot, context);
        });
    }

    private MasterPilot initPlateform(Graphics2D graphics) {
        //Create Master Pilote
        MasterPilot mp = new MasterPilot(graphics);
        mp.initPlateForm(WIDTH, HEIGHT);

        // SET REFERENTIEL
        mp.setLandMark(WIDTH / 2, HEIGHT / 2);

        return mp;
    }

    private void populatedWorld(MasterPilot masterPilot, ApplicationContext context) {
        //TODO MANAGE WITH FILE CONFIG

        Star star = new Star(masterPilot.getWorld(), 100, 250, Color.yellow);
        star.create();

        Star star2 = new Star(masterPilot.getWorld(), 100, 550, Color.GREEN);
        star2.create();

        Star star3 = new Star(masterPilot.getWorld(), 400, 350, Color.RED);
        star3.create();

        Star star4 = new Star(masterPilot.getWorld(), 400, 650, Color.BLUE);
        star4.create();


        Hero h = new Hero(masterPilot.getWorld(), 0, 0);
        //
        h.create();

        masterPilot.setHero(h);
        this.addObserver(h);

        SpaceshipFactory factory = new SpaceshipFactory(masterPilot);
        factory.createEnemy("TIE", 350, 50, h);


//        TIE tie1 = new TIE(masterPilot.getWorld(), 150, 50, h);
//        tie1.create();
//        masterPilot.addToSpaceshipManager(tie1.getBody(), tie1);
        factory.createEnemy("TIE", 150, 50, h);

//        TIE tie2 = new TIE(masterPilot.getWorld(), -20, 90, h);
//        tie2.create();
//        masterPilot.addToSpaceshipManager(tie2.getBody(), tie2);

        factory.createEnemy("TIE", -20, 90, h);
//
//        TIE tie3 = new TIE(masterPilot.getWorld(), 200, 90, h);
//        tie3.create();
//        masterPilot.addToSpaceshipManager(tie3.getBody(), tie3);

        factory.createEnemy("TIE", 200, 90, h);

//        TIE tie4 = new TIE(masterPilot.getWorld(), 50, 90, h);
//        tie4.create();
//        masterPilot.addToSpaceshipManager(tie4.getBody(), tie4);

        factory.createEnemy("TIE", 50, 90, h);

//
        GenericBomb empBomb = new GenericBomb(masterPilot.getWorld(), 70, -35, Bomb.BombType.BOMB);
        empBomb.create();

        masterPilot.addToBombManager(empBomb.getBody(), empBomb);
        empBomb = new GenericBomb(masterPilot.getWorld(), 170, -35, Bomb.BombType.MEGABOMB);
        empBomb.create();

        masterPilot.addToBombManager(empBomb.getBody(), empBomb);

        //SpaceShip tie2 =  masterPilot.removeToSpaceshipManager(body);
        //System.out.println(tie2);
        System.out.println("==========");
        for (SpaceShip space : masterPilot.getEnemyList()) {
            System.out.println(space);
        }
    }

    /**
     * main loop of the game
     *
     * @param masterPilot : This is the masterpilote world
     * @param context
     */
    private void run(MasterPilot masterPilot, ApplicationContext context) {
        long beforeTime, afterTime, timeDiff, sleepTime;

        beforeTime = System.nanoTime();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                gameTiming--;

            }
        },1000,1000);

        for (; ; ) {


            masterPilot.getWorld().step(timeStep, velocityIterations, positionIterations);

            List<Body> destroyBody = masterPilot.getDestroyBody();
            Iterator<Body> iterator = destroyBody.iterator();
            while (iterator.hasNext()) {
                Body next = iterator.next();

                iterator.remove();
                masterPilot.getWorld().destroyBody(next);
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
                masterPilot.repaint(WIDTH, HEIGHT);
                masterPilot.draw();


                masterPilot.drawFrameworkClock(gameTiming/3600,(gameTiming%3600)/60,(gameTiming%3600)%60);


                Body hero = masterPilot.getBodyHero();

                //TODO use this to center view place it in proper place
                masterPilot.setCamera(hero.getPosition());

            });


            afterTime = System.nanoTime();

            timeDiff = afterTime - beforeTime;
            sleepTime = (1000000000 / 60 - timeDiff) / 1000000;
            if (sleepTime > 0) {
                try {


                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                }
            }


            if(gameTiming < 0){
                return;
            }
            beforeTime = System.nanoTime();

            // masterPilot.getWorld().clearForces();


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

    /**
     * this will apply the spaceship logic for all ennemy
     *
     * @param spaceShips
     * @param hero
     * @see #doEnemyLogic(fr.umlv.masterPilot.Interface.SpaceShip, fr.umlv.masterPilot.hero.Hero)
     */
    private void proccessManager(List<SpaceShip> spaceShips, Hero hero) {
        for (SpaceShip space : spaceShips) {
            doEnemyLogic(space, hero);
        }
    }

    /**
     * this will contains the logic of enemy spaceship for move and fire
     * according to the hero
     *
     * @param space : the enemy spaceship
     * @param hero  : the spaceship hero
     */
    private void doEnemyLogic(SpaceShip space, Hero hero) {

    }

}
