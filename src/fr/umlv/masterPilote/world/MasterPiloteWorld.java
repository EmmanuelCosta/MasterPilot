package fr.umlv.masterPilote.world;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class MasterPiloteWorld {
	public static World world = new World(new Vec2(0, 0));
	public static float timeStep = 1.0f / 60.f;
	public static int velocityIterations = 6;
	public static int positionIterations = 3;
}
