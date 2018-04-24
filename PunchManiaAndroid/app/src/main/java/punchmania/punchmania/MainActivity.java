package punchmania.punchmania;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {
     EditText enterNameEditText;
     TextView QueueList;
     Button addBtn;
     ViewPager mainViewPager;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        addBtn = (Button) findViewById(R.id.addBtn);
        QueueList = (TextView) findViewById(R.id.QueueList);
        enterNameEditText = (EditText) findViewById(R.id.enterNameEditText);
        mainViewPager = (ViewPager) findViewById(R.id.pager);

        mainViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageSelected(int position) {
                        getActionBar().setSelectedNavigationItem(position);
                    }}
        );

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = enterNameEditText.getText().toString();
                if (enterNameEditText.length() != 0){
                    QueueList.setText(name);
                    enterNameEditText.setText("");
                } else {
                    enterNameEditText.setText("Enter a name");
                }
            }
        });
    }


    public void setQueueListView(String arg1)
    {
        TextView QueueListView = findViewById(R.id.QueueList);
        QueueListView.setText(arg1);
        QueueListView.setMovementMethod(new ScrollingMovementMethod());
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
