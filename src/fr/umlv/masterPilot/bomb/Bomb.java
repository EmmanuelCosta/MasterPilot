package fr.umlv.masterPilot.bomb;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * all bomb in the game will derive from that intrerface
 */
public interface Bomb {
    /**
     * type of bomb
     *
     * Bomb is a explosion bomb
     * <p>
     *  MegaBomb is an implosion bomb
     * </p>
     * <p>
     *     NONE : is use in the spaceShip  that may have bomb in reserve
     *     it tell that they dont hold any
     * </p>
     */
    public enum BombType{
        NONE,BOMB,MEGABOMB;
    }

    /**
     * state of bomb
     * Not armed it can not explode
     * armed it can
     */
    public enum BombState{
        NOT_ARMED,ARMED,;
    }

    /**
     * call this to explode your bomb
     * this have to define the component of bomb
     * when it explode
     */
	public void boum();

    /**
     * we simulate here an explode blast
     *
     * we will apply force on the body
     * {@link} http://www.iforce2d.net/b2dtut/explosions
     *
     * @param body
     * @param blastCenter
     * @param applyPoint
     * @param blastPower
     */
    public default void applyBlastImpulse(Body body, Vec2 blastCenter, Vec2 applyPoint, float blastPower) {
        Vec2 blastDir = applyPoint.sub(blastCenter);
        float blastRadius, invDistance, impulseMag;
        
        blastRadius= blastDir.normalize();

        if (blastRadius == 0)
            return;
        
        invDistance = 1 / blastRadius;
        impulseMag = blastPower * invDistance * invDistance;

        body.applyLinearImpulse(blastDir.mul(impulseMag), applyPoint);
    }

    public BombType getBombType();

    public Body getBody();

    public default BombState getBombeState(){
        return BombState.NOT_ARMED;
    }

    /**
     * use to change the state of bomb
     * @param launched
     */
    public default void setBombState(BombState launched){
    }

}
