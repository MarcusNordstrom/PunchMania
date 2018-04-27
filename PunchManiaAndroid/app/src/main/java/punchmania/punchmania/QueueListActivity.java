package punchmania.punchmania;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class QueueListActivity extends AppCompatActivity {

    private static final String TAG = "QueueListActivity";

    private ListView mListView;
    private Button btnHomeQ;
    private updater updater = new updater();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queuelist_layout);
        mListView = (ListView) findViewById(R.id.queueListView);
        populateListView();
        updater.start();

        btnHomeQ = (Button) findViewById(R.id.btnHomeQ);

        btnHomeQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updater.interrupt();
                finish();
                return;
            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the Queue ArrayList
        ArrayList<String> copiedQueueList = new ArrayList<>();

        if( MainActivity.getQueue().size() > 0 ) {
            for (int i = 0; i < MainActivity.getQueue().size(); i++) {

                copiedQueueList.add(i+1 + ":   " + MainActivity.getQueue().peekAt(i));

                ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, copiedQueueList);
                mListView.setAdapter(adapter);

            }

                } else {
            copiedQueueList.clear();

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, copiedQueueList);
            mListView.setAdapter(adapter);
    }



            }


    public class updater extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    synchronized (this) {
                        wait(1000);
                        if (!isInterrupted())
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    populateListView();
                                }
                            });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}