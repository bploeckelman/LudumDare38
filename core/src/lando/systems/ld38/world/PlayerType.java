package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld38.utils.Assets;

public enum PlayerType {
    BF(Assets.bfWalkDown, Assets.bfWalkUp, Assets.bfWalkSide, Assets.head_female_dark),
    WF(Assets.wfWalkDown, Assets.wfWalkUp, Assets.wfWalkSide, Assets.head_female_white),
    BM(Assets.bmWalkDown, Assets.bmWalkUp, Assets.bmWalkSide, Assets.head_male_dark),
    WM(Assets.wmWalkDown, Assets.wmWalkUp, Assets.wmWalkSide, Assets.head_male_white);

    public Animation<TextureRegion> down;
    public Animation<TextureRegion> up;
    public Animation<TextureRegion> side;
    public TextureRegion head;

    PlayerType(Animation<TextureRegion> down, Animation<TextureRegion> up, Animation<TextureRegion> side, TextureRegion head) {
        this.down = down;
        this.up = up;
        this.side = side;
        this.head = head;
    }
}
