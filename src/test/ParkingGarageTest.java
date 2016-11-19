package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import common.Ticket;
import server.IParkingGarage;
import server.ParkingGarage;

public class ParkingGarageTest {

	IParkingGarage garage;
	Ticket ticket;
	
	@Before
	public void setUp() throws Exception {
		garage = new ParkingGarage(5);
		for(int i = 0; i < 4; i++)
		{
			ticket = garage.addCarToGarage();
		}
	}

	@Test
	public void testAddingCar() {
		try {
			garage.addCarToGarage();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Assert.assertTrue(garage.getCarOccupancy() == 5);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRemovingCar() {
		try {
			garage.removeCarFromGarage(ticket);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Assert.assertTrue(garage.getCarOccupancy() == 3);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
