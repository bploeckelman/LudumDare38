package lando.systems.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.utils.Assets;

/**
 * Created by bb4k3 on 4/22/2017.
 */
public class TurnCounter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private int width = 50;
    private int height = 50;
    private int x = Gdx.graphics.getWidth();
    private int y = Gdx.graphics.getHeight();

    public TurnCounter() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch, int turn) {
        float turnCounterX = x - 70;
        float turnCounterY = y - 70;
        batch.draw(Assets.turn_counter_background, turnCounterX, turnCounterY, 56, 56);

//        Assets.drawString(batch, "TURN", turnCounterX + 12, turnCounterY + 14, Color.WHITE, .25f, Assets.font);

        float layoutCenter = Assets.layout.width / 2;

        batch.setShader(Assets.fontShader);
        Assets.fontShader.setUniformf("u_scale", .4f);
        Assets.fancyFont.getData().setScale(0.4f);
        Assets.fancyFont.setColor(Color.WHITE);
        Assets.fancyFont.draw(batch, String.valueOf(turn), turnCounterX + 25, turnCounterY + 51, 0, Align.center, false);
        Assets.fontShader.setUniformf("u_scale", .25f);
        Assets.fancyFont.getData().setScale(0.25f);
        Assets.fancyFont.draw(batch, "TURN", turnCounterX, turnCounterY + 20, 56, Align.center, false);
        Assets.fontShader.setUniformf("u_scale", 1.0f);
        Assets.fancyFont.getData().setScale(1f);
        batch.setShader(null);
    }

}
