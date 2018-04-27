package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class HighScoreDetails extends AppCompatActivity {
    private static final String TAG = "HighScoreListActivity";
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscorelist_layout);
        mListView = (ListView) findViewById(R.id.highScoreListView);

    }
}
