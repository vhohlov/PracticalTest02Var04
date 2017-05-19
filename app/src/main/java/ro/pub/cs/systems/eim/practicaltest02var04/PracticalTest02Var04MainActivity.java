package ro.pub.cs.systems.eim.practicaltest02var04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02var04.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02var04.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02var04.network.ServerThread;

public class PracticalTest02Var04MainActivity extends AppCompatActivity {

    //controlere de server
    private EditText serverPortEditText = null;
    private Button startButton = null;


    //controlere de client
    private EditText clientGetLink = null;
    private Button clientGetContent = null;
    private TextView clientShowPage = null;


    private ServerThread serverThread = null;
    private ClientThread clientThread = null;


    private class StartButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                //afiseaza un text care apare pe ecran
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            //porneste serverul
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }
    private StartButtonClickListener StartButtonClickListener = new StartButtonClickListener();




    private class ConectClientListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort = "7654";

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String address = clientGetLink.getText().toString();

            clientShowPage.setText(Constants.EMPTY_STRING);

            //creaza threadul de client cu adresa si portul serverului
            clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), address, clientShowPage);
            clientThread.start();
        }

    }
    private ConectClientListener conectClientListener = new ConectClientListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_var04_main);

        //preluare referinte catre text edit si conect buton ale serverului
        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        startButton = (Button)findViewById(R.id.start_server_button);

        //adaugare ascultator  pe butonul conect. La onClick se porneste serverul
        startButton.setOnClickListener(StartButtonClickListener);


        clientGetLink = (EditText)findViewById(R.id.client_address_edit_text);
        clientShowPage = (TextView) findViewById(R.id.show_result_text_view);
        clientGetContent = (Button)findViewById(R.id.get_content_button);

        clientGetContent.setOnClickListener(conectClientListener);
    }

}
