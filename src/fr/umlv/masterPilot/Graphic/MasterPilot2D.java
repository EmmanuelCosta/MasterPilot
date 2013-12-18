package fr.umlv.masterPilot.Graphic;

import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.pooling.arrays.IntArray;

import java.awt.*;

/**
 * This is graphical part of MasterPilot Game
 * It use the viewportTransform class of jbox2d
 * which contains some interesting methods
 * for translation calculation
 *
 * it is inspire by DebugDraw2D which is a debug drawing way given by default by jbox lirairie
 * Created by emmanuel on 10/12/13.
 */
public class MasterPilot2D {


    private final IntArray xIntsPool;
    private final IntArray yIntsPool;
    private final IViewportTransform viewportTransform;
    private final Vec2 sp1 = new Vec2();
    private final Vec2 sp2 = new Vec2();
    private final Graphics graphic;

    public MasterPilot2D(Graphics graphic) {

        viewportTransform = new OBBViewportTransform();
        viewportTransform.setYFlip(true);
        this.graphic = graphic;

        xIntsPool = new IntArray();
        yIntsPool = new IntArray();
    }


    public void drawString(Vec2 position, String s) {
        getWorldToScreenToOut(position, sp1);

        this.graphic.setColor(Color.white);
        this.graphic.drawString(s, (int)sp1.x-2, (int)sp1.y+2);
    }
    public void drawSegment(Vec2 p1, Vec2 p2, Color color) {
        getWorldToScreenToOut(p1, sp1);
        getWorldToScreenToOut(p2, sp2);


        this.graphic.setColor(color);

        this.graphic.drawLine((int) sp1.x, (int) sp1.y, (int) sp2.x, (int) sp2.y);

    }

    public void drawCircle(Vec2 center, float radius,
                           Color color, boolean filled) {


        this.graphic.setColor(color);

        if (filled == true) {
            filledCircle(center, color, (int) radius);
        } else {
            emptyCircle(center, color, (int) radius);
        }


    }

    public void drawShield(Vec2 center, float radius) {
        getWorldToScreenToOut(center, sp1);

        graphic.setColor(Color.white);
        graphic.drawOval((int) sp1.x, (int) sp1.y, (int) radius + 30, (int) radius + 30);


    }

    private void emptyCircle(Vec2 center, Color color, int radius) {
        getWorldToScreenToOut(center, sp1);


        //  System.out.println((int) ((int) sp1.x - (radius))+"  "+(int) ((int) sp1.y - (radius)) +" radius "
//        +(int) ((int) sp2.x - (int) sp1.x) * 2);
        graphic.drawOval((int) ((int) sp1.x - (radius)),
                (int) ((int) sp1.y - (radius)),
                radius * 2, radius * 2);

    }

    private void filledCircle(Vec2 center, Color color, int radius) {
        getWorldToScreenToOut(center, sp1);


        graphic.fillOval((int) ((int) sp1.x - (radius)),
                (int) ((int) sp1.y - (radius)),
                radius * 2, radius * 2);

//        graphic.fillOval((int) ((int) sp1.x - (radius)),
//                (int) ((int) sp1.y - (radius)),
//                (int) ((int) sp2.x - (int) sp1.x) * 2, (int) ((int) sp2.x - (int) sp1.x) * 2);

    }

    public void drawFilledPolygon(Vec2[] vertices, int vertexCount, Color color) {


        int[] xInts = xIntsPool.get(vertexCount);
        int[] yInts = yIntsPool.get(vertexCount);
        Vec2 temp = new Vec2();

        for (int i = 0; i < vertexCount; i++) {
            getWorldToScreenToOut(vertices[i], temp);
            xInts[i] = (int) temp.x;
            yInts[i] = (int) temp.y;

        }


        this.graphic.setColor(color);


        this.graphic.fillPolygon(xInts, yInts, vertexCount);


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
     *
     * @param position : the position af the camera
     */
    public void setCamera(Vec2 position) {

        viewportTransform.setCenter(position);
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
     * This will set the (0,0) to the
     * <p>
     * specify param
     * <p>
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
