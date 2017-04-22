package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */

public class Tile extends GameObject {
    enum Type {Grass, Dirt, Forest, Ocean}
    public Type type;

    public Tile(int col, int row, Type type){
        super(col, row);
        this.type = type;
    }

    public void render(SpriteBatch batch, float x, float y, float width, float height){
        batch.draw(Assets.blank_hex, x, y, width, height);
        switch(type){
            case Grass:batch.setColor(.5f,.5f, 0, 1);
                break;
            case Dirt:batch.setColor(1,1,0,1);
                break;
            case Forest:batch.setColor(0,1,0,1);
                break;
            case Ocean:batch.setColor(0,0,1,1);
                break;
        }
        batch.draw(Assets.blank_hex, x, y, tileWidth, tileHeight);
        batch.setColor(Color.WHITE);
    }
}
