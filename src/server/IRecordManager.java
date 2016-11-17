package server;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import common.CarStatus;
import common.IPayment;
import common.Ticket;

public interface IRecordManager extends java.rmi.Remote {
	
	public int getOccupationRecordsSize() throws RemoteException;
	
	public int getFinancialRecordsSize() throws RemoteException;
	
	/**
	 * Add Occupation Report to RecordManager
	 * @param ldt time to add to report
	 * @param status whether car was entering or leaving
	 */
	public void addOccupationRecord(LocalDateTime ldt, CarStatus status) throws RemoteException;
	
	/**
	 * Queries OccupationRecords over a time frame
	 * @param begin starting time frame
	 * @param end ending time frame
	 * @return Occupation Record
	 */
	public String getOccupationRecords(LocalDateTime begin, LocalDateTime end) throws RemoteException;
	
	/**
	 * Add Financial Report to RecordManager
	 * @param ticket Ticket paid for
	 * @param payment Payment used to pay for ticket
	 */
	public void addFinancialRecord(Ticket ticket, IPayment payment) throws RemoteException;

	/**
	 * Queries Financial Records over a time frame
	 * @param begin
	 * @param end
	 * @return
	 */
	public String getFinancialRecords(LocalDateTime begin, LocalDateTime end) throws RemoteException;
	

}
