package fr.umlv.masterPilot.Interface;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public interface Bomb {
    public enum BombType{
        NONE,BOMB,MEGABOMB;
    }
	public void boum();

    /**
     * we simulate here an explode blast
     *
     * we will apply force on the body
     * @see c++ source here : http://www.iforce2d.net/b2dtut/explosions
     *
     * @param body
     * @param blastCenter
     * @param applyPoint
     * @param blastPower
     */
    public default void applyBlastImpulse(Body body, Vec2 blastCenter, Vec2 applyPoint, float blastPower) {
        Vec2 blastDir = applyPoint.sub(blastCenter);
        float blastRadius = blastDir.normalize();

        if (blastRadius == 0)
            return;
        float invDistance = 1 / blastRadius;

        float impulseMag = blastPower * invDistance * invDistance;

        body.applyLinearImpulse(blastDir.mul(impulseMag), applyPoint);

    }



}
