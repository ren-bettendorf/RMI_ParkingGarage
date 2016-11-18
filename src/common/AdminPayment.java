package common;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class AdminPayment extends java.rmi.server.UnicastRemoteObject implements IPayment
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7098396884277547391L;
	private String userAddress, userName, userPhoneNumber;
	private LocalDateTime dateOwed;
	private double amountOwed;
	private double amountPaid;
	private LocalDateTime dateOfPayment;
	
	public AdminPayment(String userAddress, String userName, String userPhoneNumber, double amountOwed, LocalDateTime dateOwed) throws RemoteException
	{
		if(userAddress == null || userName == null || userPhoneNumber == null ||  dateOwed == null)
		{
			throw new IllegalArgumentException("Null field");
		}else if(amountOwed <= 0)
		{
			throw new IllegalArgumentException("Can't owe negative amount");
		}
		this.userAddress = userAddress;
		this.userName = userName;
		this.userPhoneNumber = userPhoneNumber;
		this.amountOwed = amountOwed;
		this.dateOwed = dateOwed;
		this.amountPaid = 0.00;
	}
	
	public double getAmountOwed()
	{
		return amountOwed;
	}
	
	public LocalDateTime getDateOwed()
	{
		return dateOwed;
	}
	
	public String getUserAddress()
	{
		return userAddress;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getUserPhoneNumber()
	{
		return userPhoneNumber;
	}

	@Override
	public double getAmountPaid() throws RemoteException {
		return amountPaid;
	}
	
	public void setDateOfPayment(LocalDateTime date)
	{
		this.dateOfPayment = date;
	}

	@Override
	public LocalDateTime getDateOfPayment() throws RemoteException {
		return dateOfPayment;
	}
}