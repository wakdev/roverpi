package com.wakdev.roverpi;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

/**
 * Main Activity
 * @package com.wakdev.roverpi
 * @author wakdev.com
 * @version 12.12.16
 */
public class MainActivity extends Activity implements RpiListener {

	public RpiNet rpiClient;
	private static final String LOG_TAG = "RoverPiLog";

	private EditText editTextIp;
	private EditText editTextPort;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editTextIp = findViewById(R.id.editText_ip);
        editTextPort = findViewById(R.id.editText_port);

        rpiClient = RpiNet.getInstance(); //I will be the one !
        rpiClient.setListener(this); //Create Listener
    }
    
    /**
     * Show toast
     * @param toast Toast text
     */
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run(){
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
   
    /**
     * Event - onConnectionError
     */
    @Override
    public void onConnectionError() {
    	Log.e(LOG_TAG, "MainActivity : Connection Error !");
    	showToast("Error");
    }

    /**
     * Event - onConnectionSuccess
     */
	@Override
	public void onConnectionSuccess() {	
		Log.v(LOG_TAG, "MainActivity : Connected");
		
		//Show ControlsActivity
		Intent intent = new Intent(MainActivity.this, ControlsActivity.class);
		startActivity(intent);
	}

    /**
     * On Click : button_connect  
     * @param view View
     */
    public void onButtonConnectClick(View view) {
    	showToast("Connection...");
    	String ip = editTextIp.getText().toString();
    	int port = 1985;
    	try{
    	    port = Integer.parseInt(editTextPort.getText().toString());
        }catch (Exception e){
    	    e.printStackTrace();
        }
    	rpiClient.connect(ip, port);
    }

    
}
