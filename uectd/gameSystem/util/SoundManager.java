package uectd.gameSystem.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uectd.gameSystem.FatalError;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

public class SoundManager {

    private static SoundManager instance;
    private Map<String, ArrayList<GameSound>> soundsMap;
    private int volume = 5;

    private SoundManager() {
        soundsMap = new HashMap<>();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void play(String filePath) {
        try {
            String fullPath = Paths.get(filePath).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            if (soundsMap.containsKey(fullPath)) {
                var sounds = soundsMap.get(Paths.get(filePath).toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
                for (var sound : sounds) {
                    if (!sound.isPlaying()) {
                        sound.start();
                        return;
                    }
                }
                sounds.add(new GameSound(fullPath));
                sounds.get(sounds.size() - 1).init();
                sounds.get(sounds.size() - 1).volume(volume);
                sounds.get(sounds.size() - 1).start();
            } else {
                var sounds = new ArrayList<GameSound>();
                var sound = new GameSound(fullPath);
                sounds.add(sound);
                soundsMap.put(fullPath, sounds);
                sound.init();
                sound.volume(volume);
                sound.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("音声ファイル入出力エラー");
        }
    }

    public void allStop() {
        for (var sounds : soundsMap.values()) {
            for (var sound : sounds) {
                if (sound.isPlaying()) {
                    sound.stop();
                }
            }
        }
    }

}
