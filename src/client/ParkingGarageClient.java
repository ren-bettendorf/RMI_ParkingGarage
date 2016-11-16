package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Scanner;

import common.EntryGate;
import common.Ticket;
import server.IParkingGarage;

public class ParkingGarageClient {
	static Scanner input = new Scanner(System.in);
	static int maxOccupancy;
	static IParkingGarage garage;
	static boolean runApp = true;
	
	public static void main(String[] args) {
		garage = null;

		try {
			garage = (IParkingGarage) Naming.lookup("rmi://" + args[0] + ":" + args[1] + "/ParkingGarageServer");
			
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
			String in = "y";
			while(in.equals("y"))
			{
				System.out.println("Car enters?");
				in = input.nextLine();
				if(in.equals("y"))
				{
					System.out.println("Read: " + in);
					Ticket t = garage.addCarToGarage();
					if(t != null)
					{
						System.out.println("Ticket number: " + t.getUniqueID());
						System.out.println("Garage Status: " + garage.getCarOccupancy() + " : " + garage.getMaxCarOccupancy());
					}else
					{
						System.out.println("Sorry but garage is full");
					}
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
