package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;

/**
 * Created by Brian on 4/24/2017
 */
public class Bird {

    public Vector2 position;
    public float direction;
    public float accumulator;
    public boolean alive;
    public Animation<TextureRegion> animation;
    public TextureRegion keyframe;
    public float speed;
    public float verticalDrift;


    public Bird(){
        accumulator = MathUtils.random(2f);
        int type = MathUtils.random(2);
        switch (type) {
            case 0: animation = Assets.birdAnimationGreen; break;
            case 1: animation = Assets.birdAnimationOrange; break;
            case 2: default: animation = Assets.birdAnimationGull; break;
        }

        if (MathUtils.randomBoolean()){
            direction = 1;
            position = new Vector2(-animation.getKeyFrame(accumulator).getRegionWidth(), MathUtils.random(Config.gameHeight));
        } else {
            direction = -1;
            position = new Vector2(Config.gameWidth + 50f, MathUtils.random(Config.gameHeight));
        }
        speed = MathUtils.random(40f, 70f);
        verticalDrift = MathUtils.random(15f, 30f);
        alive = true;
    }

    public void update(float dt) {
        accumulator += dt;
        position.x += speed  * dt * direction;

        keyframe = animation.getKeyFrame(accumulator);

        if (position.x <  -animation.getKeyFrame(accumulator).getRegionWidth()
         || position.x > Config.gameWidth + 50f + animation.getKeyFrame(accumulator).getRegionWidth()){
            alive = false;
        }
    }

    public void render(SpriteBatch batch){
        float yFloat = MathUtils.sin(accumulator * .5f) * verticalDrift;
        if (keyframe != null) {
            batch.draw(keyframe, position.x, position.y + yFloat, 16 * -direction, 16);
        }
    }

}
