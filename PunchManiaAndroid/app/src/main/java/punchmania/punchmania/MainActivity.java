package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import common.Message;


public class MainActivity extends AppCompatActivity {
     EditText enterNameEditText;
     Button btnAdd, btnViewQueue, btnViewHighScore;
     public static ArrayList<String> QueueArrayList = new ArrayList<>();


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


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = enterNameEditText.getText().toString();
                if (enterNameEditText.length() != 0) {
                    QueueArrayList.add(newEntry);
                    toastMessage("Successfully added to queue");
                    enterNameEditText.setText("");
                } else {
                    toastMessage("You must put something in the text field");
                }
            }
        });

        enterNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    switch (keyCode){
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String newEntry = enterNameEditText.getText().toString();
                            if (enterNameEditText.length() != 0) {
                                QueueArrayList.add(newEntry);
                                toastMessage("Successfully added to queue");
                                enterNameEditText.setText("");
                                return true;
                            } else {
                                toastMessage("You must put something in the text field");
                                return true;
                            }
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btnViewQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QueueListActivity.class);
                startActivity(intent);
            }
        });

        btnViewHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, HighScoreListActivity.class);
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
     * Sends a newly created user to the server and sends a message to the queue.
     *
     * @param user
     */
    public synchronized void sendUser(String user) {
        try {
            oos.writeObject(new Message(user, message.NEW_USER_TO_QUEUE));
            oos.reset();
            oos.flush();
            System.out.println(user + " skickad");
        } catch (IOException e) {
            System.err.println("Socket interrupted");
        }
    }

    /**
     *
     * method that uses the given ip and port to check if the connection is
     * established.
     *
     * @param ip
     * @param port
     */
    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Successful connection!");
            connected(ip, port);
        } catch (UnknownHostException e) {
            System.err.println("Host could not be found!");
            retry(ip, port);
        } catch (IOException e) {
            System.err.println("Could not connect to host");
            retry(ip, port);
        }
    }

    public void connected(String ip, int port) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            retry(ip, port);
        }
        dr = new Client.DataReader(this);
        dr.start();
    }


}
