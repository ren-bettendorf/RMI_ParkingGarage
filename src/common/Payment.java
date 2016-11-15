package common;

import java.time.LocalDateTime;

public abstract class Payment
{
	protected double amountPaid;
	protected LocalDateTime dateOfPayment;
	
	public double getAmountPaid()
	{
		return amountPaid;
	}
	
	public LocalDateTime getDateOfPayment()
	{
		return dateOfPayment;
	}
}