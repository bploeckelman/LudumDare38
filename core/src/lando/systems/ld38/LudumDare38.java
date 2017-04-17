package lando.systems.ld38;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.screens.BaseScreen;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.utils.Assets;

public class LudumDare38 extends ApplicationAdapter {

    public static LudumDare38 game;

    SpriteBatch batch;
    BaseScreen screen;

    @Override
    public void create () {
        Assets.load();
        float progress = 0f;
        do {
            progress = Assets.update();
        } while (progress != 1f);
        game = this;

        batch = Assets.batch;
        setScreen(new GameScreen());
    }

    @Override
    public void render () {
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);
        screen.update(dt);
        screen.render(batch);
    }

    @Override
    public void dispose () {
        Assets.dispose();
    }

    public void setScreen(BaseScreen newScreen){
        screen = newScreen;
    }

}
