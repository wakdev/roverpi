package com.roverpi.server;

import java.net.*;
import java.io.*;


class RoverPiThread implements Runnable {
  
  private Thread _t; 
  private Socket _s; 
  private PrintWriter _out; 
  private BufferedReader _in; 
  private RoverPiServer _RoverPiServer; 
  
	RoverPiThread(Socket s, RoverPiServer RoverPiServer) {
		_RoverPiServer=RoverPiServer; 
		_s=s; 
		try
		{
			_out = new PrintWriter(_s.getOutputStream());
			_in = new BufferedReader(new InputStreamReader(_s.getInputStream()));
			
		}
		catch (IOException e){ }

		_t = new Thread(this); 
		_t.start(); 
	}

  
  public void run() {
  
    String message = ""; 
    System.out.println("Client connected to RoverPi Server");
	
    try {
     
      char charCur[] = new char[1]; 
      while(_in.read(charCur, 0, 1)!=-1) {
        if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r') {
            message += charCur[0]; 
        }else if(!message.equalsIgnoreCase("")) {
			_RoverPiServer.sendToRover(message);
			message = "";
        }
      }
    }
    catch (Exception e){ }
    finally 
    {
      try
      {
      	
        System.out.println("Client disconnected from RoverPi Server");
		_RoverPiServer.sendToRover("shutdown");
        _s.close(); 
      }
      catch (IOException e){ }
    }
  }
}
