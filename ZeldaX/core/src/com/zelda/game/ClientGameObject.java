package com.zelda.game;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;

public abstract class ClientGameObject extends GameObject {
    
    abstract void loadPosition(PositionUpdater positionUpdater);

    abstract void draw(SpriteBatch sprBatch);

    abstract void update(Collection<ClientGameObject> activeObjs,Quadtree<Tile> quadTree);

}
