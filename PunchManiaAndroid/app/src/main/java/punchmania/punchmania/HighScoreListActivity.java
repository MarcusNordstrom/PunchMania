package punchmania.punchmania;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HighScoreListActivity extends AppCompatActivity {

    private static final String TAG = "HighScoreListActivity";

    private ListView listView;
    private updater updater = new updater();
    private long clickedItemId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscorelist_layout);
        listView = (ListView) findViewById(R.id.highScoreListView);

        updater.start();

    }

//    listView.setOnItemClickListener(
//            new AdapterView.OnItemClickListener()
//    {
//        @Override
//                public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
//        {
//
//        }
//    });





//   protected void onListItemClick(ListView l, View v, int position, long id) {}{
//        Cursor c = (Cursor) parent.getAdapter().getItem(position);
//        String testString = c.getString(c.getColumnIndex("col_name"));
//        Log.i("HighScoreListActivity","You clicked" + testString);
//        //MainActivity.fetchHighScoreDetails(requestedHighScore);
//    }



    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the HighScore ArrayList
        ArrayList<String> convertedHighScoreList = new ArrayList<>();
        for (int i = 0; i < MainActivity.getHighScores().size(); i++) {
            convertedHighScoreList.add(i+1 + ":   "+ MainActivity.getHighScores().getUser(i).getUser() +"    " + MainActivity.getHighScores().getUser(i).getScore());
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