package client;

import java.util.Scanner;

import common.ParkingGarageController;

public class ParkingGarageClient {
	static Scanner input = new Scanner(System.in);
	static ParkingGarageController controller;
	
	public static void main(String[] args) {
		controller = new ParkingGarageController("rmi://" + args[0] + ":" + args[1] + "/ParkingGarageServer");
	
		if(controller.addCarToGarage())
		{
			System.out.println("Ticket: " + controller.getLastTicket().getUniqueID());
			System.out.println("Garage Status: " + controller.getGarageOccupancyStatus());
		}else
		{
			System.out.println("FULL GARAGE");
		}
	}
}
