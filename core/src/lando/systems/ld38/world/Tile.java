package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */

public class Tile extends GameObject {
    enum Decoration {
        None(),
        Tree(Assets.palmtree),
        Cow(Assets.cow),
        IronMine(Assets.iron_mine),
        GoldMine(Assets.gold_mine);

        public TextureRegion tex;

        Decoration() {}
        Decoration(TextureRegion tex) {
            this.tex = tex;
        }
    }

    enum Type {
        Clay(Assets.clay_hex, Assets.clay_bottom),
        Dirt(Assets.dirt_hex, Assets.dirt_bottom, new Array<Decoration>(new Decoration[]{
            Decoration.None,
            Decoration.Tree,
            Decoration.Cow,
            Decoration.IronMine,
            Decoration.GoldMine
        })),
        Grass(Assets.grass_hex, Assets.grass_bottom, new Array<Decoration>(new Decoration[]{
            Decoration.None,
            Decoration.Tree,
            Decoration.Cow
        })),
        Sand(Assets.sand_hex, Assets.sand_bottom, new Array<Decoration>(new Decoration[]{
            Decoration.None,
            Decoration.Tree
        })),
        Snow(Assets.snow_hex, Assets.snow_bottom, new Array<Decoration>(new Decoration[]{
            Decoration.None,
            Decoration.GoldMine
        })),
        Stone(Assets.stone_hex, Assets.stone_bottom, new Array<Decoration>(new Decoration[]{
            Decoration.None,
            Decoration.IronMine
        })),
        Ocean();

        public TextureRegion top_tex;
        public TextureRegion bottom_tex;
        public Array<Decoration> availableDecorations;

        Type() {
            availableDecorations = new Array<Decoration>(new Decoration[]{
                Decoration.None
            });
        }

        Type(TextureRegion top, TextureRegion bottom) {
            top_tex = top;
            bottom_tex = bottom;
            availableDecorations = new Array<Decoration>(new Decoration[]{
                Decoration.None
            });
        }

        Type(TextureRegion top, TextureRegion bottom, Array<Decoration> availableDecorations) {
            top_tex = top;
            bottom_tex = bottom;
            this.availableDecorations = availableDecorations;
        }
    }
    public static float heightScale = 4;

    public Type type;
    public TextureRegion top_tex;
    TextureRegion bottom_tex;
    Decoration decoration;
    Color pickColor;

    public float heightOffset;


    public Tile(World world, int col, int row, float height) {
        super(world, col, row, height);
        type = Type.Ocean;
        pickColor = Tile.getColorFromPosition(row, col);
        heightOffset = this.height * heightScale;
        decoration = Decoration.None;
    }

    public void setType(Type type){
        this.type = type;
        this.top_tex = type.top_tex;
        this.bottom_tex = type.bottom_tex;
        this.decoration = type.availableDecorations.random();
    }

    public void render(SpriteBatch batch, float x, float y, float waterHeight, boolean aboveWater) {
        render(batch, x, y, waterHeight, aboveWater, false);
    }

    public void render(SpriteBatch batch, float x, float y, float waterHeight, boolean aboveWater, boolean asPickBuffer){
        if (type == Type.Ocean) return;
        TextureRegion bottomTex = bottom_tex;
        TextureRegion topTex = top_tex;
        Color texColor = Color.WHITE;
        if (asPickBuffer) {
            texColor = pickColor;
            bottomTex = Assets.white_hex;
            topTex = Assets.white_hex;
        }

        batch.setColor(texColor);
        for (int yOffset = -10; yOffset < heightOffset; yOffset += 2) {
            if (asPickBuffer || (aboveWater && yOffset > waterHeight) || (!aboveWater && yOffset <= waterHeight)) {
                batch.draw(bottomTex, x, y + yOffset, tileWidth, tileHeight);
            }
        }
        if (asPickBuffer || (aboveWater && heightOffset > waterHeight) || (!aboveWater && heightOffset <= waterHeight)) {
            batch.draw(topTex, x, y + heightOffset, tileWidth, tileHeight);
        }
        if (!decoration.equals(Decoration.None) && !asPickBuffer && aboveWater && heightOffset > waterHeight) {
            batch.draw(decoration.tex, x, y + heightOffset, tileWidth, tileHeight);
        }

        batch.setColor(Color.WHITE);
    }

    public void renderPickBuffer(SpriteBatch batch) {
        float x = col * tileWidth;
        float y = row * tileHeight * .75f;
        if (row % 2 == 0) x += tileWidth / 2f;
        render(batch, x, y, 0, true, true);
    }

    public static Color getColorFromPosition(int row, int col) {
        return new Color(
                (col * 5f) / 255f,
                (row * 5f) / 255f,
                0f, 1f);
    }

    public static Tile parsePickColorForTileInWorld(Color pickColor, World world) {
        int col = (int) (pickColor.r * (255f / 5f));
        int row = (int) (pickColor.g * (255f / 5f));
        return world.getTile(row, col);
    }

}
