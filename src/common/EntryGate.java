package common;

import java.io.Serializable;
import java.rmi.RemoteException;

import server.IParkingGarage;
import server.ParkingGarage;

public class EntryGate extends java.rmi.server.UnicastRemoteObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3866125031431042189L;
	private String gateName;
	private IParkingGarage garage;
	
	public EntryGate(String name, IParkingGarage garage) throws RemoteException
	{
		this.gateName = name;
		this.garage = garage;
	}
	
	public String getGateName()
	{
		return gateName;
	}
	
}
