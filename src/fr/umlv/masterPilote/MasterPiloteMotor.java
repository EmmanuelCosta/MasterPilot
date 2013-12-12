package fr.umlv.masterPilote;

import fr.umlv.masterPilote.Interface.fr.umlv.masterPilote.Interface.keyMotion.KeyMotionObservable;
import fr.umlv.masterPilote.Interface.fr.umlv.masterPilote.Interface.keyMotion.KeyMotionObserver;
import fr.umlv.masterPilote.enemy.TIE;
import fr.umlv.masterPilote.hero.Shield;
import fr.umlv.masterPilote.star.Star;
import fr.umlv.masterPilote.hero.Hero;
import fr.umlv.masterPilote.world.MasterPilote;
import fr.umlv.zen3.ApplicationContext;
import fr.umlv.zen3.KeyboardEvent;

import org.jbox2d.dynamics.Body;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 06/12/13.
 */
public class MasterPiloteMotor implements KeyMotionObservable, KeyListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    //OBSERVER LIST
    private final List<KeyMotionObserver> observerList = new ArrayList<>();
    //TODO MOVE IT IN MASTERPILOTE WORLD
    float timeStep = 1.0f / 6f;
    int velocityIterations = 6;
    int positionIterations = 3;

    public void launchGame(ApplicationContext context) {

        context.render(graphics -> {
            MasterPilote masterPilote = initPlateform(graphics);
            populatedWorld(masterPilote, context);


            run(masterPilote, context);
        });


    }

    private MasterPilote initPlateform(Graphics2D graphics) {
        //Create Master Pilote
        MasterPilote mp = new MasterPilote(graphics);
        mp.initPlateForm(WIDTH, HEIGHT);

        // SET REFERENTIEL
        mp.setLandMark(WIDTH / 2, HEIGHT / 2);

        return mp;
    }

    private void populatedWorld(MasterPilote masterPilote, ApplicationContext context) {
        //TODO MANAGE WITH FILE CONFIG

        Star star = new Star(masterPilote.getWorld(), 100, 250);

        star.create();

        Shield shield = new Shield(masterPilote.getWorld(), 150, 250,15);

        shield.create();
//        ClassicBomb bombTest2 = new ClassicBomb(masterPilote.getWorld(), 1, 30);
//
//        bombTest2.create();
//        bombTest2.getBody().applyForce(new Vec2(50f, -200f), new Vec2(50f, -20f));

        Hero h = new Hero(masterPilote.getWorld(), 0, 0);
        //
        h.create();
        //
        //


        masterPilote.setHero(h.getBody());
        this.addObserver(h);
        
        TIE tie = new TIE(masterPilote.getWorld(), 50, 50, h.getX(), h.getY());
        tie.create();
        for(int i = 0; i < Integer.MAX_VALUE * Integer.MAX_VALUE; i++);
        tie.fire();
         //        h.getBody().applyForce(new Vec2(50.f,60.f),new Vec2(50.f,60.f));

        //        bombTest.applyForce();
    }

    private void run(MasterPilote masterPilote, ApplicationContext context) {
        long beforeTime, afterTime, updateTime, timeDiff, sleepTime, timeSpent, startTime;
        float timeInSecs;

        beforeTime = startTime = updateTime = System.nanoTime();
        sleepTime = 0;
        for (; ; ) {

            masterPilote.getWorld().step(timeStep, velocityIterations, positionIterations);
            timeSpent = beforeTime - updateTime;


                updateTime = System.nanoTime();



            KeyboardEvent keyEvent = context.pollKeyboard();

            /**
             * this for notify the hero
             * that event have been find
             */
            if (keyEvent != null) {
                this.notifyObserver(keyEvent);
            }


            context.render(graphics -> {
                masterPilote.repaint(WIDTH, HEIGHT);
                masterPilote.draw();
                Body hero = masterPilote.getHero();

                //TODO use this to center view place it in proper place
               masterPilote.setCamera(hero.getPosition());

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

            masterPilote.getWorld().clearForces();
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
