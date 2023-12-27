
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
    private static final int maxSimulationUsers = 4; // Adjust as needed but currently there are 5 vehicles
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
            // Simulate continuous charging at the charging station

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
                	
                // waiting time calculation
                long waitingTime = System.currentTimeMillis() - user.getVehicle().getArrivalTime(); 
                
                if (waitingTime > maxWaitingTime) {
                	
                    System.out.println(user.getUsername() + " waited too long and left the queue.");
                    
                } else {
                	Random random = new Random();
                	// Simulate charging amount
                    int chargingAmount = random.nextInt(25) + 10; // Charging between 10 and 35 units

                    // Charging logic based on the charging station and energy source
                    boolean charged = chargingStation.chargeVehicle(chargingAmount);

                    if (charged) {
                        System.out.println(user.getUsername() + " is charging at Station number " +
                                (stationNumber + 1) + " (Weather: " + currentWeather + " , Charging Source: " + chargingStation.getEnergySource().getSourceName() + ")");
                        
                        try {
                            Thread.sleep(5000); // Simulating charging time
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Release the station after charging
                        chargingStation.releaseStation();

                        System.out.println(user.getUsername()+ " finished charging at Station number " +
                                (stationNumber + 1));
                    }
                }
            } else {
                // If the charging station cannot charge in the current weather, switch energy source
                EnergySource newEnergySource = energyManagementSystem.getOptimalEnergySource("");
                chargingStation.switchEnergySource(newEnergySource);
                System.out.println("Weather condition does not allow charging. Switching to a new energy source.");
            }
        }
    }
    
    private void generateUsers() {
        Random random = new Random();
        int userCount = 0;

        while (userCount < maxSimulationUsers) {
            try {
                Thread.sleep(random.nextInt(5000)); // Random arrival time from 0 to 3 seconds
                
                String role = "";
                
                if (userCount+1 == 3) {
                	role = "ADMIN";
                } else {
                	role = "EXTERNAL";
                }
                
                User user = new User(role , userCount + 1); 
                
                
                synchronized (bookingQueue) {
                	bookTimeSlot(user);
                }
                System.out.println(user.getUsername() + " has arrived to charge his vehicle battery (Queue size: " + bookingQueue.size() + ")");
                userCount++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        stopSimulation = true;

    }
}
