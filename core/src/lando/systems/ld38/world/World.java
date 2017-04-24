package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.lib.openSimplexNoise.OpenSimplexNoise;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */
public class World {


    public static final int WORLD_WIDTH = 25;

    private static final float HEIGHT_NOISE_HEIGHT = 5f;
    private static final float HEIGHT_NOISE_SCALE = 0.4f;
    private static final float ISLAND_BACK_HEIGHT = 10f;
    private static final float ISLAND_FRONT_HEIGHT = -2f;
    private static final long HEIGHT_NOISE_SEED = 23203423489124l;
    private static final float WATER_RISE_RATE = 1.3333f;
    private static final float SHOW_PLAYER_BUBBLE_ZOOM = 0.75f;

    // Computed --------------------------------------------------------------------------------------------------------
    private static final float ISLAND_RISE = ISLAND_BACK_HEIGHT - ISLAND_FRONT_HEIGHT;
    public static final float WORLD_MAX_HEIGHT = Math.max(ISLAND_BACK_HEIGHT, ISLAND_FRONT_HEIGHT) + HEIGHT_NOISE_HEIGHT;
    /* --------------------------------------------------------------------------------------------------------------- */

    // TODO: this will be per selected character, this is placeholder for now
    public Array<Tile> adjacentTiles;

    public Array<Tile> tiles;
    public Array<Player> players;
    public Water water;

    public final Array<ResourceIndicator> resIndicators = new Array<ResourceIndicator>(10);

    private OpenSimplexNoise osn;

    public Rectangle bounds;
    public GameScreen screen;

    public World(GameScreen screen){
        this.screen = screen;
        water = new Water(this);
        osn = new OpenSimplexNoise(HEIGHT_NOISE_SEED);

        generateWorldTiles();
        bounds = new Rectangle(-100, -100,(Tile.tileWidth) * WORLD_WIDTH + 200, Tile.tileHeight * WORLD_WIDTH * .75f + 200);

        players = new Array<Player>();
        for (int i = 0; i < 5; i ++){
            int row = 1;
            int col = MathUtils.random(WORLD_WIDTH);
            boolean alreadyOccupied = false;
            for (Player player : players) {
                if (player.row == row && player.col == col) {
                    alreadyOccupied = true;
                }
            }
            while ((getTile(row, col) == null || getTile(row, col).type == TileType.Ocean) || alreadyOccupied) {
                alreadyOccupied = false;
                row = 1;
                col = MathUtils.random(WORLD_WIDTH);
                for (Player player : players) {
                    if (player.row == row && player.col == col) {
                        alreadyOccupied = true;
                    }
                }
            }
            players.add(new Player(this, row, col));
        }

        adjacentTiles = new Array<Tile>();
    }

    public UserResources getResources() {
        return screen.resources;
    }

    public void update(float dt){
        water.update(dt);
        for (int i = players.size - 1; i >= 0; i--){
            Player p = players.get(i);
            if (p.dead){
                players.removeValue(p, true);
                screen.playerSelection.buildPlayerHuds();
            } else {
                p.update(dt);
                boolean showBubble =  screen.camera.zoom > SHOW_PLAYER_BUBBLE_ZOOM && screen.selectedPlayer == null;
                p.updateBubbleAlpha( dt * (showBubble ? 1 : -1));
            }
        }


        for (ResourceIndicator resIndicator : resIndicators) {
            resIndicator.update(dt);
        }

        for (int i = resIndicators.size - 1; i >= 0; i--) {
            resIndicators.get(i).update(dt);
            if (resIndicators.get(i).isComplete()) {
                resIndicators.removeIndex(i);
            }
        }
//        for (Tile t : tiles) {
//            t.isHighlighted = false;
//        }
    }

    public void orderPlayer(Player player) {
        if (player == null) return;
        players.removeValue(player, true);
        players.add(player);
    }

    public void render(SpriteBatch batch){
        for (int i = tiles.size-1; i >= 0; i--){
            Tile t = tiles.get(i);
            t.render(batch, water.waterHeight, false);
        }
        water.render(batch);

        for (int i = tiles.size-1; i >= 0; i--){
            Tile t = tiles.get(i);
            t.render(batch, water.waterHeight, true);
        }

        for (Player player : players) {
//            player.render(batch, water.waterHeight, true);
            player.renderBubble(batch);
        }

        for (ResourceIndicator resIndicator : resIndicators) {
            resIndicator.render(batch);
        }
    }

    public void renderPickBuffer(SpriteBatch batch) {
        for (int i = tiles.size-1; i >= 0; i--){
            Tile t = tiles.get(i);
            t.renderPickBuffer(batch);
        }
    }

    public Array<Tile> getNeighbors(int row, int col){
        adjacentTiles.clear();

        boolean even = (row % 2 == 0);
        Tile left = getTile(row, col - 1);
        Tile right = getTile(row, col + 1);
        Tile upLeft = getTile(row + 1, col + (even ? 0 : -1));
        Tile upRight = getTile(row + 1, col + (even ? 1 : 0));
        Tile downLeft = getTile(row - 1, col + (even ? 0 : -1));
        Tile downRight = getTile(row - 1, col + (even ? 1 : 0));

        if (left != null) adjacentTiles.add(left);
        if (right != null) adjacentTiles.add(right);
        if (upLeft != null) adjacentTiles.add(upLeft);
        if (upRight != null) adjacentTiles.add(upRight);
        if (downLeft != null) adjacentTiles.add(downLeft);
        if (downRight != null) adjacentTiles.add(downRight);

        return adjacentTiles;
    }

    public Tile getUpperLeftTile(int row, int col){
        int offset = row % 2 == 1 ? -1 : 0;
        return getTile(row +1, col + offset);
    }

    public Tile getUpperRightTile(int row, int col){
        int offset = row % 2 == 1 ? 0 : 1;
        return getTile(row +1, col + offset);
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
                float adjustedCol = (row % 2 == 0) ? col + 0.5f : col;
                float dist = c.dst(adjustedCol,row);
                // Inside the island perimeter?
                if (dist <= r) {
                    thisHeight = getTileHeight(col,row);
                    if (thisHeight > maxTileHeight) {
                        maxTileHeight = thisHeight;
                    }
                    // Only add tiles if they're above sea level
                    if (thisHeight > 0) {
                        tiles.add(new Tile(this, col, row, thisHeight));
                    } else {
                        tiles.add(new Tile(this, col, row, thisHeight));
                    }
                }  else {
                    tiles.add(new Tile(this, col, row, -10));
                }
            }
        }
        // Now, assign biomes.
        float relativeHeightAboveSeaLevel; // 0 - 1, one being the highest.  Height of 0 is bound to 0.
        float typeStep = 1 / 6f;  // Number of biomes
        for (Tile tile : tiles) {
            relativeHeightAboveSeaLevel = tile.height <= 0 ? 0 : tile.height / maxTileHeight;
            // Clay, Dirt, Grass, Sand, Snow, Stone
            TileType type;
            if (tile.height <= -1){
                type = TileType.Ocean;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 1) {
                type = TileType.Sand;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 2) {
                type = TileType.Clay;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 3) {
                type = TileType.Grass;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 4) {
                type = TileType.Dirt;
            } else if (relativeHeightAboveSeaLevel <= typeStep * 5) {
                type = TileType.Stone;
            } else {
                type = TileType.Snow;
            }
            tile.setType(type);
            Tile ul = getUpperLeftTile(tile.row, tile.col);
            Tile ur = getUpperRightTile(tile.row, tile.col);
            int shadowmap = 0;
            if (ul != null && ul.height > tile.height) shadowmap += 1;
            if (ur != null && ur.height > tile.height) shadowmap += 2;
            tile.addShadow(shadowmap);
        }
    }

    private float getTileHeight(int col, int row) {
        float adjustedRow = (col % 2 == 0) ? row + 0.5f : row;
        float depth = Math.min(adjustedRow / World.WORLD_WIDTH, 1f); // 0 to 1, 1 being furthest from the front
        float basicHeight = ISLAND_FRONT_HEIGHT + (ISLAND_RISE * depth);
        float noisePercent = (float)(osn.eval(col * HEIGHT_NOISE_SCALE, adjustedRow * HEIGHT_NOISE_SCALE) + 1) / 2; // -1 to 1
        float noiseHeight = HEIGHT_NOISE_HEIGHT * noisePercent;
        int quantHeight = (int) ((basicHeight + noiseHeight));
        return quantHeight;
    }

    public Array<Player> getPlayers(GridPoint2 location) {
        Array<Player> selectedPlayers = new Array<Player>(5);
        for(Player p : players) {
            if (p.row == location.y && p.col == location.x) {
                selectedPlayers.add(p);
            }
        }
        return selectedPlayers;
    }

    public Tile getTile(GridPoint2 location) {
        return getTile(location.y, location.x);
    }

    public Tile getTile(int row, int col){
        int index = col + row * WORLD_WIDTH;
        if (index < 0 || index >= tiles.size) return null;
        return tiles.get(index);

    }

    public void endTurn(){
        water.waterHeight += WATER_RISE_RATE;
        for (Player p : players){
            Tile t = getTile(p.row, p.col);
            if (t.heightOffset < water.waterHeight){
                p.kill();
            }
        }
    }
}
