

public class ChargingStation {

    private EnergySource energySource;

	public ChargingStation() {
		
    }
	
	public EnergySource getEnergySource() {
		return energySource;
	}

	public void setEnergySource(EnergySource energySource) {
		this.energySource = energySource;
	}
	
    public synchronized boolean chargeVehicle(int amount) {
        boolean charged = energySource.occupyStation(amount);
        if (charged) {
        	EnergySourceLogger.log("Charge the vehicle with " + amount + " units of energy. Remaining Energy level will be: " + energySource.getEnergyLevel() );
            System.out.println("Charge the vehicle with " + amount + " units of energy. Remaining Energy level will be: " + energySource.getEnergyLevel());
        } else {
            System.out.println("Insufficient energy to charge vehicle. Energy level: " + energySource.getEnergyLevel());
        }
        return charged;
    }

    public synchronized void switchEnergySource(EnergySource newEnergySource) {
        System.out.println("Switching to a new energy source.");
        this.energySource.release(30); // Replenish some energy before switching
        this.energySource = newEnergySource;
    }

    public synchronized boolean canChargeInCurrentWeather(String currentWeather) {
        
        switch (currentWeather) {
            case "Sunny":
                return true;
            case "Cloudy":
                return energySource.getEnergyLevel() >= 50; // min energy value check if the weather is cloudy
            case "Rainy":
                return false; // Cannot charge during rainy weather
            default:
                return false;
        }
    }

    public synchronized void releaseStation() {
        
    	this.energySource.release(30); // Replenish some energy before switching
    }
}
