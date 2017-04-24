package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld38.utils.Assets;

public class EndTurnButton extends Button {

    private TextureRegion keyframe;
    private Animation<TextureRegion> anim;
    private float stateTime;
    private boolean animating;

    public EndTurnButton(Rectangle bounds, OrthographicCamera camera) {
        super(Assets.woodPanel, bounds, camera, "End Turn", "tooltip");

        anim = Assets.totemAnim;
        keyframe = anim.getKeyFrame(0f);

        animating = false;
        stateTime = 0f;
    }

    public void handleTouch() {
        animating = true;
    }

    public void update(float dt) {
        super.update(dt);
        if (animating) {
            stateTime += dt;
            keyframe = anim.getKeyFrame(stateTime);
            if (anim.isAnimationFinished(stateTime)) {
                animating = false;
                stateTime = 0f;
            }
        }
        // animate if clicked?
    }
    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(0.25f, 0.25f, 0.25f, 0.75f);
        batch.draw(Assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height + keyframe.getRegionHeight() * 2f + 20f);
        batch.setColor(Color.WHITE);
        batch.draw(keyframe,
                bounds.x + bounds.width / 2f - keyframe.getRegionWidth(),
                bounds.y + bounds.height + 10f,
                keyframe.getRegionWidth() * 2f,
                keyframe.getRegionHeight() * 2f);

        super.render(batch);
    }

}
