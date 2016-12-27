package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

import common.IPayment;
import common.Ticket;

public class FinancialRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2422293596902632133L;
	private Ticket ticket;
	private IPayment payment;

	public FinancialRecord(Ticket ticket, IPayment payment) throws RemoteException {
		if (payment == null) {
			throw new IllegalArgumentException("Payment is null");
		} else if (ticket == null) {
			throw new IllegalArgumentException("Ticket is null");
		}
		this.ticket = ticket;
		this.payment = payment;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public IPayment getPayment() {
		return payment;
	}

	public LocalDateTime getRecordDate() {
		LocalDateTime paymentDate = null;
		try {
			paymentDate = payment.getDateOfPayment();
		} catch (RemoteException e) {
			System.out.println("Returning null Record Date");
		}
		return paymentDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FinancialRecord) {
			FinancialRecord rec = (FinancialRecord) obj;
			return (ticket.equals(rec.getTicket()));
		}
		return false;
	}
}
