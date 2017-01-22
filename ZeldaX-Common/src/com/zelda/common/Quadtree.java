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

public class Quadtree<E extends GameObject> {

    private List<E> elements;
    private int maxElemPerQuad;
    private Quadtree<E> topLeft;
    private Quadtree<E> topRight;
    private Quadtree<E> botLeft;
    private Quadtree<E> botRight;
    private Rectangle2D bounds;

    public Quadtree(int x, int y, int width, int height, int elemPerQuad) {
        bounds = new Rectangle2D(x, y, width, height);
        // Inflate bounds since contains method of rectangle doesnt match for
        // point exactly on the boundary
        bounds = new Rectangle2D(bounds.getMinX() - 0.01, bounds.getMinY() - 0.01, bounds.getWidth() + 0.02,
                        bounds.getHeight() + 0.02);
        maxElemPerQuad = elemPerQuad;
        elements = new ArrayList<E>(maxElemPerQuad);
    }
    
    public void clear(){
        elements.clear();
        topLeft = null;
        topRight = null;
        botLeft = null;
        botRight = null;
        bounds = null;
    }

    public boolean insert(E e) {
        if (hasChildren()) {
            if (!searchAndInsert(e)) {
                elements.add(e);
            }
        } else {
            elements.add(e);
            List<E> addBackToParent = new LinkedList<>();
            if (elements.size() > maxElemPerQuad) {
                subDivide();
                Iterator<E> itr = elements.iterator();
                while (itr.hasNext()) {
                    E current = itr.next();
                    if (!searchAndInsert(current))
                        addBackToParent.add(current);
                    itr.remove();
                }
            }
            elements.addAll(addBackToParent);
        }
        return true;
    }

    private boolean searchAndInsert(E e) {
        int halfHeight = (int) bounds.getHeight() / 2;
        int halfWidth = (int) bounds.getWidth() / 2;
        int x = (int) bounds.getMinX();
        int y = (int) bounds.getMinY();
        Rectangle2D TopLeftBounds = new Rectangle2D(x, y, halfWidth, halfHeight);
        Rectangle2D TopRightBounds = new Rectangle2D(x + halfWidth, y, halfWidth, halfHeight);
        Rectangle2D BottomRightBounds = new Rectangle2D(x + halfWidth, y + halfHeight, halfWidth, halfHeight);
        Rectangle2D BottomLeftBounds = new Rectangle2D(x, y + halfHeight, halfWidth, halfHeight);

        boolean successTopLeft = recursiveInsert(TopLeftBounds, e, topLeft, "Top Left");
        boolean successTopRight = recursiveInsert(TopRightBounds, e, topRight, "Top Right");
        boolean successBottomLeft = recursiveInsert(BottomLeftBounds, e, botLeft, "Bottom Left");
        boolean successBottomRight = recursiveInsert(BottomRightBounds, e, botRight, "Bottom Right");

        boolean success = successTopLeft || successTopRight || successBottomLeft || successBottomRight;

        return success;
    }

    private boolean recursiveInsert(Rectangle2D bound, E element, Quadtree<E> qTree, String text) {
        // Inflate bounds since contains method of rectangle doesnt match for
        // point exactly on the boundary
        
        Rectangle2D boundsInflated = new Rectangle2D(bounds.getMinX() - 0.01, bounds.getMinY() - 0.01,
                        bounds.getWidth() + 0.02, bounds.getHeight() + 0.02);
        if (boundsInflated.contains(element.getMask().getX(), element.getMask().getY(), element.getMask().getWidth(),
                        element.getMask().getHeight())) {
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
        topLeft = new Quadtree<E>(x, y, hWidth, hHeight, maxElemPerQuad);
        topRight = new Quadtree<E>(x + hWidth, y, hWidth, hHeight, maxElemPerQuad);
        botLeft = new Quadtree<E>(x, y + hHeight, hWidth, hHeight, maxElemPerQuad);
        botRight = new Quadtree<E>(x + hWidth, y + hHeight, hWidth, hHeight, maxElemPerQuad);
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

    public boolean isColliding(java.awt.geom.Rectangle2D mask, Point offset) {
        return findCollision(this, mask, offset);
    }

    private boolean findCollision(Quadtree<E> quadrant, java.awt.geom.Rectangle2D mask, Point offset) {
        boolean collisionFound = false;
        if (quadrant.hasChildren()) {
            int halfHeight = (int) quadrant.bounds.getHeight() / 2;
            int halfWidth = (int) quadrant.bounds.getWidth() / 2;
            int x = (int) quadrant.bounds.getMinX();
            int y = (int) quadrant.bounds.getMinY();
            Rectangle2D TopLeftBounds = new Rectangle2D(x, y, halfWidth, halfHeight);
            Rectangle2D TopRightBounds = new Rectangle2D(x + halfWidth, y, halfWidth, halfHeight);
            Rectangle2D BottomRightBounds = new Rectangle2D(x + halfWidth, y + halfHeight, halfWidth, halfHeight);
            Rectangle2D BottomLeftBounds = new Rectangle2D(x, y + halfHeight, halfWidth, halfHeight);
            double aheadMaskX = mask.getX() + offset.getX();
            double aheadMaskY = mask.getY() + offset.getY();

            if (TopLeftBounds.intersects(aheadMaskX, aheadMaskY, mask.getWidth(), mask.getHeight())) {
                if (findCollision(quadrant.topLeft, mask, offset))
                    return true;
            }
            if (TopRightBounds.intersects(aheadMaskX, aheadMaskY, mask.getWidth(), mask.getHeight())) {
                if (findCollision(quadrant.topRight, mask, offset))
                    return true;
            }
            if (BottomLeftBounds.intersects(aheadMaskX, aheadMaskY, mask.getWidth(), mask.getHeight())) {
                if (findCollision(quadrant.botLeft, mask, offset))
                    return true;
            }
            if (BottomRightBounds.intersects(aheadMaskX, aheadMaskY, mask.getWidth(), mask.getHeight())) {
                if (findCollision(quadrant.botRight, mask, offset))
                    return true;
            }
            // Node may have been inserted at the Quadrant level instead of leaf
            // level because it could not fit the child.
            if (checkNodeAtCurrentLevel(quadrant, mask, offset)) {
                return true;
            }
        } else {
            // now at leaf level check List of Node
            collisionFound = checkNodeAtCurrentLevel(quadrant, mask, offset);
        }
        return collisionFound;
    }

    private boolean checkNodeAtCurrentLevel(Quadtree<E> quadrant, java.awt.geom.Rectangle2D mask, Point offset) {
        for (E element : quadrant.elements) {
            if (GameObject.intersect(mask, element.getMask(), offset)) {
                return true;
            }
        }
        return false;
    }
}
