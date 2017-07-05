package net.d4rkfly3r.engines.GameEngine.world.tiles;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;

public class StoneTile extends Tile {

    public StoneTile() {
        super(TextureManager.i().getTexture("ground/stone"));
    }

}
