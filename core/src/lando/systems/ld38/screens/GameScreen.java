package lando.systems.ld38.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld38.managers.ActionManager;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.ActionTypeWait;
import lando.systems.ld38.turns.PendingAction;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.ui.*;
import lando.systems.ld38.turns.*;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.utils.Screenshake;
import lando.systems.ld38.utils.SoundManager;
import lando.systems.ld38.utils.*;
import lando.systems.ld38.utils.accessors.CameraAccessor;
import lando.systems.ld38.world.*;

//import static com.sun.glass.ui.gtk.GtkApplication.screen;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen {

    public TutorialManager tutorialManager;
    public TextureRegion debugTex;
    public World world;
    public UserResources resources;
    public TurnCounter turnCounter;
    public Modal modal;
    public Array<Tile> adjacentTiles;
    public Array<Tile> adjacentBuildTiles;
    public EndTurnButton endTurnButton;
    public PlayerSelectionHud playerSelection;
    public Player selectedPlayer;
    public ResourceCount actionCost;
    public OptionButton actionButton;
    public Array<Bird> birds;

    public FrameBuffer pickBuffer;
    public TextureRegion pickRegion;
    public Pixmap pickPixmap;
    public Color pickColor;
    public int pickMapScale = 8;

    public int turn;
    public Array<TurnAction> turnActions;
    float time;

    public Vector3 cameraTouchStart;
    public Vector3 touchStart;
    public static float zoomScale = 0.15f;
    public static float maxZoom = 1.6f;
    public static float minZoom = 0.2f;
    public static float DRAG_DELTA = 10f;

    public boolean cancelTouchUp = false;
    public boolean firstRun = false;

    public Button testingButton;

    public ActionManager actionManager;
    public MutableFloat overlayAlpha;
    public boolean pauseGame;
    public boolean gameOver;
    public boolean gameLost;
    EndGameOverlay endGameOverlay;
    public Statistics stats;
    public Screenshake shaker;
    public Vector2 cameraCenter;
    public float gullTimer;

    public GameScreen() {
        super();
        SoundManager.oceanWaves.play();
        gullTimer = 40;
        stats = new Statistics();
        cameraCenter = new Vector2();
        gameOver = false;
        actionManager = new ActionManager(camera);
        debugTex = Assets.whitePixel;
        overlayAlpha = new MutableFloat(1);
        pauseGame = true;
        time = 0;
        world = new World(this);
        resources = new UserResources(hudCamera);
        turnCounter = new TurnCounter(hudCamera);
        adjacentTiles = new Array<Tile>();
        adjacentBuildTiles = new Array<Tile>();
        turn = 0;
        turnActions = new Array<TurnAction>();
        pickBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Config.gameWidth / pickMapScale, Config.gameHeight / pickMapScale, false, false);
        pickRegion = new TextureRegion(pickBuffer.getColorBufferTexture());
        pickRegion.flip(false, true);
        pickPixmap = null;
        pickColor = new Color();
        modal = new Modal();
        birds = new Array<Bird>();

        endTurnButton = new EndTurnButton(new Rectangle(hudCamera.viewportWidth - 100 - 10, 10, 100, 30), hudCamera);
        playerSelection = new PlayerSelectionHud(this);
//        testingButton = new Button(Assets.transparentPixel, new Rectangle(50,50,50,50), hudCamera, "Too much Text!", "Tooltip");
        cameraTouchStart = new Vector3();
        touchStart = new Vector3();
        shaker = new Screenshake(120, 3);

        startScript();
    }

    @Override
    public void update(float dt) {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            Gdx.app.exit();
//        }
        if (endGameOverlay == null) {
            stats.totalTime += dt;
        }
        gullTimer -= dt;
        if (gullTimer < 0 && endGameOverlay == null){
            gullTimer = MathUtils.random(30f,90f);
            SoundManager.playSound(SoundManager.SoundOptions.seagull);
        }
        if (tutorialManager != null) {
            if (!firstRun && tutorialManager.isDisplayed()) {
                tutorialManager.update(dt);
                return;
            }
            firstRun = false;
        }

        if (endGameOverlay != null){
            endGameOverlay.update(dt);
        }

        if (!modal.isActive) {
            float movementDt = 200 * dt * camera.zoom;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera.translate(0, movementDt);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera.translate(0, -movementDt);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera.translate(-movementDt, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.translate(movementDt, 0);
            }
        }
        SoundManager.update(dt);

        time += dt;
        world.update(dt);
        endTurnButton.update(dt);
        resources.update(dt);

        if (tutorialManager != null) {
            handleKeyBindings();
        }

        if (pickPixmap != null) {
            pickPixmap.dispose();
        }

        actionManager.update(dt);
        updateCamera();

        for (int i = birds.size -1; i >= 0; i--) {
            Bird b = birds.get(i);
            b.update(dt);
            if (!b.alive){
                birds.removeIndex(i);
            }
        }
        if (MathUtils.randomBoolean(.0015f)){
            birds.add(new Bird());
        }

//        if (!modal.isActive && Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
//            float w = hudCamera.viewportWidth * (2f / 3f);
//            float h = hudCamera.viewportHeight * (2f / 3f);
//            modal.set("Busey ipsum dolor sit amet. Go with the feeling of the nature. Take it easy. Know why you're here. And remember to balance your internal energy with the environment.Sometimes horses cough and fart at the same time, so stay out of the range of its butt muscle because a horses butt muscle is thick.",
//                    hudCamera.viewportWidth / 2f - w / 2f,
//                    hudCamera.viewportHeight / 2f - h / 2f,
//                    w, h);
//            modal.isActive = true;
//            modal.scale = 0.3f;
//        }

        shaker.update(dt, camera, camera.position.x, camera.position.y);

        // NOTE: Sound DEBUG
//        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) SoundManager.playSound(SoundManager.SoundOptions.button_select);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) SoundManager.playSound(SoundManager.SoundOptions.ladder);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) SoundManager.playSound(SoundManager.SoundOptions.ocean_waves);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) SoundManager.playSound(SoundManager.SoundOptions.player_move);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) SoundManager.playSound(SoundManager.SoundOptions.resource_collected);
//        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) SoundManager.playSound(SoundManager.SoundOptions.seagull);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (modal.isActive || (tutorialManager != null && tutorialManager.isDisplayed())) {
            return false;
        }

        cameraTouchStart.set(camera.position);
        touchStart.set(screenX, screenY, 0);

        return true;
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
        if (modal.isActive || (tutorialManager != null && tutorialManager.isDisplayed())) {
            return false;
        }

        camera.position.x = cameraTouchStart.x + (touchStart.x - screenX) * camera.zoom;
        camera.position.y = cameraTouchStart.y + (screenY - touchStart.y) * camera.zoom;
        if (cameraTouchStart.dst(camera.position) > DRAG_DELTA) {
            cancelTouchUp = true;
        }
        return true;
    }

    public void showOptions(Player player) {
        actionButton = null;
        actionCost = null;
        selectedPlayer = player;
        world.orderPlayer(player);
        clearMovement();
        actionManager.showOptions(player);
    }

    public void handleBindingPress() {
        handlePlayerAction(-1, -1, -1);
    }

    private boolean handlePlayerAction(int screenX, int screenY, int button) {
        PendingAction pendingAction = actionManager.handleTouch(screenX, screenY, button);
        if (pendingAction != null) {
            actionCost = pendingAction.cost;
            actionButton = pendingAction.button;
            switch (pendingAction.action) {
                case displayMoves:
                    showMovement(selectedPlayer, null);
                    break;
                case harvest:
                    addHarvestAction(selectedPlayer, true);
                    break;
                case build:
                    if (actionButton.region == Assets.hammer_upgrade){
                        TurnAction turnAction = new TurnAction(selectedPlayer, actionCost);
                        turnAction.action = new ActionTypeBuild(turnAction, actionButton.region, selectedPlayer.col, selectedPlayer.row);
                        addAction(turnAction, selectedPlayer.getHudPostion(camera, hudCamera));
                    } else {
                        showMovement(selectedPlayer, actionButton.region);
                    }
                    break;
            }
        }
        return (pendingAction != null);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (tutorialManager != null && tutorialManager.isDisplayed()) {
            return false;
        }

        if (modal.isActive && modal.checkForTouch(screenX, screenY, hudCamera)) {
            modal.isActive = false;
            modal.set("", 0, 0, 0, 0);
            return true;
        }

        if (cancelTouchUp) {
            cancelTouchUp = false;
            return false;
        }

        if (playerSelection.handleTouch(screenX, screenY)) return false;
        if (endTurnButton.checkForTouch(screenX, screenY)) {
            endTurnButton.handleTouch();
            endTurn();
        }

            GridPoint2 location =  getGridPosition(screenX, screenY);
            if (handleMove(location)) return false;
            if (handlePlayerAction(screenX, screenY, button)) return false;

            Array<Player> players = world.getPlayers(location);
            Player player = (players.size > 0) ? players.get(0) : null;
            showOptions(player);


        return false;
    }

    private boolean handleMove(GridPoint2 location) {
        if (actionButton == null) return false;

        Tile tile = world.getTile(location);
        if (actionButton.action == Actions.displayMoves) {
            if (adjacentTiles.contains(tile, true) && !tile.isInaccessible) {
                SoundManager.playSound(SoundManager.SoundOptions.player_move);
                TurnAction turnAction = new TurnAction(selectedPlayer, actionCost);
                turnAction.action = new ActionTypeMove(turnAction, tile.col, tile.row);
                addAction(turnAction, selectedPlayer.getHudPostion(camera, hudCamera));
                clearMovement();
                return true;
            }
        } else if (actionButton.action == Actions.build){
            if (adjacentTiles.contains(tile, true)) {
                TurnAction turnAction = new TurnAction(selectedPlayer, actionCost);
                turnAction.action = new ActionTypeBuild(turnAction, actionButton.region, tile.col, tile.row);
                addAction(turnAction, selectedPlayer.getHudPostion(camera, hudCamera));
                clearMovement();
                return true;
            }
        }
        return false;
    }

    private void addAction(TurnAction turnAction, Vector2 triggeringIconScreenPos) {

        // Find and cancel an existing turnAction for the player
        for (int i = turnActions.size - 1; i >= 0; i--) {
            if (turnActions.get(i).player == turnAction.player) {
                TurnAction removeAction = turnActions.get(i);
                if (removeAction.action instanceof ActionTypeMove) {
                    removeAction.action.getTargetTile(world).isMoveTarget = false;
                }
                if (removeAction.action instanceof ActionTypeBuild) {
                    removeAction.action.getTargetTile(world).isBuildTarget = false;
                }
                if (removeAction.action instanceof ActionTypeWait) {
                    removeAction.action.getTargetTile(world).isWaitTarget = false;
                }
                removeAction.player.getResources().add(removeAction.cost);
                turnActions.removeIndex(i);
            }
        }

        if (turnAction.action instanceof ActionTypeMove) {
            turnAction.action.getTargetTile(world).isMoveTarget = true;
        }

        if (turnAction.action instanceof ActionTypeBuild) {
            turnAction.action.getTargetTile(world).isBuildTarget = true;
        }
        if (turnAction.action instanceof ActionTypeWait) {
            turnAction.action.getTargetTile(world).isWaitTarget = true;
        }
        turnActions.add(turnAction);
        turnAction.player.playerHud.setTurnAction(turnAction, triggeringIconScreenPos);

    }

    private void showMovement(Player player, TextureRegion asset) {
        // TODO: is there a situation where this could be null?
        Tile playerTile = world.getTile(player.row, player.col);

        adjacentTiles.addAll(world.getNeighbors(player.row, player.col));
        for (Tile tile : adjacentTiles) {
            tile.isHighlighted = true;

            // Water inaccessible...
            if (tile.heightOffset < world.water.waterHeight) {
                tile.isInaccessible = (asset == null);
                if (tile.isInaccessible) {
                    if (tile.item == Assets.raft) {
                        tile.isInaccessible = false;
                    } else {
                        tile.overlayObjectTex = Assets.raft;
                    }
                }
            }
            // height inaccessible
//            if (tile.height > 1f + playerTile.height) { // NOTE: this line requires ladders for tiles more than 1 'step' above player tile's height
            if (tile.height > playerTile.height) { // NOTE: this line requires ladders _ALL_ tiles above the player tile's height
                tile.isInaccessible = (asset == null);
                if (tile.isInaccessible) {
                    if (tile.item == Assets.ladder) {
                        tile.isInaccessible = false;
                    } else {
                        tile.overlayObjectTex = Assets.ladder;
                    }
                }
            }
        }
    }

    private void addHarvestAction(Player player, boolean animateAction) {
        TurnAction turnAction = new TurnAction(player, new ResourceCount());
        turnAction.action = new ActionTypeWait(turnAction, resources, player);
        addAction(turnAction, animateAction? player.getHudPostion(camera, hudCamera) : null);
    }

    private void clearMovement() {
        for (Tile tile : adjacentTiles) {
            tile.isHighlighted = false;
            tile.isInaccessible = false;
            tile.overlayObjectTex = null;
        }
        adjacentTiles.clear();
    }


    Vector3 tp = new Vector3();
    @Override
    public boolean scrolled (int change) {
        if (tutorialManager != null && tutorialManager.isDisplayed()) {
            return false;
        }

        camera.unproject(tp.set(Gdx.input.getX(), Gdx.input.getY(), 0 ));
        float px = tp.x;
        float py = tp.y;
        camera.zoom += change * camera.zoom * zoomScale;
        updateCamera();

        camera.unproject(tp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        camera.position.add(px - tp.x, py- tp.y, 0);
        camera.update();
        return true;
    }

    private void updateCamera(){
        camera.zoom = MathUtils.clamp(camera.zoom, minZoom, maxZoom);
        float minY = world.bounds.y + camera.viewportHeight/2 * camera.zoom;
        float maxY = world.bounds.height - camera.viewportHeight/2 * camera.zoom;

        float minX = world.bounds.x + camera.viewportWidth/2 * camera.zoom;
        float maxX = world.bounds.x + world.bounds.width - camera.viewportWidth/2 * camera.zoom;

        if (camera.viewportHeight * camera.zoom > world.bounds.height){
            camera.position.y = world.bounds.height/2;
        } else {
            camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);
        }


        if (camera.viewportWidth * camera.zoom > world.bounds.width){
            camera.position.x = world.bounds.x + world.bounds.width/2;
        } else {
            camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        }

        camera.update();
    }


    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw world
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            world.render(batch);
            for (Bird b : birds){
                b.render(batch);
            }
        }
        batch.end();

        // Draw lines between player and action 'target' tile
        Assets.shapes.setProjectionMatrix(camera.combined);
        Assets.shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (TurnAction turnAction : turnActions) {
            if (turnAction.action instanceof ActionTypeWait) continue;
            if (turnAction.action instanceof ActionTypeMove) {
                Assets.shapes.setColor(Color.GREEN);
            } else if (turnAction.action instanceof ActionTypeBuild){
                Assets.shapes.setColor(Color.ORANGE);
            }
            Tile targetTile = turnAction.action.getTargetTile(world);
            Tile playerTile = turnAction.player.getTile();
            Assets.shapes.rectLine(
                    playerTile.position.x + Tile.tileWidth  / 2f,
                    playerTile.position.y + Tile.tileHeight / 2f + playerTile.position.z + playerTile.heightOffset - 5f,
                    targetTile.position.x + Tile.tileWidth  / 2f,
                    targetTile.position.y + Tile.tileHeight / 2f + targetTile.position.z + targetTile.heightOffset,
                    2f);
        }
        Assets.shapes.setColor(Color.WHITE);
        Assets.shapes.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        {
            actionManager.render(batch);

        }
        batch.end();

        // Draw picking frame buffer
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

        // Draw HUD
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        {
            playerSelection.render(batch);
            resources.render(batch);
            turnCounter.render(batch, turn);
            endTurnButton.render(batch);

            actionManager.renderTooltops(batch, hudCamera);
            world.getResources().renderToolTips(batch, hudCamera);

//            testingButton.render(batch);
//            Assets.font.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 3, 16);

            if (modal.isActive) {
                modal.render(batch);
            }

            // Draw pick region stuff
//            batch.setColor(pickColor);
//            batch.draw(Assets.whitePixel, hudCamera.viewportWidth - 100 - 50, 0, 50, 50);
//            batch.setColor(Color.WHITE);
//            batch.draw(pickRegion, hudCamera.viewportWidth - 100, 0, 100, 100);
            batch.setColor(0,0,0,overlayAlpha.floatValue());
            batch.draw(Assets.whitePixel, 0,0, hudCamera.viewportWidth, hudCamera.viewportHeight);
            batch.setColor(Color.WHITE);

            if (endGameOverlay != null) endGameOverlay.render(batch);

            if (tutorialManager != null) {
                tutorialManager.render(batch);
            }
        }
        batch.end();

    }

    private void endTurn() {
        showOptions(null);
        selectedPlayer = null;
        SoundManager.playSound(SoundManager.SoundOptions.water_rise);
        for (Player p : world.players){
            boolean hasAction = false;
            for (TurnAction turnAction : turnActions){
                if (turnAction.player == p) hasAction = true;
            }
            if (!hasAction){
                addHarvestAction(p, false);
            }
        }
        for (TurnAction turnAction : turnActions) {
            turnAction.doAction();
            if (turnAction.action instanceof ActionTypeMove) {
                turnAction.action.getTargetTile(world).isMoveTarget = false;
            }
            if (turnAction.action instanceof ActionTypeBuild) {
                turnAction.action.getTargetTile(world).isBuildTarget = false;
            }
            if (turnAction.action instanceof ActionTypeWait) {
                turnAction.action.getTargetTile(world).isWaitTarget = false;
            }
        }
        turnActions.clear();
        for (Player p : world.players) {
            p.playerHud.setTurnAction(null, null);
        }
        ++turn;
        world.endTurn();
        int alivePlayers = 0;
        int playersNotOnSnow = 0;
        for (Player p : world.players){
            if (!p.dead) alivePlayers++;
            if (p.getTile().type != TileType.Snow) playersNotOnSnow++;
        }

        if (turn > 70 || alivePlayers == 0 || playersNotOnSnow == 0){
            gameLost = alivePlayers == 0;
            Gdx.input.setInputProcessor(null);

            Timeline.createSequence()
                    .push(Tween.to(camera, CameraAccessor.XYZ, 2f)
                    .target((world.bounds.x + world.bounds.width)/2f,
                            (world.bounds.y + world.bounds.height)/2f,
                            1.5f))
                    .push(Tween.call(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            SoundManager.oceanWaves.stop();
                            SoundManager.playMusic(SoundManager.MusicOptions.end_game);
                            gameOver = true;
                            endGameOverlay = new EndGameOverlay(GameScreen.this);
                        }
                    }))
                    .start(Assets.tween);

        }
    }

    public void zoomToPlayer(Player p){
        showOptions(p);
        Tween.to(camera, CameraAccessor.XYZ, .5f)
                .target(p.position.x + p.tileWidth / 2f, p.position.y + p.position.z + p.tileHeight / 2f, .5f)
                .start(Assets.tween);
    }

    public void startScript(){
        pauseGame = true;
        camera.position.x = (world.bounds.x + world.bounds.width)/2f;
        camera.position.y = (world.bounds.y + world.bounds.height)/2f;
        camera.zoom = maxZoom;
        camera.update();
        overlayAlpha.setValue(1);
        Timeline.createSequence()
                .push(Tween.to(overlayAlpha, 0, 2f)
                        .target(0))
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        shaker.shake(2);
                        SoundManager.playSound(SoundManager.SoundOptions.earthquake);
                    }
                }))
                .push(Tween.to(Tile.renderShift, 0, 2f)
                        .target(0))
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        world.addStartPlayers();
                        playerSelection.buildPlayerHuds();
                    }
                }))
                .pushPause(1f)
                .push(Tween.to(camera, CameraAccessor.XYZ, 1f)
                        .target(world.WORLD_WIDTH * Tile.tileWidth / 2f, 2 * Tile.tileHeight, .5f))
                .pushPause(0.5f)
                .push(Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        pauseGame = false;
                        firstRun = true;
                        tutorialManager = new TutorialManager(GameScreen.this);
                        Gdx.input.setInputProcessor(GameScreen.this);
                    }
                }))
                .start(Assets.tween);
    }

    private void handleKeyBindings() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playerSelection.selectNext();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            endTurn();
        }
    }

    int[] sequence = new int [] { Input.Keys.UP, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.B, Input.Keys.A};
    int index = 0;
    public boolean keyUp(int keyCode) {
        if (sequence[index] == keyCode) {
            if (++index == sequence.length) {
                resources.add(new ResourceCount(30, 30, 30, 30, 30));
            }
        } else {
            index = 0;
        }
        return false;
    }
}
