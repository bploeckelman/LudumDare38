package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */
public class Tile {
    public static float tileWidth = 40;
    public static float tileHeight = 40;
    enum Type {Grass, Dirt}
    public Type type;
    public float height;
    public int row;
    public int col;

    public Tile(int col, int row){
        this.col = col;
        this.row = row;
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch){
        float x = col * tileWidth * .75f;
        float y = row * tileHeight;
        if (col %2 == 0) y += tileHeight/2f;
        batch.draw(Assets.blank_hex, x, y, tileWidth, tileHeight);
    }
}
