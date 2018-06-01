package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Class receives scores from the requested user and shows them in the activity.
 *
 * @author Anna Brondin
 */
public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "searchActivity";
    private TextView usernameTextView;
    private ListView searchListViewHard, searchListViewFast;


    /**
     * Main method starts the other methods.
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        searchListViewHard = (ListView) findViewById(R.id.searchListView);
        searchListViewFast = (ListView) findViewById(R.id.searchListViewFast);

        Intent intent = getIntent();
        String str = intent.getStringExtra("requested user");
        usernameTextView.setText(str);
        updater updater = new updater();
        updater.start();
    }

    /**
     * Creates an array list and copies the content from the receiving list from server.
     * This array list is placed in a list adapter and shown in UI (activity_search).
     */
    private void populateListViewHard() {
        ArrayList<String> PlayerHighScoreHard = new ArrayList<>();
        if (MainActivity.getListPlayerHard().size() > 0) {
            for (int i = 0; i < MainActivity.getListPlayerHard().size(); i++) {
                Log.i(TAG, MainActivity.getListPlayerHard().getUser(i).getScore() + "");
                PlayerHighScoreHard.add(MainActivity.getListPlayerHard().getUser(i).getUser() + "   " + MainActivity.getListPlayerHard().getUser(i).getScore() + "\n");
            }

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PlayerHighScoreHard);

            searchListViewHard.setAdapter(adapter);
        } else {
            PlayerHighScoreHard = new ArrayList<>();
            PlayerHighScoreHard.add("No scores to show");

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PlayerHighScoreHard);

            searchListViewHard.setAdapter(adapter);
        }
    }

    /**
     * Creates an array list and copies the content from the receiving list from server.
     * This array list is placed in a list adapter and shown in UI (activity_search).
     */
    private void populateListViewFast() {
        ArrayList<String> PlayerHighScoreFast = new ArrayList<>();
        if (MainActivity.getListPlayerFast().size() > 0) {
            for (int i = 0; i < MainActivity.getListPlayerFast().size(); i++) {
                Log.i(TAG, MainActivity.getListPlayerFast().getUser(i).getScore() + "");
                PlayerHighScoreFast.add(MainActivity.getListPlayerFast().getUser(i).getUser() + "   " + MainActivity.getListPlayerFast().getUser(i).getScore() + "\n");
            }

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PlayerHighScoreFast);

            searchListViewFast.setAdapter(adapter);
        } else {
            PlayerHighScoreFast = new ArrayList<>();
            PlayerHighScoreFast.add("No scores to show");

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PlayerHighScoreFast);

            searchListViewFast.setAdapter(adapter);
        }
    }

    /**
     * Inner class running the methods populateListViewHard() and populateListViewFast().
     */
    public class updater extends Thread {
        public void run() {
            try {
                synchronized (this) {
                    wait(1000);
                    if (!isInterrupted())
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateListViewHard();
                                populateListViewFast();
                            }
                        });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}




