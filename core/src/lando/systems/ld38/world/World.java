package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by dsgraham on 4/22/17.
 */
public class World {

    public Array<Tile> tiles;
    public int world_width = 20;

    public World(){
        tiles = new Array<Tile>(world_width * world_width);
        for (int x = 0; x < world_width; x++){
            for (int y = 0; y < world_width; y++){
                tiles.add(new Tile(y, x));
            }
        }
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch){
        for (Tile tile : tiles){
            tile.render(batch);
        }
    }

    public Array<Tile> getNeighbors(int row, int col){
        return new Array<Tile>();
    }
}
