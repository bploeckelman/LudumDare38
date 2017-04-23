package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld38.utils.Assets;

/**
 * Created by Brian on 4/23/2017.
 */
public class OptionButton extends Button {
    public float origX, origY, width, height;

    public OptionButton(String text, Rectangle bounds, OrthographicCamera camera) {
        super(Assets.whitePixel, bounds, camera);

        this.text = text;

        origX = bounds.x;
        origY = bounds.y;
        width = bounds.width;
        height = bounds.height;
    }
}
