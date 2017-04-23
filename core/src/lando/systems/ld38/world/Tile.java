package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */

public class Tile extends GameObject {
    enum Type {Clay, Dirt, Grass, Sand, Snow, Stone, Ocean}
    public Type type;
    TextureRegion top_tex;
    TextureRegion bottom_tex;

    public Tile(World world, int col, int row, float height) {
        super(world, col, row, height);
        type = Type.Ocean;
    }

    public void setType(Type type){
        this.type = type;
        switch (type){
            case Clay:
                top_tex = Assets.clay_hex;
                bottom_tex = Assets.clay_bottom;
                break;
            case Dirt:
                top_tex = Assets.dirt_hex;
                bottom_tex = Assets.dirt_bottom;
                break;
            case Grass:
                top_tex = Assets.grass_hex;
                bottom_tex = Assets.grass_bottom;
                break;
            case Sand:
                top_tex = Assets.sand_hex;
                bottom_tex = Assets.sand_bottom;
                break;
            case Snow:
                top_tex = Assets.snow_hex;
                bottom_tex = Assets.snow_bottom;
                break;
            case Stone:
                top_tex = Assets.stone_hex;
                bottom_tex = Assets.stone_bottom;
                break;
        }
    }

    public void render(SpriteBatch batch, float x, float y, float width, float height){
        if (type == Type.Ocean) return;
        float heightOffset = this.height * 2;
        float a = Math.max(this.height / World.WORLD_MAX_HEIGHT, 0);
        for (int yOffset = 0; yOffset < heightOffset; yOffset++)  {

            batch.draw(bottom_tex, x, y + yOffset, tileWidth, tileHeight);
        }

        batch.draw(top_tex, x, y + heightOffset, tileWidth, tileHeight);

        batch.setColor(Color.WHITE);
    }
}
