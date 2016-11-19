package test;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import common.CarStatus;
import common.CashPayment;
import common.Ticket;
import server.IRecordManager;
import server.ParkingGarage;
import server.RecordManager;

public class RecordManagerTest 
{
	ParkingGarage garage;
	IRecordManager recordManager;
	@Before
	public void setUp() throws Exception 
	{
		recordManager = new RecordManager();
		garage = new ParkingGarage(10);
		LocalDateTime ldt = LocalDateTime.of(2016, 9, 27, 13, 0);
		recordManager.addOccupationRecord(ldt, CarStatus.ENTER);
		recordManager.addOccupationRecord(ldt.plusHours(2), CarStatus.LEAVE);
		recordManager.addFinancialRecord(new Ticket(ldt), new CashPayment(10.00, ldt));
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
		String expected = "Financial Records: \n\nDay\tTotal Made\n";
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
		String expected = "Financial Records: \n\nDay\tTotal Made\n2016-09-27T13:00, \t\t10.0\n";
		String actual = "";
		try {
			actual = recordManager.getFinancialRecords(LocalDateTime.of(2016, 9, 26, 1, 1), LocalDateTime.of(2016, 9, 28, 1, 1));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(actual.equals(expected));
	}
}
