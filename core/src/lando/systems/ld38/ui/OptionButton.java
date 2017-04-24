package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld38.turns.Actions;
import lando.systems.ld38.utils.Assets;

/**
 * Created by Brian on 4/23/2017.
 */
public class OptionButton extends Button {
    public float origX, origY, width, height;

    public Actions action;
    public boolean disabled;

    public OptionButton(TextureRegion asset, Rectangle bounds, Actions action, boolean disabled, OrthographicCamera camera) {
        super(asset, bounds, camera);

        this.action = action;
        this.disabled = disabled;

        origX = bounds.x;
        origY = bounds.y;
        width = bounds.width;
        height = bounds.height;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(0.25f, 0.25f, 0.25f, 0.75f);
        batch.draw(Assets.whitePixel, bounds.x - 4f, bounds.y - 4f, bounds.width + 4f * 2f, bounds.height + 4f * 2f);
        batch.setColor(Color.WHITE);
        Assets.ninePatch.draw(batch, bounds.x - 4f, bounds.y - 4f, bounds.width + 4f * 2f, bounds.height + 4f * 2f);
        super.render(batch);
    }
}