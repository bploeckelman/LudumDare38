package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.utils.Assets;

/**
 * Created by Brian on 4/23/2017
 */
public enum TileType {
    Clay(Assets.clay_hex, Assets.clay_bottom, new Array<Decoration>(new Decoration[]{
            Decoration.Cow,
            Decoration.None
    })),
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

    TileType() {
        availableDecorations = new Array<Decoration>(new Decoration[]{
                Decoration.None
        });
    }

    TileType(TextureRegion top, TextureRegion bottom) {
        top_tex = top;
        bottom_tex = bottom;
        availableDecorations = new Array<Decoration>(new Decoration[]{
                Decoration.None
        });
    }

    TileType(TextureRegion top, TextureRegion bottom, Array<Decoration> availableDecorations) {
        top_tex = top;
        bottom_tex = bottom;
        this.availableDecorations = availableDecorations;
        for (int i = 0; i < 5; i++){
            this.availableDecorations.add(Decoration.None);
        }

    }
}
