package lando.systems.ld38.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.ui.Button;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;

/**
 * Created by aeharding on 4/22/17.
 */
public class UserResources extends ResourceCount {

    private static final float MARGIN_TOP = 10f;

    public int resourceBonus;

    private final int WIDTH = 450;
    private final int HEIGHT = 35;
    private final int MARGIN = 8;
    private final float TILE_SIZE;
    private final float X;
    private final int Y;

    private final float FOOD_OFFSET_X;
    private final float WOOD_OFFSET_X;
    private final float SAND_OFFSET_X;
    private final float IRON_OFFSET_X;
    private final float GOLD_OFFSET_X;
    private final float EFF_OFFSET_X;

    private Array<Button> buttons = new Array<Button>(5);

    public UserResources(OrthographicCamera camera) {
        super(5, 10, 10, 10, 3);
        resourceBonus = 0;
        TILE_SIZE = HEIGHT - (MARGIN * 2);
        X = (Config.gameWidth - WIDTH) / 2f;
        Y = Config.gameHeight - HEIGHT - MARGIN;
        float spacing = (WIDTH - 40) / 6f;
        float offset = 20;
        FOOD_OFFSET_X = offset + (spacing * 0);
        WOOD_OFFSET_X = offset + (spacing * 1);
        SAND_OFFSET_X = offset + (spacing * 2);
        IRON_OFFSET_X = offset + (spacing * 3);
        GOLD_OFFSET_X = offset + (spacing * 4);
        EFF_OFFSET_X = offset + (spacing * 5);

        float bw = spacing - offset;
        Button foodTooltip = new Button(Assets.transparentPixel, new Rectangle(X + FOOD_OFFSET_X, Y, bw, HEIGHT), camera);
        Button goldTooltip = new Button(Assets.transparentPixel, new Rectangle(X + GOLD_OFFSET_X, Y, bw, HEIGHT), camera);
        Button ironTooltip = new Button(Assets.transparentPixel, new Rectangle(X + IRON_OFFSET_X, Y, bw, HEIGHT), camera);
        Button sandTooltip = new Button(Assets.transparentPixel, new Rectangle(X + SAND_OFFSET_X, Y, bw, HEIGHT), camera);
        Button woodTooltip = new Button(Assets.transparentPixel, new Rectangle(X + WOOD_OFFSET_X, Y, bw, HEIGHT), camera);
        Button effTooltip = new Button(Assets.transparentPixel, new Rectangle(X + EFF_OFFSET_X, Y, bw, HEIGHT), camera);

        foodTooltip.setTooltip("food");
        goldTooltip.setTooltip("gold");
        ironTooltip.setTooltip("iron");
        sandTooltip.setTooltip("sand");
        woodTooltip.setTooltip("wood");
        effTooltip.setTooltip("Tool Efficiency");
        buttons.add(foodTooltip);
        buttons.add(goldTooltip);
        buttons.add(ironTooltip);
        buttons.add(sandTooltip);
        buttons.add(woodTooltip);
        buttons.add(effTooltip);

    }

    public void update(float dt) {
        for (Button button : buttons) {
            button.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        Assets.woodPanel.draw(batch, X, Y, WIDTH, HEIGHT);
        drawResource(batch, FOOD_OFFSET_X, Assets.food, food);
        drawResource(batch, WOOD_OFFSET_X, Assets.wood, wood);
        drawResource(batch, SAND_OFFSET_X, Assets.sand, sand);
        drawResource(batch, IRON_OFFSET_X, Assets.iron, iron);
        drawResource(batch, GOLD_OFFSET_X, Assets.gold, gold);
        drawResource(batch, EFF_OFFSET_X, Assets.hammer_upgrade, resourceBonus);
        batch.setColor(Color.WHITE);
        for (Button button : buttons) {
            button.render(batch);
        }
    }

    public void renderToolTips(SpriteBatch batch, OrthographicCamera hudCamera){
        for (Button button : buttons) {
            button.renderTooltip(batch, hudCamera);
        }
    }

    
    private void drawResource(SpriteBatch batch, float offset, TextureRegion region, int amount) {
        batch.draw(region, X + offset, Y + MARGIN, TILE_SIZE, TILE_SIZE);
        Assets.drawString(batch, "" + amount, X + offset + TILE_SIZE + 5, Y + HEIGHT - 6, Color.WHITE, .3f, Assets.fancyFont);
    }

    public void upgradeResourceBonus(){
        resourceBonus ++;
    }
}
