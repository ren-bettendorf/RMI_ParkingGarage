package server;

import client.ParkingGarageObserver;

public interface ParkingGarageSubject {
	public void attach(ParkingGarageObserver obs);
	public void detach(ParkingGarageObserver obs);
	public void updateObservers(int occupancy);
}
