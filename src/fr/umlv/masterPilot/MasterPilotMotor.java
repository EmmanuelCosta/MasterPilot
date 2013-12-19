package fr.umlv.masterPilot;


import fr.umlv.masterPilot.Interface.KeyMotionObservable;
import fr.umlv.masterPilot.Interface.KeyMotionObserver;
import fr.umlv.masterPilot.Interface.SpaceShip;
import fr.umlv.masterPilot.bomb.ExplodeBomb;
import fr.umlv.masterPilot.bomb.ImplodeBomb;
import fr.umlv.masterPilot.enemy.TIE;
import fr.umlv.masterPilot.hero.Hero;
import fr.umlv.masterPilot.star.Star;
import fr.umlv.masterPilot.world.MasterPilot;
import fr.umlv.zen3.ApplicationContext;
import fr.umlv.zen3.KeyboardEvent;

import org.jbox2d.dynamics.Body;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by emmanuel on 06/12/13.
 */
public class MasterPilotMotor implements KeyMotionObservable {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    
    //OBSERVER LIST
    private final List<KeyMotionObserver> observerList = new ArrayList<>();
    
    //TODO MOVE IT IN MASTERPILOTE WORLD
    float timeStep = 1.0f / 6f;
    int velocityIterations = 6;
    int positionIterations = 3;

    public void launchGame(ApplicationContext context) {

        context.render(graphics -> {
            MasterPilot masterPilot = initPlateform(graphics);
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

        Star star = new Star(masterPilot.getWorld(), 100, 250);
        star.create();

        Star star2 = new Star(masterPilot.getWorld(), 100, 550, Color.blue);
        star2.create();

        Star star3 = new Star(masterPilot.getWorld(), 400, 350, Color.GREEN);
        star3.create();


        Hero h = new Hero(masterPilot.getWorld(), 0, 0);
        //
        h.create();
        //
        //

        masterPilot.setHero(h);
        this.addObserver(h);
//
        TIE tie1 = new TIE(masterPilot.getWorld(), 150, 50, h);
        tie1.create();
        masterPilot.getEnemyList().add(tie1);
        
        TIE tie2 = new TIE(masterPilot.getWorld(), -20, 90, h);
        tie2.create();
        masterPilot.getEnemyList().add(tie2);

        TIE tie3 = new TIE(masterPilot.getWorld(), 200, 90, h);
        tie3.create();
        masterPilot.getEnemyList().add(tie3);

        TIE tie4 = new TIE(masterPilot.getWorld(), 50, 90, h);
        tie4.create();
        masterPilot.getEnemyList().add(tie4);

        ImplodeBomb impBomb = new ImplodeBomb(masterPilot.getWorld(), 50, 35);
        impBomb.create();

        ExplodeBomb empBomb = new ExplodeBomb(masterPilot.getWorld(), 70, -35);
        empBomb.create();

    }


    /**
     * main loop of the game
     * @param masterPilot : This is the masterpilote world
     * @param context
     */
    private void run(MasterPilot masterPilot, ApplicationContext context) {
        long beforeTime, afterTime, timeDiff, sleepTime;

        beforeTime = System.nanoTime();
        Timer timer = new Timer();

        for (;;) {

            
            masterPilot.getWorld().step(timeStep, velocityIterations, positionIterations);       
            
            List<Body> destroyBody = masterPilot.getDestroyBody();
            Iterator<Body> iterator = destroyBody.iterator();
           while (iterator.hasNext()) {
                Body next = iterator.next();
                iterator.remove();
                masterPilot.getWorld().destroyBody(next);
           }


            /**
             * the tie shoot every 1 second once.
             */
//            timer.schedule(new TimerTask() {
//                
//                @Override
//                public void run() {
                    
//                    List<SpaceShip> enemeyList = masterPilot.getEnemyList();
//                    Iterator<SpaceShip> it = enemeyList.iterator();
//                    
//                    for(SpaceShip enemy : enemeyList) {
//                        enemy.fire();
//                    }
//                }
//            }, 1000, 1);
            
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


}
