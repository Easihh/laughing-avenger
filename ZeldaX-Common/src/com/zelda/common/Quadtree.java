package com.zelda.common;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.geometry.Rectangle2D;

public class Quadtree<E extends GameObject>{
    
    private List<E> elements;
    private int maxElemPerQuad;
    private Quadtree<E> topLeft;
    private Quadtree<E> topRight;
    private Quadtree<E> botLeft;
    private Quadtree<E> botRight;
    private Rectangle2D bounds;
    
    public Quadtree(int x,int y,int width,int height,int elemPerQuad){
        bounds=new Rectangle2D(x, y, width, height);
        //Inflate bounds since contains method of rectangle doesnt match for point exactly on the boundary
        bounds=new Rectangle2D(bounds.getMinX()-0.01,bounds.getMinY()-0.01,bounds.getWidth()+0.02,bounds.getHeight()+0.02);
        maxElemPerQuad=elemPerQuad;
        elements=new ArrayList<E>(maxElemPerQuad);
    }
    
    public boolean insert(E e){
        if(!bounds.contains(e.getMask().getX(),e.getMask().getY())){
            return false;
        }
        elements.add(e);
        if (elements.size() > maxElemPerQuad) {
            if (!hasChildren()) {
                subDivide();
            }
            Iterator<E> itr = elements.iterator();
            while (itr.hasNext()) {
                E current = itr.next();
                int halfHeight = (int) bounds.getHeight() / 2;
                int halfWidth = (int) bounds.getWidth() / 2;
                int x = (int) bounds.getMinX();
                int y = (int) bounds.getMinY();
                Rectangle2D TopLeftBounds = new Rectangle2D(x, y, halfWidth, halfHeight);
                Rectangle2D TopRightBounds = new Rectangle2D(x + halfWidth, y, halfWidth, halfHeight);
                Rectangle2D BottomRightBounds = new Rectangle2D(x + halfWidth, y + halfHeight, halfWidth, halfHeight);
                Rectangle2D BottomLeftBounds = new Rectangle2D(x, y + halfHeight, halfWidth, halfHeight);

                reInsertAfterDivision(TopLeftBounds, current, topLeft, "Top Left");
                reInsertAfterDivision(TopRightBounds, current, topRight, "Top Right");
                reInsertAfterDivision(BottomLeftBounds, current, botLeft, "Bottom Left");
                reInsertAfterDivision(BottomRightBounds, current, botRight, "Bottom Right");

                itr.remove();
            }
        }
        return true;
    }

    private boolean reInsertAfterDivision(Rectangle2D bound, E element, Quadtree<E> qTree, String text) {
        if (bound.contains(element.getMask().getX(), element.getMask().getY())) {
            return qTree.insert(element);
        }
        return false;
    }
    
    private boolean hasChildren() {
        return topLeft != null;
    }
    
    private boolean subDivide() {
        int hWidth = (int) bounds.getWidth() / 2;
        int hHeight = (int) bounds.getHeight() / 2;
        int x = (int) bounds.getMinX();
        int y = (int) bounds.getMinY();
        topLeft = new Quadtree<E>(x, y,hWidth,hHeight, maxElemPerQuad);
        topRight = new Quadtree<E>(x + hWidth, y,hWidth,hHeight, maxElemPerQuad);
        botLeft = new Quadtree<E>(x, y + hHeight,hWidth,hHeight, maxElemPerQuad);
        botRight = new Quadtree<E>(x + hWidth, y + hHeight,hWidth,hHeight, maxElemPerQuad);
        return true;
    }
    
    /**
     * This method return the List of Object in the Quad Tree without Duplicate
     **/
    public List<? extends GameObject> QuadToList() {
        Set<E> quadSet = new HashSet<>();
        List<E> returnList;
        quadSet.addAll(elements);

        if (hasChildren()) {
            quadSet.addAll(traverse(topLeft));
            quadSet.addAll(traverse(topRight));
            quadSet.addAll(traverse(botLeft));
            quadSet.addAll(traverse(botRight));
        }
        returnList = new LinkedList<>(quadSet);
        return returnList;
    }

    private Collection<? extends E> traverse(Quadtree<E> quadrant) {
        Set<E> quadSet = new HashSet<>();
        List<E> returnList;

        quadSet.addAll(quadrant.elements);
        if (quadrant.hasChildren()) {
            quadSet.addAll(traverse(quadrant.topLeft));
            quadSet.addAll(traverse(quadrant.topRight));
            quadSet.addAll(traverse(quadrant.botLeft));
            quadSet.addAll(traverse(quadrant.botRight));
        }
        returnList = new LinkedList<E>(quadSet);
        return returnList;
    }
    
    public boolean isColliding(java.awt.geom.Rectangle2D mask){
        if (hasChildren()) {
            if (findCollision(topLeft,mask) || findCollision(topRight,mask) || findCollision(botLeft,mask)
                            || findCollision(botRight,mask)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean findCollision(Quadtree<E> quadrant, java.awt.geom.Rectangle2D mask) {
        boolean collisionFound = false;
        if (quadrant.hasChildren()) {
            int halfHeight = (int) bounds.getHeight() / 2;
            int halfWidth = (int) bounds.getWidth() / 2;
            int x = (int) bounds.getMinX();
            int y = (int) bounds.getMinY();
            Rectangle2D TopLeftBounds = new Rectangle2D(x, y, halfWidth, halfHeight);
            Rectangle2D TopRightBounds = new Rectangle2D(x + halfWidth, y, halfWidth, halfHeight);
            Rectangle2D BottomRightBounds = new Rectangle2D(x + halfWidth, y + halfHeight, halfWidth, halfHeight);
            Rectangle2D BottomLeftBounds = new Rectangle2D(x, y + halfHeight, halfWidth, halfHeight);
            if (TopLeftBounds.contains(mask.getX(),mask.getY())) {
                if (findCollision(quadrant.topLeft, mask))
                    return true;
            }
            if (TopRightBounds.contains(mask.getX(),mask.getY())) {
                if (findCollision(quadrant.topRight, mask))
                    return true;
            }
            if (BottomLeftBounds.contains(mask.getX(),mask.getY())) {
                if (findCollision(quadrant.botLeft, mask))
                    return true;
            }
            if (BottomRightBounds.contains(mask.getX(),mask.getY())) {
                if (findCollision(quadrant.botRight, mask))
                    return true;
            }
        }
        //now at leaf level check List of Node
        for(E element:quadrant.elements){
            if(element.intersect(element.getMask(), mask, new Point(0,0))){
                collisionFound=true;
                break;
            }
        }
        return collisionFound;
    }
}
    