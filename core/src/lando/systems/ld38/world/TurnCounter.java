package lando.systems.ld38.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.utils.Assets;

/**
 * Created by bb4k3 on 4/22/2017.
 */
public class TurnCounter {
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    private int width = 50;
    private int height = 50;
    private int x = Gdx.graphics.getWidth();
    private int y = Gdx.graphics.getHeight();

    public TurnCounter() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/vinque.fnt"), // generated with http://kvazars.com/littera/
                Gdx.files.internal("fonts/vinque.png"), false);
        shapeRenderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch, int turn) {

        float turnCounterX = x - 70;
        float turnCounterY = y - 70;
        batch.draw(Assets.turn_counter_background, turnCounterX, turnCounterY, 50, 50);

        BitmapFont turnCounterLabelFont = new BitmapFont();
        turnCounterLabelFont.getData().setScale(0.8f);
        turnCounterLabelFont.draw(batch, "TURN", turnCounterX + 10, turnCounterY + 12);

        BitmapFont turnCounterFont = new BitmapFont();
        turnCounterFont.getData().setScale(2.5f);
        Assets.layout.setText(turnCounterFont, String.valueOf(turn));
        float layoutCenter = Assets.layout.width / 2;

        turnCounterFont.draw(batch, String.valueOf(turn), turnCounterX + 25 - layoutCenter, turnCounterY + 45);

    }

}
