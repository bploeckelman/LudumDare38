package lando.systems.ld38.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen{

    public Texture debugTex;

    public GameScreen(){
        super();
        debugTex = Assets.whitePixel;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(debugTex, 0, 0, 50, 50);

        batch.end();
    }

}
