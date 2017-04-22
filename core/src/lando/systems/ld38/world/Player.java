package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;

/**
 * Created by Brian on 4/22/2017.
 */
public class Player extends GameObject {

    public void render(SpriteBatch batch, float x, float y, float width, float height) {
        batch.setColor(Color.RED);
        batch.draw(Assets.whitePixel, x, y, width, height);
    }
}
