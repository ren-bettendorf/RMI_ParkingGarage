package cs414.a5.rbetten.test;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs414.a5.rbetten.common.CarStatus;
import cs414.a5.rbetten.common.CashPayment;
import cs414.a5.rbetten.common.Ticket;
import cs414.a5.rbetten.server.IRecordManager;
import cs414.a5.rbetten.server.ParkingGarage;
import cs414.a5.rbetten.server.RecordManager;

public class RecordManagerTest 
{
	ParkingGarage garage;
	IRecordManager recordManager;
	@Before
	public void setUp() throws Exception 
	{
		recordManager = new RecordManager();
		LocalDateTime ldt = LocalDateTime.of(2016, 9, 27, 13, 0);
		LocalDateTime ldtNextDay = LocalDateTime.of(2016, 9, 28, 13, 0);
		LocalDateTime ldtNotInQuery = LocalDateTime.of(2016, 10, 3, 13, 0);
		recordManager.addOccupationRecord(ldt, CarStatus.ENTER);
		recordManager.addOccupationRecord(ldt.plusHours(2), CarStatus.LEAVE);
		recordManager.addFinancialRecord(new Ticket(ldt), new CashPayment(10.00, ldt));
		recordManager.addFinancialRecord(new Ticket(ldtNextDay), new CashPayment(20.00, ldtNextDay));
		recordManager.addFinancialRecord(new Ticket(ldtNotInQuery), new CashPayment(20.00, ldtNotInQuery));
	}
	
	//getOccupationRecords(LocalDateTime begin, LocalDateTime end)
	@Test
	public void testBadOccupationQuery()
	{
		String expected = "Cars Entered Garage: \nTimestamp\t\tTotal Entered Garage\n" + 
						"Cars Left Garage: \nTimestamp\t\tTotal Left Garage\n" + "\n\n";
		String actual = "";
		try {
			actual = recordManager.getOccupationRecords(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(2, 1, 1, 1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(actual.equals(expected));
	}
	@Test
	public void testGoodOccupationQuery()
	{
		String expected = "Cars Entered Garage: \nTimestamp\t\t"
				+ "Total Entered Garage\n2016-09-27T13:00, \t1\n"
				+ "Cars Left Garage: \nTimestamp\t\tTotal Left Garage\n2016-09-27T15:00, \t1\n\n\n";
		String actual = "";
		try {
			actual = recordManager.getOccupationRecords(LocalDateTime.of(2016, 9, 26, 1, 1), LocalDateTime.of(2016, 9, 28, 1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(actual.equals(expected));
	}
	
	//getFinancialRecords(LocalDateTime begin, LocalDateTime end)
	@Test
	public void testBadFinancialQuery()
	{
		String expected = "Financial Records: \n\nDay\t\t\tTotal Made\n";
		String actual = "";
		try {
			actual = recordManager.getFinancialRecords(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(2, 1, 1, 1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(actual.equals(expected));
	}
	@Test
	public void testGoodFinancialQuery()
	{
		String expected = "Financial Records: \n\nDay\t\t\tTotal Made\n2016-09-28T13:00, \t20.0\n2016-09-27T13:00, \t10.0\n";
		
		String actual = "";
		try {
			actual = recordManager.getFinancialRecords(LocalDateTime.of(2016, 9, 26, 1, 1), LocalDateTime.of(2016, 10, 1, 1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(actual.equals(expected));
	}
}
