package lando.systems.ld38.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.ui.EndTurnButton;
import lando.systems.ld38.ui.OptionButton;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.world.*;
import java.util.ArrayList;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen {

    public TextureRegion debugTex;
    public World world;
    public UserResources resources;
    public TurnCounter turnCounter;
    public Array<Tile> adjacentTiles;
    public EndTurnButton endTurnButton;

    public FrameBuffer pickBuffer;
    public TextureRegion pickRegion;
    public Pixmap pickPixmap;
    public Color pickColor;
    public int pickMapScale = 8;

    public boolean alternate = true;
    public int turn;
    public Array<TurnAction> turnActions;
    float time;

    public Vector3 cameraTouchStart;
    public Vector3 touchStart;
    public static float zoomScale = 0.05f;
    public static float maxZoom = 1.5f;
    public static float minZoom = 0.2f;

    public GameScreen() {
        super();
        debugTex = Assets.whitePixel;
        time = 0;
        world = new World();
        resources = new UserResources();
        turnCounter = new TurnCounter();
        adjacentTiles = new Array<Tile>();
        turn = 0;
        turnActions = new Array<TurnAction>();
        pickBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Config.gameWidth / pickMapScale, Config.gameHeight / pickMapScale, false, false);
        pickRegion = new TextureRegion(pickBuffer.getColorBufferTexture());
        pickRegion.flip(false, true);
        pickPixmap = null;
        pickColor = new Color();

        endTurnButton = new EndTurnButton(Assets.whitePixel, new Rectangle(20, 20, 100, 30));

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
            for (Tile tile : adjacentTiles) {
                tile.isHighlighted = false;
            }
            adjacentTiles.clear();
            adjacentTiles.addAll(world.getNeighbors(world.players.first().row, world.players.first().col));
            for (Tile tile : adjacentTiles) {
                tile.isHighlighted = true;
            }
        }

        if (pickPixmap != null) {
            pickPixmap.dispose();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cameraTouchStart.set(camera.position);
        touchStart.set(screenX, screenY, 0);

        selectPlayer(screenX, screenY);

        return true;
    }

    public GridPoint2 getGridPosition() {
        return getGridPosition(Gdx.input.getX(), Gdx.input.getY());
    }

    public GridPoint2 getGridPosition(int screenX, int screenY) {
        int x = screenX;
        int y = Gdx.graphics.getHeight() - screenY;
        pickColor.set(pickPixmap.getPixel(x / pickMapScale, y / pickMapScale));
        int col = (int) (pickColor.r * (255f / 5f));
        int row = (int) (pickColor.g * (255f / 5f));
        return new GridPoint2(col, row);
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

    private Vector2 touchPosScreen    = new Vector2();
    private Vector3 touchPosUnproject = new Vector3();

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchPosUnproject = hudCamera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);

        if (endTurnButton.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
            endTurn();
        }
//        if (resetProgressBtn.checkForTouch(touchPosScreen.x, touchPosScreen.y)) {
//            showConfirmDlg = !showConfirmDlg;
//            return true;
//        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == 0) {
            return false;
        }

        // Change zoom in smaller increments when zoomed in
        float newZoom = camera.zoom + (amount * zoomScale * camera.zoom);

        camera.zoom = MathUtils.clamp(newZoom, minZoom, maxZoom);
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

//            batch.draw(pickRegion, hudCamera.viewportWidth - 100, 0, 100, 100);

            endTurnButton.render(batch);
        }
        batch.end();
    }

    private void endTurn() {
        for (TurnAction turnAction : turnActions) {
            turnAction.doAction();
        }
        turnActions.clear();
        ++turn;
        world.endTurn();
    }

    private void selectPlayer(int screenX, int screenY) {
        GridPoint2 location = getGridPosition(screenX, screenY);
        Array<Player> players = world.getPlayers(location);
        if (players.size == 0) return;

        // will have to z order players on moveTo and grab top player - when going to the player from the character
        // menu, reorder that player on top
        Player player = players.get(0);
        
        Tile tile = world.getTile(location);
        showOptions(player, tile);
    }

    private void showOptions(Player player, Tile tile) {
        // determine available options from tile - for now, use all three
        Array<OptionButton> optionButtons = new Array<OptionButton>(3);

        float x = player.position.x + 10;
        float y = player.position.y + 20;
        Rectangle buttonBounds = new Rectangle(x, y, 1, 1);
        optionButtons.add(new OptionButton("Build", buttonBounds));




    }

    private void hideOptions(Player player) {

    }
}
