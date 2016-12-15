package com.zelda.game;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.GameObject;

public abstract class ClientGameObject extends GameObject {

    public abstract void loadPosition(PositionUpdater positionUpdater);

    public abstract void draw(SpriteBatch sprBatch);

    public abstract void update(Collection<ClientGameObject> collection);

}
