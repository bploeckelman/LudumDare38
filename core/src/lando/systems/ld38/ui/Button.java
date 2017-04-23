package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;

import static com.badlogic.gdx.Gdx.input;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class Button {

    public final TextureRegion region;
    public final Rectangle bounds;
    public String text = null;
    public String tooltip = null;
    private OrthographicCamera camera;
    private Vector2 touchPosScreen = new Vector2();

    public Button(TextureRegion region, Rectangle bounds, OrthographicCamera camera, String text, String tooltip) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = region;
        this.text = text;
        this.setTooltip(tooltip);
    }

    public Button(TextureRegion region, Rectangle bounds, OrthographicCamera camera) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = region;
    }


    public boolean checkForTouch(int screenX, int screenY) {
        Vector3 touchPosUnproject = camera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);
        return bounds.contains(touchPosScreen.x, touchPosScreen.y);
    }

    private float tooltipBackgroundHeight;
    private float tooltipBackgroundWidth;
    private float tooltipTextOffsetY = 0f;
    private GlyphLayout tooltipTextGlyphLayout;

    private static final float TOOLTIP_TEXT_OFFSET_Y = 24f; // Catches characters with ligatures below the baseline
    private static final float TOOLTIP_TEXT_PADDING_X = 18f; // Padding for all 4 sides around the text
    private static final float TOOLTIP_TEXT_PADDING_Y = 4f; // Padding for all 4 sides around the text
    private static final float TOOLTIP_TEXT_SCALE = 0.34f; // Padding for all 4 sides around the text
    private static final float TOOLTIP_TEXT_OFFSET_X = TOOLTIP_TEXT_PADDING_X * TOOLTIP_TEXT_SCALE;

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
        tooltipTextGlyphLayout = new GlyphLayout(Assets.fancyFont, tooltip);
        tooltipBackgroundHeight = tooltipTextGlyphLayout.height + (TOOLTIP_TEXT_PADDING_Y * 2) + TOOLTIP_TEXT_OFFSET_Y;
        tooltipBackgroundWidth = tooltipTextGlyphLayout.width + (TOOLTIP_TEXT_PADDING_X * 2);
        tooltipTextOffsetY = (tooltipTextGlyphLayout.height + TOOLTIP_TEXT_PADDING_Y + TOOLTIP_TEXT_OFFSET_Y) * TOOLTIP_TEXT_SCALE;
    }


    public void render(SpriteBatch batch) {
        batch.setColor(Color.RED);
        batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);

        if (tooltip != null && checkForTouch(input.getX(), input.getY())) {
            float tX = input.getX();
            float tY = Config.gameHeight - input.getY();
            Assets.woodPanel.draw(batch, tX, tY, tooltipBackgroundWidth * TOOLTIP_TEXT_SCALE, tooltipBackgroundHeight * TOOLTIP_TEXT_SCALE);
            Assets.drawString(batch,
                    tooltip,
                    tX + TOOLTIP_TEXT_OFFSET_X,
                    tY + tooltipTextOffsetY,
                    Color.WHITE,
                    TOOLTIP_TEXT_SCALE,
                    Assets.fancyFont);
        }

    }

}
