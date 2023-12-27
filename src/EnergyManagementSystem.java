import java.util.Random;

public class EnergyManagementSystem {
	
    private final EnergySource[] energySources;

    public EnergyManagementSystem(int numberOfEnergySources) {
        this.energySources = new EnergySource[numberOfEnergySources];
        for (int i = 0; i < numberOfEnergySources; i++) {
        	if (i == 0) {
        		energySources[i] = new EnergySource(100, "Electrical Grid"); // Initial energy level, adjust as needed	
            } else {
            	energySources[i] = new EnergySource(100, "Solar"); // Initial energy level, adjust as needed
            }
            
        }
    }

    public EnergySource getOptimalEnergySource(String weather) {
        
    	switch (weather) {
        case "Sunny":
        	return energySources[1];
        case "Cloudy":
        	return energySources[0];
        default:
        	return energySources[0];
    	}
    	
    }
}