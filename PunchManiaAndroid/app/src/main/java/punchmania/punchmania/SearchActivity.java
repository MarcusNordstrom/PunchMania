package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    TextView usernameTextView;
    Button btnHomeSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        btnHomeSearch = (Button) findViewById(R.id.btnHomeSearch);

        Intent intent =  getIntent();
        String str = intent.getStringExtra("Hejsan");
        usernameTextView.setText(str);

        btnHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });


    }
}
    /* private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the Queue ArrayList
        ArrayList<String> copiedPlayerList = new ArrayList<>();
        for (int i = 0; i < MainActivity.getListPlayer().size(); i++) {
            {
                copiedPlayerList.add(MainActivity.getListPlayer().peekAt(i));
            }

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, copiedPlayerList);

            searchListView.setAdapter(adapter);

        }
    }
} */
