package lando.systems.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lando.systems.ld38.utils.Assets;

/**
 * Created by aeharding on 4/22/17.
 */
public class UserResources {
    private ShapeRenderer shapeRenderer;

    public int gold = 1;
    public int wood = 2;
    public int clay = 3;

    private int width = 100;
    private int height = 25;
    private int x = 0;
    private int y = Gdx.graphics.getHeight();

    public UserResources() {
        shapeRenderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(.34f, .21f, .12f, 0));
        shapeRenderer.rect(this.x, this.y - this.height, this.width, this.height);
        shapeRenderer.end();

        int inset = 2;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(.66f, .4f, .23f, 0));
        shapeRenderer.rect(this.x + inset, this.y - this.height + inset, this.width - (inset * 2), this.height - (inset * 2));
        shapeRenderer.end();

        Assets.drawString(batch, Integer.toString(this.gold) + "  " + Integer.toString(this.wood) + "  " + Integer.toString(clay), this.x + 8, this.y - 4, Color.WHITE, .45f, Assets.fancyFont);
    }
}