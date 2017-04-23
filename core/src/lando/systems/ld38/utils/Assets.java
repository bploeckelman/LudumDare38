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
    public static BitmapFont fancyFont;
    public static ShaderProgram fontShader;
    public static ShaderProgram waterShader;

    public static TextureAtlas atlas;

    public static TextureRegion whitePixel;
    public static Texture blank_hex;
    public static TextureRegion clay_hex;
    public static TextureRegion clay_bottom;
    public static TextureRegion dirt_hex;
    public static TextureRegion dirt_bottom;
    public static TextureRegion grass_hex;
    public static TextureRegion grass_bottom;
    public static TextureRegion sand_hex;
    public static TextureRegion sand_bottom;
    public static TextureRegion snow_hex;
    public static TextureRegion snow_bottom;
    public static TextureRegion stone_hex;
    public static TextureRegion stone_bottom;
    public static Texture water_hex;
    public static TextureRegion select_hex;
    public static TextureRegion white_hex;
    public static Texture water_bumpmap;
    public static Texture turn_counter_background;

    public static Animation<TextureRegion> womanWalkUp;
    public static Animation<TextureRegion> womanWalkDown;
    public static Animation<TextureRegion> womanWalkSide;

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
        mgr.load("images/blank-hex.png", Texture.class, nearestParams);
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

        whitePixel = atlas.findRegion("white");
        blank_hex = mgr.get("images/blank-hex.png", Texture.class);

        white_hex = atlas.findRegion("white-hex");
        select_hex = atlas.findRegion("select-hex");
        clay_hex = atlas.findRegion("clay_top");
        dirt_hex = atlas.findRegion("dirt_top");
        grass_hex = atlas.findRegion("grass_top");
        sand_hex = atlas.findRegion("sand_top");
        snow_hex = atlas.findRegion("snow_top");
        stone_hex = atlas.findRegion("stone_top");

        clay_bottom = atlas.findRegion("clay_bottom");
        dirt_bottom = atlas.findRegion("dirt_bottom");
        grass_bottom = atlas.findRegion("grass_bottom");
        sand_bottom = atlas.findRegion("sand_bottom");
        snow_bottom = atlas.findRegion("snow_bottom");
        stone_bottom = atlas.findRegion("stone_bottom");

        water_bumpmap = mgr.get("images/water-bump.png", Texture.class);  // NEVER MOVE THIS INTO THE ATLAS!!!!!
        water_bumpmap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        water_bumpmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        turn_counter_background = mgr.get("images/turn_counter_background.png", Texture.class);

        womanWalkUp = new Animation<TextureRegion>(.15f, atlas.findRegions("walk_up"), Animation.PlayMode.LOOP);
        womanWalkDown = new Animation<TextureRegion>(.15f, atlas.findRegions("walk_down"), Animation.PlayMode.LOOP);
        womanWalkSide = new Animation<TextureRegion>(.15f, atlas.findRegions("walk_side"), Animation.PlayMode.LOOP);

        final Texture distText = new Texture(Gdx.files.internal("fonts/ubuntu.png"), true);
        distText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/ubuntu.fnt"), new TextureRegion(distText), false);

        final Texture fancyDistText = new Texture(Gdx.files.internal("fonts/vinque.png"), true);
        fancyDistText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        fancyFont = new BitmapFont(Gdx.files.internal("fonts/vinque.fnt"), new TextureRegion(fancyDistText), false);

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
        fancyFont.dispose();
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

    public static void drawString(SpriteBatch batch, String text, float x, float y, Color c, float scale, BitmapFont font){
        batch.setShader(fontShader);
        //fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
        //fontShader.setUniformf("u_scale", 1f);
        font.getData().setScale(scale);
        batch.setShader(null);
    }

}
