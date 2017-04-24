package lando.systems.ld38.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Brian on 4/24/2017
 */
public class TutorialInfo {

    public String text;
    public Vector2 pos;
    public Rectangle highlightBounds;
    public int wrapWidth;

    public TutorialInfo(String text, Rectangle bounds){
        this(text, new Vector2(Config.gameWidth / 2f, Config.gameHeight / 2f), 250, bounds);
    }

    public TutorialInfo(String text, Vector2 centerPos, int wrapWidth, Rectangle bounds) {
        this.text = text;// + "\n\nClick to Continue\nEscape to Cancel Tutorial";
        this.pos = centerPos;
        this.highlightBounds = bounds;
        this.wrapWidth = wrapWidth;
    }

}
