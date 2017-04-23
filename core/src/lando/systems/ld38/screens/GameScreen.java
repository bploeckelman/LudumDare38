package lando.systems.ld38.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.TurnAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.TurnCounter;
import lando.systems.ld38.world.UserResources;
import lando.systems.ld38.world.Tile;
import lando.systems.ld38.world.World;
import sun.font.GlyphLayout;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen {

    public Texture debugTex;
    public World world;
    public UserResources resources;
    public TurnCounter turnCounter;

    public boolean alternate = true;
    public int turn;
    public Array<TurnAction> turnActions;
    float time;

    public Vector3 cameraTouchStart;
    public Vector3 touchStart;
    public static float zoomScale = 0.05f;
    public static float maxZoom = 1f;
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

        float newZoom = camera.zoom + (amount * zoomScale);

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

        world.render(batch);

        batch.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        resources.render(batch);
        batch.setColor(Color.WHITE);

        turnCounter.render(batch, turn);

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
