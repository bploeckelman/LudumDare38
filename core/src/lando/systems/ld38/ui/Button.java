package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private static final float TOOLTIP_TEXT_OFFSET_Y = 3f; // Catches characters with ligatures below the baseline
    private static final float TOOLTIP_TEXT_PADDING_X = 8f;
    private static final float TOOLTIP_TEXT_PADDING_Y = 8f;
    private static final float TOOLTIP_TEXT_SCALE = 0.3f;

    private float tooltipBackgroundHeight;
    private float tooltipBackgroundWidth;
    private float tooltipTextOffsetY;

    private final TextureRegion region;
    private final NinePatch ninePatch;
    public final Rectangle bounds;
    public String text = null;
    public String tooltip = null;

    private OrthographicCamera camera;
    private Vector2 touchPosScreen = new Vector2();
    private float textScale = 0.3f;
    private float textOffsetY = 3f;
    private Color textColor = Color.WHITE;
    private float textX;
    private float textY;


    // Constructors ----------------------------------------------------------------------------------------------------

    public Button(TextureRegion region, Rectangle bounds, OrthographicCamera camera, String text, String tooltip) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = region;
        this.setText(text);
        this.setTooltip(tooltip);
        this.ninePatch = null;
    }

    public Button(TextureRegion region, Rectangle bounds, OrthographicCamera camera) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = region;
        this.ninePatch = null;
    }

    public Button(NinePatch ninePatch, Rectangle bounds, OrthographicCamera camera, String text, String tooltip) {
        this.ninePatch = ninePatch;
        this.region = null;
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.setText(text);
        this.setTooltip(tooltip);
    }

    public Button(NinePatch ninePatch, Rectangle bounds, OrthographicCamera camera) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = null;
        this.ninePatch = ninePatch;
    }


    // Update & Render -------------------------------------------------------------------------------------------------

    public void render(SpriteBatch batch) {
        // Button texture
        if (region != null) {
            batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
        } else if (ninePatch != null) {
            Assets.woodPanel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        }
        // Button text
        if (text != null && !text.equals("")) {
            Assets.drawString(batch, text, textX, textY, textColor, textScale, Assets.fancyFont);
        }
        // Tooltip
        if (tooltip != null && !tooltip.equals("")) {
            if (showTooltip) {
                float tX = input.getX();
                float tY = Config.gameHeight - input.getY();
                Assets.woodPanel.draw(batch, tX, tY, tooltipBackgroundWidth, tooltipBackgroundHeight);
                Assets.drawString(batch,
                        tooltip,
                        tX + TOOLTIP_TEXT_PADDING_X,
                        tY + tooltipTextOffsetY,
                        Color.WHITE,
                        TOOLTIP_TEXT_SCALE,
                        Assets.fancyFont);
            }
        }
    }

    private float timeHovered = 0;
    private static final float TOOLTIP_SHOW_DELAY = 0.5f;
    private boolean showTooltip = false;

    public void update(float dt) {
        boolean isTouching = checkForTouch(input.getX(), input.getY());
        if (isTouching) {
            timeHovered += dt;
        } else {
            timeHovered = 0;
        }
        showTooltip = timeHovered >= TOOLTIP_SHOW_DELAY;
    }


    // -----------------------------------------------------------------------------------------------------------------

    public boolean checkForTouch(int screenX, int screenY) {
        Vector3 touchPosUnproject = camera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);
        return bounds.contains(touchPosScreen.x, touchPosScreen.y);
    }

    public void setText(String text) {
        this.text = text;
        if (text != null) {
            Assets.fancyFont.getData().setScale(textScale);
            Assets.layout.setText(Assets.fancyFont, text);
            Assets.fancyFont.getData().setScale(1f);
            float textWidth = Assets.layout.width;
            float textHeight = Assets.layout.height;
            textX = bounds.x + (bounds.width / 2) - (textWidth / 2);
            textY = bounds.y + (bounds.height / 2) + (textHeight / 2) + textOffsetY;
        }
    }

    private void setTooltip(String tooltip) {
        this.tooltip = tooltip;
        if (tooltip != null) {
            Assets.fancyFont.getData().setScale(TOOLTIP_TEXT_SCALE);
            Assets.layout.setText(Assets.fancyFont, tooltip);
            tooltipBackgroundHeight = Assets.layout.height + (TOOLTIP_TEXT_PADDING_Y * 2);
            tooltipBackgroundWidth = Assets.layout.width + (TOOLTIP_TEXT_PADDING_X * 2);
            tooltipTextOffsetY = (Assets.layout.height + TOOLTIP_TEXT_PADDING_Y + TOOLTIP_TEXT_OFFSET_Y);
            Assets.fancyFont.getData().setScale(1f);
        }
    }

    public void updateTextProperties(float textScale, Color textColor, float textOffsetY) {
        this.textScale = textScale;
        this.textColor = textColor;
        this.textOffsetY = textOffsetY;
    }


}
