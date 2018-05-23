package punchmania.punchmania;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int startSound;
    private static  int startSwoosh;

    public SoundPlayer(Context context)  {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        startSound = soundPool.load(context, R.raw.start,1);
    }

    public void playStartSound(){
        soundPool.play(startSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
