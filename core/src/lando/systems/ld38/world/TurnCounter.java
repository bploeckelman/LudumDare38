package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld38.utils.Assets;

/**
 * Created by bb4k3 on 4/22/2017.
 */
public class TurnCounter {

    private static final float MARGIN = 10f;
    private static final float SCALE_COUNT = 0.4f;
    private static final float SCALE_TEXT = 0.25f;
    private static final float WIDTH = 75f;
    private static final float HEIGHT = 60f;
    private static final float PADDING = 2f;

    private Rectangle bounds;
    private GlyphLayout layout;
    private BitmapFont font;

    public TurnCounter(OrthographicCamera camera) {
        bounds = new Rectangle(camera.viewportWidth - WIDTH - MARGIN,
                               camera.viewportHeight - HEIGHT - MARGIN,
                               WIDTH, HEIGHT);
        layout = Assets.layout;
        font = Assets.fancyFont;
    }

    public void render(SpriteBatch batch, int turn) {
        Assets.woodPanel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        String turnNumberText = String.valueOf(turn);
        font.getData().setScale(SCALE_COUNT);
        layout.setText(font, turnNumberText);
        Assets.drawString(batch, turnNumberText,
                bounds.x + bounds.width / 2f - layout.width / 2f,
                bounds.y + bounds.height - Assets.woodPanel.getTopHeight() - PADDING,
                Color.ORANGE, SCALE_COUNT, font);

        String turnText = "Turn";
        font.getData().setScale(SCALE_TEXT);
        layout.setText(font, turnText);
        Assets.drawString(batch, turnText,
                bounds.x + bounds.width / 2f - layout.width / 2f,
                bounds.y + layout.height + Assets.woodPanel.getBottomHeight() + PADDING,
                Color.WHITE, SCALE_TEXT, font);

        font.getData().setScale(1f);
    }

}
