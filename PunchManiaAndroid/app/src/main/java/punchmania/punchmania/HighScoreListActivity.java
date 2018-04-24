package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
public class HighScoreListActivity extends AppCompatActivity {
    private Button btnHomeHS;


    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.highscorelist_layout);

        btnHomeHS = (Button) findViewById(R.id.btnHomeHS);

        btnHomeHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HighScoreListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
