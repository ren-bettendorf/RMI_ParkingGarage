package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import common.CarStatus;
import common.Payment;
import common.Ticket;

public class RecordManager extends java.rmi.server.UnicastRemoteObject implements Serializable, IRecordManager
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9092826944053480194L;
	private ArrayList<FinancialRecord> financialRecords = new ArrayList<FinancialRecord>();
	private ArrayList<OccupationRecord> occupationRecords = new ArrayList<OccupationRecord>();

	public RecordManager() throws RemoteException
	{
	}
	
	public int getOccupationRecordsSize()
	{
		return occupationRecords.size();
	}
	
	public int getFinancialRecordsSize()
	{
		return financialRecords.size();
	}
	
	/**
	 * Add Occupation Report to RecordManager
	 * @param ldt time to add to report
	 * @param status whether car was entering or leaving
	 */
	public void addOccupationRecord(LocalDateTime ldt, CarStatus status)
	{
		OccupationRecord record = new OccupationRecord(ldt, status);
		occupationRecords.add(record);
	}
	
	/**
	 * Queries OccupationRecords over a time frame
	 * @param begin starting time frame
	 * @param end ending time frame
	 * @return Occupation Record
	 */
	public String getOccupationRecords(LocalDateTime begin, LocalDateTime end)
	{
		String returnedTotals = "";
		HashMap<LocalDateTime, Integer> carsVisited = new HashMap<LocalDateTime, Integer>();
		HashMap<LocalDateTime, Integer> carsLeft = new HashMap<LocalDateTime, Integer>();
		
		// Check to make sure that records exist
		if(occupationRecords.size() > 0)
		{

			int numVisited = 0;
			int numLeft = 0;
			
			// Check to all records to see if they fall in time frame
			for(OccupationRecord record : occupationRecords)
			{
				// Check to see if record is not in query time frame
				LocalDateTime ldt = record.getTime();
				if(ldt.isBefore(begin) || ldt.isAfter(end))
				{
					continue;
				}
				
				CarStatus status = record.getCarStatus();
				if( status == CarStatus.ENTER)
				{
					if( carsVisited.containsKey(ldt) )
					{
						numVisited = carsVisited.get(ldt) + 1;
					}
					else
					{
						numVisited = 1;
					}

					carsVisited.put(ldt, numVisited);
				}
				else
				{
					if( carsLeft.containsKey(ldt) )
					{
						numLeft = carsLeft.get(ldt) + 1;
					}
					else
					{
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
	 * @param ticket Ticket paid for
	 * @param payment Payment used to pay for ticket
	 */
	public void addFinancialRecord(Ticket ticket, Payment payment)
	{
		FinancialRecord record = new FinancialRecord(ticket, payment);
		financialRecords.add(record);
	}

	/**
	 * Queries Financial Records over a time frame
	 * @param begin
	 * @param end
	 * @return
	 */
	public String getFinancialRecords(LocalDateTime begin, LocalDateTime end)
	{
		String returnedTotals;
		HashMap<LocalDateTime, Double> dailyTotals = new HashMap<LocalDateTime, Double>();
		
		// Check to make sure that records exist
		if(financialRecords.size() > 0)
		{
			for(FinancialRecord record : financialRecords)
			{
				// Check to see if record is not in query time frame
				LocalDateTime recordDate = record.getRecordDate();
				if(recordDate.isBefore(begin) || recordDate.isAfter(end))
				{
					continue;
				}
				double recordPayment = record.getPayment().getAmountPaid();
				
				// Check to see if we have a record of this day already and adds to otherwise creates a new entry
				if(dailyTotals.containsKey(recordDate))
				{
					double runningTotals = dailyTotals.get(recordDate) + recordPayment;
					dailyTotals.replace(recordDate, runningTotals);
				}
				else
				{
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
	 * @param entered
	 * @param left
	 * @return
	 */
	private String changeOccupationToLines(HashMap<LocalDateTime, Integer> entered, HashMap<LocalDateTime, Integer> left)
	{
		String ret = "Cars Entered Garage: \nTimestamp\t\tTotal Entered Garage\n";
		for(LocalDateTime day : entered.keySet())
		{
			ret += day + ", \t" + entered.get(day) + "\n";
		}
		ret += "Cars Left Garage: \nTimestamp\t\tTotal Left Garage\n";
		for(LocalDateTime day : left.keySet())
		{
			ret += day + ", \t" + left.get(day) + "\n";
		}
		ret += "\n\n";
		return ret;
	}

	/**
	 * Converts HashMaps to Strings for display
	 * @param dailyTotals
	 * @return
	 */
	private String changeFinancialToLines(HashMap<LocalDateTime, Double> dailyTotals) 
	{
		String ret = "Financial Records: \n\nDay\tTotal Made\n";
		for(LocalDateTime day : dailyTotals.keySet())
		{
			ret += day + ", \t" + dailyTotals.get(day) + "\n";
		}
		return ret;
	}

}
