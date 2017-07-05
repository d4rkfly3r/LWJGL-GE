package net.d4rkfly3r.engines.GameEngine.world.tiles;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;

public class BrokenStoneTile extends Tile {

    public BrokenStoneTile() {
        super(TextureManager.i().getTexture("ground/broken_stone"));
    }

}
