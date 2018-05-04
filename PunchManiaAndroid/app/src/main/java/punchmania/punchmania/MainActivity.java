package punchmania.punchmania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import common.HighScoreList;
import common.Message;
import common.Queue;

public class MainActivity extends AppCompatActivity {
    EditText enterNameEditText;
    Button btnSearch, btnViewQueue, btnViewHighScore, btnViewHighScoreFast;

    private static Queue queue = new Queue();
    private static HighScoreList list = new HighScoreList();
    private static HighScoreList listPlayer = new HighScoreList();
    private static HighScoreList listFast = new HighScoreList();
    private static HighScoreList listPlayerFast = new HighScoreList();

    private static ArrayList<ArrayList<Integer>> highScoreDetails = new ArrayList<>();
    private Socket socket = new Socket();
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private String ip = "192.168.0.148";
    private int port = 12346;
    public static boolean connected = false;
    private DataReader dataReader = new DataReader();
    private static boolean dataReaderRunning = false;
    private SearchActivity search;
    private static long requestedHit = Long.MAX_VALUE;

    private Message message;
    private String user;

    public static final int NEW_QUEUE = 1;
    public static final int NEW_HIGHSCORELIST = 2;
    public static final int NEW_USER_TO_QUEUE = 3;
    public static final int NEW_HS = 4;
    public static final int REQUEST_PLAYERSCORES = 5;
    public static final int PLAYERSCORES = 6;
    public static final int REQUEST_HSDETAILS = 7;
    public static final int HSDETAILS = 8;

    public static final int GAMEMODE_FASTPUNCH = 9;
    public static final int GAMEMODE_HARDPUNCH = 12;

    public static final int NEW_HIGHSCORELIST_FASTPUNCH = 10;
    public static final int PLAYERSCORES_FASTPUNCH = 11;
    public static final int NEW_USER_TO_QUEUE_FASTPUNCH = 13;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        dataReader.start();
        //HighScoreListActivity highScoreListActivity = new HighScoreListActivity();
        //highScoreListActivity.onCreate(savedInstanceState);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnViewQueue = (Button) findViewById(R.id.btnViewQueue);
        btnViewHighScore = (Button) findViewById(R.id.btnViewHighScore);
        btnViewHighScoreFast = (Button) findViewById(R.id.btnViewHSFast);
        enterNameEditText = (EditText) findViewById(R.id.enterNameEditText);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = enterNameEditText.getText().toString();

                if (enterNameEditText.length() != 0) {
                    send(newEntry, REQUEST_PLAYERSCORES);
                    enterNameEditText.setText("");
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Hejsan", newEntry);
                    startActivity(intent);
                } else {
                    toastMessage("You must put something in the text field");

                }
            }
        });

        enterNameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String newEntry = enterNameEditText.getText().toString();
                            if (enterNameEditText.length() != 0) {
                                send(newEntry, REQUEST_PLAYERSCORES);
                                enterNameEditText.setText("");
                                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                intent.putExtra("Hejsan", newEntry);
                                startActivity(intent);
                                return true;
                            } else {
                                toastMessage("That's a short name you've got there");
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
                Intent intent = new Intent(MainActivity.this, HighScoreListActivity.class);
                startActivity(intent);
            }
        });

        btnViewHighScoreFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreFast.class);
                startActivity(intent);
            }
        });
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static Queue getQueue() {
        return queue;
    }

    public static HighScoreList getHighScores() {
        return list;
    }

    public static ArrayList getHighScoreDetails() {
        return highScoreDetails;
    }

    public static HighScoreList getListPlayer() {
        return listPlayer;
    }

    public static HighScoreList getListPlayerFast() {
        return listPlayerFast;
    }

    public static HighScoreList getHighScoresFast() {
        return listFast;
    }

    public synchronized void send(Object arg1, int arg2) {
        DataSender dataSender = new DataSender(arg1, arg2);
    }

    public synchronized static void staticSend(Object arg1, int arg2) {
        StaticDataSender dataSender = new StaticDataSender(arg1, arg2);
    }

    public static class StaticDataSender extends Thread {
        private Object send;
        private int instruction;

        public StaticDataSender(Object send, int instruction){
            this.send = send;
            this.instruction = instruction;
            this.start();
}

        public synchronized void run() {
            if (connected && send != null && instruction != 0 && !isInterrupted()) {
                try {
                    Log.i("DataSender: ", "Trying to send!");
                    oos.writeObject(new Message(send, instruction));
                    oos.reset();
                    oos.flush();
                    send = null;
                    instruction = 0;
                    Log.i("DataSender: ", "Sent!");
                } catch (IOException e) {
                    Log.i("DataSender: ", "Socket interrupted");
                }
            }
        }
    }

    public class DataSender extends Thread {
        private Object send;
        private int instruction;

        public DataSender(Object send, int instruction){
            this.send = send;
            this.instruction = instruction;
            this.start();
}

        public synchronized void run() {
            if (connected && send != null && instruction != 0 && !isInterrupted()) {
                try {
                    Log.i("DataSender: ", "Trying to send!");
                    oos.writeObject(new Message(send, instruction));
                    oos.reset();
                    oos.flush();
                    send = null;
                    instruction = 0;
                    Log.i("DataSender: ", "Sent!");
                } catch (IOException e) {
                    Log.i("DataSender: ", "Socket interrupted");
                }
            }
        }
    }

    private class DataReader extends Thread {

        public boolean retry() {
            while (!connected) {
                System.err.print("Reconnecting in ");
                for (int i = 5; i > 0; i--) {
                    System.err.print(i + " ");
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.err.println();
                connected = connect();
            }
            return true;
        }

        public boolean connect() {
            Log.i(this.getName(), "Searching for host");
            try {
                InetAddress addr = InetAddress.getByName(ip);
                socket = new Socket(addr, port);
                Log.i(this.getName(), "Gad em!");
                return true;
            } catch (UnknownHostException e) {
                Log.i(this.getName(), "Stranger danger!");
                return false;
            } catch (IOException e) {
                Log.i(this.getName(), "404: Server not found :(");
                return false;
            }
        }

        public DataReader() {
            Log.i(this.getName(), "DataReader initiated");
        }

        public void run() {
            if (!dataReaderRunning) {
                Log.i("DataReader", "Starting");
                dataReaderRunning = true;
                Object obj;
                while (true) {
                    connected = retry();
                    try {
                        ois = new ObjectInputStream(socket.getInputStream());
                        oos = new ObjectOutputStream(socket.getOutputStream());
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }

                    while (connected) {
                        try {
                            Log.i(this.getName(), "Waiting for object");
                            obj = ois.readObject();
                            Log.i(this.getName(), obj.toString());
                            if (obj instanceof Message) {
                                Message readMessage = (Message) obj;
                                Log.i(this.getName(), "Object has arrived!");
                                switch (readMessage.getInstruction()) {
                                    case NEW_QUEUE:
                                        Log.i(this.getName(), "It's a Queue!");
                                        queue = (Queue) readMessage.getPayload();
                                        break;
                                    case NEW_HIGHSCORELIST:
                                        Log.i(this.getName(), "It's a HighScoreList HardPunch!");
                                        list = (HighScoreList) readMessage.getPayload();
                                        break;
                                    case PLAYERSCORES:
                                        Log.i(this.getName(), "It´s userscores HardPunch!");
                                        listPlayer = (HighScoreList) readMessage.getPayload();
                                        break;
                                    case HSDETAILS:
                                        Log.i(this.getName(), "It's HighScore details!");
                                        highScoreDetails = (ArrayList<ArrayList<Integer>>) readMessage.getPayload();
                                        break;
                                    case NEW_HIGHSCORELIST_FASTPUNCH:
                                        Log.i(this.getName(), "It´s a HighScoreList FastPunch");
                                        listFast = (HighScoreList) readMessage.getPayload();
                                        break;
                                    case PLAYERSCORES_FASTPUNCH:
                                        Log.i(this.getName(), "It´s userscores FastPunch");
                                        listPlayerFast = (HighScoreList) readMessage.getPayload();
                                        break;
                                    default:
                                        Log.i(this.getName(), "get object");
                                        break;
                                }
                                readMessage = null;
                            }
                        } catch (IOException e1) {
                            Log.i(this.getName(), "Connection lost!");
                            connected = false;
                        } catch (ClassNotFoundException e) {
                            Log.i(this.getName(), "Class not found!");
                        }
                    }
                }
            }
        }

//    // Geofencing stuff
//    private PendingIntent getGeofencePendingIntent() {
//        // Reuse the PendingIntent if we already have it.
//        if (mGeofencePendingIntent != null) {
//            return mGeofencePendingIntent;
//        }
//        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
//        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
//        // calling addGeofences() and removeGeofences().
//        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
//                FLAG_UPDATE_CURRENT);
//        return mGeofencePendingIntent;
//
//    }
    }
}
