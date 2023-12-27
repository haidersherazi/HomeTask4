
public class EnergySource {

    private int energyLevel;
    private String sourceName;

    public EnergySource(int initialEnergy, String sourceName) {
        this.energyLevel = initialEnergy;
        this.sourceName = sourceName;
    }

    public synchronized boolean occupyStation(int requireEnergy) {
        if (energyLevel >= requireEnergy) {
            energyLevel -= requireEnergy;
            return true;
        } else {
            return false;
        }
    }

    public synchronized void release(int replenishSomeEnergy) {
//    	if ((energyLevel + replenishSomeEnergy) > 100) {
//    		energyLevel = 100;
//    	} else {
//    		energyLevel += replenishSomeEnergy;	
//    	}
    	energyLevel += replenishSomeEnergy;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
    
    
}