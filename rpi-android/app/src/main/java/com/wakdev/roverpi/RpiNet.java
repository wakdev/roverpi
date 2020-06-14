package com.wakdev.roverpi;

// Import
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
 * @author wakdev.com
 * @version 1.0
 */
interface RpiListener extends EventListener {
    void onConnectionSuccess();
    void onConnectionError();
}

/**
 * RPI Communication class
 * @author wakdev.com
 * @version 1.0
 */
public class RpiNet implements Runnable{

	// Log tag
	private static final String LOG_TAG = "RoverPiLog";

	// Vars
	private static RpiNet instance; // Singleton
	private Socket rpiSocket;
	private PrintWriter rpiOut;
	private RpiListener rpiListener = null;
	private String rpiIP;
	private int rpiPort;
	
	/**
	 * Constructor
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
	 * setListener
	 * @param listener Listener
	 */
	public void setListener(RpiListener listener) {
        this.rpiListener = listener;
    }
	
	/**
	 * Connect
	 * @param IP Ip
	 * @param Port Port
	 */
	public void connect(String IP,int Port)  {
		rpiIP = IP;
		rpiPort = Port;
		rpiSocket = new Socket();
		Thread currentThread = new Thread(this);
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

	/**
	 * {@inheritDoc}
	 */
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

