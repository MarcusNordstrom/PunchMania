package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class HighScoreDetails extends AppCompatActivity {
    private static final String TAG = "HighScoreDetails";
    private ListView mListView;
    private ArrayList HighScoreDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscorelist_layout);
        mListView = (ListView) findViewById(R.id.highScoreListView);
        HighScoreDetails = MainActivity.getHighScoreDetails();
        Log.i("Received x:", HighScoreDetails.get(0).toString());
        Log.i("Received y:", HighScoreDetails.get(1).toString());
        Log.i("Received z:", HighScoreDetails.get(2).toString());

    }
}
