package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class ParkingGarageServer {
	
	private String url;

	public ParkingGarageServer(String url, int occupationSize) throws RemoteException {
		this.url = url;
		try {
			System.setProperty("java.rmi.server.hostname","192.168.0.13");
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
		String url = new String("rmi://192.168.0.13:2500/ParkingGarageServer");
		try {
			new ParkingGarageServer(url, Integer.parseInt(args[2]));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
