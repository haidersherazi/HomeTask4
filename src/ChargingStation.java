

public class ChargingStation {

    private int availableChargingCapacity;

    public ChargingStation(int availableCapacity) {
        this.availableChargingCapacity = availableCapacity;
    }
    
    
    public synchronized boolean chargeVehicle() {
        if (availableChargingCapacity > 0) {
        	availableChargingCapacity-- ;
            return true;
        }
        return false;
    }

    public synchronized void releaseStation() {
    	availableChargingCapacity++ ;
    }
}
