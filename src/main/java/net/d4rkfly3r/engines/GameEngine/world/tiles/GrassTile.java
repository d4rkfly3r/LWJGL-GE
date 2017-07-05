package net.d4rkfly3r.engines.GameEngine.world.tiles;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;

import static org.lwjgl.opengl.GL11.*;

public class GrassTile extends Tile {

    public GrassTile() {
        super(TextureManager.i().getTexture("ground/grass"));
    }

}
