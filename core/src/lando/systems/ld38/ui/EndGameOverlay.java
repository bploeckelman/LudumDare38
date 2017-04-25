package lando.systems.ld38.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.primitives.MutableFloat;
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

    public MutableFloat alpha;

    GameScreen screen;
    float delay;
    Rectangle bounds;

    public EndGameOverlay(GameScreen screen){
        alpha = new MutableFloat(0);
        this.screen = screen;
        bounds = new Rectangle(WINDOW_MARGIN, WINDOW_MARGIN, screen.hudCamera.viewportWidth - 2* WINDOW_MARGIN, screen.hudCamera.viewportHeight - 2 * WINDOW_MARGIN);
        Tween.to(alpha, 1, 1)
                .target(1)
                .start(Assets.tween);
    }


    public void update(float dt){
        delay += dt;
    }

    public void render(SpriteBatch batch){
        batch.setColor(.25f, .25f, .25f, alpha.floatValue());
        batch.draw(Assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(1,1,1,alpha.floatValue());
        Assets.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        Assets.drawString(batch, gameOver,
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height,
                new Color(1,1,1, alpha.floatValue()), .7f, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.center);
        Assets.drawString(batch, screen.gameLost ? gameLost : gameWon,
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height - 50,
                screen.gameLost ? new Color(1,0,0,alpha.floatValue()) : new Color(0,1,0,alpha.floatValue()), .4f, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.center);

        Assets.drawString(batch, "Statistics",
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height - 100,
                new Color(1,1,1,alpha.floatValue()), .4f, Assets.fancyFont,
                bounds.width - 2f * MARGIN,
                Align.center);

        int min = (int)(screen.stats.totalTime / 60);
        int sec = (int)(screen.stats.totalTime % 60);

        float x = bounds.x + MARGIN + 30;
        float y = bounds.y - MARGIN + bounds.height - 150;
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
        y = bounds.y - MARGIN + bounds.height - 150;
        renderString(batch, "Total Turns: " + screen.turn, x, y, .3f);
        y -= 25;
        renderString(batch, "Food Collected: " + screen.stats.food, x, y, .3f);
        y -= 25;
        renderString(batch, "Wood Collected: " + screen.stats.wood, x, y, .3f);
        y -= 25;
        renderString(batch, "Sand Collected: " + screen.stats.sand, x, y, .3f);
        y -= 25;
        renderString(batch, "Iron Collected: " + screen.stats.iron, x, y, .3f);
        y -= 25;
        renderString(batch, "Gold Collected: " + screen.stats.gold, x, y, .3f);


        renderString(batch, "Play Time: " + min + " minutes " + sec + " seconds",
                bounds.x + MARGIN,
                bounds.y - MARGIN + bounds.height - 300,
                .4f, bounds.width - 2*MARGIN, Align.center);
    }




    public void renderString(SpriteBatch batch, String text, float x, float y, float scale) {
        renderString(batch, text, x, y, scale, 300, Align.left);
    }

    public void renderString(SpriteBatch batch, String text, float x, float y, float scale, float width, int align){
        Assets.drawString(batch, text, x, y, new Color(1,1,1,alpha.floatValue()), scale, Assets.fancyFont, width, align);
    }
}

