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
    public static BitmapFont tutorialFont;
    public static ShaderProgram fontShader;
    public static ShaderProgram waterShader;

    public static TextureAtlas atlas;

    public static Texture titleTexture;
    public static Texture blank_hex;
    public static Texture water_hex;
    public static Texture water_bumpmap;
    public static Texture turn_counter_background;
    public static TextureRegion transparentPixel;
    public static TextureRegion whitePixel;
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
    public static TextureRegion head_male_dark;
    public static TextureRegion head_male_white;
    public static TextureRegion head_female_dark;
    public static TextureRegion head_female_white;
    public static TextureRegion arrow;
    public static TextureRegion hammer;
    public static TextureRegion questionmark;
    public static TextureRegion wait;
    public static TextureRegion bubble;
    public static TextureRegion select_hex;
    public static TextureRegion white_hex;
    public static TextureRegion shadowUL;
    public static TextureRegion shadowUR;
    public static TextureRegion shadowU;
    public static Animation<TextureRegion> bfWalkUp;
    public static Animation<TextureRegion> bfWalkDown;
    public static Animation<TextureRegion> bfWalkSide;
    public static Animation<TextureRegion> wfWalkUp;
    public static Animation<TextureRegion> wfWalkDown;
    public static Animation<TextureRegion> wfWalkSide;
    public static Animation<TextureRegion> wmWalkUp;
    public static Animation<TextureRegion> wmWalkDown;
    public static Animation<TextureRegion> wmWalkSide;
    public static Animation<TextureRegion> bmWalkUp;
    public static Animation<TextureRegion> bmWalkDown;
    public static Animation<TextureRegion> bmWalkSide;
    public static Animation<TextureRegion> totemAnim;
    public static Animation<TextureRegion> birdAnimationGreen;
    public static Animation<TextureRegion> birdAnimationOrange;
    public static Animation<TextureRegion> birdAnimationGull;

    public static TextureRegion cow;
    public static TextureRegion gold_mine;
    public static TextureRegion hut;
    public static TextureRegion iron_mine;
    public static TextureRegion ladder;
    public static TextureRegion palmtree;
    public static TextureRegion palmtree1;
    public static TextureRegion palmtree2;
    public static TextureRegion palmtree3;
    public static TextureRegion raft;
    public static TextureRegion sandbags;

    public static TextureRegion axe;
    public static TextureRegion pickaxe;
    public static TextureRegion shotgun;
    public static TextureRegion shovel;
    public static TextureRegion heart;
    public static TextureRegion hammer_upgrade;

    public static TextureRegion food;
    public static TextureRegion iron;
    public static TextureRegion gold;
    public static TextureRegion wood;
    public static TextureRegion sand;

    public static NinePatch woodPanel;
    public static NinePatch ninePatch;

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
        mgr.load("images/title-credits-screen.png", Texture.class, nearestParams);
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
        transparentPixel = atlas.findRegion("transparent-pixel");
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

        food = atlas.findRegion("steak");
        iron = atlas.findRegion("iron");
        gold = atlas.findRegion("gold");
        wood = atlas.findRegion("wood");
        sand = atlas.findRegion("sand-pile");
        heart = atlas.findRegion("heart");

        axe = atlas.findRegion("axe");
        pickaxe = atlas.findRegion("pickaxe");
        shotgun = atlas.findRegion("shotgun");
        shovel = atlas.findRegion("shovel");
        hammer_upgrade = atlas.findRegion("hammer_upgrade");

        bubble = atlas.findRegion("bubble");

        head_male_dark = atlas.findRegion("head_male_dark");
        head_male_white = atlas.findRegion("head_male_white");
        head_female_dark = atlas.findRegion("head_female_dark");
        head_female_white = atlas.findRegion("head_female_white");

        // Actions
        arrow = atlas.findRegion("arrow");
        hammer = atlas.findRegion("hammer");
        questionmark = atlas.findRegion("questionmark");
        wait = atlas.findRegion("wait");

        woodPanel = new NinePatch(atlas.findRegion("wood_border"), 7,7,7,7);
        ninePatch = new NinePatch(atlas.findRegion("ninepatch"), 6,6,6,6);

        shadowU = atlas.findRegion("shadow_u");
        shadowUR = atlas.findRegion("shadow_ur");
        shadowUL = atlas.findRegion("shadow_ul");

        titleTexture = mgr.get("images/title-credits-screen.png", Texture.class);

        water_bumpmap = mgr.get("images/water-bump.png", Texture.class);  // NEVER MOVE THIS INTO THE ATLAS!!!!!
        water_bumpmap.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        water_bumpmap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        turn_counter_background = mgr.get("images/turn_counter_background.png", Texture.class);

        bfWalkUp = new Animation<TextureRegion>(.15f, atlas.findRegions("bf_walk_up"), Animation.PlayMode.LOOP);
        bfWalkDown = new Animation<TextureRegion>(.15f, atlas.findRegions("bf_walk_down"), Animation.PlayMode.LOOP);
        bfWalkSide = new Animation<TextureRegion>(.15f, atlas.findRegions("bf_walk_side"), Animation.PlayMode.LOOP);
        wfWalkUp = new Animation<TextureRegion>(.15f, atlas.findRegions("wf_walk_up"), Animation.PlayMode.LOOP);
        wfWalkDown = new Animation<TextureRegion>(.15f, atlas.findRegions("wf_walk_down"), Animation.PlayMode.LOOP);
        wfWalkSide = new Animation<TextureRegion>(.15f, atlas.findRegions("wf_walk_side"), Animation.PlayMode.LOOP);
        wmWalkUp = new Animation<TextureRegion>(.15f, atlas.findRegions("wm_walk_up"), Animation.PlayMode.LOOP);
        wmWalkDown = new Animation<TextureRegion>(.15f, atlas.findRegions("wm_walk_down"), Animation.PlayMode.LOOP);
        wmWalkSide = new Animation<TextureRegion>(.15f, atlas.findRegions("wm_walk_side"), Animation.PlayMode.LOOP);
        bmWalkUp = new Animation<TextureRegion>(.15f, atlas.findRegions("bm_walk_up"), Animation.PlayMode.LOOP);
        bmWalkDown = new Animation<TextureRegion>(.15f, atlas.findRegions("bm_walk_down"), Animation.PlayMode.LOOP);
        bmWalkSide = new Animation<TextureRegion>(.15f, atlas.findRegions("bm_walk_side"), Animation.PlayMode.LOOP);

        totemAnim = new Animation<TextureRegion>(0.15f, atlas.findRegions("totem"), Animation.PlayMode.NORMAL);

        birdAnimationGreen = new Animation<TextureRegion>(0.02f, atlas.findRegions("birdgreen"), Animation.PlayMode.LOOP);
        birdAnimationOrange = new Animation<TextureRegion>(0.02f, atlas.findRegions("birdorange"), Animation.PlayMode.LOOP);
        birdAnimationGull = new Animation<TextureRegion>(0.2f, atlas.findRegions("birdgull"), Animation.PlayMode.LOOP);

        cow = atlas.findRegion("cow");
        gold_mine = atlas.findRegion("gold_mine");
        hut = atlas.findRegion("hut");
        iron_mine = atlas.findRegion("iron_mine");
        ladder = atlas.findRegion("ladder-small");
        palmtree1 = atlas.findRegion("palmtree1");
        palmtree2 = atlas.findRegion("palmtree2");
        palmtree3 = atlas.findRegion("palmtree3");
        raft = atlas.findRegion("raft-small");
        sandbags = atlas.findRegion("sand_bag");
        palmtree = palmtree1;

        final Texture distText = new Texture(Gdx.files.internal("fonts/ubuntu.png"), true);
        distText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/ubuntu.fnt"), new TextureRegion(distText), false);
        font.getData().setScale(.3f);

        final Texture fancyDistText = new Texture(Gdx.files.internal("fonts/vinque.png"), true);
        fancyDistText.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        fancyFont = new BitmapFont(Gdx.files.internal("fonts/vinque.fnt"), new TextureRegion(fancyDistText), false);

        tutorialFont = new BitmapFont();
        tutorialFont.getData().markupEnabled = true;

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
        fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y);
        font.getData().setScale(1f);
        fontShader.setUniformf("u_scale", 1f);
        font.getData().setScale(scale);
        batch.setShader(null);
    }

    public static void drawString(SpriteBatch batch, String text, float x, float y, Color c, float scale, BitmapFont font, float targetWidth, int halign){
        batch.setShader(fontShader);
        fontShader.setUniformf("u_scale", scale);
        font.getData().setScale(scale);
        font.setColor(c);
        font.draw(batch, text, x, y, targetWidth, halign, true);
        font.getData().setScale(1f);
        fontShader.setUniformf("u_scale", 1f);
        font.getData().setScale(scale);
        batch.setShader(null);
    }

}
