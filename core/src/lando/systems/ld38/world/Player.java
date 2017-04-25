package lando.systems.ld38.world;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.ActionTypeBuild;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.ActionTypeWait;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.ui.PlayerHud;
import lando.systems.ld38.ui.PlayerSelectionHud;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.accessors.Vector2Accessor;
import lando.systems.ld38.utils.accessors.Vector3Accessor;

public class Player extends GameObject {
    public TextureRegion tex;
    public TextureRegion faceTex;
    public float timer = 0f;

    public boolean walkRight = false;
    public PlayerType type;
    public Animation<TextureRegion> animation;
    public boolean dead;
    float bubbleAlpha;
    public boolean moving;
    public PlayerHud playerHud;

    private TextureRegion turnActionRegion;
    private Vector2 turnActionRegionDimensions = new Vector2(0,0);
    private Vector2 turnActionRegionPos = new Vector2(0,0);
    private Vector2 turnActionRegionTargetOffset = new Vector2(0,0);
    private Vector2 turnActionRegionStartingOffset = new Vector2(0,0);

    public Player(World world, int row, int col) {
        super(world);
        dead = false;
        moving = false;
        bubbleAlpha = 0;
        type = new Array<PlayerType>(PlayerType.values()).random();
        animation = type.down;
        tex = animation.getKeyFrame(timer);
        faceTex = type.head;
        this.row = row;
        this.col = col;
        float tileOffset = 0f;
        Tile tile = getTile(row, col);

        if (tile != null) {
            tileOffset += tile.height * Tile.heightScale;
        }
        position.x = getX(row, col);
        position.y = getY(row);
        position.z = tileOffset + (tileHeight * .25f);
        setTurnAction(null, new Vector2(0,0));
    }

    public UserResources getResources() {
        return world.getResources();
    }

    // Renders and Updates ---------------------------------------------------------------------------------------------


    @Override
    public void update(float dt) {
        super.update(dt);
        timer += dt;
        tex = animation.getKeyFrame(timer);
    }

    public void updateBubbleAlpha(float dt){
        bubbleAlpha += dt * 2;
        bubbleAlpha = MathUtils.clamp(bubbleAlpha, 0, 1);
    }

    public void render(SpriteBatch batch, float x, float y, float waterHeight, boolean aboveWater) {
        if (walkRight) {
            batch.draw(tex, x + tileWidth, y, -tileWidth, tileHeight);
        } else {
            batch.draw(tex, x, y, tileWidth, tileHeight);
        }
    }

    public void renderBubble(SpriteBatch batch){
        batch.setColor(1,1,1,bubbleAlpha);
        batch.draw(Assets.bubble, position.x - 32 + tileWidth/2, (position.y + position.z + tileHeight), 64, 64);
        batch.draw(faceTex, position.x - 19 + tileWidth/2, (position.y + position.z + tileHeight) + 18, 38, 38);
        batch.setColor(Color.WHITE);
    }

    public void renderHud(SpriteBatch batch) {
        if (isSelected()) {
            batch.setColor(0.04f, 0.51f, 0.01f, 1);
            batch.draw(Assets.whitePixel, playerHud.bounds.x - 4, playerHud.bounds.y - 2, PlayerSelectionHud.BOUNDS_WIDTH - 12, 25 + 4);
            batch.setColor(Color.WHITE);
        }
        batch.draw(
                faceTex,
                playerHud.bounds.x, playerHud.bounds.y,
                25, 25);
        batch.draw(turnActionRegion,
                playerHud.bounds.x + turnActionRegionPos.x, playerHud.bounds.y + turnActionRegionPos.y,
                turnActionRegionDimensions.x, turnActionRegionDimensions.y);
    }


    // -----------------------------------------------------------------------------------------------------------------

    public void setTurnAction(TurnAction turnAction, Vector2 triggeringIconScreenPos) {

        if (actionTween != null) {
//            actionTween.free();
        }

        if (turnAction != null){
            if (turnAction.action instanceof ActionTypeMove){
                turnActionRegion = Assets.arrow;
                turnActionRegionDimensions.set(25,25);
                turnActionRegionTargetOffset.set(30, 0);
            } else if (turnAction.action instanceof ActionTypeWait) {
                Tile t = world.getTile(row, col);
                turnActionRegion = Assets.wait;
                switch (t.decoration){
                    case Tree:
                        turnActionRegion = Assets.axe;
                        break;
                    case Cow:
                        turnActionRegion = Assets.shotgun;
                        break;
                    case IronMine:
                    case GoldMine:
                        turnActionRegion = Assets.pickaxe;
                        break;
                    case Sand:
                        turnActionRegion = Assets.shovel;
                        break;
                    case Hut:
                        turnActionRegion = Assets.heart;
                        break;
                }
                turnActionRegionDimensions.set(25,25);
                turnActionRegionTargetOffset.set(30, 0);
            } else if (turnAction.action instanceof ActionTypeBuild) {
                turnActionRegion = Assets.hammer;
                turnActionRegionDimensions.set(25,25);
                turnActionRegionTargetOffset.set(30, 0);
            }
        } else {
            turnActionRegion = Assets.questionmark;
            turnActionRegionDimensions.set(25,25);
            turnActionRegionTargetOffset.set(30, 0);
        }

        // If there's no turn action, if the player hud is unknown (still in setup), or there was no triggering location,
        // don't animate.
        if (turnAction == null || playerHud == null || triggeringIconScreenPos == null) {
            // Just start it at its final position
            turnActionRegionStartingOffset.set(turnActionRegionTargetOffset);
        } else {
            // Start it at the provided screen position
            turnActionRegionStartingOffset.set(triggeringIconScreenPos.sub(playerHud.bounds.x, playerHud.bounds.y));
        }

        turnActionRegionPos.set(turnActionRegionStartingOffset);
        actionTween = Tween.to(turnActionRegionPos,
                Vector2Accessor.XY, 1f)
                .target(turnActionRegionTargetOffset.x, turnActionRegionTargetOffset.y)
                .start(Assets.tween);


    }
    private Tween actionTween;

    public GridPoint2 getLocation() {
        return new GridPoint2(col, row);
    }

    public void moveTo(final int row, final int col) {
        moving = true;
        float newX = getX(row, col);
        float newY = getY(row);
        float tileOffset = (tileHeight * .25f);
        Tile tile = getTile(row, col);

        if (tile != null) {
            tileOffset += tile.height * Tile.heightScale;
        }

        Vector2 from = new Vector2(position.x, position.y + position.z);
        Vector2 to = new Vector2(newX, newY + tileOffset);
        int xDir = Float.valueOf(from.x).compareTo(to.x) * -1;
        float xDiff = from.x > to.x ? from.x - to.x : to.x - from.x;
        int yDir = Float.valueOf(from.y).compareTo(to.y) * -1;
        float yDiff = from.y > to.y ? from.y - to.y : to.y - from.y;

        if (yDiff > xDiff && yDir == -1) {
            animation = type.down;
        } else if (yDiff > xDiff && yDir == 1) {
            animation = type.up;
        } else if (yDiff < xDiff) {
            animation = type.side;
            walkRight = xDir == 1;
        }

        this.row = row;
        this.col = col;
        Tween.to(position, Vector3Accessor.XYZ, 1f)
            .target(newX, newY, tileOffset)
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int eventType, BaseTween<?> source) {
                    walkRight = false;
                    animation = type.down;
                    moving = false;
                }
            })
            .start(Assets.tween);

    }

    public void displayResourceGather(TextureRegion icon, int numResourcesGathered, int offset)
    {
        if (icon == null) return;

        float size = 15;

        Rectangle resourcePos =  new Rectangle(position.x, position.y + position.z + (offset * (size + 2)), size, size);
        world.resIndicators.add(new ResourceIndicator(icon, resourcePos, numResourcesGathered));
    }

    public void kill(){
        // TODO something fancy here?
        dead = true;
    }

    public Vector2 getHudPostion(OrthographicCamera worldCamera, OrthographicCamera hudCamera) {
        Vector2 currentPos = new Vector2(position.x, position.y + position.z);
        Vector3 worldPos = worldCamera.project(new Vector3(currentPos.x, currentPos.y, 0));
        Vector3 hudPos = hudCamera.project(worldPos);
        return new Vector2(hudPos.x, hudPos.y);
    }

    public boolean isSelected() {
        return world.screen.selectedPlayer == this;
    }

}
