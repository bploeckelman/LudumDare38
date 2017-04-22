package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */

public class Tile extends GameObject {
    enum Type {Grass, Sand, Forest, Ocean, Clay, Stone}
    public Type type;

    public Tile(int col, int row, Type type){
        super(col, row);
        this.type = type;
    }

    public void render(SpriteBatch batch, float x, float y, float width, float height){
        batch.draw(Assets.blank_hex, x, y, width, height);
        Texture tex = Assets.blank_hex;
        switch(type){
            case Grass:tex = Assets.blank_hex;
                break;
            case Sand:tex = Assets.sand_hex;
                break;
            case Forest:tex = Assets.forest_hex;
                break;
            case Stone:tex = Assets.stone_hex;
                break;
            case Clay:tex = Assets.clay_hex;
                break;
            case Ocean: tex = Assets.water_hex;
                break;
        }
        batch.draw(tex, x, y, tileWidth, tileHeight);
        batch.setColor(Color.WHITE);
    }
}
