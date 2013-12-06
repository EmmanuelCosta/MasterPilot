package fr.umlv.masterPilote.world;

import fr.umlv.masterPilote.MasterPilote;
import fr.umlv.masterPilote.MasterPiloteMotor;
import fr.umlv.zen3.Application;

/**
 * Created by emmanuel on 06/12/13.
 */
public class Main {
    public static void main(String[] args) {
        MasterPiloteMotor mPM = new MasterPiloteMotor();

        Application.run("Master Pilote Game", mPM.getWIDTH(), mPM.getHEIGHT(), context -> {
            ;
            context.render(graphics -> {

                mPM.launchGame(context);
                System.out.println(graphics.hashCode());

            });

        });

    }
}
