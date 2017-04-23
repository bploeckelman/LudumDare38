package lando.systems.ld38.world;

import lando.systems.ld38.utils.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Brian on 4/23/2017.
 */



public class ResourceIndicator {
    private final TextureRegion _icon;
    private final Rectangle _bounds;
    private float _initialY;
    private float _initialX;
    private float _time;
    private float _distance = 72;
    private String _count;
    private float _fadeTime = 1.5f;
    private Color _color = new Color(1, 1, 1, 1);
    private float _solidTime = 2f; // 2 seconds before fading

    public ResourceIndicator(TextureRegion icon, Rectangle bounds, int count) {
        _icon = icon;
        _bounds = bounds;
        _initialY = _bounds.y;
        _initialX = _bounds.x;
        _count = ((count > 0) ? "+" : "") + count;
    }

    public void update(float dt) {
        _time += dt;

        float distance = (_distance * _time / 2);
        _bounds.y = _initialY + distance;
        _bounds.x = (float) (_initialX + (5 * Math.sin(distance/10)));
    }

    public boolean isComplete() {
        return _time >= (_fadeTime + _solidTime);
    }

    public void render(SpriteBatch batch) {

        _color.a = (_time < _solidTime) ? _solidTime :  1f - ((_time - _solidTime) / _fadeTime);

        Color last = batch.getColor();
        batch.setColor(_color);
        batch.draw(_icon, _bounds.x, _bounds.y, _bounds.width, _bounds.height);
        batch.setColor(last);

        if (_time > 0.5f) {
            Assets.font.setColor(_color);
            Assets.font.draw(batch,  _count, _bounds.x - 20,  _bounds.y + _bounds.height - 2);

            Assets.font.setColor(Color.WHITE);
        }

    }
}
