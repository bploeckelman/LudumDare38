package lando.systems.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private static final float MARGIN_TOP = 10f;

    public int food = 5;
    public int sand = 0;
    public int iron = 0;
    public int gold = 1;
    public int wood = 2;

    private int width = 450;
    private int height = 35;
    private int margin = 8;
    private float tileSize;
    private float x = 0;
    private int y = Gdx.graphics.getHeight();

    public UserResources() {

    }

    public void render(SpriteBatch batch) {
        tileSize = height - (margin *2);
        x = (Gdx.graphics.getWidth() - width) /2f;
        y = Gdx.graphics.getHeight() - height;
        Assets.woodPanel.draw(batch, x, y - MARGIN_TOP, width, height);
        float spacing = (width - 40) / 6f;
        float offset = 20;
        drawResource(batch, offset, Assets.food, food);
        offset += spacing;
        drawResource(batch, offset, Assets.wood, wood);
        offset += spacing;
        drawResource(batch, offset, Assets.sand, sand);
        offset += spacing;
        drawResource(batch, offset, Assets.iron, iron);
        offset += spacing;
        drawResource(batch, offset, Assets.gold, gold);
        batch.setColor(Color.WHITE);
    }

    private void drawResource(SpriteBatch batch, float offset, TextureRegion region, int amount){
        batch.draw(region, x + offset, y+margin - MARGIN_TOP, tileSize, tileSize);
        Assets.drawString(batch, "" + amount, x + offset + tileSize + 5, y + height - 6 - MARGIN_TOP, Color.WHITE, .3f, Assets.fancyFont);
    }

    public boolean canBuildLadder() {
        return true;
    }

    public boolean canBuildRaft() {
        return true;
    }
}
