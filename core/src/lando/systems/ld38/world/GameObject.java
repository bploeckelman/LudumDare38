package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Brian on 4/22/2017.
 */
public abstract class GameObject {
    public World world;

    public static float tileWidth = 26;
    public static float tileHeight = 30;

    public static float getX(int row, int col) {
        float x = col * tileWidth;
        if (row % 2 == 0) x += tileWidth / 2f;
        return x;
    }

    public static float getY(int row) {
        return row * tileHeight * .75f;
    }

    public float height;
    public int row;
    public int col;
    public Vector3 position;

    public GameObject(World world) {
        this.world = world;
        this.position = new Vector3(0,0, 0);
    }

    public GameObject(World world, int col, int row, float height){
        this(world);
        this.col = col;
        this.row = row;
        this.position.x = getX(row, col);
        this.position.y = getY(row);
        this.height = height;
    }

    public void update(float dt){

    }

    public void render(SpriteBatch batch) {
        render(batch, position.x, position.y + position.z, tileWidth, tileHeight);
    }

    protected abstract void render(SpriteBatch batch, float x, float y, float width, float height);

    public Tile getTile() {
        return getTile(row, col);
    }

    public Tile getTile(int row, int col) {
        return world.getTile(row, col);
    }
}
