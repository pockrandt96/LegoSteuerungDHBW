package com.dhbw.legosteuerung;

        import java.io.IOException;
        import java.io.OutputStream;
        import java.io.PrintStream;
        import java.net.InetAddress;
        import java.net.Socket;
        import java.net.UnknownHostException;

/**
 * Created by D062299 on 09.06.2017.
 */

/*class ClientThread implements Runnable {

    public Socket socket;
    public OutputStream ostream;
    public PrintStream pstream;

    private static final int SERVERPORT = 3141;
    private static final String SERVER_IP = "192.168.43.39";

    @Override
    public void run() {

        try {
            socket = new Socket(SERVER_IP, SERVERPORT);
            ostream = socket.getOutputStream();
            pstream = new PrintStream(ostream);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}*/

