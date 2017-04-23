package lando.systems.ld38.world;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.accessors.Vector3Accessor;

public class Player extends GameObject {
    public TextureRegion tex;
    public float timer = 0f;

    public boolean walkRight = false;
    public Animation<TextureRegion> animation;

    public Player(World world) {
        super(world);
        animation = Assets.womanWalkDown;
        tex = animation.getKeyFrame(timer);

        float tileOffset = 0f;
        Tile tile = getTile(row, col);

        if (tile != null) {
            tileOffset += tile.height * Tile.heightScale;
        }
        position.z = position.y + tileOffset + (tileHeight * .25f);
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
        float tileOffset = 0f;
        Tile tile = getTile(row, col);

        if (tile != null) {
            tileOffset += tile.height * Tile.heightScale;
        }

        // temp for viewing the resource gather
        displayResourceGather(1);

        animation = Assets.womanWalkSide;
        this.row = row;
        this.col = col;

        if (position.x < newX) {
            walkRight = true;
        }
        Tween.to(position, Vector3Accessor.XYZ, 1f)
            .target(getX(row, col), newY, tileOffset + (tileHeight * .25f))
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    walkRight = false;
                    animation = Assets.womanWalkDown;
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
}
