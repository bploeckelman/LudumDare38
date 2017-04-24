package lando.systems.ld38.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Created by Brian on 4/23/2017
 */
public class SoundManager{

    private static final float MUSIC_VOLUME = 1.0f;

    public enum SoundOptions {
        button_select, resource_collected, seagull, player_move, ocean_waves, ladder, water_rise, end_turn
    }

    private static HashMap<SoundOptions, Sound> soundMap = new HashMap<SoundOptions, Sound>();

    public static Music gameMusic;
    public static MutableFloat musicVolume;

    public static void load(boolean playMusic) {
        soundMap.put(SoundOptions.button_select, Gdx.audio.newSound(Gdx.files.internal("sounds/button_select.mp3")));
        soundMap.put(SoundOptions.resource_collected, Gdx.audio.newSound(Gdx.files.internal("sounds/resource_collected.mp3")));
        soundMap.put(SoundOptions.seagull, Gdx.audio.newSound(Gdx.files.internal("sounds/seagull.mp3")));
        soundMap.put(SoundOptions.player_move, Gdx.audio.newSound(Gdx.files.internal("sounds/player_move.mp3")));
        soundMap.put(SoundOptions.ocean_waves, Gdx.audio.newSound(Gdx.files.internal("sounds/ocean_waves.mp3")));
        soundMap.put(SoundOptions.ladder, Gdx.audio.newSound(Gdx.files.internal("sounds/ladder.mp3")));
        soundMap.put(SoundOptions.water_rise, Gdx.audio.newSound(Gdx.files.internal("sounds/water_rise.mp3")));
//        soundMap.put(SoundOptions.end_turn, Gdx.audio.newSound(Gdx.files.internal("sounds/end_turn.mp3")));
//        soundMap.put(SoundOptions.foo, Gdx.audio.newSound(Gdx.files.internal("sounds/foo.mp3")));
//        soundMap.put(SoundOptions.foo, Gdx.audio.newSound(Gdx.files.internal("sounds/foo.mp3")));
//        soundMap.put(SoundOptions.foo, Gdx.audio.newSound(Gdx.files.internal("sounds/foo.mp3")));

        if (gameMusic == null) {
            musicVolume = new MutableFloat(MUSIC_VOLUME);
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
            gameMusic.setLooping(true);
            if (playMusic) {
                gameMusic.play();
            }
            setMusicVolume(MUSIC_VOLUME);
        }
    }

    public static void update(float dt){
        gameMusic.setVolume(musicVolume.floatValue());
    }

    public static void dispose() {
        SoundOptions[] allSounds = SoundOptions.values();
        for (SoundOptions allSound : allSounds) {
            soundMap.get(allSound).dispose();
        }
        gameMusic.dispose();
    }

    public static long playSound(SoundOptions soundOption) {
        return soundMap.get(soundOption).play();
    }

    public static void stopSound(SoundOptions soundOption) {
        Sound sound = soundMap.get(soundOption);
        if (sound != null) {
            sound.stop();
        }
    }

    private static long currentLoopID;
    private static Sound currentLoopSound;

    public static void setMusicVolume(float level){
        Assets.tween.killTarget(musicVolume);
        Tween.to(musicVolume, 1, 2f)
                .target(level)
                .ease(Sine.IN)
                .start(Assets.tween);
    }

}
