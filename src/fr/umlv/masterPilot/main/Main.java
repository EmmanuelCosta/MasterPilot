package fr.umlv.masterPilot.main;

import fr.umlv.masterPilot.MasterPilotMotor;
import fr.umlv.zen3.Application;

/**
 * Created by emmanuel on 06/12/13.
 */
public class Main {
    public static void main(String[] args) {
        MasterPilotMotor mPM = new MasterPilotMotor();

        Application.run("Master Pilote Game", mPM.getWIDTH(), mPM.getHEIGHT(), context -> {

            context.render(graphics -> {
                mPM.launchGame(context);
            });
        });

    }
}