package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by dsgraham on 4/22/17.
 */
public class World {

    public Array<Tile> tiles;
    public Array<Player> players;
    public int world_width = 16;

    public World(){
        tiles = new Array<Tile>(world_width * world_width);
        for (int x = 0; x < world_width; x++){
            for (int y = 0; y < world_width; y++){
                Tile.Type type = Tile.Type.Sand;
//                Tile.Type type = Tile.Type.Ocean;
                float h = MathUtils.random();
//                if (t > .5f) type = Tile.Type.Sand;
//                if (t > .6f) type = Tile.Type.Stone;
//                if (t > .7f) type = Tile.Type.Clay;
//                if (t > .8f) type = Tile.Type.Grass;
//                if (t > .9f) type = Tile.Type.Forest;
                tiles.add(new Tile(y, x, h, type));
            }
        }


        players = new Array<Player>(world_width * world_width);

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
}
