package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import common.HighScoreList;

public class HighScoreListActivity extends AppCompatActivity {

    private static final String TAG = "HighScoreListActivity";

    private ListView listView;
    private updater updater = new updater();
    private long clickedItemId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscorelist_layout);
        listView = (ListView) findViewById(R.id.highScoreListView);

        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the HighScore ArrayList

        updater.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HighScoreList userToFetch = new HighScoreList();
                String userToFetchName = MainActivity.getHighScores().getUser(position).getUser();
                int userToFetchScore = MainActivity.getHighScores().getUser(position).getScore();
                userToFetch.add(userToFetchName, userToFetchScore);
                MainActivity.send(userToFetch, 7);
                Toast.makeText(HighScoreListActivity.this, MainActivity.getHighScores().getUser(position).getUser(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the HighScore ArrayList
        ArrayList<String> convertedHighScoreList = new ArrayList<>();
        for (int i = 0; i < MainActivity.getHighScores().size(); i++) {
            convertedHighScoreList.add(i + 1 + ":   " + MainActivity.getHighScores().getUser(i).getUser() + "    " + MainActivity.getHighScores().getUser(i).getScore());
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertedHighScoreList);
        listView.setAdapter(adapter);
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