package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import common.Ticket;
import server.IParkingGarage;
import server.ParkingGarage;

public class ParkingGarageClient {
	static Scanner input = new Scanner(System.in);
	static int maxOccupancy;
	static IParkingGarage garage;
	static boolean runApp = true;

	public static void main(String[] args) {
		garage = null;

		try {
			garage = (IParkingGarage) Naming.lookup("rmi://"+ args[0] + ":" + args[1] +"/ParkingGarageServer");
			
		} catch (RemoteException re) {
			System.out.println("RemoteException");
			System.out.println(re);
			System.exit(-1);

		} catch (MalformedURLException murle)
		{
			System.out.println("MalformedURLException");
			System.out.println(murle);
			System.exit(-1);

		}catch (NotBoundException nbe) {
			System.out.println("NotBoundException");
			System.out.println(nbe);
			System.exit(-1);

		}
		
		
		try {

			System.out.println("Car Full: "+ garage.checkGarageSpace());
			ArrayList<Ticket> tickets = new ArrayList<Ticket>();
			
			for (int index = 0; index < 1000; index++) {
				for (int i = 0; i < 10; i++) {
					System.out.println("Adding car...");
					Ticket ticket = new Ticket(LocalDateTime.now());
					tickets.add(ticket);
					garage.addCarToGarage(ticket);
				}
				System.out.println("Car Full: " + garage.checkGarageSpace());
				for (int i = 0; i < 10; i++) {
					System.out.println("Removing car...");
					Ticket ticket = tickets.get(i);
					garage.removeCarFromGarage(ticket);
				} 
			}
			System.out.println("Car Full: "+ garage.checkGarageSpace());
			
		}catch (RemoteException re) {
			System.out.println("RemoteException"); 
			System.out.println(re);
			System.exit(-1);

		}
	}
}
