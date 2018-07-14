package com.dhbw.legosteuerung;


        import android.os.AsyncTask;
        import android.util.Log;

        import java.io.IOException;
        import java.net.DatagramPacket;
        import java.net.DatagramSocket;
        import java.net.InetAddress;
        import java.net.SocketException;
        import java.net.UnknownHostException;
        import java.nio.ByteBuffer;
        import java.nio.charset.Charset;

/**
 * Created by D062299 on 02.02.2017.
 */


public class ConnectionHandler {

    public static final int SERVER_PORT = 4711;

    private InetAddress ia;
    private final DatagramSocket dSocket;
    private String servAddr;

    public ConnectionHandler(String clientAddr) throws SocketException {
        servAddr = clientAddr;
        dSocket = new DatagramSocket();
    }

    public void open() throws UnknownHostException {
        ia = InetAddress.getByName(servAddr);
    }

    public void sendPacket(String s) throws IOException {
        byte[] raw = s.getBytes();
        DatagramPacket packet = new DatagramPacket(raw, raw.length, ia, SERVER_PORT);

        dSocket.send(packet);
    }

    public void sendGyroPacket(ByteBuffer buffer) throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.array().length, ia, SERVER_PORT);

        dSocket.send(packet);
    }
}
/*
public class ConnectionHandler extends AsyncTask<Void, Void, Void> {

    private InetAddress ia;
    private int servPort;
    private String message = "s";

    public ConnectionHandler( String servAddr, int port) throws UnknownHostException {
        Log.d("debug", servAddr);
        ia = InetAddress.getByName(servAddr);
        servPort = port;
    }

    public void setMessage(String s) {
        message = s;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            Log.d("tag", message);
            byte[] raw = message.getBytes();

            DatagramPacket packet = new DatagramPacket(raw, raw.length, ia, servPort);
            DatagramSocket dSocket = null;
            try {
                dSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                dSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}*/
