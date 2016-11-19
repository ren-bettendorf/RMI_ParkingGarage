package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import common.AdminPayment;
import common.CarStatus;
import common.CashPayment;
import common.CreditPayment;
import common.IPayment;
import common.Ticket;

public class RecordManager implements Serializable, IRecordManager {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9092826944053480194L;
	private ArrayList<FinancialRecord> financialRecords = new ArrayList<FinancialRecord>();
	private ArrayList<OccupationRecord> occupationRecords = new ArrayList<OccupationRecord>();
	private ArrayList<IPayment> adminRecords = new ArrayList<IPayment>();

	public RecordManager() throws RemoteException {
		super();
	}

	/**
	 * Add Occupation Report to RecordManager
	 * 
	 * @param ldt
	 *            time to add to report
	 * @param status
	 *            whether car was entering or leaving
	 */
	public void addOccupationRecord(LocalDateTime ldt, CarStatus status) throws RemoteException {
		OccupationRecord record = new OccupationRecord(ldt, status);
		occupationRecords.add(record);
	}

	/**
	 * Queries OccupationRecords over a time frame
	 * 
	 * @param begin
	 *            starting time frame
	 * @param end
	 *            ending time frame
	 * @return Occupation Record
	 */
	public String getOccupationRecords(LocalDateTime begin, LocalDateTime end) throws RemoteException {
		String returnedTotals = "";
		HashMap<LocalDateTime, Integer> carsVisited = new HashMap<LocalDateTime, Integer>();
		HashMap<LocalDateTime, Integer> carsLeft = new HashMap<LocalDateTime, Integer>();

		// Check to make sure that records exist
		if (occupationRecords.size() > 0) {

			int numVisited = 0;
			int numLeft = 0;

			// Check to all records to see if they fall in time frame
			for (OccupationRecord record : occupationRecords) {
				// Check to see if record is not in query time frame
				LocalDateTime ldt = record.getTime();
				if (ldt.isBefore(begin) || ldt.isAfter(end)) {
					continue;
				}

				CarStatus status = record.getCarStatus();
				if (status == CarStatus.ENTER) {
					if (carsVisited.containsKey(ldt)) {
						numVisited = carsVisited.get(ldt) + 1;
					} else {
						numVisited = 1;
					}

					carsVisited.put(ldt, numVisited);
				} else {
					if (carsLeft.containsKey(ldt)) {
						numLeft = carsLeft.get(ldt) + 1;
					} else {
						numLeft = 1;
					}

					carsLeft.put(ldt, numLeft);
				}
			}
		}

		// Change HashMaps to String
		returnedTotals = changeOccupationToLines(carsVisited, carsLeft);
		return returnedTotals;
	}

	/**
	 * Add Financial Report to RecordManager
	 * 
	 * @param ticket
	 *            Ticket paid for
	 * @param payment
	 *            Payment used to pay for ticket
	 */
	public void addFinancialRecord(Ticket ticket, IPayment payment) throws RemoteException {
		FinancialRecord record = new FinancialRecord(ticket, payment);
		financialRecords.add(record);
	}
	
	public void addAdminRecord(IPayment payment) throws RemoteException
	{
		adminRecords.add(payment);
	}
	
	public int getAdminRecordSize() throws RemoteException
	{
		return adminRecords.size();
	}

	/**
	 * Queries Financial Records over a time frame
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public String getFinancialRecords(LocalDateTime begin, LocalDateTime end) throws RemoteException {
		String returnedTotals;
		HashMap<LocalDateTime, Double> dailyTotals = new HashMap<LocalDateTime, Double>();

		// Check to make sure that records exist
		if (financialRecords.size() > 0) {
			for (FinancialRecord record : financialRecords) {
				// Check to see if record is not in query time frame
				LocalDateTime recordDate = record.getRecordDate();
				if (recordDate == null) {
					continue;
				}
				if (recordDate.isBefore(begin) || recordDate.isAfter(end)) {
					continue;
				}
				double recordPayment = 0;
				try {
					recordPayment = record.getPayment().getAmountPaid();
				} catch (RemoteException re) {
					System.out.println("Trouble: " + re);
				}
				// Check to see if we have a record of this day already and adds
				// to otherwise creates a new entry
				if (dailyTotals.containsKey(recordDate)) {
					double runningTotals = dailyTotals.get(recordDate) + recordPayment;
					dailyTotals.replace(recordDate, runningTotals);
				} else {
					dailyTotals.put(recordDate, recordPayment);
				}
			}
		}

		// Change HashMaps to String
		returnedTotals = changeFinancialToLines(dailyTotals);
		return returnedTotals;
	}

	/**
	 * Converts HashMaps to Strings for display
	 * 
	 * @param entered
	 * @param left
	 * @return
	 */
	private String changeOccupationToLines(HashMap<LocalDateTime, Integer> entered,
			HashMap<LocalDateTime, Integer> left) throws RemoteException {
		StringBuilder ret = new StringBuilder();
		ret.append("Cars Entered Garage: \nTimestamp\t\tTotal Entered Garage\n");
		for (LocalDateTime day : entered.keySet()) {
			ret.append(day + ", \t" + entered.get(day) + "\n");
		}
		ret.append("Cars Left Garage: \nTimestamp\t\tTotal Left Garage\n");
		for (LocalDateTime day : left.keySet()) {
			ret.append(day + ", \t" + left.get(day) + "\n");
		}
		ret.append("\n\n");
		return ret.toString();
	}

	/**
	 * Converts HashMaps to Strings for display
	 * 
	 * @param dailyTotals
	 * @return
	 */
	private String changeFinancialToLines(HashMap<LocalDateTime, Double> dailyTotals) throws RemoteException {
		StringBuilder ret = new StringBuilder();
		ret.append("Financial Records: \n\nDay\tTotal Made\n");
		for (LocalDateTime day : dailyTotals.keySet()) {
			ret.append(day + ", \t\t" + dailyTotals.get(day) + "\n");
		}
		ret.append("");
		return ret.toString();
	}

	public IPayment createCashPayment(double amountPaid, LocalDateTime ldt) throws RemoteException {
		IPayment payment = null;
		try {
			payment = new CashPayment(amountPaid, ldt);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return payment;
	}

	public IPayment createCreditPayment(String cardNumber, LocalDateTime expDate, double amountPaid,
			LocalDateTime dateOfPayment) throws RemoteException {
		IPayment payment = null;
		try {
			payment = new CreditPayment(cardNumber, expDate, amountPaid, dateOfPayment);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return payment;
	}

	public IPayment createAdminPayment(String userAddress, String userName, String userPhoneNumber, double amountOwed,
			LocalDateTime dateOwed) throws RemoteException {
		IPayment payment = null;
		try {
			payment = new AdminPayment(userAddress, userName, userPhoneNumber, amountOwed, dateOwed);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return payment;
	}
}