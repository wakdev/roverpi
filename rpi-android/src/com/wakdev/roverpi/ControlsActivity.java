package com.wakdev.roverpi;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * ControlsActivity
 * @package com.wakdev.roverpi
 * @author wakdev.com
 * @version 12.12.16
 */
public class ControlsActivity extends Activity implements OnClickListener{

	public RpiNet rpiClient;
	
	//Buttons
	private Button buttonForward;
	private Button buttonBackward;
	private Button buttonLeft;
	private Button buttonRight;
	private Button buttonStop;
	
	//For stream (http://IP:8585/?action=snapshot)
	private ImageView imageViewStream;
	private Timer myTimer;
	private boolean isRunning;
	
	private RpiStream task;
	
	//Command
	private String rpiCmd;
	
	private static final String LOG_TAG = "RoverPiLog";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controls);
		
		rpiClient = RpiNet.getInstance();
		isRunning = false;
		
		initLayout();
		launchStream();
	}

	
	/**
	 * Initialize Layout
	 */
	public void initLayout () {
		
		buttonForward = (Button)findViewById(R.id.button_forward);
		buttonBackward = (Button)findViewById(R.id.button_backward);
		buttonLeft = (Button)findViewById(R.id.button_left);
		buttonRight = (Button)findViewById(R.id.button_right);
		buttonStop = (Button)findViewById(R.id.button_stop);
		
		buttonForward.setOnClickListener(this);
		buttonBackward.setOnClickListener(this);
		buttonLeft.setOnClickListener(this);
		buttonRight.setOnClickListener(this);
		buttonStop.setOnClickListener(this);

		imageViewStream = (ImageView)findViewById(R.id.imageView_stream);
		
	}
	
	/**
     * Show toast
     * @param toast Toast text
     */
    public void launchStream() {
    	
    	final String streamURL = "http://"+rpiClient.getIP()+":8585/?action=snapshot";
    	
    	Log.v(LOG_TAG, "ControlsActivity : Loading stream " + streamURL);
    	
    	task = new RpiStream(imageViewStream);
        task.execute(streamURL);
    }

    
    /**
     * Back
     */
    @Override
    public void onBackPressed() {
    	
    	Log.v(LOG_TAG, "ControlsActivity : Close connection");
    	
    	task.cancel(true);
    	rpiClient.disconnect();
    	super.onBackPressed();
    	finish();
    } 
    	
    
	/**
	 * onClick
	 * @param View v
	 */
	@Override
	public void onClick(View v) {
	
		rpiCmd = null;
		
		if (isRunning == true) {
			myTimer.cancel();
			rpiClient.sendToRpi("shutdown");
			isRunning = false;
		}
		
		if (v==buttonForward) { rpiCmd = "forward";}
		if (v==buttonBackward) { rpiCmd = "backward";}
		if (v==buttonLeft) { rpiCmd = "left";}
		if (v==buttonRight) { rpiCmd = "right";}
		
		if (rpiCmd != null) {
			
			//Send command
			myTimer = new Timer();
			myTimer.schedule(new TimerTask() {
		        @Override
		        public void run() {
		        	rpiClient.sendToRpi(rpiCmd);
		        }
		    }, 0, 300);
			
			isRunning = true;
			
		}
		
	}
	
}
