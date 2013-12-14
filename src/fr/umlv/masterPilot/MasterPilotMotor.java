package fr.umlv.masterPilot;

import fr.umlv.masterPilot.Interface.KeyMotionObservable;
import fr.umlv.masterPilot.Interface.KeyMotionObserver;
import fr.umlv.masterPilot.enemy.TIE;
import fr.umlv.masterPilot.hero.Hero;
import fr.umlv.masterPilot.hero.Shield;
import fr.umlv.masterPilot.star.Star;
import fr.umlv.masterPilot.world.MasterPilot;
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
public class MasterPilotMotor implements KeyMotionObservable, KeyListener {
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

        Shield shield = new Shield(masterPilot.getWorld(), 150, 250, 15);

        shield.create();
//

        Hero h = new Hero(masterPilot.getWorld(), 0, 0);
        //
        h.create();
        //
        //


        masterPilot.setHero(h.getBody());
        this.addObserver(h);

        TIE tie = new TIE(masterPilot.getWorld(), 50, 50, h.getX(), h.getY());
        tie.create();
        for (int i = 0; i < Integer.MAX_VALUE * Integer.MAX_VALUE; i++) ;
        tie.fire();
        //        h.getBody().applyForce(new Vec2(50.f,60.f),new Vec2(50.f,60.f));

        //        bombTest.applyForce();
    }

    private void run(MasterPilot masterPilot, ApplicationContext context) {
        long beforeTime, afterTime, updateTime, timeDiff, sleepTime, timeSpent, startTime;
        float timeInSecs;
        float frameRate = 60;
        beforeTime = startTime = updateTime = System.nanoTime();
        sleepTime = 0;
        for (; ; ) {

            masterPilot.getWorld().step(timeStep, velocityIterations, positionIterations);
            timeSpent = beforeTime - updateTime;
            if (timeSpent > 0) {

                timeInSecs = timeSpent * 1.0f / 1000000000.0f;
                updateTime = System.nanoTime();
                frameRate = (frameRate * 0.9f) + (1.0f / timeInSecs) * 0.1f;
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
                Body hero = masterPilot.getHero();

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
