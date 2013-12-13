package fr.umlv.masterPilot.Graphic;

import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.pooling.arrays.IntArray;

import java.awt.*;

/**
 * This is graphical part of MasterPilot Game
 * It use the viewportTransform class of jbox2d
 * which contains some interisting methods
 * for translation calculation
 * Created by emmanuel on 10/12/13.
 */
public class MasterPilot2D {


    private final IntArray xIntsPool;
    private final IntArray yIntsPool;
    private final IViewportTransform viewportTransform;
    private final Vec2 sp1 = new Vec2();
    private final Vec2 sp2 = new Vec2();
    private final Vec2 saxis = new Vec2();
    private Graphics graphic;

    public MasterPilot2D(Graphics graphic) {

        viewportTransform = new OBBViewportTransform();
        viewportTransform.setYFlip(true);
        this.graphic = graphic;

        xIntsPool = new IntArray();
        yIntsPool = new IntArray();
    }

    public void drawSegment(Vec2 p1, Vec2 p2, Color color) {
        getWorldToScreenToOut(p1, sp1);
        getWorldToScreenToOut(p2, sp2);


        this.graphic.setColor(color);

        this.graphic.drawLine((int) sp1.x, (int) sp1.y, (int) sp2.x, (int) sp2.y);

    }

    public void drawCircle(Vec2 center, float radius, Vec2 axis,
                           Color color, boolean filled) {

        saxis.set(axis).mulLocal(radius).addLocal(center);

        this.graphic.setColor(color);

        if (filled == true) {
            filledCircle(center, saxis, color, (int) radius);
        } else {
            emptyCircle(center, saxis, color, (int) radius);
        }


    }

    private void emptyCircle(Vec2 center, Vec2 axis, Color color, int radius) {
        getWorldToScreenToOut(center, sp1);
        getWorldToScreenToOut(axis, sp2);

        graphic.drawOval((int) ((int) sp1.x - (radius)),
                (int) ((int) sp1.y - (radius)),
                (int) ((int) sp2.x - (int) sp1.x) * 2, (int) ((int) sp2.x - (int) sp1.x) * 2);

    }

    private void filledCircle(Vec2 center, Vec2 axis, Color color, int radius) {
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
        Vec2 temp = new Vec2();

        for (int i = 0; i < vertexCount; i++) {
            getWorldToScreenToOut(vertices[i], temp);
            xInts[i] = (int) temp.x;
            yInts[i] = (int) temp.y;

        }


        g.setColor(color);


        g.fillPolygon(xInts, yInts, vertexCount);

        // outside
        drawPolygon(vertices, vertexCount, color);
    }

    /**
     * Draw Polygon
     *
     * @param vertices
     * @param vertexCount
     * @param color
     */
    public void drawPolygon(Vec2[] vertices, int vertexCount, Color color) {


        for (int i = 0; i < vertexCount - 1; i += 1) {
            drawSegment(vertices[i], vertices[i + 1], color);
        }


    }

    public Graphics getGraphics() {
        return this.graphic;
    }

    /**
     * use this to manage with translation and rotation
     *
     * @return
     */
    public IViewportTransform getViewportTranform() {
        return viewportTransform;
    }

    /**
     * This method will position your graphic cam to the specify coordinate(x,y)
     * @param position : the position af the camera
     */
    public void setCamera(Vec2 position) {

        viewportTransform.setCenter(position);
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
        this.viewportTransform.getScreenToWorld(screen, screen);
        return screen;
    }

    /**
     * This will set the (0,0) to the
     *
     * specify param
     *
     * use this to change referentiel
     * made a translation of (0,0) -> (i,j)
     *
     * @param i
     * @param j
     */
    public void setLandMark(int i, int j) {
        this.viewportTransform.setExtents(i, j);

    }

}
