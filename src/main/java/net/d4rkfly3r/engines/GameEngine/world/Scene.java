package net.d4rkfly3r.engines.GameEngine.world;

import net.d4rkfly3r.engines.GameEngine.world.tiles.Brick2Tile;
import net.d4rkfly3r.engines.GameEngine.world.tiles.BrickTile;
import net.d4rkfly3r.engines.GameEngine.world.tiles.BrokenStoneTile;
import net.d4rkfly3r.engines.GameEngine.world.tiles.Tile;

public class Scene {
    private Tile[][][] tileLayers = new Tile[5][200][200];

    {
        tileLayers[0][1][1] = new BrickTile();
        tileLayers[0][2][1] = new BrickTile();
        tileLayers[0][3][1] = new BrickTile();
        tileLayers[0][4][1] = new BrickTile();
        tileLayers[0][5][1] = new BrickTile();
        tileLayers[0][6][1] = new BrickTile();
        tileLayers[0][6][0] = new BrickTile();

        tileLayers[0][1][2] = new BrokenStoneTile();
        tileLayers[0][2][2] = new BrokenStoneTile();
        tileLayers[0][3][2] = new BrokenStoneTile();
        tileLayers[0][4][2] = new BrokenStoneTile();
        tileLayers[0][5][2] = new BrokenStoneTile();
        tileLayers[0][6][2] = new BrokenStoneTile();

        tileLayers[0][1][3] = new BrickTile();
        tileLayers[0][2][3] = new BrickTile();
        tileLayers[0][3][3] = new BrickTile();
        tileLayers[0][4][3] = new BrickTile();
        tileLayers[0][5][3] = new BrickTile();
        tileLayers[0][6][3] = new BrickTile();
        tileLayers[0][6][4] = new BrickTile();
        tileLayers[0][6][5] = new BrickTile();
        tileLayers[0][6][6] = new BrickTile();

        tileLayers[0][7][0] = new BrokenStoneTile();
        tileLayers[0][7][1] = new BrokenStoneTile();
        tileLayers[0][7][2] = new BrokenStoneTile();
        tileLayers[0][7][3] = new BrokenStoneTile();
        tileLayers[0][7][4] = new BrokenStoneTile();
        tileLayers[0][7][5] = new BrokenStoneTile();
        tileLayers[0][7][6] = new BrokenStoneTile();

        tileLayers[0][8][0] = new BrickTile();
        tileLayers[0][8][1] = new BrickTile();
        tileLayers[0][8][2] = new BrickTile();
        tileLayers[0][8][3] = new BrickTile();
        tileLayers[0][8][4] = new BrickTile();
        tileLayers[0][8][5] = new BrickTile();
        tileLayers[0][8][6] = new BrickTile();

        tileLayers[0][6][7] = new Brick2Tile();
        tileLayers[0][7][7] = new Brick2Tile();
        tileLayers[0][8][7] = new Brick2Tile();
    }

    public void render() {
        for (int layer = 0; layer < tileLayers.length; layer++) {
            for (int x = 0; x < tileLayers[layer].length; x++) {
                for (int y = 0; y < tileLayers[layer][x].length; y++) {
                    final Tile tile = tileLayers[layer][x][y];
                    if (tile != null) {
                        tile.render(x * 64, y * 64);
                    }
                }
            }
        }
    }
}
