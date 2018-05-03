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

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "searchActivity";
    private TextView usernameTextView;
    private ListView searchListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        searchListView = (ListView) findViewById(R.id.searchListView);


        Intent intent = getIntent();
        String str = intent.getStringExtra("Hejsan");
        usernameTextView.setText(str);
        updater updater = new updater();
        updater.start();


    }

    private void populateListView() {
        ArrayList<String> PlayerHighScore = new ArrayList<>();
        for (int i = 0; i < MainActivity.getListPlayer().size(); i++) {
            Log.i(TAG, MainActivity.getListPlayer().getUser(i).getScore() + "");

            PlayerHighScore.add(MainActivity.getListPlayer().getUser(i).getUser() + "   " + MainActivity.getListPlayer().getUser(i).getScore() + "\n");
        }
        Log.i(TAG, "1");
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PlayerHighScore);
        Log.i(TAG, "2");
        searchListView.setAdapter(adapter);
        Log.i(TAG, "3");


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



