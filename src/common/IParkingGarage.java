package common;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IParkingGarage extends java.rmi.Remote {

	public boolean checkGarageSpace() throws RemoteException;
	public ITicket addCarToGarage() throws RemoteException;
	public void removeCarFromGarage(ITicket ticket) throws RemoteException;
	public String runReports(LocalDateTime begin, LocalDateTime end) throws RemoteException;
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
	public ArrayList<ITicket> getTickets() throws RemoteException;
	public int getCarOccupancy() throws RemoteException;
	public int getMaxCarOccupancy() throws RemoteException;
	public IRecordManager getRecordManager() throws RemoteException;
	public void addExitRecords(ITicket ticket, IPayment payment) throws RemoteException;
	public void addEntryRecords(ITicket ticket) throws RemoteException;
}
