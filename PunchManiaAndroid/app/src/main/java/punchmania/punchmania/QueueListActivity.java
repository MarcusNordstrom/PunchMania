package punchmania.punchmania;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Class is receiving the queue list from server.
 */
public class QueueListActivity extends AppCompatActivity {

    private static final String TAG = "QueueListActivity";
    private ListView mListView;
    private updater updater = new updater();
    private ArrayList<String> copiedQueueListOld = new ArrayList<>();

    /**
     * Main method starts the other methods
     * @param savedInstanceState
     */
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queuelist_layout);
        mListView = (ListView) findViewById(R.id.queueListView);
        populateListView();
        updater.start();
    }
    /**
     * Creates an array list and copies the content from the receiving list from server.
     * This array list is placed in a list adapter and shown in UI (queuelist_layout).
     */
    private void populateListView() {
        //Log.d(TAG, "populateListView: Displaying data in the ListView.");
        //create the list adapter and set the adapter to the Queue ArrayList
        ArrayList<String> copiedQueueList = new ArrayList<>();

        if (MainActivity.getQueue().size() > 0) {
            for (int i = 0; i < MainActivity.getQueue().size(); i++) {

                copiedQueueList.add(i + 1 + ":   " + (String) MainActivity.getQueue().peekAt(i));

            }
            if (!copiedQueueListOld.toString().equals(copiedQueueList.toString())) {
                copiedQueueListOld = copiedQueueList;
                ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, copiedQueueList);
                mListView.setAdapter(adapter);
            }

        } else {
            copiedQueueList.clear();
            copiedQueueList.add("Queue  is empty!");

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, copiedQueueList);
            mListView.setAdapter(adapter);
        }


    }

    /**
     * Inner class running the method populateListView() every second to se if the list should be updated in UI.
     */
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