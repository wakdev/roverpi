package com.wakdev.roverpi;

// Import
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ControlsActivity
 * @author wakdev.com
 * @version 1.0
 */
public class ControlsActivity extends Activity implements OnClickListener{

	// Log tag
	private static final String LOG_TAG = "RoverPiLog";

	// Buttons
	private Button buttonForward;
	private Button buttonBackward;
	private Button buttonLeft;
	private Button buttonRight;
	private Button buttonStop;
	
	// For stream (http://IP:8585/?action=snapshot)
	private ImageView imageViewStream;
	private Timer timer;
	private boolean isRunning;
	private RpiStream task;
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	// Rpi
	private RpiNet rpiClient;
	private String rpiCmd;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controls);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Force orientation
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
     * Launch stream
     */
    public void launchStream() {
    	String streamURL = "http://"+rpiClient.getIP()+":8585/?action=snapshot";
    	Log.v(LOG_TAG, "ControlsActivity : Loading stream " + streamURL);
    	task = new RpiStream(imageViewStream);
        task.execute(streamURL);
    }

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View view) {
		rpiCmd = null;
		if (isRunning) {
			timer.cancel();
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					rpiClient.sendToRpi("shutdown");
				}
			});
			isRunning = false;
		}
		if (view == buttonForward) {
			rpiCmd = "forward";
		}else if (view == buttonBackward) {
			rpiCmd = "backward";
		}else if (view == buttonLeft) {
			rpiCmd = "left";
		}else if (view == buttonRight) {
			rpiCmd = "right";
		}

		// Send command
		if (rpiCmd != null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					rpiClient.sendToRpi(rpiCmd);
				}
			}, 0, 300);
			isRunning = true;
		}
	}
	
}