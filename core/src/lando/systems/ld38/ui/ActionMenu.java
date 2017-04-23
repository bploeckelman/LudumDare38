package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.PendingAction;
import lando.systems.ld38.world.Player;

/**
 * Created by Brian on 4/23/2017.
 */
public class ActionMenu {
    public static float movementSpeed = 0.5f; // seconds
    public Array<OptionButton> options;
    public Player player;
    public PendingAction pendingAction = new PendingAction();

    float scale = 0f;
    float sw, sh;

    enum DisplayState { grow, show, shrink, hide }

    private DisplayState displayState = DisplayState.grow;

    public ActionMenu(Player player, Array<OptionButton> options)
    {
        this.player = player;
        this.options = options;
        pendingAction.player = player;

        OptionButton button = options.first();
        sw = 1f / button.bounds.width;
        sh = 1f / button.bounds.height;
    }

    public void hide() {
        displayState = DisplayState.shrink;
    }

    public void update(float dt) {
        if (displayState == DisplayState.show) return;

        if (displayState == DisplayState.grow) {
            scale += dt / movementSpeed;
        } else {
            scale -= dt / movementSpeed;
        }

        // up 0, left 1, right, 2
        int direction = 0;
        for (OptionButton button : options) {
            move(button, scale, direction++);
        }
    }

    public void render(SpriteBatch batch) {
        for (OptionButton button : options) {
            button.render(batch);
        }
    }

    public void move(OptionButton button, float scale, int direction) {
        if (scale < 0.001) {
            scale = 0.001f;
            if (displayState == DisplayState.shrink) {
                displayState = DisplayState.hide;
            }
        }

        if (scale > 1f) {
            scale = 1f;
            if (displayState == DisplayState.grow) {
                displayState = DisplayState.show;
            }
        }

        float dw = button.width * scale;
        float dh = button.height * scale;

        float x = button.origX;
        float y = button.origY;

        switch (direction) {
            case 0:
                x = button.origX - (dw /2);
                y +=  20 * scale;
                break;
            case 1:
                y = button.origY - (dh /2);
                x -= (20 * scale + dw);
                break;
            case 2:
                x += 20 * scale;
                y = button.origY - (dh /2);
                break;
        }

        button.bounds.set(x, y, dw, dh);
    }

    public boolean isComplete() {
        return displayState == DisplayState.hide;
    }

    public boolean handleTouch(int screenX, int screenY) {
        for (OptionButton button : options) {
            if (button.checkForTouch(screenX, screenY)) {
                if (displayState == DisplayState.show) {
                    pendingAction.action = button.action;
                }
                return true;
            }
        }
        return false;
    }
}
