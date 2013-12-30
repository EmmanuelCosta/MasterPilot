package fr.umlv.masterPilot.graphic;

import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.pooling.arrays.IntArray;
import java.awt.*;


/**
 * This is graphical part of MasterPilotWorld Game
 * It use the viewportTransform class of jbox2d
 * which contains some interesting methods
 * for translation calculation
 * <p>
 * using @IViewportTransform which is use every time to manage with transformation from jbox world to oustide
 * <p>
 * Created by emmanuel on 10/12/13.
 */
public class MasterPilot2D {


    private final IViewportTransform viewportTransform;
    private final Graphics2D graphic;

    public MasterPilot2D(Graphics2D graphic) {

        viewportTransform = new OBBViewportTransform();
        viewportTransform.setYFlip(true);
        this.graphic = graphic;


    }

    public void drawString(Vec2 position, String s) {
        Vec2 sp1 = new Vec2();


        getWorldToScreenToOut(position, sp1);

//        AffineTransform inverse = new AffineTransform();
//        AffineTransform transform = new AffineTransform() ;
//        transform.scale(20,20);
//        inverse.scale(-20,20);
//
//        Graphics2D g2d = (Graphics2D) graphic ;
//        g2d.transform(transform);
        this.graphic.setColor(Color.white);
        this.graphic.drawString(s, (int) sp1.x - 2, (int) sp1.y + 2);
//        g2d.transform(inverse);
    }

    public void drawSegment(Vec2 p1, Vec2 p2, Color color) {

        Vec2 sp1 = new Vec2();
        Vec2 sp2 = new Vec2();

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
        Vec2 sp1 = new Vec2();

        getWorldToScreenToOut(center, sp1);

        graphic.setColor(Color.white);
        graphic.drawOval((int) sp1.x, (int) sp1.y, (int) radius + 30, (int) radius + 30);


    }

    private void emptyCircle(Vec2 center, Color color, int radius) {

        Vec2 sp1 = new Vec2();
        getWorldToScreenToOut(center, sp1);


        //  System.out.println((int) ((int) sp1.x - (radius))+"  "+(int) ((int) sp1.y - (radius)) +" radius "
//        +(int) ((int) sp2.x - (int) sp1.x) * 2);
        graphic.drawOval((int) ((int) sp1.x - (radius)),
                (int) ((int) sp1.y - (radius)),
                radius * 2, radius * 2);


    }

    private void filledCircle(Vec2 center, Color color, int radius) {
        Vec2 sp1 = new Vec2();

        getWorldToScreenToOut(center, sp1);


        graphic.fillOval((int) ((int) sp1.x - (radius)),
                (int) ((int) sp1.y - (radius)),
                radius * 2, radius * 2);

//        graphic.fillOval((int) ((int) sp1.x - (radius)),
//                (int) ((int) sp1.y - (radius)),
//                (int) ((int) sp2.x - (int) sp1.x) * 2, (int) ((int) sp2.x - (int) sp1.x) * 2);

    }

    public void drawFilledPolygon(Vec2[] vertices, int vertexCount, Color color) {

        IntArray xIntsPool = new IntArray();
        IntArray yIntsPool = new IntArray();

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
     * This method will position your graphic cam to the specify coordinate(x,y)
     *
     * @param position : the position af the camera
     */
    public void setCamera(Vec2 position) {
        viewportTransform.setCenter(position);
    }

    /**
     * Use this to get position in the world and convert it into outside coordinate
     *
     * @param argWorld
     * @param argScreen
     * 
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
     * made a translation of (0,0) to (i,j)
     *
     * @param i
     * @param j
     */
    public void setLandMark(int i, int j) {
        this.viewportTransform.setExtents(i, j);

    }

    public void drawFrameworkClock(int hour, int minute, int second) {

        this.graphic.setColor(Color.WHITE);
        this.graphic.fill3DRect(0, 0, 100, 30, true);

//           this.graphic.draw3DRect(0,0,100,30,false);

        this.graphic.setColor(Color.BLACK);

        StringBuilder strb = new StringBuilder();

        strb.append(hour)
            .append(":")
            .append(minute)
            .append(":")
            .append(second);

        Font font = this.graphic.getFont();
        this.graphic.setFont(new Font(null, Font.PLAIN, 28));
        this.graphic.drawString(strb.toString(), 7, 23);
        this.graphic.setFont(font);


    }


    public void drawFrameworkEnd(boolean win,int x,int y) {

      if(win){
          this.graphic.setColor(Color.WHITE);
          //this.graphic.fill3DRect(0, 0, 100, 30, true);
          Font font = this.graphic.getFont();
          this.graphic.setFont(new Font(null, Font.PLAIN, 38));
          this.graphic.drawString(" WINNER ", x/2,y/2);
          this.graphic.setFont(font);

      }else{
          this.graphic.setColor(Color.GRAY);

          Font font = this.graphic.getFont();
          this.graphic.setFont(new Font(null, Font.PLAIN, 38));
          this.graphic.drawString(" GAME OVER ", x/2,y/2);
          this.graphic.drawString(" YOU LOSE ", x,y);
          this.graphic.setFont(font);
      }
    }
}
