package com.zelda.game;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;

public abstract class ClientGameObject extends GameObject {

    public abstract void loadPosition(PositionUpdater positionUpdater);

    public abstract void draw(SpriteBatch sprBatch);

    public abstract void update(Collection<ClientGameObject> activeObjs,Quadtree<Tile> quadTree);

}
