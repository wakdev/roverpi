package com.roverpi.controls;

//RaspBerry Pi - GPIO
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

//Timer
import java.util.Timer;
import java.util.TimerTask;

//import com.roverpi.controls.RoverTask;

public class RoverControls {

	private Timer timer;
	public RoverTask rpTask;
	
	private GpioController gpio;
	private GpioPinDigitalOutput pin0;
	private GpioPinDigitalOutput pin1;
	private GpioPinDigitalOutput pin2;
	private GpioPinDigitalOutput pin3;
	private GpioPinDigitalOutput pin4;

	//Contructor
	public RoverControls() {
		gpio = GpioFactory.getInstance();
	}
	
	public void initialize(){

		this.initializeGpio();
		this.initializeTimer();
	}
	
	/**
	*
	*/
	public void moveForward(){
		pin0.high();
		pin1.low();
		pin2.low();
		pin3.high();
		
		startDelayES();
		
	}
	
	/**
	*
	*/
	public void moveBackward(){
		pin0.low();
		pin1.high();
		pin2.high();
		pin3.low();
		
		startDelayES();
	}
	
	public void moveRight(){
		pin0.low();
		pin1.high();
		pin2.low();
		pin3.high();
		
		startDelayES();
	}
	
	public void moveLeft(){
		pin0.high();
		pin1.low();
		pin2.high();
		pin3.low();
		
		startDelayES();
	}
	
	/**
	*
	*/
	public void stopEngine(boolean value){
		
		if (value == true) {
			 pin0.high();
			 pin1.high();
			 pin2.high();
			 pin3.high();
		}else{
			 pin0.low();
			 pin1.low();
			 pin2.low();
			 pin3.low();
		}
	
		stopDelayES();
	}
	
	public void light(boolean lightstate) {
		if (lightstate == true) {
			pin4.high();
		}else{
			pin4.low();
		}
	}
	
	/**
	*
	*/
	private void initializeGpio() {
		
		pin0 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_00, "pin0", PinState.LOW);
		pin1 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_01, "pin1", PinState.LOW);
		pin2 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_02, "pin2", PinState.LOW);
		pin3 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_03, "pin3", PinState.LOW);
		
		pin4 = gpio.provisionDigitalOuputPin(RaspiPin.GPIO_04, "pin4", PinState.LOW);
		
	}
	
	
	private void initializeTimer(){
		timer = new Timer();
		rpTask = new RoverTask(this);
		timer.schedule(rpTask, 0, 1000);
	}
	
	private void startDelayES() {
		rpTask.setCounter(3);
	}
	
	private void stopDelayES() {
		rpTask.setCounter(0);
	}
}









