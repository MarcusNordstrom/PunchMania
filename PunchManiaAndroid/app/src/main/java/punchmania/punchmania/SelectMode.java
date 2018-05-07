package punchmania.punchmania;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectMode extends AppCompatActivity {

    public static final int GAMEMODE_FASTPUNCH = 9;
    public static final int GAMEMODE_HARDPUNCH = 12;

    private Button btnHardPunch, btnFastPunch;
    private MainActivity main;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        btnHardPunch = (Button) findViewById(R.id.btnHardPunch);
        btnFastPunch = (Button) findViewById(R.id.btnFastPunch);

        btnHardPunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                main.staticSend(null, GAMEMODE_HARDPUNCH);

            }
        });

        btnFastPunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                main.staticSend(null, GAMEMODE_FASTPUNCH);
            }
        });
    }


}
