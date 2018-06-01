package punchmania.punchmania;

import android.content.Intent;
import android.media.MediaPlayer;
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import common.HighScoreList;
import common.Message;
import common.Queue;

/**
 * Connecting client to server via port and ip-address.
 * Sending and receiving objects from server using output- and input streams.
 * Starts other classes and activities.
 * @author Benjamin Zakrisson
 */

public class MainActivity extends AppCompatActivity {
    public static boolean connected = false;
    private static Queue queue = new Queue();
    private static HighScoreList listHard = new HighScoreList();
    private static HighScoreList listPlayerHard = new HighScoreList();
    private static HighScoreList listFast = new HighScoreList();
    private static HighScoreList listPlayerFast = new HighScoreList();
    private static ArrayList<ArrayList<Integer>> highScoreDetails = new ArrayList<>();
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private String ip = "10.2.26.21";
    private int port = 9192;
    private DataReader dataReader = new DataReader();
    private static boolean dataReaderRunning = false;
    private static long requestedHit = Long.MAX_VALUE;
    private SoundPlayer soundPlayer;
    private MediaPlayer mediaPlayer1;
    private Boolean playing = false;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    EditText enterNameEditText;
    Button btnSearch, btnViewQueue, btnViewHighScore, btnViewHighScoreFast, btnPunch;
    private Socket socket = new Socket();

    /**
     * Method returns the queue receiving from input stream.
     *
     * @return queue
     */
    public static Queue getQueue() {
        return queue;
    }

    public void newHighscoreSound() {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.test);
        Log.i("SOUND", "START WINNING SONG");
        mediaPlayer1.start();
    }

    public void startSound() {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.punchmania);
        Log.i("SOUND", "START TIGER");
        mediaPlayer1.seekTo(10000);
        mediaPlayer1.start();
    }

    public void startCount() {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.countdown);
        Log.i("SOUND", "START COUNTDOWN");
        mediaPlayer1.start();
    }

    public void stopSound() {
        Log.i("SOUND", "STOPSTARTED");
        if(!playing){
            Log.i("SOUND", "PLAYING FALSE");
            if (mediaPlayer1 != null) {
                Log.i("SOUND", "STOP");
                mediaPlayer1.stop();
                mediaPlayer1.release();
                mediaPlayer1 = null;
            }
        }
    }
    /**
     * Method returns highscore list receiving from input stream.
     *
     * @return listHard
     */
    public static HighScoreList getHighScoresHard() {
        return listHard;
    }

    /**
     * @return
     */
    public static ArrayList<ArrayList<Integer>> getHighScoreDetails() {
        return highScoreDetails;
    }

    /**
     * Method returns the player list for the game mode Hardpunch
     *
     * @return listPlayerHard
     */
    public static HighScoreList getListPlayerHard() {
        return listPlayerHard;
    }

    /**
     * method returns the player list for the game mode Fastpunch
     *
     * @return listPlayerFast
     */
    public static HighScoreList getListPlayerFast() {
        return listPlayerFast;
    }

    /**
     * Method returns highscore list for the game mode FastPunch
     *
     * @return Highscore list FastPunch
     */
    public static HighScoreList getHighScoresFast() {
        return listFast;
    }

    /**
     * This method searches in the activity_client layout where all the widgets are defined.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        soundPlayer = new SoundPlayer(this);

        dataReader.start();
        //HighScoreListActivity highScoreListActivity = new HighScoreListActivity();
        //highScoreListActivity.onCreate(savedInstanceState);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnViewQueue = (Button) findViewById(R.id.btnViewQueue);
        btnViewHighScore = (Button) findViewById(R.id.btnViewHighScore);
        btnViewHighScoreFast = (Button) findViewById(R.id.btnViewHSFast);
        btnPunch = (Button) findViewById(R.id.btnPunch);
        enterNameEditText = (EditText) findViewById(R.id.enterNameEditText);


        /**
         * This method make sure that when the search button is pushed it start another activity.
         */
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newEntry = enterNameEditText.getText().toString();

                if (enterNameEditText.length() != 0) {
                    staticSend(newEntry, Message.CLIENT_REQUEST_PLAYERSCORES_HARDPUNCH);
                    staticSend(newEntry, Message.CLIENT_REQUES_PLAYERSCORES_FASTPUNCH);
                    enterNameEditText.setText("");
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("requested user", newEntry);
                    soundPlayer.playStartSound();
                    startActivity(intent);
                } else {
                    toastMessage("You must put something in the text field");

                }
            }
        });
        /**
         * This method starts another activity with the enter key, just like when you press the Search button.
         */
        enterNameEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String newEntry = enterNameEditText.getText().toString();
                            if (enterNameEditText.length() != 0) {
                                staticSend(newEntry, Message.CLIENT_REQUEST_PLAYERSCORES_HARDPUNCH);
                                staticSend(newEntry, Message.CLIENT_REQUES_PLAYERSCORES_FASTPUNCH);
                                enterNameEditText.setText("");
                                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                intent.putExtra("requested user", newEntry);
                                soundPlayer.playStartSound();
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

        /**
         * This method starts another activity when Queue button is pushed.
         */
        btnViewQueue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QueueListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * This method starts another activity when Highscore Hard button is pushed.
         */
        btnViewHighScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * This method starts another activity when Highscore Fast button is pushed.
         */
        btnViewHighScoreFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreFast.class);
                startActivity(intent);
            }
        });

        /**
         * This method starts another activity when a password is entered in the text field and if you press the PunchMania icon.
         */
        btnPunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String password = enterNameEditText.getText().toString();
                if (password.equals("1337")) {
                    enterNameEditText.setText("");
                    Intent intent = new Intent(MainActivity.this, SelectMode.class);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * This method displays a popup message given in the String message.
     *
     * @param message
     */
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method is used when object should be send to server via the subclass DataSender.
     *
     * @param arg1 object we want to send
     * @param arg2 instruction what kind of object we are sending
     */
    public synchronized void send(Object arg1, int arg2) {
        DataSender dataSender = new DataSender(arg1, arg2);
        Log.i("instruktion", "skickar arg" + arg2);
    }

    /**
     * Method is used when object should be send to server via the subclass StaticDataSender.
     *
     * @param arg1 object we want to send
     * @param arg2 instruction what kind of object we are sending
     */
    public synchronized static void staticSend(Object arg1, int arg2) {
        StaticDataSender dataSender = new StaticDataSender(arg1, arg2);
    }

    /**
     * Subclass sending a message containing an object and instruction to server.
     */
    public static class StaticDataSender extends Thread {
        private Object send;
        private int instruction;
        Random random = new Random();

        public StaticDataSender(Object send, int instruction) {
            this.send = send;
            this.instruction = instruction;
            this.start();
        }

        public synchronized void run() {

            if (connected && send != null && instruction != 0 && !isInterrupted()) {
                try {
                    this.sleep(random.nextInt(200));
                    Log.i("StaticDataSender: ", "Trying to send!");
                    oos.reset();
                    oos.writeObject(new Message(send, instruction));
                    oos.flush();
                    send = null;
                    instruction = 0;
                    Log.i("StaticDataSender: ", "Sent!");
                } catch (IOException e) {
                    Log.i("StaticDataSender: ", "Socket interrupted");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Subclass sending a message containing an object and instruction to server.
     */
    public class DataSender extends Thread{
        private Object send;
        private int instruction;

        public DataSender(Object send, int instruction) {
            this.send = send;
            this.instruction = instruction;
            this.start();
        }

        public synchronized void run() {
            if (connected && send != null && instruction != 0 && !isInterrupted()) {
                try {
                    Log.i("DataSender: ", "Trying to send!");
                    oos.writeObject(new Message(send, instruction));
                    Log.i("DataSender", send + "" + instruction );
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

    /**
     * Subclass receiving messages containing an object and instruction from server.
     */
    private class DataReader extends Thread {

        public DataReader() {
            Log.i(this.getName(), "DataReader initiated");
        }


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
                                    case Message.NEW_QUEUE:
                                        Log.i(this.getName(), "It's a Queue!");
                                        queue = (Queue) readMessage.getPayload();
                                        break;
                                    case Message.NEW_HIGHSCORELIST_HARDPUNCH:
                                        Log.i(this.getName(), "It's a HighScoreList HardPunch!");
                                        listHard = (HighScoreList) readMessage.getPayload();
                                        break;
                                    case Message.SERVER_SEND_PLAYERSCORES_HARDPUNCH:
                                        Log.i(this.getName(), "It´s userscores HardPunch!");
                                        listPlayerHard = (HighScoreList) readMessage.getPayload();
                                        break;
                                    case Message.SERVER_SEND_HSDETAILS:
                                        Log.i(this.getName(), "It's HighScore details!");
                                        highScoreDetails = (ArrayList<ArrayList<Integer>>) readMessage.getPayload();
                                        break;
                                    case Message.NEW_HIGHSCORELIST_FASTPUNCH:
                                        Log.i(this.getName(), "It´s a HighScoreList FastPunch");
                                        listFast = (HighScoreList) readMessage.getPayload();
                                        break;
                                    case Message.SERVER_SEND_PLAYERSCORES_FASTPUNCH:
                                        Log.i(this.getName(), "It´s userscores FastPunch");
                                        listPlayerFast = (HighScoreList) readMessage.getPayload();
                                        break;
                                        case Message.GAMEMODE:
                                            String mode = (String) readMessage.getPayload();
                                            switch(mode){
                                                case "HARD":
                                                    Log.i("SOUND", mode);
                                                    playing = true;
                                                    Log.i("SOUND", playing + "");
                                                    startCount();
                                                    playing = false;
                                                    break;
                                                case "FAST":
                                                    Log.i("SOUND", mode);
                                                    playing = true;
                                                    Log.i("SOUND", playing + "");
                                                    startCount();
                                                    startSound();
                                                    playing = false;
                                                    break;
                                                case "TOP":
                                                    Log.i("SOUND", mode);
                                                    playing = true;
                                                    Log.i("SOUND", playing + "");
                                                    newHighscoreSound();
                                                    break;
                                                case "DONE":
                                                    Log.i("SOUND", mode);
                                                    Log.i("SOUND", playing + "");
                                                    stopSound();
                                                    break;

                                            }
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
