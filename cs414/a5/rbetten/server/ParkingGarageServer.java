package cs414.a5.rbetten.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import common.IParkingGarage;

public class ParkingGarageServer {
	
	private String url;

	public ParkingGarageServer(String url, int occupationSize) throws RemoteException {
		this.url = url;
		try {
<<<<<<< HEAD:src/server/ParkingGarageServer.java
			LocateRegistry.createRegistry(2502);
			//System.setProperty("java.rmi.server.hostname","192.168.0.13");
=======
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/server/ParkingGarageServer.java
			IParkingGarage garage = new ParkingGarage(occupationSize);
			Naming.rebind(url, garage);
			System.out.println("Parking Garage server running...");
		} catch (RemoteException re) {
			System.out.println("Trouble: " + re);
		} catch (MalformedURLException murle) {
			System.out.println("Trouble: " + murle);
		}
	}

	// run the program using
	// java CalculatorServer <host> <port>

	public static void main(String args[]) {
<<<<<<< HEAD:src/server/ParkingGarageServer.java
		String url = new String("rmi://"+args[0] + ":" + args[1]+ "/ParkingGarageServer");
=======
		String url = new String("rmi://"+ args[0] + ":" + args[1] + "/ParkingGarageServer");
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/server/ParkingGarageServer.java
		try {
			new ParkingGarageServer(url, Integer.parseInt(args[2]));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
