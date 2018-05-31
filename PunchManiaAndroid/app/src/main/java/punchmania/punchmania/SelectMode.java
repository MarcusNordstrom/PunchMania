package punchmania.punchmania;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import common.Message;

/**
 * This class is for choosing game mode for next punch.
 *
 * @author Anna Brondin
 */
public class SelectMode extends AppCompatActivity {
    private Button btnHardPunch, btnFastPunch;
    private updater updater = new updater();
    private MainActivity main;

    /**
     * This main method searches in the activity_select_mode layout where all the widgets are defined.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        btnHardPunch = (Button) findViewById(R.id.btnHardPunch);
        btnFastPunch = (Button) findViewById(R.id.btnFastPunch);
        setButtons();
        updater.start();

    }

    /**
     * This method reassures if the queue list is empty the buttons for choosing game mode is disabled
     */
    private void setButtons() {

        if (MainActivity.getQueue().size() == 0) {
            btnFastPunch.setEnabled(false);
            btnHardPunch.setEnabled(false);

        } else {
            btnFastPunch.setEnabled(true);
            btnHardPunch.setEnabled(true);
            /**
             * When the Hard mode is pressed it sends a object to the server via StaticDataSender. A popup message is also shown when pressed.
             */
            btnHardPunch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    main.staticSend("HARD", Message.GAMEMODE);    // staticSend eller send?
                    String message = "HARDPUNCH ACTIVATED";
                    Toast.makeText(SelectMode.this, message, Toast.LENGTH_SHORT).show();
                }
            });
            /**
            * When the Fast mode is pressed it sends a object to the server via StaticDataSender. A popup message is also shown when pressed.
            */
            btnFastPunch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    main.staticSend("FAST", Message.GAMEMODE);
                    String message = "FASTPUNCH ACTIVATED";
                    Toast.makeText(SelectMode.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**
     * Inner class running the method setButtons() every second to se if the list should be updated in UI.
     */
    public class updater extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    synchronized (this) {
                        wait(1000);
                        if (!isInterrupted())
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setButtons();
                                }
                            });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
