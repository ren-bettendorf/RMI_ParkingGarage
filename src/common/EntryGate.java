package common;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

import server.ParkingGarage;

public class EntryGate extends java.rmi.server.UnicastRemoteObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6595824430074269071L;
	private String gateName;
	private ParkingGarage garage;
	
	public EntryGate(String name, ParkingGarage garage) throws RemoteException
	{
		this.gateName = name;
		this.garage = garage;
	}
	
	public String getGateName()
	{
		return gateName;
	}
	
}
