package com.dhbw.legosteuerung;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class MainActivity extends AppCompatActivity {

    public static final String FORWARD_MODE = "F";
    public static final String BACKWARD_MODE = "B";
    public static final String LEFT_MODE = "L";
    public static final String RIGHT_MODE = "R";
    public static final String STILL_MODE = "S";
    public static final String RECALL_MODE = "r";
    private static final String TOGGLE_MODE = "a";
    public static String servAddr = "";
    public static Boolean switchState = true;

    private static String mode = STILL_MODE;
    private static TextView textViewSent;
    private static Sender sender;
    private ConnectionHandler standardConnectionHandler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btForward = findViewById(R.id.buttonForward);
        final Button btBackward = findViewById(R.id.buttonBackward);
        Button btLeft = findViewById(R.id.buttonLeft);
        Button btRight = findViewById(R.id.buttonRight);
        Button btRecall = findViewById(R.id.buttonRecallMain);
        Switch swToggleAutomatedDodge = findViewById(R.id.switchToggleAutomatedDodge);
        textViewSent = findViewById(R.id.textViewSent);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Client IP-Address");

        // Set up the input
        if (GyroModeActivity.servAddr != null) {
            servAddr = GyroModeActivity.servAddr;
            try {
                standardConnectionHandler = new ConnectionHandler(servAddr);
                standardConnectionHandler.open();
                sender = new Sender();
                sender.start();
            } catch (UnknownHostException e) {
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    servAddr = input.getText().toString();
                    try {
                        standardConnectionHandler = new ConnectionHandler(servAddr);
                        standardConnectionHandler.open();
                        sender = new Sender();
                        sender.start();
                    } catch (UnknownHostException e) {
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(MainActivity.this, "No address specified. Stopping application!",
                            Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        if (GyroModeActivity.switchState != null) {
            switchState = GyroModeActivity.switchState;
        }
        swToggleAutomatedDodge.setChecked(switchState);

        btForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sent;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //standardConnectionHandler.sendPacket(FORWARD_MODE);
                        //String s = "Sent: " + FORWARD_MODE;
                        //textViewSent.setText(s);
                        mode = FORWARD_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        //standardConnectionHandler.sendPacket(STILL_MODE);
                        //String s = "Sent: " + STILL_MODE;
                        //textViewSent.setText(s);
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        //standardConnectionHandler.sendPacket(STILL_MODE);
                        //String s = "Sent: " + STILL_MODE;
                        //textViewSent.setText(s);
                        mode = STILL_MODE;
                        String s = "Sent: " + mode;
                        textViewSent.setText(s);
                        return true;
                }
                return false;
            }
        });

        btBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sent;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mode = BACKWARD_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                }
                return false;
            }
        });

        btLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sent;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mode = LEFT_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                }
                return false;
            }
        });

        btRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sent;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mode = RIGHT_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                }
                return false;
            }
        });

        btRecall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sent;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mode = RECALL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mode = STILL_MODE;
                        sent = "Sent: " + mode;
                        textViewSent.setText(sent);
                        return true;
                }
                return false;
            }
        });

        swToggleAutomatedDodge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mode = TOGGLE_MODE + String.valueOf(isChecked ? 1 : 0);
                String sent = "Sent: " + mode;
                textViewSent.setText(sent);
                switchState = isChecked;
            }
        });
    }

    public void switchToGyroMode(View view) throws IOException {
        sender.interrupt();
        Intent intent = new Intent(this, GyroModeActivity.class);
        startActivity(intent);
        finish();
    }

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


    private class Sender extends Thread {
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        standardConnectionHandler.sendPacket(mode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(200);
                }
            } catch (InterruptedException consumed) {
                return;
            }
        }
    }
}
