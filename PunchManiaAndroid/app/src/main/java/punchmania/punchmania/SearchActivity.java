package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    TextView usernameTextView;
    Button btnHomeSearch;
    ListView searchListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchListView = (ListView) findViewById(R.id.searchListView);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        btnHomeSearch = (Button) findViewById(R.id.btnHomeSearch);


        Intent intent =  getIntent();
        String str = intent.getStringExtra("Name");
        usernameTextView.setText(str);

        btnHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the HighScore ArrayList
        ArrayList<String> convertedListPlayer = new ArrayList<>();
        for (int i = 0; i < MainActivity.getHighScores().size(); i++) {
            convertedListPlayer.add(MainActivity.getListPlayer().getUser(i).getUser() + "\n" + MainActivity.getListPlayer().getUser(i).getScore());
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertedListPlayer);
        searchListView.setAdapter(adapter);
    }
}
