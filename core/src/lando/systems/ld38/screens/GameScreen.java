package lando.systems.ld38.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.TurnCounter;
import lando.systems.ld38.world.UserResources;
import lando.systems.ld38.world.World;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen {

    public TextureRegion debugTex;
    public World world;
    public UserResources resources;
    public TurnCounter turnCounter;
    public FrameBuffer pickBuffer;
    public TextureRegion pickRegion;

    public boolean alternate = true;
    public int turn;
    public Array<TurnAction> turnActions;
    float time;

    public Vector3 cameraTouchStart;
    public Vector3 touchStart;
    public static float zoomScale = 0.05f;
    public static float maxZoom = 1.5f;
    public static float minZoom = 0.2f;

    public GameScreen(){
        super();
        time = 0;
        world = new World();
        resources = new UserResources();
        turnCounter = new TurnCounter();
        debugTex = Assets.whitePixel;
        turn = 0;
        turnActions = new Array<TurnAction>();
        pickBuffer = new FrameBuffer(Pixmap.Format.RGB888, Config.gameWidth, Config.gameHeight, false, false);
        pickRegion = new TextureRegion(pickBuffer.getColorBufferTexture());
        pickRegion.flip(false, true);

        cameraTouchStart = new Vector3();
        touchStart = new Vector3();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        time += dt;
        world.update(dt);

        if (Gdx.input.justTouched()) {
            Player character = world.players.first();
            int toCol = character.col + (alternate ? 1 : 0);
            int toRow = character.row + (alternate ? 0 : 1);
            TurnAction turnAction = new TurnAction();
            turnAction.character = character;
            turnAction.action = new ActionTypeMove(turnAction, toCol, toRow);
            turnActions.add(turnAction);
            alternate = !alternate;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            endTurn();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cameraTouchStart.set(camera.position);
        touchStart.set(screenX, screenY, 0);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.position.x = MathUtils.clamp(cameraTouchStart.x + (touchStart.x - screenX) * camera.zoom,
           0, world.bounds.width);
        camera.position.y = MathUtils.clamp(cameraTouchStart.y + (screenY - touchStart.y) * camera.zoom,
           0, world.bounds.height);
        camera.update();
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == 0) {
            return false;
        }

        // Change zoom in smaller increments when zoomed in
        float newZoom = camera.zoom + (amount * zoomScale * camera.zoom);

        if (newZoom < minZoom || newZoom > maxZoom) {
            return false;
        }

        camera.zoom = newZoom;
        camera.update();
        return true;
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            world.render(batch);
        }
        batch.end();
        pickBuffer.begin();
        {
            Gdx.gl.glClearColor(0f, 0f, 1f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            world.renderPickBuffer(batch);
            batch.end();
        }
        pickBuffer.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            resources.render(batch);
            batch.setColor(Color.WHITE);
            turnCounter.render(batch, turn);
            Assets.font.draw(batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 3, 16);

            batch.draw(pickRegion, hudCamera.viewportWidth - 100, 0, 100, 100);
        }
        batch.end();
    }

    private void endTurn() {
        for (TurnAction turnAction : turnActions) {
            turnAction.doAction();
        }
        turnActions.clear();
        ++turn;
    }

}
