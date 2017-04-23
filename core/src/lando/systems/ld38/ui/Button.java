package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Brian Ploeckelman created on 4/16/2016.
 */
public class Button {

//    private static final float offset = 4f;

    public final TextureRegion region;
    public final Rectangle bounds;
    public String text = null;
    public String tooltip = null;
    private OrthographicCamera camera;
    private Vector2 touchPosScreen = new Vector2();

//    public boolean active;
//    public boolean drawNinePatch;

    public Button(TextureRegion region, Rectangle bounds, OrthographicCamera camera, String text, String tooltip) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = region;
        this.text = text;
        this.tooltip = tooltip;
//        this.active = false;
//        this.drawNinePatch = true;
    }

    public Button(TextureRegion region, Rectangle bounds, OrthographicCamera camera) {
        this.bounds = new Rectangle(bounds);
        this.camera = camera;
        this.region = region;
//        this.active = false;
//        this.drawNinePatch = true;
    }

//    public Button(TextureRegion region, Rectangle bounds) { // , boolean drawNinePatch) {
//        this(region, bounds);
//        this.drawNinePatch = drawNinePatch;
//    }

//    public boolean checkForTouch(float screenX, float screenY) {
//        return bounds.contains(screenX,screenY);
//    }



    public boolean checkForTouch(int screenX, int screenY) {
        Vector3 touchPosUnproject = camera.unproject(new Vector3(screenX, screenY, 0));
        touchPosScreen.set(touchPosUnproject.x, touchPosUnproject.y);
        return bounds.contains(touchPosScreen.x, touchPosScreen.y);
    }



    public void render(SpriteBatch batch) {
//        if (drawNinePatch) {
//            if (active) Assets.selectedNinepatch.draw(batch,
//                                                      bounds.x - offset,
//                                                      bounds.y - offset,
//                                                      bounds.width + 2f * offset,
//                                                      bounds.height + 2f * offset);
//            else        Assets.transparentNinepatch.draw(batch,
//                                                      bounds.x - offset,
//                                                      bounds.y - offset,
//                                                      bounds.width + 2f * offset,
//                                                      bounds.height + 2f * offset);
//        }
        batch.draw(region, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
