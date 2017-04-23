package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld38.utils.Assets;

public class Player extends GameObject {
    public TextureRegion tex;
    public float timer = 0f;

    public Player(World world) {
        super(world);
        tex = Assets.womanWalkSide.getKeyFrame(timer);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        timer += dt;
        tex = Assets.womanWalkDown.getKeyFrame(timer);
    }

    public void render(SpriteBatch batch, float x, float y, float width, float height) {
        Tile tile = getTile();

        if (tile == null) {
            batch.draw(tex, x, y, width, height);
        } else {
            batch.draw(tex, x, y + (tile.height * 2) + (tileHeight * .25f), width, height);
        }
    }
}
