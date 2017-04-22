package lando.systems.ld38.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld38.utils.accessors.*;

/**
 * Created by Brian on 4/16/2017.
 */
public class Assets {

    public static AssetManager mgr;
    public static TweenManager tween;
    public static SpriteBatch batch;
    public static ShapeRenderer shapes;
    public static GlyphLayout layout;
    public static BitmapFont font;
    public static ShaderProgram fontShader;
    public static ShaderProgram waterShader;

    public static TextureAtlas atlas;

    public static Texture whitePixel;
    public static Texture blank_hex;
    public static Texture clay_hex;
    public static Texture forest_hex;
    public static Texture sand_hex;
    public static Texture stone_hex;
    public static Texture water_hex;
    public static Texture water_bumpmap;
    public static Texture turn_counter_background;

    public static boolean initialized;

    public static void load() {
        initialized = false;

        final TextureLoader.TextureParameter linearParams = new TextureLoader.TextureParameter();
        linearParams.minFilter = Texture.TextureFilter.Linear;
        linearParams.magFilter = Texture.TextureFilter.Linear;

        final TextureLoader.TextureParameter nearestParams = new TextureLoader.TextureParameter();
        nearestParams.minFilter = Texture.TextureFilter.Nearest;
        nearestParams.magFilter = Texture.TextureFilter.Nearest;

        mgr = new AssetManager();
        mgr.load("images/white-pixel.png", Texture.class, nearestParams);
        mgr.load("images/blank-hex.png", Texture.class, nearestParams);
        mgr.load("images/clay.png", Texture.class, nearestParams);
        mgr.load("images/sand.png", Texture.class, nearestParams);
        mgr.load("images/stone.png", Texture.class, nearestParams);
        mgr.load("images/tree.png", Texture.class, nearestParams);
        mgr.load("images/water.png", Texture.class, nearestParams);
        mgr.load("images/water-bump.png", Texture.class, nearestParams);
        mgr.load("images/turn_counter_background.png", Texture.class, nearestParams);

        atlas = new TextureAtlas(Gdx.files.internal("sprites.atlas"));

        if (tween == null) {
            tween = new TweenManager();
            Tween.setCombinedAttributesLimit(4);
            Tween.registerAccessor(Color.class, new ColorAccessor());
            Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
            Tween.registerAccessor(Vector2.class, new Vector2Accessor());
            Tween.registerAccessor(Vector3.class, new Vector3Accessor());
            Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
        }

        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        layout = new GlyphLayout();
    }

    public static float update() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1f;
        initialized = true;

        whitePixel = mgr.get("images/white-pixel.png", Texture.class);
        blank_hex = mgr.get("images/blank-hex.png", Texture.class);
        forest_hex = mgr.get("images/tree.png", Texture.class);
        clay_hex = mgr.get("images/clay.png", Texture.class);
        stone_hex = mgr.get("images/stone.png", Texture.class);
        sand_hex = mgr.get("images/sand.png", Texture.class);
        water_hex = mgr.get("images/water.png", Texture.class);
        water_bumpmap = mgr.get("images/water-bump.png", Texture.class);
        water_bumpmap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        turn_counter_background = mgr.get("images/turn_counter_background.png", Texture.class);


        final Texture distText = new Texture(Gdx.files.internal("fonts/ubuntu.png"), true);
        distText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/ubuntu.fnt"), new TextureRegion(distText), false);

        fontShader = new ShaderProgram(Gdx.files.internal("shaders/dist.vert"),
                Gdx.files.internal("shaders/dist.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }
        ShaderProgram.pedantic = false;
        waterShader = new ShaderProgram(Gdx.files.internal("shaders/dist.vert"),
                Gdx.files.internal("shaders/water.frag"));
        if (!waterShader.isCompiled()){
            Gdx.app.error("WaterShader", "compilation failed:\n" + waterShader.getLog());
        }

        return 1f;
    }

    public static void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
        mgr.clear();
    }

    private static ShaderProgram compileShaderProgram(FileHandle vertSource, FileHandle fragSource) {
        ShaderProgram.pedantic = false;
        final ShaderProgram shader = new ShaderProgram(vertSource, fragSource);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Failed to compile shader program:\n" + shader.getLog());
        }
        else if (shader.getLog().length() > 0) {
            Gdx.app.debug("SHADER", "ShaderProgram compilation log:\n" + shader.getLog());
        }
        return shader;
    }

    public static void drawString(SpriteBatch batch, String text, float x, float y, Color c, float scale){
        batch.setShader(fontShader);
        fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
        fontShader.setUniformf("u_scale", 1f);
        font.getData().setScale(scale);
        batch.setShader(null);
    }

}
