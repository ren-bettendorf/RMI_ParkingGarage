package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import common.Ticket;
import server.IParkingGarage;

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
	public boolean removeCarFromGarage(String ticketID, double amountPaid, LocalDateTime ldt) {
		boolean retStatus = true;

		Ticket ticket = findTicket(ticketID);

		if (ticket != null) {
			try {
				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			retStatus = false;
		}

		return retStatus;
	}

	public boolean checkGarageSpace() {
		boolean status = true;
		try {
			status = garage.checkGarageSpace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
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

	public String runOccuReports(LocalDateTime begin, LocalDateTime end) {
		String sb = null;
		try {
			sb = garage.runOccupationReports(begin, end);
		} catch (RemoteException re) {
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
	public void payForTicketCash(Ticket ticket, double amountPaid) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			try {
				garage.addFinancialRecordCash(ticket, amountPaid, ldt);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void payForTicketAdmin(String userName, String userAddress, String userPhoneNumber) {
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			LocalDateTime dateOwed = ldt.plusWeeks(2);
			try {
				garage.addFinancialRecordAdmin(userName, userAddress, userPhoneNumber, 50.00, dateOwed);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}
	
	public void payForTicketCredit(Ticket ticket, String ccNumber, LocalDateTime expDate, double amountPaid) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			try {
				garage.addFinancialRecordCredit(ticket, ccNumber, expDate, amountPaid, ldt);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Attempts to pay for ticket and starts checkout process
	 * 
	 * @param ticketID
	 *            uniqueID of ticket to be found
	 * @return boolean true if car checked out, false otherwise
	 */
	public boolean checkTicketPayment(String ticketID) {
		Ticket ticket = findTicket(ticketID);
		
		if (ticket != null) {
			if (ticket.getPaymentStatus()) {
				return true;
			}
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
	public Ticket findTicket(String ticketID) {
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
	public double getAmountDueOnTicket(Ticket ticket) {
		LocalDateTime ldt = LocalDateTime.now();
		LocalDateTime tempDateTime = LocalDateTime.from(ticket.getCheckinTime());
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
