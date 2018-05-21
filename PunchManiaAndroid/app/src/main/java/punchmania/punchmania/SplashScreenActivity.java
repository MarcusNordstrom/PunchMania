package punchmania.punchmania;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLAH_TIME_OUT = 4000;
    private SoundPlayer soundPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        soundPlayer = new SoundPlayer(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPlayer.playStartSound();
                Intent splashIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },SPLAH_TIME_OUT);
    }
}
