package net.d4rkfly3r.engines.GameEngine.world.tiles;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;

public class BrickTile extends Tile {

    public BrickTile() {
        super(TextureManager.i().getTexture("walls/brick"));
    }

}
