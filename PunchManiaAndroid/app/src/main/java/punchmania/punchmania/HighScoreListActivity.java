package punchmania.punchmania;

import android.content.Intent;
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
    private ArrayList<String> convertedHighScoreListOld = new ArrayList<>();

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
                Intent intent = new Intent(HighScoreListActivity.this, OpenGLES20Activity.class);
                startActivity(intent);

//                HighScoreList userToFetch = new HighScoreList();
//                String userToFetchName = MainActivity.getHighScores().getUser(position).getUser();
//                int userToFetchScore = MainActivity.getHighScores().getUser(position).getScore();
//                userToFetch.add(userToFetchName, userToFetchScore);
//                Log.i("HighScoreList: ", "Requesting details for " + userToFetch.getUser(0).getUser());
//                MainActivity.staticSend(userToFetch, 7);
//                Toast.makeText(HighScoreListActivity.this, MainActivity.getHighScores().getUser(position).getUser(), Toast.LENGTH_LONG).show();
//                ArrayList HighScoreDetails;
//                boolean printed = false;
//                while (!printed) {
//                    if (MainActivity.getHighScoreDetails() != null && MainActivity.getHighScoreDetails().size() != 0) {
//                        HighScoreDetails = MainActivity.getHighScoreDetails();
//                        Log.i("Received x:", HighScoreDetails.get(0).toString());
//                        Log.i("Received y:", HighScoreDetails.get(1).toString());
//                        Log.i("Received z:", HighScoreDetails.get(2).toString());
//                        printed = true;
//                    }
//                }
//
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
        if(convertedHighScoreListOld != convertedHighScoreList) {
            convertedHighScoreListOld = convertedHighScoreList;
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertedHighScoreList);
            listView.setAdapter(adapter);
        }
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