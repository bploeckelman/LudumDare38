package lando.systems.ld38.world;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Quint;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.ui.Button;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.utils.accessors.Vector2Accessor;

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

    private class AddRemoveIndicator {
        public String text;
        public TextureRegion icon;
        public Vector2 position;
        public int count;
        public boolean remove;
        public boolean isAdd;
        public MutableFloat alpha;
        public Color textColor;
        public AddRemoveIndicator() {
            text = "";
            icon = null;
            position = new Vector2();
            remove = false;
            isAdd = true;
            count = 0;
            alpha = new MutableFloat(1f);
            textColor = new Color(1f, 1f, 1f, alpha.floatValue());
        }
        public void render(SpriteBatch batch) {
            batch.setColor(0.4f, 0.4f, 0.4f, MathUtils.clamp(alpha.floatValue() * 0.5f, 0f, 1f));
            batch.draw(Assets.whitePixel, position.x - 4f, position.y - 4f, 2f * TILE_SIZE + 10f, TILE_SIZE + 4f);

            batch.setColor(1f, 1f, 1f, alpha.floatValue());
            batch.draw(icon, position.x + 2f, position.y, TILE_SIZE, TILE_SIZE);

            if (isAdd) textColor.set(0f, 1f, 0f, alpha.floatValue());
            else       textColor.set(1f, 0f, 0f, alpha.floatValue());
            Assets.drawString(batch, text,
                    position.x + TILE_SIZE + 4f,
                    position.y + TILE_SIZE + 2f,
                    textColor, .35f, Assets.fancyFont);
        }
    }

    private Array<Button> buttons = new Array<Button>(5);
    private Array<AddRemoveIndicator> indicators;
    public Rectangle bounds;

    public UserResources(OrthographicCamera camera) {
        super(10, 0, 0, 0, 10);
        this.indicators = new Array<AddRemoveIndicator>();
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

        bounds = new Rectangle(X, Y, WIDTH, HEIGHT);
    }

    public void update(float dt) {
        for (Button button : buttons) {
            button.update(dt);
        }
        for (int i = indicators.size - 1; i >= 0; --i) {
            if (indicators.get(i).remove) {
                indicators.removeIndex(i);
            }
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
        for (AddRemoveIndicator indicator : indicators) {
            indicator.render(batch);
        }
        batch.setColor(Color.WHITE);
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
        resourceBonus++;
    }

    @Override
    public void add(ResourceCount resources) {
//        super.add(resources);
        spawnIndicators(resources, true);
    }

    @Override
    public void remove(ResourceCount resources) {
//        super.remove(resources);
        spawnIndicators(resources, false);
    }

    private void spawnIndicators(ResourceCount resources, boolean isAdd) {
        if (resources.food > 0) spawnIndicator(resources.food, Assets.food, FOOD_OFFSET_X, isAdd);
        if (resources.wood > 0) spawnIndicator(resources.wood, Assets.wood, WOOD_OFFSET_X, isAdd);
        if (resources.sand > 0) spawnIndicator(resources.sand, Assets.sand, SAND_OFFSET_X, isAdd);
        if (resources.iron > 0) spawnIndicator(resources.iron, Assets.iron, IRON_OFFSET_X, isAdd);
        if (resources.gold > 0) spawnIndicator(resources.gold, Assets.gold, GOLD_OFFSET_X, isAdd);
    }

    private void spawnIndicator(final int itemCount, final TextureRegion itemIcon, float xOffset, final boolean isAdd) {
        final float LOW_HEIGHT_OFFSET = -25f;
        final float HIGH_HEIGHT_OFFSET = 10f;
        final float TWEEN_DURATION = 0.85f;

        for (AddRemoveIndicator indicator : indicators) {
            if (indicator.icon == itemIcon && indicator.isAdd == isAdd) {
                indicator.count += itemCount;
                indicator.text = ((isAdd) ? "+" : "-") + String.valueOf(indicator.count);
                return;
            }
        }

        final AddRemoveIndicator indicator = new AddRemoveIndicator();
        indicator.count = itemCount;
        indicator.text = ((isAdd) ? "+" : "-") + String.valueOf(indicator.count);
        indicator.icon = itemIcon;
        indicator.isAdd = isAdd;
        indicator.position.set(X + xOffset, Y + (isAdd ? LOW_HEIGHT_OFFSET : HIGH_HEIGHT_OFFSET));
//        indicator.position.set(X + xOffset, Y + LOW_HEIGHT_OFFSET);

        Timeline.createParallel()
                .push(
                        Tween.to(indicator.position, Vector2Accessor.Y, TWEEN_DURATION)
                                .target((isAdd ? (Y + HIGH_HEIGHT_OFFSET) : (Y + LOW_HEIGHT_OFFSET)))
//                                .target(Y + HIGH_HEIGHT_OFFSET)
                                .ease(Bounce.OUT)
                )
                .push(
                        Tween.to(indicator.alpha, -1, TWEEN_DURATION)
                                .target(0f)
                                .ease(Expo.IN)
//                        .delay(TWEEN_DURATION / 2f)
                )
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        indicator.remove = true;
                        if (itemIcon == Assets.food) food += (isAdd ? indicator.count : -indicator.count);
                        if (itemIcon == Assets.wood) wood += (isAdd ? indicator.count : -indicator.count);
                        if (itemIcon == Assets.sand) sand += (isAdd ? indicator.count : -indicator.count);
                        if (itemIcon == Assets.iron) iron += (isAdd ? indicator.count : -indicator.count);
                        if (itemIcon == Assets.gold) gold += (isAdd ? indicator.count : -indicator.count);
                    }
                })
                .start(Assets.tween);

        indicators.add(indicator);
    }

}
