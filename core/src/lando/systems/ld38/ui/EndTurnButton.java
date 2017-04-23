package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld38.utils.Assets;

public class EndTurnButton extends Button {

    private String text;
    private TextureRegion keyframe;
    private Animation<TextureRegion> anim;
    private float width;
    private float scale;
    private float stateTime;
    private boolean animating;

    public EndTurnButton(TextureRegion region, Rectangle bounds) {
        super(region, bounds);
        text = "End Turn";
        scale = 0.3f;

        Assets.fancyFont.getData().setScale(scale);
        Assets.layout.setText(Assets.fancyFont, text);
        width = Assets.layout.width;
        Assets.fancyFont.getData().setScale(1f);

        anim = Assets.totemAnim;
        keyframe = anim.getKeyFrame(0f);

        animating = false;
        stateTime = 0f;
    }

    public void handleTouch() {
        animating = true;
    }

    public void update(float dt) {
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
        batch.draw(keyframe,
                bounds.x + bounds.width / 2f - keyframe.getRegionWidth(),
                bounds.y + bounds.height + 10f,
                keyframe.getRegionWidth() * 2f,
                keyframe.getRegionHeight() * 2f);
//        batch.draw(keyframe, bounds.x + bounds.width / 2f - keyframe.getRegionWidth() / 2f, bounds.y + bounds.height + 10f);
        Assets.woodPanel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        Assets.drawString(batch, text,
                bounds.x + bounds.width / 2f - width / 2f,
                bounds.y + bounds.height - 3f,
                Color.WHITE, scale, Assets.fancyFont);
    }

}
