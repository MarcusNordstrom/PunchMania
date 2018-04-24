package punchmania.punchmania;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queuelist_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView(){
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        ArrayList<String> QueueArrayList = MainActivity.getQueue();
        //create the list adapter and set the adapter to the Queue ArrayList
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,QueueArrayList);
        mListView.setAdapter(adapter);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
