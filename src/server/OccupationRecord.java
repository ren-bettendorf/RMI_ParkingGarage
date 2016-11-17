package server;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

import common.CarStatus;

public class OccupationRecord
{
	private LocalDateTime time;
	private CarStatus status;
	
	public OccupationRecord(LocalDateTime time, CarStatus status) throws RemoteException
	{
		this.time = LocalDateTime.of(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), time.getHour(), 0);
		this.status = status;
	}

	public LocalDateTime getTime()
	{
		return time;
	}
	
	public CarStatus getCarStatus()
	{
		return status;
	}
}
