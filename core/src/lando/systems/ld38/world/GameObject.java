package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Brian on 4/22/2017.
 */
public abstract class GameObject {
    public static float tileWidth = 40;
    public static float tileHeight = 26;

    public float height;
    public int row;
    public int col;

    public GameObject() {}

    public GameObject(int col, int row, float height){
        this.col = col;
        this.row = row;
        this.height = height;
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch) {
        float x = col * tileWidth * .75f;
        float y = row * tileHeight;
        if (col % 2 == 0) y += tileHeight / 2f;
        render(batch, x, y, tileWidth, tileHeight);
    }

    protected abstract void render(SpriteBatch batch, float x, float y, float width, float height);
}
