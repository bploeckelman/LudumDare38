package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/24/17.
 */
public class EndGameOverlay {
    private static final float MARGIN = 10f;
    private static final float WINDOW_MARGIN = 40;
    String gameOver = "Game Over";
    String gameLost = "You lost all your natives";
    String gameWon = "Congratulations";

    GameScreen screen;
    float delay;
    Rectangle bounds;

    public EndGameOverlay(GameScreen screen){
        this.screen = screen;
        bounds = new Rectangle(WINDOW_MARGIN, WINDOW_MARGIN, screen.hudCamera.viewportWidth - 2* WINDOW_MARGIN, screen.hudCamera.viewportHeight - 2 * WINDOW_MARGIN);
    }


    public void update(float dt){
        delay += dt;
    }

    public void render(SpriteBatch batch){
        batch.setColor(Color.DARK_GRAY);
        batch.draw(Assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        Assets.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        Assets.drawString(batch, gameOver,
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height,
                Color.WHITE, .7f, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.center);
        Assets.drawString(batch, screen.gameLost ? gameLost : gameWon,
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height - 50,
                screen.gameLost ? Color.RED :Color.GREEN, .4f, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.center);

        Assets.drawString(batch, "Statistics",
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height - 100,
                Color.WHITE, .4f, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.center);

        float x = bounds.x + MARGIN + 30;
        float y = bounds.y - MARGIN + bounds.height - 160;
        renderString(batch, "Natives Lost: " + screen.stats.deaths, x, y, .3f);
        y -= 25;
        renderString(batch, "Natives Born: " + screen.stats.births, x, y, .3f);
        y -= 25;
        renderString(batch, "Rafts Built: " + screen.stats.rafts, x, y, .3f);
        y -= 25;
        renderString(batch, "Ladders Built: " + screen.stats.ladders, x, y, .3f);
        y -= 25;
        renderString(batch, "Sandbags Built: " + screen.stats.sandbags, x, y, .3f);
        y -= 25;
        renderString(batch, "Huts Built: " + screen.stats.huts, x, y, .3f);

        x = bounds.x + (bounds.width/2);
        y = bounds.y - MARGIN + bounds.height - 160;
        renderString(batch, "Food Collected: " + screen.stats.food, x, y, .3f);
        y -= 25;
        renderString(batch, "Wood Collected: " + screen.stats.wood, x, y, .3f);
        y -= 25;
        renderString(batch, "Sand Collected: " + screen.stats.sand, x, y, .3f);
        y -= 25;
        renderString(batch, "Iron Collected: " + screen.stats.iron, x, y, .3f);
        y -= 25;
        renderString(batch, "Gold Collected: " + screen.stats.gold, x, y, .3f);

    }




    public void renderString(SpriteBatch batch, String text, float x, float y, float scale) {
        renderString(batch, text, x, y, scale, 300, Align.left);
    }

    public void renderString(SpriteBatch batch, String text, float x, float y, float scale, float width, int align){
        Assets.drawString(batch, text, x, y, Color.WHITE, scale, Assets.fancyFont, width, align);
    }
}

