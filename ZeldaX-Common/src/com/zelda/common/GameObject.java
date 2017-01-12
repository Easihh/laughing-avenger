package com.zelda.common;


import java.awt.Point;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {
    protected int xPosition;
    protected int yPosition;
    protected int height;
    protected int width;
    protected Rectangle2D mask;
    protected int idIdentifier;
    
    /** Rectangle order matters, first rect is the check/move obj and the 2nd rectangle is the object you check against **/
    public static boolean intersect(Rectangle2D obj1Mask, Rectangle2D obj2Mask, Point offset) {
        double rectAx = obj1Mask.getX();
        double rectAxSize = obj1Mask.getWidth();
        double rectAy = obj1Mask.getY();
        double rectAySize = obj1Mask.getHeight();
        double rectBx = obj2Mask.getX();
        double rectBxSize = obj2Mask.getWidth();
        double rectBy = obj2Mask.getY();
        double rectBySize = obj2Mask.getHeight();
        return rectAx + (double) offset.x < rectBx + rectBxSize && rectAx + rectAxSize + (double) offset.x > rectBx
                        && rectAy + (double) offset.y < rectBy + rectBySize
                        && rectAy + rectAySize + (double) offset.y > rectBy;                                                              
    }

    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        this.updateMask();
    }

    protected void updateMask() {
        this.mask.setRect(this.xPosition, this.yPosition, this.width, this.height);
    }

    public int getxPosition() {
        return this.xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return this.yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public Rectangle2D getMask() {
        return this.mask;
    }

    public String getFullIdentifier() {
        return this.getTypeIdenfitier() + this.getIdIdentifier();
    }

    public int getIdIdentifier() {
        return this.idIdentifier;
    }

    public abstract String getTypeIdenfitier();
}