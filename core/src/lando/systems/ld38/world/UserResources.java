package lando.systems.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lando.systems.ld38.utils.Assets;

/**
 * Created by aeharding on 4/22/17.
 */
public class UserResources {

    public int food = 5;
    public int sand = 0;
    public int iron = 0;
    public int gold = 1;
    public int wood = 2;
    public int clay = 3;

    private int width = 450;
    private int height = 35;
    private int margin = 10;
    private float tileSize;
    private float x = 0;
    private int y = Gdx.graphics.getHeight();

    public UserResources() {

    }

    public void render(SpriteBatch batch) {
        tileSize = height - (margin *2);
        x = (Gdx.graphics.getWidth() - width) /2f;
        y = Gdx.graphics.getHeight() - height;
        Assets.woodPanel.draw(batch, x, y, width, height);
        float spacing = (width - 40) / 6f;
        float offset = 20;
        drawResource(batch, offset, Assets.food, food);
//        batch.draw(Assets.food, x + offset, y + margin, tileSize, tileSize);
    }

    private void drawResource(SpriteBatch batch, float offset, TextureRegion region, int amount){
        batch.draw(region, x + offset, y+margin, tileSize, tileSize);
    }
}
