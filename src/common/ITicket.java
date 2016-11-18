package common;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface ITicket extends java.rmi.Remote  {
	
	
	public LocalDateTime getCheckinTime() throws RemoteException;

	public String getUniqueID() throws RemoteException;
	
	public boolean getPaymentStatus() throws RemoteException;

	public void setPaymentStatus(boolean paymentStatus) throws RemoteException;

}
