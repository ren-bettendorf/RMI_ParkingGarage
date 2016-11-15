package common;

import java.time.LocalDateTime;

public class FinancialRecord 
{
	private Ticket ticket;
	private Payment payment;
	public FinancialRecord(Ticket ticket, Payment payment)
	{
		this.ticket = ticket;
		this.payment = payment;
	}

	public Ticket getTicket()
	{
		return ticket;
	}
	
	public Payment getPayment()
	{
		return payment;
	}
	
	public LocalDateTime getRecordDate()
	{
		return payment.getDateOfPayment();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FinancialRecord) {
			FinancialRecord rec = (FinancialRecord)obj;
			return ( ticket.equals(rec.getTicket()) );
		}
		return false;
	}
}
