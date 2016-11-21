package cs414.a5.rbetten.server;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import cs414.a5.rbetten.common.CarStatus;
import cs414.a5.rbetten.common.IPayment;
import cs414.a5.rbetten.common.Ticket;

public interface IRecordManager {
	/**
	 * Add Occupation Report to RecordManager
	 * 
	 * @param ldt
	 *            time to add to report
	 * @param status
	 *            whether car was entering or leaving
	 */
	public void addOccupationRecord(LocalDateTime ldt, CarStatus status) throws RemoteException;

	/**
	 * Queries OccupationRecords over a time frame
	 * 
	 * @param begin
	 *            starting time frame
	 * @param end
	 *            ending time frame
	 * @return Occupation Record
	 */
	public String getOccupationRecords(LocalDateTime begin, LocalDateTime end) throws RemoteException;

	/**
	 * Add Financial Report to RecordManager
	 * 
	 * @param ticket
	 *            Ticket paid for
	 * @param payment
	 *            Payment used to pay for ticket
	 */
	public void addFinancialRecord(Ticket ticket, IPayment payment) throws RemoteException;

	/**
	 * Queries Financial Records over a time frame
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public String getFinancialRecords(LocalDateTime begin, LocalDateTime end) throws RemoteException;

	public IPayment createCashPayment(double amountPaid, LocalDateTime ldt) throws RemoteException;

	public IPayment createCreditPayment(String cardNumber, LocalDateTime expDate, double amountPaid,
			LocalDateTime dateOfPayment) throws RemoteException;

	public IPayment createAdminPayment(String userAddress, String userName, String userPhoneNumber, double amountOwed,
			LocalDateTime dateOwed) throws RemoteException;

	public void addAdminRecord(IPayment payment) throws RemoteException;
	public int getAdminRecordSize() throws RemoteException;
}
