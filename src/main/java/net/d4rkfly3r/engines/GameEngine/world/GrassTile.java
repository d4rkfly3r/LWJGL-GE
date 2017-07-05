package net.d4rkfly3r.engines.GameEngine.world;

import net.d4rkfly3r.engines.GameEngine.graphics.TextureManager;

import static org.lwjgl.opengl.GL11.*;

public class GrassTile extends Tile {
    private final int x;
    private final int y;

    public GrassTile(int x, int y) {
        super(TextureManager.i().getTexture("ground/grass"));
        this.x = x;
        this.y = y;
    }

    @Override
    public void render() {
        glBindTexture(GL_TEXTURE_2D, TextureManager.i().getStitchedTextureID());
        glColor4f(1f, 1f, 1f, 1f);
        glBegin(GL_QUADS);
        glTexCoord2d(texture.u, texture.v);
        glVertex2f(x, y);
        glTexCoord2d(texture.u, texture.v2);
        glVertex2f(x, y + 64);
        glTexCoord2d(texture.u2, texture.v2);
        glVertex2f(x + 64, y + 64);
        glTexCoord2d(texture.u2, texture.v);
        glVertex2f(x + 64, y);
        glEnd();

    }
}
