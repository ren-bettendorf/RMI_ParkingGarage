package cs414.a5.rbetten.common;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

<<<<<<< HEAD:src/common/IPayment.java
public interface IPayment extends java.rmi.Remote 
{
=======
public interface IPayment extends java.rmi.Remote {
>>>>>>> 76eb227836a5e3da999e4736b1fc54bf73efa3dc:cs414/a5/rbetten/common/IPayment.java
	public double getAmountPaid() throws RemoteException;

	public LocalDateTime getDateOfPayment() throws RemoteException;
}