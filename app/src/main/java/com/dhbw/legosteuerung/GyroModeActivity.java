package com.dhbw.legosteuerung;

        import android.content.Intent;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.Switch;
        import android.widget.TextView;

        import java.io.IOException;
        import java.nio.ByteBuffer;

public class GyroModeActivity extends AppCompatActivity {

    public static final String STILL_MODE = "S";
    public static final String RECALL_MODE = "r";
    public static final String TOGGLE_MODE = "a";
    private SensorManager mSensorManager;
    private MyListener mListener;
    public ConnectionHandler gyroConnectionHandler;
    public TextView textViewSent;
    private static Sender sender;
    public static String servAddr;
    public static Boolean switchState;
    private Button recallButton;
    private String mode;
    private ByteBuffer bufferToSend = ByteBuffer.allocate(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Switch swToggleAutomatedDodge = findViewById(R.id.switchToggleAutomatedDodgeGyro);
        switchState = MainActivity.switchState;
        swToggleAutomatedDodge.setChecked(switchState);

        textViewSent = findViewById(R.id.textViewSentG);
        recallButton = findViewById(R.id.buttonRecallGyro);

        // Get an instance of the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        servAddr = MainActivity.servAddr;
        try {
            mListener = new MyListener();
            mListener.start();
            servAddr = MainActivity.servAddr;
            gyroConnectionHandler = new ConnectionHandler(servAddr);
            gyroConnectionHandler.open();
            sender = new Sender();
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recallButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String sent;
                mListener.pause();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mode = RECALL_MODE;
                        sent = "Sent: " + RECALL_MODE;
                        textViewSent.setText(sent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mode = STILL_MODE;
                        sent = "Sent: " + STILL_MODE;
                        textViewSent.setText(sent);
                        mListener.resume();
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mode = STILL_MODE;
                        sent = "Sent: " + STILL_MODE;
                        textViewSent.setText(sent);
                        mListener.resume();
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

    public void switchToMainMode(View view) throws IOException {
        mListener.stop();
        sender.interrupt();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class MyListener implements SensorEventListener {

        private Sensor mRotationVectorSensor;
        private final float[] mRotationMatrix = new float[16];
        private int updateCounter = 0;
        private boolean paused = false;

        public MyListener() throws IOException {
            // find the rotation-vector sensor
            mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            // initialize the rotation matrix
            mRotationMatrix[0] = 1;
            mRotationMatrix[4] = 1;
            mRotationMatrix[8] = 1;
            mRotationMatrix[12] = 1;
        }

        public void start() {
            // enable our sensor when the activity is resumed, ask for
            // 100 ms updates.
            mSensorManager.registerListener(this, mRotationVectorSensor, 100000);
        }

        public void stop() {
            // make sure to turn our sensor off when the activity is paused
            mSensorManager.unregisterListener(this);
        }

        public void pause() {
            paused = true;
        }

        public void resume() {
            paused = false;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (!paused) {
                // we received a sensor event. it is a good practice to check
                // that we received the proper event
                if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    // convert the rotation-vector to a 4x4 matrix.
                    SensorManager.getRotationMatrixFromVector(
                            mRotationMatrix, event.values);
                    updateCounter++;
                    updateDebugPanel(mRotationMatrix);
                }

                //convert rotation data and send it off
                int ix = (int) Math.floor((mRotationMatrix[8]) * 200);
                ByteBuffer buffer = ByteBuffer.allocate(5);
                buffer.put((byte) 71);
                ix = -ix;

                if (ix > 100) ix = 100;
                if (ix < -100) ix = -100;

                if (ix >= 0) {
                    buffer.put((byte) 70);
                    buffer.put((byte) ix);
                } else {
                    buffer.put((byte) 66);
                    buffer.put((byte) -ix);
                }

                int iy = (int) Math.floor((mRotationMatrix[9]) * 200);
                if (iy > 100) iy = 100;
                if (iy < -100) iy = -100;

                if (iy >= 0) {
                    buffer.put((byte) 82);
                    buffer.put((byte) iy);
                } else {
                    buffer.put((byte) 76);
                    buffer.put((byte) -iy);
                }
                bufferToSend = buffer;
                textViewSent.setText("Sent: " + String.valueOf(ix) + " " + String.valueOf(iy));
            }
        }

        public void updateDebugPanel(float[] matrix) {
            TextView textViewUpdateCounter = findViewById(R.id.textViewUpdateCount);
            TextView textViewArray8 = findViewById(R.id.textViewArray8);
            TextView textViewArray9 = findViewById(R.id.textViewArray9);
            TextView textViewArray10 = findViewById(R.id.textViewArray10);
            textViewUpdateCounter.setText(Integer.toString(updateCounter));
            textViewArray8.setText(Float.toString(matrix[8]));
            textViewArray9.setText(Float.toString(matrix[9]));
            textViewArray10.setText(Float.toString(matrix[10]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }
    private class Sender extends Thread {
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (mode == RECALL_MODE)
                            gyroConnectionHandler.sendPacket(mode);
                        else
                            gyroConnectionHandler.sendGyroPacket(bufferToSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(50);
                }
            } catch (InterruptedException consumed) {
                return;
            }
        }
    }
}

/*class Sender extends Thread {
    private final BlockingQueue<ByteBuffer> blockingQueue = new LinkedBlockingDeque<>();
    private boolean interrupt = false;
    private String mode = GyroModeActivity.STILL_MODE;
    private ConnectionHandler connectionHandler;
    private ByteBuffer recallByteBuffer;

    public Sender(String servAddr) {
        try {
            connectionHandler = new ConnectionHandler(servAddr);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            connectionHandler.open();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        recallByteBuffer = ByteBuffer.allocate(1);
        recallByteBuffer.put((byte) 114);
    }

    public boolean addPacket(ByteBuffer buffer) {
        return  blockingQueue.offer(buffer);
    }

    public void setMode(String m) { mode = m; }

    public void interrupt() {
        interrupt = true;
    }

    public void run() {
        while (!interrupt) {
            try {
                try {
                    ByteBuffer buffer;
                    if (mode == GyroModeActivity.RECALL_MODE) {
                        buffer = recallByteBuffer;
                    } else {
                        buffer = blockingQueue.take();
                    }
                    connectionHandler.sendGyroPacket(buffer);
                    Log.d("Sender", String.valueOf(buffer.get(0) & 0xff));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}*/