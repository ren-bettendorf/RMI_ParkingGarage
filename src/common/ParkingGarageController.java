package common;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ParkingGarageController extends java.rmi.server.UnicastRemoteObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2763667860187372667L;
	ITicket lastTicket;
	IParkingGarage garage;

	public ParkingGarageController(String url) throws RemoteException {
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
			setLastTicket((ITicket)garage.addCarToGarage());
			
			ITicket stub = (ITicket)UnicastRemoteObject.exportObject(getLastTicket(), 0);
			
			garage.addEntryRecords(stub);
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

		ITicket ticket = findTicket(ticketID);

		if (ticket != null && payment != null) {
			try {
				IPayment payStub = (IPayment)UnicastRemoteObject.exportObject(payment, 0);
				garage.addExitRecords(ticket, payStub);
				
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
	public void payForTicket(ITicket ticket) {
		try {
			if (!ticket.getPaymentStatus()) {
				ticket.setPaymentStatus(true);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		ITicket ticket = findTicket(ticketID);
		if (ticket != null) {
			try {
				if (ticket.getPaymentStatus()) {
					payForTicket(ticket);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	private ITicket findTicket(String ticketID) {
		ArrayList<ITicket> tickets = null;
		try {
			tickets = garage.getTickets();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Ticket t = null;
		for (ITicket ticket : tickets) {
			try {
				if (ticket.getUniqueID().equals(ticketID)) {
					return ticket;
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		ITicket t = findTicket(ticketID);
		LocalDateTime ldt = LocalDateTime.now();
		LocalDateTime tempDateTime = null;
		try {
			tempDateTime = LocalDateTime.from(t.getCheckinTime());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double amountDue = tempDateTime.until(ldt, ChronoUnit.HOURS) + 1.00;

		return amountDue;
	}

	public ITicket getLastTicket() {
		return lastTicket;
	}

	public void setLastTicket(ITicket ticket) {
		lastTicket = ticket;
	}
}
