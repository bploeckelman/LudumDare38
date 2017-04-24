package lando.systems.ld38.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Elastic;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.PendingAction;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.accessors.RectangleAccessor;
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

    enum DisplayState { grow, show, shrink, hide }

    private DisplayState displayState = DisplayState.grow;

    public ActionMenu(Player player, Array<OptionButton> options)
    {
        this.player = player;
        this.options = options;
        pendingAction.player = player;

        float buttonSpread = 50;
        float dir = 90;
        float dr = 360 / options.size;
        for (OptionButton btn : options){
            float x = btn.origX + (MathUtils.cosDeg(dir) * buttonSpread) - btn.width/2;
            float y = btn.origY + (MathUtils.sinDeg(dir) * buttonSpread) - btn.height/2;


            btn.bounds.set(btn.origX, btn.origY, 0, 0);
            Tween.to(btn.bounds, RectangleAccessor.XYWH, 1f)
                    .target(x, y, btn.width, btn.height)
                    .ease(Elastic.OUT)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            displayState = DisplayState.show;
                        }
                    })
                    .start(Assets.tween);
            dir += dr;
        }
    }

    public void hide() {
        displayState = DisplayState.shrink;
        for (OptionButton btn : options){
            Tween.to(btn.bounds, RectangleAccessor.XYWH, .5f)
                    .target(btn.origX, btn.origY, 0, 0)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            displayState = DisplayState.hide;
                        }
                    })
                    .start(Assets.tween);

        }

    }

    public void update(float dt) {
        for (OptionButton button : options) {
            button.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        for (OptionButton button : options) {
            button.render(batch);
        }
    }


    public boolean isComplete() {
        return displayState == DisplayState.hide;
    }

    public boolean handleTouch(int screenX, int screenY) {
        for (OptionButton button : options) {
            if (button.checkForTouch(screenX, screenY)) {
                if (displayState == DisplayState.show || displayState == DisplayState.grow) {
                    pendingAction.action = button.action;
                }
                return true;
            }
        }
        return false;
    }
}
