
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ChargingSimulator {
    private static final int numberOfChargingStations = 3; // Adjust as needed but currently there are 3 
    private static final int maxWaitingTime = 15 * 60 * 1000; // 15 minutes in milliseconds
    private static final int maxSimulationUsers = 5; // Adjust as needed but currently there are 5 vehicles
    private static final int numberOfEnergySources = 2;
    
    private final EnergyManagementSystem energyManagementSystem;
    private ChargingStation[] chargingStations;
    private volatile boolean stopSimulation = false;
	
 	private List<User> userList = new ArrayList<>();
    private Queue<User> bookingQueue = new PriorityQueue<>(Comparator.comparingInt(u -> u.getRole().equals("EXTERNAL") ? 1 : 0));
     
    public ChargingSimulator() {
    	
        chargingStations = new ChargingStation[numberOfChargingStations];
        energyManagementSystem = new EnergyManagementSystem(numberOfEnergySources);
        
        for (int i = 0; i < numberOfChargingStations; i++) {
            chargingStations[i] = new ChargingStation();
            LogFileManager.createLogFile("ChargingStation" + (i+1) + ".txt", false);
        }
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void bookTimeSlot(User user) {
        bookingQueue.offer(user);
        System.out.println(user.getUsername() + " booked a time slot.");
    }

    public void prioritizeQueue() {
        System.out.println("Prioritized Queue:");
        for (User user : bookingQueue) {
            System.out.println(user.getUsername());
        }
    }
    
    public void simulateCharging() {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfChargingStations);

        executorService.submit(this::generateUsers);

        for (int i = 0; i < numberOfChargingStations; i++) {
            final int stationNumber = i;
            executorService.submit(() -> processCharging(stationNumber));
        }

        
        // Shutdown the executor service after the simulation is done
        executorService.shutdown();
        
        
     // Polling loop to check if the executor service has terminated
        while (!executorService.isTerminated()) {
            try {
                // Sleep for a short duration before checking again
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Handle interruption if necessary
                e.printStackTrace();
            }
        }

        
        // Executor service has Finished
        SystemLogger.log("System simulation to charge the resserve batteries of vehicles has been finished successfully.");
        System.out.println("Simulation has been completed. \n\n");
        checkLogFiles();
        
    }
    
    private void checkLogFiles() {
    	Scanner scanner = new Scanner(System.in);
    	String userInput ;
    	
    	System.out.print("Do you want to see the logs [y/n]? ");
    	userInput = scanner.nextLine();
    	
    	
    	while (!userInput.matches("[ynNY]")) {
    		
    		System.out.print("Invalid input, Please enter again [y/n]? " );
    	    userInput = scanner.nextLine();
    	}
        
    	if (userInput.matches("[yY]") ) {
			
			// Get user input for equipment name or date
	        System.out.print("Enter the date (format DD-MM-YYYY): ");
	        userInput = scanner.nextLine();

	        // Construct the file path based on user input
	        String folderPath = LogFileOpener.constructLogFolderPath(userInput);

	        // Open the file using the default text editor (platform-dependent)
	        LogFileOpener.openLogFilesInFolder(folderPath, ".*\\.txt");
	        
		} else {
			System.out.print("Thanks for using our system. See you next time!! ");
		}
        
    }
    
    private void processCharging(int stationNumber) {
        
        while (!stopSimulation || !bookingQueue.isEmpty()) {

            // Get the current weather condition
            String currentWeather = WeatherSimulator.simulateWeather();
            
            ChargingStation chargingStation = chargingStations[stationNumber];
            chargingStation.setEnergySource(energyManagementSystem.getOptimalEnergySource(currentWeather));

            // Check if the charging station can charge in the current weather
            if (chargingStation.canChargeInCurrentWeather(currentWeather)) {
            	
                User user;
                
                synchronized (bookingQueue) {
                    if (!bookingQueue.isEmpty()) {
                    	user = bookingQueue.poll();
                    } else {
                        continue; // No vehicle in the queue
                    }
                }
                
                EnergySourceLogger.log("Station number " + (stationNumber  + 1) + " has been assigned to " + user.getUsername() + " to charge the reserve battery." );
                
                // waiting time calculation
                long waitingTime = System.currentTimeMillis() - user.getVehicle().getArrivalTime(); 
                
                if (waitingTime > maxWaitingTime) {
                	
                    System.out.println(user.getUsername() + " waited too long and left the queue.");
                    
                    SystemLogger.log(user.getUsername() + "has waited too long and left the queue." );
                    
                } else {
                	Random random = new Random();
                	// Simulate charging amount
                    int chargingAmount = random.nextInt(25) + 10; // Charging between 10 and 35 units

                    // Charging logic based on the charging station and energy source
                    boolean charged = chargingStation.chargeVehicle(chargingAmount);

                    if (charged) {
                    	SystemLogger.log("Our System allocated station number " + (stationNumber + 1) + " to " + user.getUsername() + " to charge the reserve battery.");
                    	
                    	ChargingStationLogger.log("Reserve Battery of " + user.getUsername() + " vehicle has started charging at this station." , (stationNumber  + 1) );
                    	
                    	EnergyManagementLogger.log("Reserve battery of " + user.getUsername() +" vehicle is getting energy from " + chargingStation.getEnergySource().getSourceName()
                    			+ " because the weather is " + currentWeather + ".");
                    	
                    	System.out.println(user.getUsername() + " is charging at Station number " +
                                (stationNumber + 1) + " (Weather: " + currentWeather + " , Charging Source: " + chargingStation.getEnergySource().getSourceName() + ")");
                        
                        try {
                            Thread.sleep(5000); // Simulating charging time
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Release the station after charging
                        chargingStation.releaseStation();
                        ChargingStationLogger.log("Reserve Battery of " + user.getUsername() + " vehicle has finished charging at this station." , (stationNumber  + 1));
                        SystemLogger.log(user.getUsername() + " has finished charging successfully.");
                        
                        System.out.println(user.getUsername()+ " finished charging at Station number " + (stationNumber + 1));
                    }
                }
            } else {
            	
                // If the charging station cannot charge in the current weather, switch energy source
            	currentWeather = WeatherSimulator.simulateWeather();
                EnergySource newEnergySource = energyManagementSystem.getOptimalEnergySource(currentWeather);
                chargingStation.switchEnergySource(newEnergySource);
                SystemLogger.log("Weather condition does not allow charging at Charging station number" + (stationNumber + 1) + ". Switching to a new energy source.");
                System.out.println("Weather condition does not allow charging. Switching to a new energy source.");
            }
        }
    }
    
    private void generateUsers() {
        Random random = new Random();
        int userCount = 0;

        while (userCount < maxSimulationUsers) {
            try {
                Thread.sleep(random.nextInt(4000)); // Random arrival time from 0 to 4 seconds
                
                String role = "";
                
                if (userCount+1 == 9) {
                	role = "ADMIN";
                } else {
                	role = "EXTERNAL";
                }
                
                User user = new User(role , userCount + 1); 
                
                
                synchronized (bookingQueue) {
                	bookTimeSlot(user);
                }
                
                SystemLogger.log(user.getUsername() + " has booked the timeslot in our system successfully.");
                System.out.println(user.getUsername() + " has arrived to charge reserve battery of the vehicle (Queue size: " + bookingQueue.size() + ")");
                userCount++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        stopSimulation = true;

    }
}
