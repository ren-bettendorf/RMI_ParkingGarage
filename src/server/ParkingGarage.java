package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.CarStatus;
import common.IParkingGarage;
import common.IPayment;
import common.IRecordManager;
import common.ITicket;
import common.Ticket;

public class ParkingGarage extends java.rmi.server.UnicastRemoteObject implements IParkingGarage, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ITicket> ticketsInGarage = new ArrayList<ITicket>();
	private IRecordManager recordManager;
	private int maxOccupancy;

	public ParkingGarage(int maxOccu) throws RemoteException
	{
		super();
		this.recordManager = new RecordManager();
		this.maxOccupancy = maxOccu;
	}

	public boolean checkGarageSpace() throws RemoteException
	{
		if(ticketsInGarage.size() == maxOccupancy)
		{
			return true;
		}
		return false;
	}

	public ITicket addCarToGarage() throws RemoteException
	{
		ITicket ticket = null;
		try {
			if( !checkGarageSpace() )
			{
				ticket = new Ticket(LocalDateTime.now());
				ticket = (ITicket)UnicastRemoteObject.exportObject(ticket, 0);
				ticketsInGarage.add(ticket);
				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticket;
	}
	
	public void removeCarFromGarage(ITicket ticket) throws RemoteException
	{
		ticketsInGarage.remove(ticket);	
	}
	
	public void addEntryRecords(ITicket ticket) throws RemoteException
	{
		if(ticket == null)
		{
			throw new IllegalArgumentException("Trouble: TICKET NULL");
		}
		recordManager.addOccupationRecord(ticket.getCheckinTime(), CarStatus.ENTER);
	}
	
	public void addExitRecords(ITicket ticket, IPayment payment) throws RemoteException
	{
		if(ticket == null)
		{
			throw new IllegalArgumentException("Trouble: TICKET NULL");
		}
		if(payment == null)
		{
			throw new IllegalArgumentException("Trouble: PAYMENT NULL");
		}
		recordManager.addFinancialRecord(ticket, payment);
		recordManager.addOccupationRecord(payment.getDateOfPayment(), CarStatus.LEAVE);
	}
	
	public String runReports(LocalDateTime begin, LocalDateTime end) throws RemoteException
	{
		String retString = "";
		
		retString += runOccupationReports(begin,end);
		retString += runFinancialReports(begin,end);
		
		return retString;
	}
	
	/**
	 * Creates the Occupation Report for a period of dates
	 * @param begin beginning date
	 * @param end ending date
	 * @return Occupation Report
	 */
	public String runOccupationReports(LocalDateTime begin, LocalDateTime end) throws RemoteException
	{
		return recordManager.getOccupationRecords(begin, end);
	}
	
	/**
	 * Creates the Financial Report for a period of dates
	 * @param begin beginning date
	 * @param end ending date
	 * @return Financial Report
	 */
	public String runFinancialReports(LocalDateTime begin, LocalDateTime end) throws RemoteException
	{
		return recordManager.getFinancialRecords(begin, end);
	}
	
	public IRecordManager getRecordManager() throws RemoteException
	{
		return recordManager;
	}
	
	public int getMaxCarOccupancy() throws RemoteException
	{
		return maxOccupancy;
	}
	
	public int getCarOccupancy() throws RemoteException
	{
		return ticketsInGarage.size();
	}
	
	public ArrayList<ITicket> getTickets() throws RemoteException
	{
		return ticketsInGarage;
	}
	
}
