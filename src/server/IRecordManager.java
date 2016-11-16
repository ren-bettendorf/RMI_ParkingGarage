package server;

import java.time.LocalDateTime;

import common.CarStatus;
import common.Payment;
import common.Ticket;

public interface IRecordManager {
	
	public int getOccupationRecordsSize();
	
	public int getFinancialRecordsSize();
	
	/**
	 * Add Occupation Report to RecordManager
	 * @param ldt time to add to report
	 * @param status whether car was entering or leaving
	 */
	public void addOccupationRecord(LocalDateTime ldt, CarStatus status);
	
	/**
	 * Queries OccupationRecords over a time frame
	 * @param begin starting time frame
	 * @param end ending time frame
	 * @return Occupation Record
	 */
	public String getOccupationRecords(LocalDateTime begin, LocalDateTime end);
	
	/**
	 * Add Financial Report to RecordManager
	 * @param ticket Ticket paid for
	 * @param payment Payment used to pay for ticket
	 */
	public void addFinancialRecord(Ticket ticket, Payment payment);

	/**
	 * Queries Financial Records over a time frame
	 * @param begin
	 * @param end
	 * @return
	 */
	public String getFinancialRecords(LocalDateTime begin, LocalDateTime end);
	

}
