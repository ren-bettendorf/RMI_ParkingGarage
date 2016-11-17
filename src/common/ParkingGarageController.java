package common;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import server.IParkingGarage;
import server.IRecordManager;

public class ParkingGarageController {

	Ticket lastTicket;
	IParkingGarage garage;

	public ParkingGarageController(String url) {
		try {
			this.garage = (IParkingGarage) Naming.lookup(url);

		} catch (RemoteException re) {
			System.out.println("RemoteException");
			System.out.println(re);
			System.exit(-1);

		} catch (MalformedURLException murle) {
			System.out.println("MalformedURLException");
			System.out.println(murle);
			System.exit(-1);

		} catch (NotBoundException nbe) {
			System.out.println("NotBoundException");
			System.out.println(nbe);
			System.exit(-1);

		}
	}

	public boolean addCarToGarage() {
		try {
			setLastTicket(garage.addCarToGarage());

			garage.addEntryRecords(getLastTicket());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		boolean ticketStatus = true;

		if (lastTicket == null) {
			ticketStatus = false;
		}

		return ticketStatus;
	}
	
	/**
	 * Removes the car from the garage system while recording exit and payment
	 * 
	 * @param ticketID
	 *            uniqueID to be found
	 * @param payment
	 *            payment the car used
	 */
	public boolean removeCarFromGarage(String ticketID, IPayment payment) {
		boolean retStatus = true;
		IRecordManager records = null;

		Ticket ticket = findTicket(ticketID);

		if (ticket != null) {
			try {
				garage.addExitRecords(ticket, payment);
				
				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else
		{
			retStatus = false;
		}

		return retStatus;
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
	
	public String runOccuReports(LocalDateTime begin, LocalDateTime end)
	{
		String sb = null;
		try {
			sb = garage.runOccupationReports(begin, end);
		} catch (RemoteException re)
		{
			re.printStackTrace();
		}
		return sb;
	}

	public String runReports(LocalDateTime begin, LocalDateTime end) {
		String sb = null;
		try {
			sb = garage.runReports(begin, end);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Checks if the ticket hasn't been paid for and if it has pays for the
	 * ticket
	 * 
	 * @param ticket
	 *            Ticket's payment status to be checked
	 */
	public void payForTicket(Ticket ticket) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
		}

	}

	/**
	 * Attempts to pay for ticket and starts checkout process
	 * 
	 * @param ticketID
	 *            uniqueID of ticket to be found
	 * @return boolean true if car checked out, false otherwise
	 */
	public boolean attemptCheckoutCar(String ticketID) {
		Ticket ticket = findTicket(ticketID);
		if (ticket != null) {
			if (ticket.getPaymentStatus()) {
				payForTicket(ticket);
			}
			return true;
		}
		return false;
	}

	/**
	 * Searches garage for the ticket
	 * 
	 * @param ticketID
	 *            uniqueID to be found
	 * @return t Ticket if found otherwise null
	 */
	private Ticket findTicket(String ticketID) {
		ArrayList<Ticket> tickets = null;
		try {
			tickets = garage.getTickets();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Ticket t = null;
		for (Ticket ticket : tickets) {
			if (ticket.getUniqueID().equals(ticketID)) {
				return ticket;
			}
		}

		return t;
	}

	

	/**
	 * Calculates the amount needed to pay for parking
	 * 
	 * @param ticketID
	 *            uniqueID to be found
	 * @return double amount due for parking
	 */
	public double amountDueOnTicket(String ticketID) {
		Ticket t = findTicket(ticketID);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDateTime tempDateTime = LocalDateTime.from(t.getCheckinTime());
		double amountDue = tempDateTime.until(ldt, ChronoUnit.HOURS) + 1.00;

		return amountDue;
	}

	public Ticket getLastTicket() {
		return lastTicket;
	}

	public void setLastTicket(Ticket ticket) {
		lastTicket = ticket;
	}
}
