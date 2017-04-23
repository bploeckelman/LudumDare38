package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Brian on 4/22/2017.
 */
public abstract class GameObject {
    public World world;

    public static float tileWidth = 26;
    public static float tileHeight = 30;

    public float height;
    public int row;
    public int col;

    public GameObject(World world) {
        this.world = world;
    }

    public GameObject(World world, int col, int row, float height){
        this(world);
        this.col = col;
        this.row = row;
        this.height = height;
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch) {
        float x = col * tileWidth;
        float y = row * tileHeight * .75f;
        if (row % 2 == 0) x += tileWidth / 2f;
        render(batch, x, y, tileWidth, tileHeight);
    }

    protected abstract void render(SpriteBatch batch, float x, float y, float width, float height);

    public Tile getTile() {
        return world.getTile(row, col);
    }
}
