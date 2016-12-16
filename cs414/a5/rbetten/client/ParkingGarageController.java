package cs414.a5.rbetten.client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
<<<<<<< HEAD:src/common/ParkingGarageController.java
=======

import cs414.a5.rbetten.common.Ticket;
import cs414.a5.rbetten.server.IParkingGarage;
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java

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
<<<<<<< HEAD:src/common/ParkingGarageController.java
			setLastTicket((ITicket)garage.addCarToGarage());
			
			ITicket stub = (ITicket)UnicastRemoteObject.exportObject(getLastTicket(), 0);
			
			garage.addEntryRecords(stub);
=======
			setLastTicket(garage.addCarToGarage());
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java
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

		ITicket ticket = findTicket(ticketID);

		if (ticket != null && payment != null) {
			try {
<<<<<<< HEAD:src/common/ParkingGarageController.java
				IPayment payStub = (IPayment)UnicastRemoteObject.exportObject(payment, 0);
				garage.addExitRecords(ticket, payStub);
				
=======
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java
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
<<<<<<< HEAD:src/common/ParkingGarageController.java
	public void payForTicket(ITicket ticket) {
		try {
			if (!ticket.getPaymentStatus()) {
				ticket.setPaymentStatus(true);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
=======
	public void payForTicket(Ticket ticket, double amountPaid) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			try {
				garage.addFinancialRecordCash(ticket, amountPaid, ldt);
				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void payForTicket(String userName, String userAddress, String userPhoneNumber) {
		LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
				LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
		LocalDateTime dateOwed = ldt.plusWeeks(2);
		try {
			garage.addFinancialRecordAdmin(userName, userAddress, userPhoneNumber, 50.00, dateOwed);

			// garage.removeCarFromGarage(ticket);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void payForTicket(Ticket ticket, String ccNumber, LocalDateTime expDate, double amountPaid) {
		if (!ticket.getPaymentStatus()) {
			ticket.setPaymentStatus(true);
			LocalDateTime ldt = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),
					LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHour(), 0);
			try {
				garage.addFinancialRecordCredit(ticket, ccNumber, expDate, amountPaid, ldt);

				garage.removeCarFromGarage(ticket);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java
		}

	}

	/**
	 * Attempts to pay for ticket and starts checkout process
	 * 
	 * @param ticketID
	 *            uniqueID of ticket to be found
	 * @return boolean true if car checked out, false otherwise
	 */
<<<<<<< HEAD:src/common/ParkingGarageController.java
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
=======
	public boolean checkTicketPayment(String ticketID) {
		Ticket ticket = findTicket(ticketID);

		if (ticket != null) {
			if (ticket.getPaymentStatus()) {
				return true;
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java
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
<<<<<<< HEAD:src/common/ParkingGarageController.java
	private ITicket findTicket(String ticketID) {
		ArrayList<ITicket> tickets = null;
=======
	public Ticket findTicket(String ticketID) {
		ArrayList<Ticket> tickets = null;
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java
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
<<<<<<< HEAD:src/common/ParkingGarageController.java
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
=======
	public double getAmountDueOnTicket(Ticket ticket) {
		LocalDateTime ldt = LocalDateTime.now();
		LocalDateTime tempDateTime = LocalDateTime.from(ticket.getCheckinTime());
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/client/ParkingGarageController.java
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
