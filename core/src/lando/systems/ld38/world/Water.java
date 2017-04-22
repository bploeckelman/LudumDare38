package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld38.utils.Assets;

/**
 * Created by dsgraham on 4/22/17.
 */
public class Water {
    public float waterHeight;
    public float time;
    public World world;
    float waterBound = 400;

    public Water(World world){
        time = 0;
        this.world = world;
    }

    public void update(float dt){
        time += dt;
//        waterHeight += dt;
    }

    public void render(SpriteBatch batch){
        batch.end();
        batch.setShader(Assets.waterShader);
        batch.begin();
        Assets.waterShader.setUniformf("u_time", time);
        Assets.waterShader.setUniformf("u_light" , 0, .5f, 1);
        batch.draw(Assets.water_bumpmap, -waterBound, -waterBound, world.bounds.width + waterBound*2, world.bounds.height + waterBound*2);
        batch.end();
        batch.setShader(null);
        batch.begin();
    }
}
