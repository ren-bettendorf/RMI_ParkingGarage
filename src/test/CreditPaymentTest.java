package test;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import common.CreditPayment;

public class CreditPaymentTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeAmount()
	{
		String cardNumber = "1234567890123456";
		LocalDateTime date = LocalDateTime.of(2016, 9, 9, 0, 0);
		try {
			new CreditPayment(cardNumber, date, -1.00, LocalDateTime.now());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBadCCNumber()
	{
		String cardNumber = "1234567890";
		LocalDateTime date = LocalDateTime.of(2016, 9, 9, 0, 0);
		try {
			new CreditPayment(cardNumber, date, 1.00, LocalDateTime.now());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullLDT()
	{
		String cardNumber = "1234567890123456";
		try {
			new CreditPayment(cardNumber, null, 1.00, LocalDateTime.now());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullLDTSecond()
	{
		String cardNumber = "1234567890123456";
		try {
			new CreditPayment(cardNumber, LocalDateTime.now(), 1.00, null);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
