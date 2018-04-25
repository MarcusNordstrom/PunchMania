package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class QueueListActivity extends AppCompatActivity {

    private static final String TAG = "QueueListActivity";

    private ListView mListView;
    private Button btnHomeQ;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queuelist_layout);
        mListView = (ListView) findViewById(R.id.queueListView);

        populateQueueListView();

        btnHomeQ = (Button) findViewById(R.id.btnHomeQ);

        btnHomeQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueueListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateQueueListView(){
        mListView = (ListView) findViewById(R.id.queueListView);
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the Queue ArrayList
        ArrayList<String> copiedQueueList = new ArrayList<>();
        for(int i = 0; i < MainActivity.getQueue().size(); i++)
        {
            copiedQueueList.add(MainActivity.getQueue().peekAt(i));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,copiedQueueList);
        mListView.setAdapter(adapter);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }





}
