package lando.systems.ld38.world;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */

public class Tile extends GameObject {

    public static float heightScale = 8;
    public static MutableFloat renderShift = new MutableFloat(-120);

    public TileType type;
    public TextureRegion top_tex;
    public TextureRegion bottom_tex;
    public TextureRegion decoration_tex;
    public Decoration decoration;
    public TextureRegion shadow_tex;
    public Color pickColor;
    public TextureRegion overlayObjectTex;
    public TextureRegion item;
    public boolean isHighlighted;
    public boolean isInaccessible;
    public boolean isMoveTarget;
    public boolean isBuildTarget;
    public float heightOffset;


    public Tile(World world, int col, int row, float height) {
        super(world, col, row, height);
        type = TileType.Ocean;
        pickColor = Tile.getColorFromPosition(row, col);
        decoration = Decoration.None;
        decoration_tex = null;
        isHighlighted = false;
        isInaccessible = false;
        isMoveTarget = false;
        isBuildTarget = false;
        overlayObjectTex = null;
    }

    public void setType(TileType type){
        this.type = type;
        this.top_tex = type.top_tex;
        this.bottom_tex = type.bottom_tex;
        this.decoration = type.availableDecorations.random();
        this.decoration_tex = this.decoration.tex;
        if (this.decoration == Decoration.Tree) {
            int treeType = MathUtils.random(2);
            switch (treeType) {
                case 0: this.decoration_tex = Assets.palmtree1; break;
                case 1: this.decoration_tex = Assets.palmtree2; break;
                case 2: this.decoration_tex = Assets.palmtree3; break;
            }
        }
    }

    public void addShadow(int type){
        shadow_tex = null;
        switch (type){
            case 1:
                shadow_tex = Assets.shadowUL;
                break;
            case 2:
                shadow_tex = Assets.shadowUR;
                break;
            case 3:
                shadow_tex = Assets.shadowU;
                break;
        }
    }

    public void render(SpriteBatch batch, float x, float y, float waterHeight, boolean aboveWater) {
        render(batch, x, y, waterHeight, aboveWater, false);
    }

    public void render(SpriteBatch batch, float x, float y, float waterHeight, boolean aboveWater, boolean asPickBuffer){
        if (type == TileType.Ocean) return;
        heightOffset = this.height * heightScale + renderShift.floatValue();

        TextureRegion bottomTex = bottom_tex;
        TextureRegion topTex = top_tex;
        Color texColor = Color.WHITE;
        if (asPickBuffer) {
            texColor = pickColor;
            bottomTex = Assets.white_hex;
            topTex = Assets.white_hex;
        }

        batch.setColor(texColor);

        if (!aboveWater || asPickBuffer){
            float maxHeight = Math.min(waterHeight, heightOffset);
            for (int yOffset = -20; yOffset < maxHeight; yOffset += 10) {
                    batch.draw(bottomTex, x, y + yOffset, tileWidth, tileHeight);
            }
        }
        if (aboveWater || asPickBuffer){
            for (int yOffset = (int)waterHeight; yOffset < heightOffset; yOffset += 10) {
                batch.draw(bottomTex, x, y + yOffset, tileWidth, tileHeight);
            }
        }


        if (asPickBuffer || (aboveWater && heightOffset > waterHeight) || (!aboveWater && heightOffset <= waterHeight)) {
            batch.draw(topTex, x, y + heightOffset, tileWidth, tileHeight);

            if (!asPickBuffer && shadow_tex != null){
                batch.setColor(0,0,0,.7f);
                batch.draw(shadow_tex, x, y +heightOffset, tileWidth, tileHeight);
                batch.setColor(Color.WHITE);
            }
        }
        batch.setColor(Color.WHITE);

        if (asPickBuffer) return;

        if (isHighlighted) {
            batch.setColor((isInaccessible) ? Color.RED : Color.CYAN);
            batch.draw(Assets.select_hex, x, y + heightOffset, tileWidth, tileHeight);
            batch.setColor(Color.WHITE);
        }
        if (isMoveTarget) {
            batch.setColor(Color.GREEN);
            batch.draw(Assets.select_hex, x, y + heightOffset, tileWidth, tileHeight);
            batch.setColor(Color.WHITE);
        }
        if (isBuildTarget) {
            batch.setColor(Color.ORANGE);
            batch.draw(Assets.select_hex, x, y + heightOffset, tileWidth, tileHeight);
            batch.setColor(Color.WHITE);
        }
        if (!decoration.equals(Decoration.None) && aboveWater && heightOffset > waterHeight) {
            if (decoration_tex != null) {
                batch.draw(decoration_tex, x, y + heightOffset + (tileHeight * .35f), tileWidth, tileHeight);
            }
        }
        if (isInaccessible && overlayObjectTex != null) {
            batch.draw(overlayObjectTex, x, y + heightOffset, tileWidth, tileHeight);
        } else if (item != null) {
            batch.draw(item, x, y + heightOffset, tileWidth, tileHeight);
        }

        for (Player p : world.players){
            if (p.row == row && p.col == col && !p.moving) {
                p.render(batch, waterHeight, aboveWater);
            }
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
