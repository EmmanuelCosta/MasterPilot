package fr.umlv.masterPilot.main;

import java.util.Objects;
import fr.umlv.masterPilot.MasterPilotMotor;
import fr.umlv.zen3.Application;


/**
 *Main class of the Game
 * Created by emmanuel on 06/12/13.
 * Version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        MasterPilotMotor mPM = new MasterPilotMotor();
        if(args.length == 1) {

            Application.run("Master Pilot Game", mPM.getWIDTH(), mPM.getHEIGHT(), context -> {
                context.render(graphics -> {
                    mPM.launchGame(context,Objects.requireNonNull(args[0]), "-c");
                });

            });
        } else {
            Application.run("Master Pilot Game", mPM.getWIDTH(), mPM.getHEIGHT(), context -> {
                context.render(graphics -> {
                    mPM.launchGame(context,Objects.requireNonNull(args[0]), args[1]);
                });
            });    
        } 
    }
}
