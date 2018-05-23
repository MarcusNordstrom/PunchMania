package punchmania.punchmania;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLAH_TIME_OUT = 4000;
    private SoundPlayer soundPlayer;
    private GifImageView gifImageView;
    private ProgressBar progressBar;
   private MediaPlayer mediaPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        soundPlayer = new SoundPlayer(this);


        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);

        try{
            InputStream inputStream = getAssets().open("splash_screen.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
            mediaPlayer1 = MediaPlayer.create(this, R.raw.swoosh);
            mediaPlayer1.start();
            mediaPlayer1.setLooping(true);

        } catch (IOException e){ }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer1.setLooping(false);
                soundPlayer.playStartSound();
                Intent splashIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },SPLAH_TIME_OUT);
    }
}
