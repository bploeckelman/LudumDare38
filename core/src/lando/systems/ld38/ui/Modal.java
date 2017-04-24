package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld38.utils.Assets;

/**
 * Created by Brian on 4/23/2017
 */
public class Modal {

    private static final float MARGIN = 10f;

    public boolean isActive = false;
    public float scale = 0.25f;

    String text;
    Rectangle bounds;
    GlyphLayout layout;
    BitmapFont font;

    public Modal() {
        this.text = "";
        this.bounds = new Rectangle();
        this.layout = Assets.layout;
        this.font = Assets.fancyFont;
    }

    public void set(String text, float x, float y, float w, float h) {
        this.text = text;
        this.bounds.set(x, y, w, h);
    }

    public boolean checkForTouch(int screenX, int screenY, OrthographicCamera camera) {
        Vector3 touchPosUnproject = camera.unproject(new Vector3(screenX, screenY, 0));
        return bounds.contains(touchPosUnproject.x, touchPosUnproject.y);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.DARK_GRAY);
        batch.draw(Assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        Assets.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        Assets.drawString(batch, text,
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height,
                Color.WHITE, scale, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.left);
    }

}
