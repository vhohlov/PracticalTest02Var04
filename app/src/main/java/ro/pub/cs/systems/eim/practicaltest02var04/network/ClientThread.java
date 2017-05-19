package ro.pub.cs.systems.eim.practicaltest02var04.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02var04.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02var04.general.Utilities;

/**
 * Created by vhohlov on 5/19/17.
 */

public class ClientThread extends Thread {

    private String address, pageAddress;
    private int port;
    private String city;
    private String informationType;
    private TextView clientShowpage;

    private Socket socket;

    public ClientThread(String address, int port, String pageAddress, TextView clientShowpage) {
        this.address = address;
        this.port = port;
        this.clientShowpage = clientShowpage;
        this.pageAddress = pageAddress;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(pageAddress);
            printWriter.flush();


            String info;
            while ((info = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = info;
                clientShowpage.post(new Runnable() {
                    @Override
                    public void run() {
                        clientShowpage.setText(finalizedWeateherInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}

