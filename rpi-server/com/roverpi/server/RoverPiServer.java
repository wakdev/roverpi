package com.roverpi.server;

import java.net.*;
import java.io.*;
import java.util.*;
import com.roverpi.controls.*;

public class RoverPiServer {

private RoverControls rpcontrols; 
  
	public void connect(RoverPiServer piServ){
    
		rpcontrols = new RoverControls();
		
		// Initialisation GPIO
		System.out.println("> Initialization process");
		try {
			rpcontrols.initialize();
		}catch (Exception e) { 
			System.out.println("> Initialization error");
			System.out.println("> Exit... Have a nice day !");
			System.exit(0);
		}
		System.out.println("> Initialization success");
	
		// Initialisation Serveur
		System.out.println("> Initialization server");
		try {
		  
			Integer port = new Integer("1985");
			ServerSocket ss = new ServerSocket(port.intValue());
			
			System.out.println("> Initialization server success (port : 1985)");
			
			while (true) {
				new RoverPiThread(ss.accept(),piServ); 
			}
		} catch (Exception e) { 
			System.out.println("> Initialization server error");
			System.out.println("> Exit... Have a nice day !");
			System.exit(0);
		}
		
	}

 
	synchronized public void sendToRover(String message){
		
		switch(message) {
			case "forward":
				System.out.println("> Move forward");
				rpcontrols.moveForward();
			break;
			case "backward":
				System.out.println("> Move backward");
				rpcontrols.moveBackward();
			break;
			case "left":
				System.out.println("> Move Left");
				rpcontrols.moveLeft();
			break;
			case "right":
				System.out.println("> Move Right");
				rpcontrols.moveRight();
			break;
			case "stop":
				System.out.println("> Emergency stop");
				rpcontrols.stopEngine(true);
			break;
			case "light on":
				System.out.println("> Light ON");
				rpcontrols.light(true);
			break;
			case "light off":
				System.out.println("> Light OFF");
				rpcontrols.light(false);
			break;
			case "shutdown":
				System.out.println("> Stop engine");			
				rpcontrols.stopEngine(false);
			break;
			default:
				System.out.println("> "+message+" is not a RoverPi CMD.");
			
		}
		
		
	}

}
