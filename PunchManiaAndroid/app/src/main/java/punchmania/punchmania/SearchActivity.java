package punchmania.punchmania;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {
    private TextView username;


    public void updateName (TextView name) {
        username = (TextView) findViewById(R.id.username);
        username = name;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


    }
}
