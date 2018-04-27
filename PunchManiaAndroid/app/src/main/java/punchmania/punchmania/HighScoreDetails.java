package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import common.HighScoreList;

import static punchmania.punchmania.MainActivity.getHighScoreDetails;

public class HighScoreDetails extends AppCompatActivity {
    private static final String TAG = "HighScoreDetails";
    private ListView mListView;
    private int[][] HighScoreDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscorelist_layout);
        mListView = (ListView) findViewById(R.id.highScoreListView);
    //MainActivity.getHighScoreDetails(HighScoreListActivity.getClickedItemId());
        HighScoreDetails = MainActivity.getHighScoreDetails();
        Log.i("Received x:", HighScoreDetails[0].toString());
        Log.i("Received y:", HighScoreDetails[1].toString());
        Log.i("Received z:", HighScoreDetails[2].toString());
    }
}
