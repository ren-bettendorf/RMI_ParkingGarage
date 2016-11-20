package cs414.a5.rbetten.common;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class CreditPayment extends java.rmi.server.UnicastRemoteObject implements IPayment {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4044131105820382865L;
	private String cardNumber;
	private LocalDateTime expirationDate;

	private double amountPaid;
	private LocalDateTime dateOfPayment;

	public CreditPayment(String cardNumber, LocalDateTime expDate, double amountPaid, LocalDateTime dateOfPayment)
			throws RemoteException {
		// Credit Card Numbers must have 16 characters
		if (cardNumber.length() != 16) {
			throw new IllegalArgumentException("Bad Credit Card Number");
		}
		// Disallow a negative payment
		if (amountPaid <= 0) {
			throw new IllegalArgumentException("Amount paid can't be less than 0");
		} else if (cardNumber == null || expDate == null || dateOfPayment == null) {
			throw new IllegalArgumentException("Null field");
		}
		this.cardNumber = cardNumber;
		this.expirationDate = expDate;
		this.amountPaid = amountPaid;
		this.dateOfPayment = dateOfPayment;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
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