package punchmania.punchmania;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HighscoreMode2 extends AppCompatActivity {

    private static final String TAG = "HighScore FastMode";
    private ListView listViewFast;
    private updater updater = new updater();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_mode2);
        listViewFast = (ListView) findViewById(R.id.highScoreListViewFast);

        updater.start();
    }

    private void populateListView() {
        //create the list adapter and set the adapter to the HighScore ArrayList
        ArrayList<String> convertedHighScoreList = new ArrayList<>();
        for (int i = 0; i < MainActivity.getHighScoresFast().size(); i++) {
            convertedHighScoreList.add(i + 1 + ":   " + MainActivity.getHighScoresFast().getUser(i).getUser() + "    " + MainActivity.getHighScoresFast().getUser(i).getScore());
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertedHighScoreList);
        listViewFast.setAdapter(adapter);
    }

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
                                    populateListView();
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
