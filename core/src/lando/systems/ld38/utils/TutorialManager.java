package lando.systems.ld38.utils;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.screens.GameScreen;

/**
 * Created by Brian on 4/24/2017
 * (adapted from dsgraham's code in LudumDare34)
 */
public class TutorialManager {

    GameScreen gameScreen;
    Array<TutorialInfo> screens;
    MutableFloat sceneAlpha;
    boolean acceptInput;

    public TutorialManager(GameScreen gameScreen){
        acceptInput = true;
        sceneAlpha = new MutableFloat(1);
        screens = new Array<TutorialInfo>();

//        [#FFFF00xALPHAx] Gold[]
        Rectangle rect = new Rectangle();
        GameScreen g = gameScreen;
        TutorialInfo info;

        // Introduction -------------------------------------------------------
        screens.add(new TutorialInfo(
                        "Welcome to [#00FFFFxALPHAx] Higher Ground[]\n\n* Ludum Dare 38 jam entry * \n\nCreated by [#F16F75xALPHAx] Team LandoSystems[]"
                           + "\n\n[#FFFF00xALPHAx] Click[] to [#FFFF00xALPHAx] Continue[]"
                           + "\n[#FF0000xALPHAx] Escape[] to [#FF0000xALPHAx] Skip tutorial[]",
                        new Rectangle(0, 0, 0, 0)));

        screens.add(new TutorialInfo(
                        "[#0099FFxALPHAx] Ocean levels are rising![]"
                      + "\n\nGuide the natives to\n[#FF6500xALPHAx] higher ground[] within [#FF00FFxALPHAx] 70 turns[]",
                        new Rectangle(g.turnCounter.bounds.x - 2f, g.turnCounter.bounds.y - 2f,
                                      g.turnCounter.bounds.width + 4f, g.turnCounter.bounds.height + 4f)));

        screens.add(new TutorialInfo(
                "Mouse wheel: [#FFFF00xALPHAx] ZOOMS[]\n\nClick & Drag: [#00FFFFxALPHAx] PANS[]\n\nClick: [#FF00FFxALPHAx] SELECTS[]",
                new Rectangle(0, 0, 0,0)));

        // Resources ----------------------------------------------------------

        screens.add(new TutorialInfo(
                "[#FF6500xALPHAx] Resources[] are [#FF0000xALPHAx] critical[]"
                   + "\n\n[#FF6500xALPHAx] Harvest[] them as you move\nStay ahead of the [#0099FFxALPHAx] rising ocean[]",
                new Rectangle(g.resources.bounds.x - 2f, g.resources.bounds.y - 2f,
                              g.resources.bounds.width + 4f, g.resources.bounds.height + 4f)));


        // Natives ------------------------------------------------------------

        screens.add(new TutorialInfo(
                "These are your [#FF6500xALPHAx] natives[]"
                + "\n\n[#FF6500xALPHAx] Select them[] by [#FFFF00xALPHAx] clicking[] on this panel",
                new Rectangle(g.playerSelection.bounds.x - 2f, g.playerSelection.bounds.y - 2f,
                              g.playerSelection.bounds.width + 4f, g.playerSelection.bounds.height + 4f)));

        info = new TutorialInfo(
                "You can also [#FF6500xALPHAx] select them[]\nby [#FFFF00xALPHAx] clicking directly on them[]\nin the world",
                new Rectangle(75f, 175f, 500f, 100f));
        info.pos.y = 350f;
        screens.add(info);

        // Actions ------------------------------------------------------------

        info = new TutorialInfo(
                "[#FF6500xALPHAx] Selecting[] a native\nopens their [#FF6500xALPHAx] action menu[]",
                new Rectangle(75f, 175f, 500f, 100f));
        info.pos.y = 350f;
        screens.add(info);

        info = new TutorialInfo(
                "[#FFFF00xALPHAx] Click[] a button in the"
                + "\n[#FF6500xALPHAx] action menu[] to assign actions"
                + "\n\nPossible actions are:\n[#FF00FFxALPHAx] Harvest[] / [#FF00FFxALPHAx] Forage[], [#00FF00xALPHAx] Move[] and [#FF6500xALPHAx] Build[]",
                new Rectangle(75f, 175f, 500f, 100f));
        info.pos.y = 300f;
        screens.add(info);

        screens.add(new TutorialInfo(
                "[#FF6500xALPHAx] Assigning[] the [#FF00FFxALPHAx] Harvest [] / [#FF00FFxALPHAx] Forage[] action\nwill [#FF00FFxALPHAx] harvest resources[]\nfrom the [#FFFF00xALPHAx] current tile[]",
                new Rectangle(0, 0, 0,0)));

        screens.add(new TutorialInfo(
                "[#FF6500xALPHAx] Assigning[] the [#00FF00xALPHAx] Move[] action\n[#00FF00xALPHAx] moves[] the player to another tile\n[#00FEDAxALPHAx] (if possible)[]",
                new Rectangle(0, 0, 0,0)));

        screens.add(new TutorialInfo(
                "[#FF6500xALPHAx] Assigning[] the [#FF6500xALPHAx] Build[] action\n[#FF6500xALPHAx] builds[] the selected object\n[#00FEDAxALPHAx] (if required resources are available)[]",
                new Rectangle(0, 0, 0,0)));

        screens.add(new TutorialInfo(
                "[#FF6500xALPHAx] Assigned actions[] are shown\nin the [#00FF00xALPHAx] top-left panel[]"
                + "\n\n[#FF6500xALPHAx] Assigned actions[] are completed\nat the [#FF0000xALPHAx] end of a turn[]"
                + "\n\n** [#FF6500xALPHAx] Actions[] can be [#FFFF00xALPHAx] reassigned[] **"
                + "\n** until the current turn is [#FF0000xALPHAx] ended[] **",
                new Rectangle(g.playerSelection.bounds.x - 2f, g.playerSelection.bounds.y - 2f,
                        g.playerSelection.bounds.width + 4f, g.playerSelection.bounds.height + 4f)));

        screens.add(new TutorialInfo(
                "[#FFFF00xALPHAx] Click[] the [#FF0000xALPHAx] End Turn button[]\n to end the current turn\nand execute [#FF6500xALPHAx] assigned actions[]",
                new Rectangle(g.endTurnButton.bounds.x - 2f, g.endTurnButton.bounds.y - 2f,
                              g.endTurnButton.bounds.width + 4f, g.endTurnButton.bounds.height + 4f)));

        // Buildings-----------------------------------------------------------


        screens.add(new TutorialInfo(
                "[#00FEDAxALPHAx] Buildings:[]"
                + "\n\n[#00FEDAxALPHAx] Ladders[] are [#D82039xALPHAx] extremely important[]"
                + "\nThey are [#FF0000xALPHAx] required[] for moving to [#FFFF00xALPHAx] higher ground[]!"
                + "\n\n[#F7D900xALPHAx] Rafts[] allow natives to [#F7D900xALPHAx] float[] on submerged tiles that would otherwise be deadly"
                + "\n\n[#ABD0BCxALPHAx] Tools[] improve [#ABD0BCxALPHAx] harvesting efficiency[], resulting in more resources per [#FF00FFxALPHAx] harvest[] action"
                + "\n\n[#53F54AxALPHAx] Sandbags[] act as [#53F54AxALPHAx] flood prevention[], keeping tiles above water for extra turns"
                + "\n\n[#F54A53xALPHAx] Huts[] produce [#F54A53xALPHAx] new natives[], build them, then [#FF00FFxALPHAx] 'harvest'[] them to add to the tribe",
                new Rectangle(0, 0, 0,0)));

        // --------------------------------------------------------------------

        screens.add(new TutorialInfo(
                "Get your natives to the\n[#0099FFxALPHAx] snowy mountain-tops[] to save them!\n\n[#FF6500xALPHAx] Good Luck![]",
                new Rectangle(0, 0, 0,0)));


        // --------------------------------------------------------------------
    }

    public boolean isDisplayed(){
        return screens.size > 0;
    }

    public void update(float dt){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            screens.clear();
            return;
        }

        if (acceptInput && Gdx.input.justTouched()) {
            acceptInput = false;
            if (screens.size > 1) {
                screens.removeIndex(0);
                acceptInput = true;
            } else {
                screens.removeIndex(0);
            }
        }
    }

    public Rectangle expandRectangle(Rectangle rect){
        return new Rectangle(rect.x - 10, rect.y - 10, rect.width +20, rect.height + 20);
    }

    public void render(SpriteBatch batch){
        if (screens.size <= 0) return;
        TutorialInfo info = screens.get(0);
        drawHighlight(batch, info);

        String coloredReplace = info.text;
        Color color = new Color(1,1,1,sceneAlpha.floatValue());
        int intAlpha = (int)(sceneAlpha.floatValue() * 255);
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(intAlpha));
        if (sb.length() < 2) sb.insert(0, '0'); // pad with leading zero if needed
        String hex = sb.toString();

        Assets.layout.setText(Assets.tutorialFont, coloredReplace.replace("xALPHAx", hex), color, info.wrapWidth, Align.center, true);
        float txtH = Assets.layout.height;
        float boxWidth = (info.wrapWidth + 20);
        Rectangle bounds = new Rectangle(info.pos.x - boxWidth /2 - 10, info.pos.y - txtH /2 - 10, boxWidth, txtH + 20);

        batch.setColor(62f / 255, 42f / 255, 0, sceneAlpha.floatValue());
        batch.draw(Assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);

        batch.setColor(new Color(1, 1, 1, sceneAlpha.floatValue()));
        Assets.ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        Assets.tutorialFont.draw(batch, Assets.layout, bounds.x + 10, bounds.y + bounds.height - 10);
    }

    private void drawHighlight(SpriteBatch batch, TutorialInfo info){
        Rectangle light = info.highlightBounds;
        batch.setColor(0,0,0,.75f * sceneAlpha.floatValue());
        batch.draw(Assets.whitePixel, 0, 0, light.x, light.y);
        batch.draw(Assets.whitePixel, light.x, 0, light.width, light.y);
        batch.draw(Assets.whitePixel, light.x + light.width, 0, Config.gameWidth, light.y);

        batch.draw(Assets.whitePixel, 0, light.y, light.x, light.height);
        batch.draw(Assets.whitePixel, light.x + light.width, light.y, Config.gameWidth, light.height);

        batch.draw(Assets.whitePixel, 0, light.y + light.height, light.x, Config.gameHeight);
        batch.draw(Assets.whitePixel, light.x, light.y + light.height,light.width, Config.gameHeight);
        batch.draw(Assets.whitePixel, light.x + light.width, light.y + light.height, Config.gameWidth, Config.gameHeight);
        batch.setColor(Color.WHITE);
    }

}
