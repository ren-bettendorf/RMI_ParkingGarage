package cs414.a5.rbetten.common;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface IPayment extends java.rmi.Remote {
	public double getAmountPaid() throws RemoteException;

	public LocalDateTime getDateOfPayment() throws RemoteException;
}