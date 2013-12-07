package fr.umlv.masterPilote;

import fr.umlv.masterPilote.Interface.fr.umlv.masterPilote.Interface.keyMotion.KeyMotionObservable;
import fr.umlv.masterPilote.Interface.fr.umlv.masterPilote.Interface.keyMotion.KeyMotionObserver;
import fr.umlv.masterPilote.bomb.ClassicBomb;
import fr.umlv.masterPilote.hero.Hero;
import fr.umlv.zen3.ApplicationContext;
import fr.umlv.zen3.KeyboardEvent;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 06/12/13.
 */
public class MasterPiloteMotor implements KeyMotionObservable {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    //OBSERVER LIST
    private final List<KeyMotionObserver> observerList = new ArrayList<>();


    //TODO MOVE IT IN MASTERPILOTE WORLD
    float timeStep = 1.0f / 1f;
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

        //        ClassicBomb bombTest = new ClassicBomb(masterPilote.getWorld(), 1, 5);
        //
        //        bombTest.create();
        ClassicBomb bombTest2 = new ClassicBomb(masterPilote.getWorld(), 1, 30);

        bombTest2.create();
        bombTest2.getBody().applyForce(new Vec2(50f, -200f), new Vec2(50f, -20f));

        Hero h = new Hero(masterPilote.getWorld(), 20, -20);
        //
        h.create();
        //
        //
        masterPilote.setHero(h.getBody());

        this.addObserver(h);


        //        h.getBody().applyForce(new Vec2(50.f,60.f),new Vec2(50.f,60.f));

        //        bombTest.applyForce();
    }

    private void run(MasterPilote masterPilote, ApplicationContext context) {

        for (; ; ) {
            KeyboardEvent keyEvent = context.pollKeyboard();

            /**
             * this for notify the hero that event have been
             * find
             */
            if (keyEvent != null) {
                this.notifyObserver(keyEvent);
            }


            masterPilote.getWorld()
                    .step(timeStep, velocityIterations, positionIterations);
            context.render(graphics -> {
                masterPilote.draw();
                Body hero = masterPilote.getHero();

                //TODO use this to center view place it in proper place
//                masterPilote.setCamera(hero.getPosition());

            });

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.render(graphics -> {
                masterPilote.repaint(WIDTH, HEIGHT);

            });
//


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
