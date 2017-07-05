package net.d4rkfly3r.engines.GameEngine.world;

import net.d4rkfly3r.engines.GameEngine.graphics.Texture;

public abstract class Tile {

    Texture texture;
    boolean obstructive = false;

    public Tile(final Texture texture) {
        this.texture = texture;
    }

    public abstract void render();
}
