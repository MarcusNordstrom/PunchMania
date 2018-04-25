package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HighScoreListActivity extends AppCompatActivity {

    private static final String TAG = "HighScoreListActivity";

    private Button btnHomeHS;

    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.highscorelist_layout);

        populateListView();

        btnHomeHS = (Button) findViewById(R.id.btnHomeHS);

        btnHomeHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScoreListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateListView(){
        ListView mListView = (ListView) findViewById(R.id.highScoreListView);
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the HighScore ArrayList
        ArrayList<String> convertedHighScoreList = new ArrayList<>();
        for(int i = 0; i < MainActivity.getHighScores().size(); i++)
        {
            convertedHighScoreList.add(MainActivity.getHighScores().getUser(i).getUser() + "\n" +MainActivity.getHighScores().getUser(i).getScore());
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,convertedHighScoreList);
        mListView.setAdapter(adapter);
    }


}
