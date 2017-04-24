package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld38.utils.Assets;

/**
 * Created by Brian on 4/23/2017
 */
public enum Decoration {
    None(),
    Tree(Assets.palmtree),
    Cow(Assets.cow),
    IronMine(Assets.iron_mine),
    GoldMine(Assets.gold_mine),
    Sand();

    public TextureRegion tex;

    Decoration() {}
    Decoration(TextureRegion tex) {
        this.tex = tex;
    }
}
