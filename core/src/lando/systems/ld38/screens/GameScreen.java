package lando.systems.ld38.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.world.UserResources;
import lando.systems.ld38.world.World;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen{

    public Texture debugTex;
    public World world;
    public UserResources resources;

    public GameScreen(){
        super();
        world = new World();
        resources = new UserResources();
        debugTex = Assets.whitePixel;
    }

    @Override
    public void update(float dt) {
        world.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        world.render(batch);

        batch.end();

        resources.render();
    }

}
