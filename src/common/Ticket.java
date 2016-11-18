package common;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class Ticket extends java.rmi.server.UnicastRemoteObject implements Serializable, ITicket {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4580375843474140985L;
	private LocalDateTime checkinTime;
	private String uniqueID;
	private boolean paymentStatus = false;
	
	
	public Ticket(LocalDateTime checkinTime) throws RemoteException
	{
		this.checkinTime = checkinTime;
		// Trim off all non numeric characters
		uniqueID = checkinTime.toString().replaceAll("[^0-9]", "");
		// Trim off the milliseconds
		uniqueID = uniqueID.substring(0, uniqueID.length()-2);
	}
	
	public LocalDateTime getCheckinTime() throws RemoteException
	{
		return checkinTime;
	}

	public String getUniqueID() throws RemoteException
	{
		return uniqueID;
	}
	
	public boolean getPaymentStatus()  throws RemoteException
	{
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus)  throws RemoteException
	{
		this.paymentStatus = paymentStatus;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(!(obj == null) && obj instanceof Ticket) 
		{
			Ticket tic = (Ticket)obj;
			try {
				return this.checkinTime.equals(tic.getCheckinTime());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return checkinTime.toString();
	}
}
