package lando.systems.ld38.world;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.TurnAction;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.accessors.Vector3Accessor;

public class Player extends GameObject {
    enum Type {
        BF(Assets.bfWalkDown, Assets.bfWalkUp, Assets.bfWalkSide, Assets.head_female_dark),
        WF(Assets.wfWalkDown, Assets.wfWalkUp, Assets.wfWalkSide, Assets.head_female_white),
        BM(Assets.bmWalkDown, Assets.bmWalkUp, Assets.bmWalkSide, Assets.head_male_dark),
        WM(Assets.wmWalkDown, Assets.wmWalkUp, Assets.wmWalkSide, Assets.head_male_white);

        public Animation<TextureRegion> down;
        public Animation<TextureRegion> up;
        public Animation<TextureRegion> side;
        public TextureRegion head;
        Type(Animation<TextureRegion> down, Animation<TextureRegion> up, Animation<TextureRegion> side, TextureRegion head) {
            this.down = down;
            this.up = up;
            this.side = side;
            this.head = head;
        }
    }

    public TextureRegion tex;
    public TextureRegion faceTex;
    public float timer = 0f;

    public boolean walkRight = false;
    public Type type;
    public Animation<TextureRegion> animation;

    public Player(World world, int row, int col) {
        super(world);
        type = new Array<Type>(Type.values()).random();
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
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        timer += dt;
        tex = animation.getKeyFrame(timer);
    }

    public void render(SpriteBatch batch, float x, float y, float waterHeight, boolean aboveWater) {
        if (walkRight) {
            batch.draw(tex, x + tileWidth, y, -tileWidth, tileHeight);
        } else {
            batch.draw(tex, x, y, tileWidth, tileHeight);
        }
    }

    public void moveTo(final int row, final int col) {
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

        // temp for viewing the resource gather
        displayResourceGather(1);

        this.row = row;
        this.col = col;
        Tween.to(position, Vector3Accessor.XYZ, 1f)
            .target(newX, newY, tileOffset)
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int eventType, BaseTween<?> source) {
                    walkRight = false;
                    animation = type.down;
                }
            })
            .start(Assets.tween);

    }

    public void displayResourceGather(int numResourcesGathered)
    {
        TextureRegion icon = getResourceIcon();
        if (icon == null) return;
        Rectangle resourcePos = getResourceBounds();
        world.resIndicators.add(new ResourceIndicator(icon, resourcePos, numResourcesGathered));
    }

    public TextureRegion getResourceIcon() {
        Tile tile = getTile(row, col);
        return tile.top_tex;
    }

    public Rectangle getResourceBounds() {
        return new Rectangle(position.x, position.y, 15, 15);
    }

    public void renderHud(SpriteBatch batch, float x, float y, TurnAction action){
        batch.draw(faceTex, x, y, 25, 25);
        if (action != null){
            if (action.action instanceof ActionTypeMove){
                batch.draw(Assets.arrow, x + 30, y, 25, 25);
            }
        }
        // TODO draw action icon here

    }
}
