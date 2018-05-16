package punchmania.punchmania;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectMode extends AppCompatActivity {

    public static final int GAMEMODE = 9;

    private Button btnHardPunch, btnFastPunch;
    private MainActivity main;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        btnHardPunch = (Button) findViewById(R.id.btnHardPunch);
        btnFastPunch = (Button) findViewById(R.id.btnFastPunch);

        btnHardPunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                main.staticSend("HARD", GAMEMODE);    // staticSend eller send?
                String message = "HARDPUNCH ACTIVATED";
                Toast.makeText(SelectMode.this,  message , Toast.LENGTH_SHORT).show();

            }
        });

        btnFastPunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                main.staticSend("FAST", GAMEMODE);
                String message = "FASTPUNCH ACTIVATED";
                Toast.makeText(SelectMode.this,  message , Toast.LENGTH_SHORT).show();
            }
        });
    }


}
