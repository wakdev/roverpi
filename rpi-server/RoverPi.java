
import com.roverpi.server.*;

public class RoverPi {

	public static void main(String[] args) throws InterruptedException{

		System.out.println("---------------");
		System.out.println("--- RoverPi ---");
		System.out.println("---------------");
		
		
		RoverPiServer piServer = new RoverPiServer();
		piServer.connect(piServer);
		
		
	}

}
