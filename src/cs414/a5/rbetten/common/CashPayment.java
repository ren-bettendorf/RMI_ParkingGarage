package cs414.a5.rbetten.common;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class CashPayment implements IPayment, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -397660428839552619L;
	private double amountPaid;
	private LocalDateTime dateOfPayment;

	public CashPayment(double amountPaid, LocalDateTime dateOfPayment) throws RemoteException {
		super();
		// Disallow a negative payment
		if (amountPaid <= 0) {
			throw new IllegalArgumentException("Amount paid can't be less than 0");
		} else if (dateOfPayment == null) {
			throw new IllegalArgumentException("Date is null");
		}
		this.amountPaid = amountPaid;
		this.dateOfPayment = dateOfPayment;
	}

	@Override
	public double getAmountPaid() throws RemoteException {
		return amountPaid;
	}

	@Override
	public LocalDateTime getDateOfPayment() throws RemoteException {
		return dateOfPayment;
	}

}