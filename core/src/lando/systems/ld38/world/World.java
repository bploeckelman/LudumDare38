package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.lib.openSimplexNoise.OpenSimplexNoise;

/**
 * Created by dsgraham on 4/22/17.
 */
public class World {


    public static final int WORLD_WIDTH = 18;

    private static final float HEIGHT_NOISE_HEIGHT = 5f;
    private static final float HEIGHT_NOISE_SCALE = 0.6f;
    private static final float ISLAND_BACK_HEIGHT = 3f;
    private static final float ISLAND_FRONT_HEIGHT = -2f;
    private static final long HEIGHT_NOISE_SEED = 23203423489124l;

    // Computed --------------------------------------------------------------------------------------------------------
    private static final float ISLAND_RISE = ISLAND_BACK_HEIGHT - ISLAND_FRONT_HEIGHT;
    public static final float WORLD_MAX_HEIGHT = Math.max(ISLAND_BACK_HEIGHT, ISLAND_FRONT_HEIGHT) + HEIGHT_NOISE_HEIGHT;
    /* --------------------------------------------------------------------------------------------------------------- */

    public Array<Tile> tiles;
    public Array<Player> players;
    public Water water;

    private OpenSimplexNoise osn;

    public Rectangle bounds;

    public World(){
        water = new Water(this);
        osn = new OpenSimplexNoise(HEIGHT_NOISE_SEED);

        generateWorldTiles();
        bounds = new Rectangle(0, 0,(Tile.tileWidth) * WORLD_WIDTH ,Tile.tileHeight * WORLD_WIDTH * .75f);

        players = new Array<Player>(WORLD_WIDTH * WORLD_WIDTH);

        Player player = new Player();
        player.row = 1;
        player.col = 1;
        players.add(player);
    }

    public void update(float dt){
        water.update(dt);
    }

    public void render(SpriteBatch batch){
        for (int i = tiles.size-1; i >= 0; i--){
            Tile t = tiles.get(i);
            if (t.height <= water.waterHeight) t.render(batch);
        }
        water.render(batch);

        for (int i = tiles.size-1; i >= 0; i--){
            Tile t = tiles.get(i);
            if (t.height > water.waterHeight) t.render(batch);
        }

        for (Player player : players) {
            player.render(batch);
        }
    }

    public Array<Tile> getNeighbors(int row, int col){
        return new Array<Tile>();
    }

    private void generateWorldTiles() {
        float r = ((World.WORLD_WIDTH - 1) / 2); // sub 1 because we're zero indexing the tiles
        Vector2 c = new Vector2(r,r);
        float maxTileHeight = 0;
        float thisHeight;
        tiles = new Array<Tile>(WORLD_WIDTH * WORLD_WIDTH );
        // Create the tiles.
        for (int row = 0; row < WORLD_WIDTH; row++){
            for (int col = 0; col < WORLD_WIDTH; col++){
                float adjustedRow = (col % 2 == 0) ? row + 0.5f : row;
                float dist = c.dst(col,adjustedRow);
                // Inside the island perimeter?
                if (dist <= r) {
                    thisHeight = getTileHeight(col,row);
                    if (thisHeight > maxTileHeight) {
                        maxTileHeight = thisHeight;
                    }
                    // Only add tiles if they're above sea level
                    if (thisHeight > 0) {
                        tiles.add(new Tile(col, row, thisHeight));
                    }
                }
            }
        }
        // Now, assign biomes.
        float relativeHeightAboveSeaLevel; // 0 - 1, one being the highest.  Height of 0 is bound to 0.
        float typeStep = 1 / 6f;  // Number of biomes
        for (Tile tile : tiles) {
            relativeHeightAboveSeaLevel = tile.height <= 0 ? 0 : tile.height / maxTileHeight;
            // Clay, Dirt, Grass, Sand, Snow, Stone
            Tile.Type type;
            if (relativeHeightAboveSeaLevel <= typeStep * 1) {
                type = Tile.Type.Sand;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 2) {
                type = Tile.Type.Clay;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 3) {
                type = Tile.Type.Grass;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 4) {
                type = Tile.Type.Dirt;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 5) {
                type = Tile.Type.Stone;
            } else {
                type = Tile.Type.Snow;
            }
            tile.setType(type);
        }
    }

    private float getTileHeight(int col, int row) {
        float adjustedRow = (col % 2 == 0) ? row + 0.5f : row;
        float depth = Math.min(adjustedRow / World.WORLD_WIDTH, 1f); // 0 to 1, 1 being furthest from the front
        float basicHeight = ISLAND_FRONT_HEIGHT + (ISLAND_RISE * depth);
        float noisePercent = (float)(osn.eval(col * HEIGHT_NOISE_SCALE, adjustedRow * HEIGHT_NOISE_SCALE) + 1) / 2; // -1 to 1
        float noiseHeight = HEIGHT_NOISE_HEIGHT * noisePercent;
        return basicHeight + noiseHeight;
    }


}
