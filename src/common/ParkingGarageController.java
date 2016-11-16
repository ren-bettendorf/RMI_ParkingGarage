package common;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

import server.IParkingGarage;
import server.RecordManager;

public class ParkingGarageController {
	
	Ticket lastTicket;
	IParkingGarage garage;
	
	public ParkingGarageController(String url)
	{
		try {
			this.garage = (IParkingGarage) Naming.lookup(url);
			
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
	
	/**
	 * Checks if the ticket hasn't been paid for and if it has pays for the ticket
	 * @param ticket Ticket's payment status to be checked
	 */
	public void payForTicket(Ticket ticket)
	{
		if( !ticket.getPaymentStatus() )
		{
			ticket.setPaymentStatus(true);
		}

	}

	/**
	 * Attempts to pay for ticket and starts checkout process
	 * @param ticketID uniqueID of ticket to be found
	 * @return boolean true if car checked out, false otherwise
	 */
	public boolean attemptCheckoutCar(String ticketID)
	{
		Ticket ticket = findTicket(ticketID);
		if( ticket != null)
		{
			if( ticket.getPaymentStatus() )
			{
				payForTicket( ticket );
			}
			return true;
		}
		return false;
	}

	/**
	 * Searches garage for the ticket
	 * @param ticketID uniqueID to be found
	 * @return t Ticket if found otherwise null
	 */
	private Ticket findTicket(String ticketID)
	{
		HashSet<Ticket> tickets = null;
		try {
			tickets = garage.getTickets();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Ticket t = null;
		for(Ticket ticket : tickets)
		{
			if( ticket.getUniqueID().equals(ticketID) )
			{
				t = ticket;
				break;
			}
		}

		return t;
	}

	/**
	 * Removes the car from the garage system while recording exit and payment
	 * @param ticketID uniqueID to be found
	 * @param payment payment the car used
	 */
	public void removeCarFromGarage(String ticketID, Payment payment)
	{
		RecordManager records = null;
		try {
			records = garage.getRecordManager();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Ticket t = findTicket(ticketID);		
		records.addOccupationRecord(payment.getDateOfPayment(), CarStatus.LEAVE);
		records.addFinancialRecord(t, payment);
		try {
			garage.removeCarFromGarage(t);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the amount needed to pay for parking
	 * @param ticketID uniqueID to be found
	 * @return double amount due for parking
	 */
	public double amountDueOnTicket(String ticketID) 
	{
		Ticket t = findTicket(ticketID);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDateTime tempDateTime = LocalDateTime.from( t.getCheckinTime() );
		double amountDue = tempDateTime.until( ldt, ChronoUnit.HOURS) + 1.00;


		return amountDue;
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
