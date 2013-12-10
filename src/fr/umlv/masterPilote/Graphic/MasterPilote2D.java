package fr.umlv.masterPilote.Graphic;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.*;
import org.jbox2d.pooling.arrays.IntArray;
import org.jbox2d.pooling.arrays.Vec2Array;
import org.jbox2d.testbed.pooling.ColorPool;

import java.awt.*;

/**
 * This is graphical part of MasterPilote Game
 * It use the viewportTransform class of jbox2d
 * which contains some interisting methods
 * for translation calculation
 * Created by emmanuel on 10/12/13.
 */
public class MasterPilote2D {


    private final static IntArray xIntsPool = new IntArray();
    private final static IntArray yIntsPool = new IntArray();
    public static int circlePoints = 13;
    private final IViewportTransform viewportTransform;
    private final Graphics graphic;
    private final ColorPool cpool = new ColorPool();
    private final Vec2Array vec2Array = new Vec2Array();
    private final Vec2 sp1 = new Vec2();
    private final Vec2 sp2 = new Vec2();
    private final Vec2 saxis = new Vec2();
    // TODO change IntegerArray to a specific class for int[] arrays
    private final Vec2 temp = new Vec2();
    private final Vec2 temp2 = new Vec2();

    public MasterPilote2D(Graphics graphic) {

        viewportTransform = new OBBViewportTransform();
        viewportTransform.setYFlip(true);
        this.graphic = graphic;

    }

    public void drawCircle(Vec2 center, float radius, Color color) {

        Vec2[] vecs = vec2Array.get(circlePoints);
        generateCirle(center, radius, vecs, circlePoints);
        drawPolygon(vecs, circlePoints, color);
    }

    public void drawPoint(Vec2 argPoint, float argRadiusOnScreen,
                          Color argColor) {
        getWorldToScreenToOut(argPoint, sp1);
        Graphics g = getGraphics();


        g.setColor(argColor);
        sp1.x -= argRadiusOnScreen;
        sp1.y -= argRadiusOnScreen;
        g.fillOval((int) sp1.x, (int) sp1.y, (int) argRadiusOnScreen * 2,
                (int) argRadiusOnScreen * 2);
    }

    public void drawSegment(Vec2 p1, Vec2 p2, Color color) {
        getWorldToScreenToOut(p1, sp1);
        getWorldToScreenToOut(p2, sp2);

        Graphics g = getGraphics();



        g.setColor(color);

        g.drawLine((int) sp1.x, (int) sp1.y, (int) sp2.x, (int) sp2.y);

    }

    public void drawAABB(AABB argAABB, Color color) {
        Vec2 vecs[] = vec2Array.get(4);
        argAABB.getVertices(vecs);
        drawPolygon(vecs, 4, color);
    }

    public void drawFilledCircle(Vec2 center, float radius, Vec2 axis,
                                 Color color) {
        Vec2[] vecs = vec2Array.get(circlePoints);
        generateCirle(center, radius, vecs, circlePoints);

        if (axis != null) {
            saxis.set(axis).mulLocal(radius).addLocal(center);
            drawSegment(center, saxis, color);
            drawCircle2(center, saxis, color, (int) radius);
        }
    }



    public void drawCircle2(Vec2 center, Vec2 axis, Color color,int radius) {
       getWorldToScreenToOut(center, sp1);
        getWorldToScreenToOut(axis, sp2);
        graphic.fillOval((int) ((int) sp1.x - (radius)),
                (int) ((int) sp1.y - (radius)),
                (int) ((int) sp2.x - (int) sp1.x) * 2, (int) ((int) sp2.x - (int) sp1.x) * 2);

    }

    public void drawFilledPolygon(Vec2[] vertices, int vertexCount, Color color) {

        // inside
        Graphics g = getGraphics();
        int[] xInts = xIntsPool.get(vertexCount);
        int[] yInts = yIntsPool.get(vertexCount);

        for (int i = 0; i < vertexCount; i++) {
            getWorldToScreenToOut(vertices[i], temp);
            xInts[i] = (int) temp.x;
            yInts[i] = (int) temp.y;

//			 System.out.println("====>  "+temp);
        }


        g.setColor(color);


        g.fillPolygon(xInts, yInts, vertexCount);

        // outside
        drawPolygon(vertices, vertexCount, color);
    }

    public void drawString(float x, float y, String s, Color3f color) {
        Graphics g = getGraphics();
        if (g == null) {
            return;
        }
        Color c = cpool.getColor(color.x, color.y, color.z);
        g.setColor(c);
        g.drawString(s, (int) x, (int) y);
    }

    /**
     * Draw a closed polygon provided in CCW order.  This implementation
     * uses {@link #drawSegment(Vec2, Vec2, Color)} to draw each side of the
     * polygon.
     *
     * @param vertices
     * @param vertexCount
     * @param color
     */
    public void drawPolygon(Vec2[] vertices, int vertexCount, Color color) {
        if (vertexCount == 1) {
            drawSegment(vertices[0], vertices[0], color);
            return;
        }

        for (int i = 0; i < vertexCount - 1; i += 1) {
            drawSegment(vertices[i], vertices[i + 1], color);
        }

        if (vertexCount > 2) {
            drawSegment(vertices[vertexCount - 1], vertices[0], color);
        }
    }

    public Graphics getGraphics() {
        return graphic;
    }

    public void drawTransform(Transform xf) {
        Graphics g = getGraphics();
        getWorldToScreenToOut(xf.p, temp);
        temp2.setZero();
        float k_axisScale = 0.4f;

        Color c = cpool.getColor(1, 0, 0);
        g.setColor(c);

        temp2.x = xf.p.x + k_axisScale * xf.q.c;
        temp2.y = xf.p.y + k_axisScale * xf.q.s;
        getWorldToScreenToOut(temp2, temp2);
        g.drawLine((int) temp.x, (int) temp.y, (int) temp2.x, (int) temp2.y);

        c = cpool.getColor(0, 1, 0);
        g.setColor(c);
        temp2.x = xf.p.x + k_axisScale * xf.q.c;
        temp2.y = xf.p.y + k_axisScale * xf.q.s;
        getWorldToScreenToOut(temp2, temp2);
        g.drawLine((int) temp.x, (int) temp.y, (int) temp2.x, (int) temp2.y);
    }

    // CIRCLE GENERATOR

    private void generateCirle(Vec2 argCenter, float argRadius,
                               Vec2[] argPoints, int argNumPoints) {
        float inc = MathUtils.TWOPI / argNumPoints;

        for (int i = 0; i < argNumPoints; i++) {
            argPoints[i].x = (argCenter.x + MathUtils.cos(i * inc) * argRadius);
            argPoints[i].y = (argCenter.y + MathUtils.sin(i * inc) * argRadius);
        }
    }

    public void drawString(Vec2 pos, String s, Color3f color) {
        drawString(pos.x, pos.y, s, color);
    }

    /**
     * use this to manage with translation and rotation
     *
     * @return
     */
    public IViewportTransform getViewportTranform() {
        return viewportTransform;
    }

    public void setCamera(float x, float y, float scale) {
        viewportTransform.setCamera(x, y, scale);
    }

    /**
     * @param argScreen
     * @param argWorld
     * @see org.jbox2d.common.IViewportTransform#getScreenToWorld(org.jbox2d.common.Vec2, org.jbox2d.common.Vec2)
     */
    public void getScreenToWorldToOut(Vec2 argScreen, Vec2 argWorld) {
        viewportTransform.getScreenToWorld(argScreen, argWorld);
    }

    /**
     * @param argWorld
     * @param argScreen
     * @see org.jbox2d.common.IViewportTransform#getWorldToScreen(org.jbox2d.common.Vec2, org.jbox2d.common.Vec2)
     */
    public void getWorldToScreenToOut(Vec2 argWorld, Vec2 argScreen) {
        viewportTransform.getWorldToScreen(argWorld, argScreen);
    }

    /**
     * Takes the world coordinates and puts the corresponding screen
     * coordinates in argScreen.
     *
     * @param worldX
     * @param worldY
     * @param argScreen
     */
    public void getWorldToScreenToOut(float worldX, float worldY, Vec2 argScreen) {
        argScreen.set(worldX, worldY);
        viewportTransform.getWorldToScreen(argScreen, argScreen);
    }

    /**
     * takes the world coordinate (argWorld) and returns
     * the screen coordinates.
     *
     * @param argWorld
     */
    public Vec2 getWorldToScreen(Vec2 argWorld) {
        Vec2 screen = new Vec2();
        viewportTransform.getWorldToScreen(argWorld, screen);
        return screen;
    }

    /**
     * Takes the world coordinates and returns the screen
     * coordinates.
     *
     * @param worldX
     * @param worldY
     */
    public Vec2 getWorldToScreen(float worldX, float worldY) {
        Vec2 argScreen = new Vec2(worldX, worldY);
        viewportTransform.getWorldToScreen(argScreen, argScreen);
        return argScreen;
    }

    /**
     * takes the screen coordinates and puts the corresponding
     * world coordinates in argWorld.
     *
     * @param screenX
     * @param screenY
     * @param argWorld
     */
    public void getScreenToWorldToOut(float screenX, float screenY, Vec2 argWorld) {
        argWorld.set(screenX, screenY);
        viewportTransform.getScreenToWorld(argWorld, argWorld);
    }

    /**
     * takes the screen coordinates (argScreen) and returns
     * the world coordinates
     *
     * @param argScreen
     */
    public Vec2 getScreenToWorld(Vec2 argScreen) {
        Vec2 world = new Vec2();
        viewportTransform.getScreenToWorld(argScreen, world);
        return world;
    }

    /**
     * takes the screen coordinates and returns the
     * world coordinates.
     *
     * @param screenX
     * @param screenY
     */
    public Vec2 getScreenToWorld(float screenX, float screenY) {
        Vec2 screen = new Vec2(screenX, screenY);
        viewportTransform.getScreenToWorld(screen, screen);
        return screen;
    }

}
