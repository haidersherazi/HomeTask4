
public class ChargingStationMain {

	public static void main(String[] args) {

		// Metadata for the project
        Metadata metadata = new Metadata("ChargingStationSoftware", "1.0","Charging stations management software to handle different stations.");
        metadata.saveLogs();
        
		ChargingSimulator simulator = new ChargingSimulator();
        simulator.simulateCharging();
       
        // Create log files
        LogFileManager.createLogFile("SystemLog.txt", false);
//        LogFileManager.createLogFile("ChargingStationLog.txt", false);
        LogFileManager.createLogFile("EnergyManagementLog.txt", false);
        
        // Log some messages
        SystemLogger.log("Log Message of System functionality");
//        ChargingStationLogger.log("Log Message of Charging station functionality");
        EnergyManagementLogger.log("Log Message of Energy management system functionality");

        
        // 	Archive log files
//        LogFileManager.archiveLogFile("SystemLog.txt");
//        LogFileManager.archiveLogFile("ChargingStationLog.txt");
//        LogFileManager.archiveLogFile("EnergyManagementLog.txt");
        

        // Data exchange simulation example test run
//        DataExchangeSimulator.simulateByteStream();
//        DataExchangeSimulator.simulateCharacterStream();
        

        
	}
}
