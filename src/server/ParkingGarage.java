package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.CarStatus;
import common.IPayment;
import common.Ticket;

public class ParkingGarage extends java.rmi.server.UnicastRemoteObject implements IParkingGarage, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Ticket> ticketsInGarage = new ArrayList<Ticket>();
	private IRecordManager recordManager;
	private int maxOccupancy;

	public ParkingGarage(int maxOccu) throws RemoteException {
		super();
		recordManager = new RecordManager();
		this.maxOccupancy = maxOccu;
	}

	public boolean checkGarageSpace() throws RemoteException {
		if (ticketsInGarage.size() == maxOccupancy) {
			return true;
		}
		return false;
	}

	public Ticket addCarToGarage() throws RemoteException {
		Ticket t = null;
		try {
			if (!checkGarageSpace()) {
				t = new Ticket(LocalDateTime.now());

				ticketsInGarage.add(t);
				addEntryRecords(t);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}

	public void removeCarFromGarage(Ticket ticket) throws RemoteException {
		ticketsInGarage.remove(ticket);
	}

	private void addEntryRecords(Ticket ticket) throws RemoteException {
		recordManager.addOccupationRecord(ticket.getCheckinTime(), CarStatus.ENTER);
	}

	public void addFinancialRecordCash(Ticket ticket, double amountPaid, LocalDateTime ldt) throws RemoteException {
		IPayment payment = recordManager.createCashPayment(amountPaid, ldt);
		recordManager.addFinancialRecord(ticket, payment);
		ticket.setPaymentStatus(true);
	}

	public void addFinancialRecordCredit(Ticket ticket, String cardNumber, LocalDateTime expDate, double amountPaid, LocalDateTime dateOfPayment) throws RemoteException {
		IPayment payment = recordManager.createCreditPayment(cardNumber, expDate, amountPaid, dateOfPayment);
		recordManager.addFinancialRecord(ticket, payment);
	}
	
	public void addFinancialRecordAdmin(String userAddress, String userName, String userPhoneNumber, double amountOwed, LocalDateTime dateOwed) throws RemoteException {
		LocalDateTime today = LocalDateTime.now();
		IPayment payment = recordManager.createAdminPayment(userAddress, userName, userPhoneNumber, amountOwed, dateOwed);
		//recordManager.addFinancialRecord(payment);
	}
	
	public void addExitRecords(Ticket ticket, double amountPaid, LocalDateTime ldt) throws RemoteException {
		recordManager.addOccupationRecord(ldt, CarStatus.LEAVE);
	}

	public String runReports(LocalDateTime begin, LocalDateTime end) throws RemoteException {
		StringBuilder sb = new StringBuilder();

		sb.append(runOccupationReports(begin, end));
		sb.append("\n");
		sb.append(runFinancialReports(begin, end));

		return sb.toString();
	}

	/**
	 * Creates the Occupation Report for a period of dates
	 * 
	 * @param begin
	 *            beginning date
	 * @param end
	 *            ending date
	 * @return Occupation Report
	 */
	public String runOccupationReports(LocalDateTime begin, LocalDateTime end) throws RemoteException {
		return recordManager.getOccupationRecords(begin, end);
	}

	/**
	 * Creates the Financial Report for a period of dates
	 * 
	 * @param begin
	 *            beginning date
	 * @param end
	 *            ending date
	 * @return Financial Report
	 */
	public String runFinancialReports(LocalDateTime begin, LocalDateTime end) throws RemoteException {
		return recordManager.getFinancialRecords(begin, end);
	}

	public IRecordManager getRecordManager() throws RemoteException {
		return recordManager;
	}

	public int getMaxCarOccupancy() throws RemoteException {
		return maxOccupancy;
	}

	public int getCarOccupancy() throws RemoteException {
		return ticketsInGarage.size();
	}

	public ArrayList<Ticket> getTickets() throws RemoteException {
		return ticketsInGarage;
	}

}
