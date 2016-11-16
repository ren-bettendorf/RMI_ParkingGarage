package common;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

import server.IParkingGarage;

public class ParkingGarageController {
	
	Ticket lastTicket;
	IParkingGarage garage;
	public ParkingGarageController(String url)
	{
		try {
			garage = (IParkingGarage) Naming.lookup(url);
			
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
	}
	
	public boolean addCarToGarage()
	{
		try {
			setLastTicket(garage.addCarToGarage());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		boolean ticketStatus = true;
		
		if(lastTicket == null)
		{
			ticketStatus = false;
		}
		
		
		return ticketStatus;
	}
	
	public void removeCarFromGarage(String ticketID)
	{
		
	}

	public String getGarageOccupancyStatus() {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(garage.getCarOccupancy() + " : " + garage.getMaxCarOccupancy());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public String runReports(LocalDateTime begin, LocalDateTime end)
	{
		LocalDateTime beginLDT = LocalDateTime.of(begin.getYear(), begin.getMonth(), begin.getDayOfMonth(), 0, 0);

		LocalDateTime endLDT = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 0, 0);
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(garage.runOccupationReports(beginLDT, endLDT));
			sb.append("\n");
			sb.append(garage.runFinancialReports(beginLDT, endLDT));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public Ticket getLastTicket()
	{
		return lastTicket;
	}
	
	public void setLastTicket(Ticket ticket)
	{
		lastTicket = ticket;
	}
}
