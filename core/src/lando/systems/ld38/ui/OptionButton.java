package lando.systems.ld38.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.turns.Actions;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.world.ResourceCount;

/**
 * Created by Brian on 4/23/2017.
 */
public class OptionButton extends Button {
    public float origX, origY, width, height;

    public Actions action;
    public boolean disabled;
    public ResourceCount cost;

    public int binding;

    // holy hackjob batman!
    public boolean isTriggered;
    public GameScreen gameScreen;

    public OptionButton(TextureRegion asset, Rectangle bounds, Actions action, OrthographicCamera camera, String tooltip, ResourceCount resources, ResourceCount cost, int binding) {
        super(asset, bounds, camera);

        this.cost = cost;
        this.action = action;
        this.disabled = !resources.hasEnough(cost);

        if (this.disabled) {
            setTooltip(resources.getRequired(cost));
        } else {
            setTooltip(tooltip);
        }

        this.binding = binding;

        origX = bounds.x;
        origY = bounds.y;
        width = 30;
        height = 30;
    }

    public void update(float dt) {
        isTriggered = (Gdx.input.isKeyJustPressed(binding));
        if (isTriggered) {
            gameScreen.handleBindingPress();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (disabled) {
            batch.setColor(1, 0.25f, 0.25f, 0.75f);
        } else {
            batch.setColor(1f, 1f, 1f, 0.75f);
        }
        batch.draw(Assets.whitePixel, bounds.x - 4f, bounds.y - 4f, bounds.width + 4f * 2f, bounds.height + 4f * 2f);
        batch.setColor(Color.WHITE);
        Assets.ninePatch.draw(batch, bounds.x - 4f, bounds.y - 4f, bounds.width + 4f * 2f, bounds.height + 4f * 2f);


        String text = getKey();
        if (!disabled && text != "") {
            float keySize = 20;
            float x = bounds.x + bounds.width - 5;
            float y = bounds.y - 10;
            batch.setColor(Color.WHITE);
            batch.draw(Assets.whitePixel, x, y, keySize, keySize);
            Assets.ninePatch.draw(batch, x, y, keySize, keySize);
            Assets.drawString(batch, text,
                    x, y + (keySize * .9f),
                    Color.BLACK, 0.2f, Assets.fancyFont, keySize,
                    Align.center);
        }
        batch.setColor(Color.WHITE);
        super.render(batch);
    }

    private String getKey() {
        switch (binding) {
            case Input.Keys.M:
                return "M";
            case Input.Keys.B:
                return "B";
            case Input.Keys.L:
                return "L";
            case Input.Keys.R:
                return "R";
            case Input.Keys.S:
                return "S";
            case Input.Keys.H:
                return "H";
            case Input.Keys.U:
                return "U";
            case Input.Keys.N:
                return "N";
            default:
                return "";
        }
    }

    public boolean checkForTouch(int screenX, int screenY) {
        if (isTriggered) {
            isTriggered = false;
            return true;
        }
        return super.checkForTouch(screenX, screenY);
    }
}