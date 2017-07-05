package net.d4rkfly3r.engines.GameEngine.world.tiles;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;

import static org.lwjgl.opengl.GL11.*;

public class SandTile extends Tile {

    public SandTile() {
        super(TextureManager.i().getTexture("ground/sand"));
    }

}
