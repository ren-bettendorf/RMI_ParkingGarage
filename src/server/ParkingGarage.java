package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.HashSet;

import common.RecordManager;
import common.Ticket;

public class ParkingGarage extends java.rmi.server.UnicastRemoteObject implements IParkingGarage, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashSet<Ticket> ticketsInGarage = new HashSet<Ticket>();
	private RecordManager recordManager = new RecordManager();
	//private EntryGate entryGate;
	//private ExitGate exitGate;
	private int maxOccupancy;

	public ParkingGarage(int maxOccu) throws RemoteException
	{
		super();
		this.maxOccupancy = maxOccu;
		//this.entryGate = new EntryGate("Entrance Gate", this);
		//this.exitGate = new ExitGate("Exit Gate", this);
	}

	public boolean checkGarageSpace() throws RemoteException
	{
		if(ticketsInGarage.size() == maxOccupancy)
		{
			return true;
		}
		return false;
	}

	public void addCarToGarage(Ticket ticket) throws RemoteException
	{
		ticketsInGarage.add(ticket);
	}
	
	public void removeCarFromGarage(Ticket ticket) throws RemoteException
	{
		ticketsInGarage.remove(ticket);
		
	}
	
	/**
	 * Creates the Occupation Report for a period of dates
	 * @param begin beginning date
	 * @param end ending date
	 * @return Occupation Report
	 */
	public String runOccupationReports(LocalDateTime begin, LocalDateTime end)
	{
		LocalDateTime beginLDT = LocalDateTime.of(begin.getYear(), begin.getMonth(), begin.getDayOfMonth(), 0, 0);

		LocalDateTime endLDT = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 0, 0);
		return recordManager.getOccupationRecords(beginLDT, endLDT);

	}
	
	/**
	 * Creates the Financial Report for a period of dates
	 * @param begin beginning date
	 * @param end ending date
	 * @return Financial Report
	 */
	public String runFinancialReports(LocalDateTime begin, LocalDateTime end)
	{
		LocalDateTime beginLDT = LocalDateTime.of(begin.getYear(), begin.getMonth(), begin.getDayOfMonth(), 0, 0);

		LocalDateTime endLDT = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 0, 0);

		return recordManager.getFinancialRecords(beginLDT, endLDT);
	}
	
	public RecordManager getRecordManager()
	{
		return recordManager;
	}
	
	public int getMaxCarOccupancy()
	{
		return maxOccupancy;
	}
	
//	public EntryGate getEntranceGate()
//	{
//		return entryGate;
//	}
//	
//	public ExitGate getExitGate()
//	{
//		return exitGate;
//	}
	
	public int getCarOccupancy()
	{
		return ticketsInGarage.size();
	}
	
	public HashSet<Ticket> getTickets()
	{
		return ticketsInGarage;
	}
	
}
