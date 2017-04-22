package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */
public class Tile {
    public static float tileWidth = 40;
    public static float tileHeight = 30;

    enum Type {Grass, Dirt, Forest, Ocean}
    public Type type;
    public float height;
    public int row;
    public int col;

    public Tile(int col, int row, Type type){
        this.col = col;
        this.row = row;
        this.type = type;
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch){
        float x = col * tileWidth * .75f;
        float y = row * tileHeight;
        if (col %2 == 0) y += tileHeight/2f;
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
