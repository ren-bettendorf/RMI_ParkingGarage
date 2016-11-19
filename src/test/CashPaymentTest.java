package test;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import common.CashPayment;

public class CashPaymentTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeAmount()
	{
		try {
			new CashPayment(-1.00, LocalDateTime.now());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullLDT()
	{
		try {
			new CashPayment(1.00, null);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
