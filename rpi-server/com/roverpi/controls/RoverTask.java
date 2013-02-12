package com.roverpi.controls;

import java.util.Timer;
import java.util.TimerTask;

class RoverTask extends TimerTask {

	public RoverControls rpControls;
	public int counter;

	public RoverTask(RoverControls rpC) {
		rpControls = rpC;
		counter = 0;
	}

	public void run() {
	
		if (counter > 0) { 
			
			counter--;
			
			if (counter == 0) {
				System.out.println("Emergency STOP !");
				rpControls.stopEngine(false);
			}
			
		}else{
			//System.out.println("Stanby");
		}
	}
	
	public void setCounter(int value){
		counter = value;
	}
 
}