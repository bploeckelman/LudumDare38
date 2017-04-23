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
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.world.*;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen {

    public TextureRegion debugTex;
    public World world;
    public Tile selectedTile;
    public UserResources resources;
    public TurnCounter turnCounter;

    public FrameBuffer pickBuffer;
    public TextureRegion pickRegion;
    public Pixmap pickPixmap;
    public Color pickColor;
    public int pickMapScale = 4;

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
        selectedTile = null;
        pickBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Config.gameWidth/pickMapScale, Config.gameHeight/pickMapScale, false, false);
        pickRegion = new TextureRegion(pickBuffer.getColorBufferTexture());
        pickRegion.flip(false, true);
        pickPixmap = null;
        pickColor = new Color();

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

        if (pickPixmap != null) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            pickColor.set(pickPixmap.getPixel(x/pickMapScale, y/pickMapScale));
            selectedTile = Tile.parsePickColorForTileInWorld(pickColor, world);
        }

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

        if (pickPixmap != null){
            pickPixmap.dispose();
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
            if (selectedTile != null) {
                float x = selectedTile.col * Tile.tileWidth;
                float y = selectedTile.row * Tile.tileHeight * .75f;
                if (selectedTile.row % 2 == 0) x += Tile.tileWidth / 2f;
                float heightOffset = selectedTile.height * 2;
                batch.setColor(Color.MAGENTA);
                batch.draw(Assets.white_hex, x, y + heightOffset, Tile.tileWidth, Tile.tileHeight);
                batch.setColor(Color.WHITE);
            }
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
        pickPixmap = ScreenUtils.getFrameBufferPixmap(0, 0, pickBuffer.getWidth(), pickBuffer.getHeight());
        pickBuffer.end();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            resources.render(batch);
            batch.setColor(Color.WHITE);
            turnCounter.render(batch, turn);
            Assets.font.draw(batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 3, 16);

            batch.setColor(pickColor);
            batch.draw(Assets.whitePixel, hudCamera.viewportWidth - 100 - 50, 0, 50, 50);
            batch.setColor(Color.WHITE);

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
