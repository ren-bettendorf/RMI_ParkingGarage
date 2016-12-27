package common;

import java.io.Serializable;
import java.rmi.Remote;
import java.time.LocalDateTime;

public class Ticket implements Serializable, Remote {

	private static final long serialVersionUID = 4580375843474140985L;
	private LocalDateTime checkinTime;
	private String uniqueID;
	private boolean paymentStatus;

	public Ticket(LocalDateTime checkinTime) {
		setPaymentStatus(false);
		this.checkinTime = checkinTime;

		uniqueID = checkinTime.toString().replaceAll("[^0-9]", "");

		uniqueID = uniqueID.substring(0, uniqueID.length() - 2);
	}

	public LocalDateTime getCheckinTime() {
		return checkinTime;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public boolean equals(Object obj) {
		if (!(obj == null) && obj instanceof Ticket) {
			Ticket tic = (Ticket) obj;
			return this.uniqueID.equals(tic.getUniqueID());
		}
		return false;
	}


	public String toString() {
		return uniqueID;
	}
}
