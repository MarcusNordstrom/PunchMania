package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
     EditText enterNameEditText;
     Button btnAdd, btnViewQueue, btnViewHighScore;
     DatabaseHelper mDatabaseHelper;
     public static ArrayList<String> QueueArrayList;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewQueue = (Button) findViewById(R.id.btnViewQueue);
        btnViewHighScore = (Button) findViewById(R.id.btnViewHighScore);
        enterNameEditText = (EditText) findViewById(R.id.enterNameEditText);
        mDatabaseHelper = new DatabaseHelper(this);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = enterNameEditText.getText().toString();
                if (enterNameEditText.length() != 0) {
                    QueueArrayList.add(newEntry);
                    enterNameEditText.setText("");
                } else {
                    toastMessage("You must put something in the text field");
                }
            }
        });

        btnViewQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Umm, keep the rest of the example code underneath this comment

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<String> getQueue()
    {
        return QueueArrayList;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
