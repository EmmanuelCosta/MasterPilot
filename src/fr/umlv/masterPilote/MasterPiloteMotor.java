package fr.umlv.masterPilote;

import fr.umlv.masterPilote.bomb.ClassicBombTest;
import fr.umlv.zen3.ApplicationContext;
import org.jbox2d.common.Vec2;

import java.awt.*;
/**
 * Created by emmanuel on 06/12/13.
 */
public class MasterPiloteMotor {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

//TODO MOVE IT IN MASTERPILOTE WORLD

    float timeStep = 1.0f / 60.f;
    int velocityIterations = 8;
    int positionIterations = 3;

    public void launchGame(ApplicationContext context){
        context.render(graphics -> {
            MasterPilote masterPilote = initPlateform(graphics);
            populatedWorld(masterPilote);

            run(masterPilote,context);
        });


    }

    private void run(MasterPilote masterPilote, ApplicationContext context) {
        for(;;){
            masterPilote.getWorld()
                    .step(timeStep, velocityIterations, positionIterations);
            context.render(graphics->{
                masterPilote.draw();
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            context.render(graphics->{
                masterPilote.repaint(WIDTH,HEIGHT);
            });


        }
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    private void populatedWorld(MasterPilote masterPilote) {
        //TODO MANAGE WITH FILE CONFIG

        ClassicBombTest bombTest = new ClassicBombTest(masterPilote.getWorld(), 1, 5);

        bombTest.create();
        ClassicBombTest bombTest2 = new ClassicBombTest(masterPilote.getWorld(), 1, 30);

        bombTest2.create();
        bombTest2.getBody().applyForce(new Vec2(50f,-200f),new Vec2(50f,-20f));
//        bombTest.applyForce();
    }



    private MasterPilote initPlateform(Graphics2D graphics) {
        //Create Master Pilote
        MasterPilote mp = new MasterPilote(graphics);
        mp.initPlateForm(WIDTH,HEIGHT);

        // SET REFERENTIEL
        mp.setLandMark(WIDTH / 2, HEIGHT / 2);

        return mp;
    }



}
