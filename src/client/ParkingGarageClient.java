package client;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Scanner;

import common.CashPayment;
import common.IPayment;
import common.ParkingGarageController;

public class ParkingGarageClient {
	static Scanner input = new Scanner(System.in);
	static ParkingGarageController controller;
	
	public static void main(String[] args) {
		try {
			controller = new ParkingGarageController("rmi://"+args[0] + ":" + args[1]+ "/ParkingGarageServer");
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(controller.addCarToGarage())
		{
			try {
				System.out.println("Ticket: " + controller.getLastTicket().getUniqueID());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Garage Status: " + controller.getGarageOccupancyStatus());
		}else
		{
			System.out.println("FULL GARAGE");
		}
		
		try {
			LocalDateTime today = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			IPayment payment = new CashPayment(10.00, today);
			controller.removeCarFromGarage(controller.getLastTicket().getUniqueID(), payment);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Garage Status: " + controller.getGarageOccupancyStatus());


		System.out.println("\n\nReports:\n" + controller.runReports(LocalDateTime.of(2016, 9, 1, 0, 0), LocalDateTime.of(2017, 9, 1, 0, 0)));
		System.out.println("End of Reports");
	}
}
