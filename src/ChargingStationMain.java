
public class ChargingStationMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Multi-user access  
		User externalUser = new User("SyedHaiderAli", "EXTERNAL");
		User adminUser = new User("AdminUser", "ADMIN");
		
        ChargingStation chargingStation = new ChargingStation(1);
        chargingStation.addUser(externalUser);
        chargingStation.addUser(adminUser);

        chargingStation.bookTimeSlot(externalUser);
        chargingStation.bookTimeSlot(adminUser);

        chargingStation.prioritizeQueue();

        // Metadata for the project
        Metadata metadata = new Metadata("ChargingStationSoftware", "1.1"," Charging stations management software to handle different stations.");
        
        // Create log files
        LogFileManager.createLogFile("SystemLog.txt");
        LogFileManager.createLogFile("ChargingStationLog.txt");
        LogFileManager.createLogFile("EnergyManagementLog.txt");
        
        // Log some messages
        SystemLogger.log("Log Message of System functionality");
        ChargingStationLogger.log("Log Message of Charging station functionality");
        EnergyManagementLogger.log("Log Message of Energy management system functionality");

        // 	Archive log files
        LogFileManager.archiveLogFile("SystemLog.txt");
        LogFileManager.archiveLogFile("ChargingStationLog.txt");
        LogFileManager.archiveLogFile("EnergyManagementLog.txt");
        

        // Data exchange simulation
        DataExchangeSimulator.simulateByteStream();
        DataExchangeSimulator.simulateCharacterStream();
        
        
        ChargingSimulator simulator = new ChargingSimulator();
        simulator.simulateCharging();
	}
}
