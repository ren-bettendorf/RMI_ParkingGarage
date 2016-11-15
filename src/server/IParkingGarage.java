package server;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.HashSet;

import common.RecordManager;
import common.Ticket;

public interface IParkingGarage extends java.rmi.Remote {

	public boolean checkGarageSpace() throws RemoteException;
	public void addCarToGarage(Ticket ticket) throws RemoteException;
	public void removeCarFromGarage(Ticket ticket) throws RemoteException;
	
	/**
	 * Creates the Occupation Report for a period of dates
	 * @param begin beginning date
	 * @param end ending date
	 * @return Occupation Report
	 */
	public String runOccupationReports(LocalDateTime begin, LocalDateTime end) throws RemoteException;
	
	/**
	 * Creates the Financial Report for a period of dates
	 * @param begin beginning date
	 * @param end ending date
	 * @return Financial Report
	 */
	public String runFinancialReports(LocalDateTime begin, LocalDateTime end) throws RemoteException;
	public HashSet<Ticket> getTickets() throws RemoteException;
	public int getCarOccupancy() throws RemoteException;
	public int getMaxCarOccupancy() throws RemoteException;
	public RecordManager getRecordManager() throws RemoteException;
}
