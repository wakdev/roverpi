package com.wakdev.roverpi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.EventListener;
import android.util.Log;

/**
 * Custom RPI Listener
 * @package com.wakdev.roverpi
 * @author wakdev.com
 * @version 12.12.16
 */
interface RpiListener extends EventListener {
    void onConnectionSuccess();
    void onConnectionError();
}

/**
 * RPI Communication class
 * @package com.wakdev.roverpi
 * @author wakdev.com
 * @version 12.12.16
 */
public class RpiNet implements Runnable{

	private static RpiNet instance; //Singleton
	
	private Socket rpiSocket;
	private PrintWriter rpiOut;
	private Thread currentThread;
	private RpiListener rpiListener = null;
	
	private String rpiIP;
	private int rpiPort;
	
	private static final String LOG_TAG = "RoverPiLog";
	
	/**
	 * Contructor
	 */
	private RpiNet(){}
	
	/**
	 * getInstance
	 * @return instance RpiNet
	 */
	public static synchronized RpiNet getInstance(){
	   if(instance==null){
	      instance=new RpiNet();
	   }
	    return instance;
	}
	
	/**
	 * setListenet
	 * @param listener 
	 */
	public void setListener(RpiListener listener) {
        this.rpiListener = listener;
    }
	
	/**
	 * Connect
	 * @param IP
	 * @param Port
	 */
	public void connect(String IP,int Port)  {
		
		rpiIP = IP;
		rpiPort = Port;
		
		rpiSocket = new Socket();
		
		currentThread = new Thread(this);
        currentThread.start();
        
	}
	
	/**
	 * Disconnect Socket
	 */
	public void disconnect(){
		try {
			rpiOut.close();
			rpiSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * SendToRoverpi
	 * @param msg Message
	 */
	public void sendToRpi(String msg) {
		
		if (rpiOut != null && !rpiOut.checkError()) {
			rpiOut.println(msg);
			rpiOut.flush();
        }
        
	}
	
	

	@Override
	public void run() {
		
		Log.v(LOG_TAG, "RpiNet : Connexion...");
		
		try { 		
			rpiSocket.connect(new InetSocketAddress(rpiIP,rpiPort), 5000);
			rpiOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(rpiSocket.getOutputStream())), true);
			rpiListener.onConnectionSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			rpiListener.onConnectionError();
		}
		
	}
	
	/**
	 * Get IP
	 * @return rpiIP
	 */
	public String getIP() {
		return this.rpiIP;
	}
	
	
	/**
	 * Get Port
	 * @return rpiPort
	 */
	public int getPort() {
		return this.rpiPort;
	}
	
}

