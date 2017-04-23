package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.world.Player;

/**
 * Created by Brian on 4/23/2017.
 */
public class ActionMenu {
    public Array<OptionButton> options;
    public Player player;

    float totalTime = 0;

    enum DisplayState { grow, show, shrink, hide }

    private DisplayState displayState = DisplayState.grow;

    public ActionMenu(Player player, Array<OptionButton> options)
    {
        this.player = player;
        this.options = options;
    }

    public void hide() {
        displayState = DisplayState.shrink;
    }

    public void update(float dt) {
        totalTime += dt;

        // up 0, left 1, right, 2
        int direction = 0;
        for (OptionButton button : options) {
            move(button, direction);
            direction++;
        }
    }

    public void render(SpriteBatch batch) {
        for (OptionButton button : options) {
            button.render(batch);
        }
    }

    private void move(OptionButton button, int direction) {

        switch (direction) {
            case 0:
                button.bounds.y += 1;
                break;
            case 1:
                button.bounds.x -= 1;
                break;
            case 2:
                button.bounds.x += 1;
                break;
        }
    }

    public boolean isComplete() {
        return false;
    }
}
