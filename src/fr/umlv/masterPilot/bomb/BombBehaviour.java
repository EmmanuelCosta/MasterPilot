package fr.umlv.masterPilot.bomb;

import fr.umlv.masterPilot.common.UserSpec;
import fr.umlv.masterPilot.world.MasterPilotWorld;
import org.jbox2d.dynamics.Fixture;

import java.awt.*;

/**
 * Created by emmanuel on 21/12/13.
 */
public class BombBehaviour implements UserSpec {


    private final Bomb bomb;
    private boolean hide = false;
    private boolean addable = false;
    private boolean destroyed = false;
    private int state = 0;

    public BombBehaviour(Bomb bomb) {
        this.bomb = bomb;
    }



    @Override
    public void onCollide(Fixture fix2, boolean flag) {
        /**
         * hero take bomb
         */
        if ((fix2.getFilterData().categoryBits == MasterPilotWorld.HERO
                || fix2.getFilterData().categoryBits == MasterPilotWorld.SHIELD) && state == 0) {
            addable = true;
            hide = true;
            state++;



        } else if (this.bomb.getBombeState() != Bomb.BombState.NOT_ARMED) {
            hide = false;
        }
        if (flag == true) {
            if (this.bomb.getBombeState() == Bomb.BombState.ARMED) {
                /**
                 * can collide if is not hero or shield
                 */

                if (fix2.getFilterData().categoryBits != MasterPilotWorld.HERO
                        && fix2.getFilterData().categoryBits != MasterPilotWorld.SHIELD) {
                    this.bomb.boum();
                    destroyed = true;


                }
                /**
                 * collide with hero or shield
                 * only after bomb has been lauch
                 * not inside the launcher
                 */
                else if ((fix2.getFilterData().categoryBits == MasterPilotWorld.HERO
                        || fix2.getFilterData().categoryBits == MasterPilotWorld.SHIELD) && state >= 2) {
                    this.bomb.boum();
                    destroyed = true;


                }
                /**
                 * if is hero or shield do not explode
                 * when launching
                 */
                else if ((fix2.getFilterData().categoryBits == MasterPilotWorld.HERO
                        || fix2.getFilterData().categoryBits == MasterPilotWorld.SHIELD)) {
                    state++;
                    hide = false;


                }

            }

        }
    }

    @Override
    public boolean isDestroyable() {
        return destroyed;
    }

    @Override
    public boolean isItem() {
        return addable;
    }

    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY;
    }

    @Override
    public boolean getSensor() {
        return hide;
    }
}
