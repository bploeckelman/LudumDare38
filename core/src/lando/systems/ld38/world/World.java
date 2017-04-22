package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.lib.openSimplexNoise.OpenSimplexNoise;

/**
 * Created by dsgraham on 4/22/17.
 */
public class World {


    public static final int WORLD_WIDTH = 18;

    public Array<Tile> tiles;
    public Array<Player> players;

    private OpenSimplexNoise osn;


    public World(){

        osn = new OpenSimplexNoise(HEIGHT_NOISE_SEED);

        generateWorldTiles();

        players = new Array<Player>(WORLD_WIDTH * WORLD_WIDTH);

        Player player = new Player();
        player.row = 1;
        player.col = 1;
        players.add(player);
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch){
        for (Tile tile : tiles){
            tile.render(batch);
        }

        for (Player player : players) {
            player.render(batch);
        }
    }

    public Array<Tile> getNeighbors(int row, int col){
        return new Array<Tile>();
    }

    private void generateWorldTiles() {
        float r = (World.WORLD_WIDTH - 1) / 2; // sub 1 because we're zero indexing the tiles
        Vector2 c = new Vector2(r,r);
        tiles = new Array<Tile>(WORLD_WIDTH * WORLD_WIDTH );
        for (int col = 0; col < WORLD_WIDTH; col++){
            for (int row = 0; row < WORLD_WIDTH; row++){
                float adjustedRow = (col % 2 == 0) ? row + 0.5f : row;
                float dist = c.dst(col,adjustedRow);
                // Create a tile in a circle around the center.
                if (dist <= r) {
                    Tile.Type type = Tile.Type.Ocean;
                    float h = getTileHeight(col,row);
                    tiles.add(new Tile(col, row, h, type));
                }
            }
        }
    }




    private static final long HEIGHT_NOISE_SEED = 23203423489124l;
    private static final float HEIGHT_NOISE_SCALE = 0.6f;


    public static final float TILE_HEIGHT_CAP = 8;

//    private static final float MAX_NOISE_HEIGHT;
    private static final float ISLAND_FRONT_HEIGHT = -2f;
    private static final float ISLAND_BACK_HEIGHT = 3f;
    private static final float HEIGHT_NOISE_HEIGHT = 5f;

    // Computed --------------------------------------------------------------------------------------------------------
    private static final float ISLAND_RISE = ISLAND_BACK_HEIGHT - ISLAND_FRONT_HEIGHT;
    // -----------------------------------------------------------------------------------------------------------------

    private float getTileHeight(int col, int row) {

        float adjustedRow = (col % 2 == 0) ? row + 0.5f : row;
        float depth = Math.min(adjustedRow / World.WORLD_WIDTH, 1f); // 0 to 1, 1 being furthest from the front

        float basicHeight = ISLAND_FRONT_HEIGHT + (ISLAND_RISE * depth);

        float noisePercent = (float)(osn.eval(col * HEIGHT_NOISE_SCALE, adjustedRow * HEIGHT_NOISE_SCALE) + 1) / 2; // -1 to 1
        float noiseHeight = HEIGHT_NOISE_HEIGHT * noisePercent;

        return basicHeight + noiseHeight;
    }


}
