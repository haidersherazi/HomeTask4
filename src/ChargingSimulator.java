
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;
import java.util.Random;

public class ChargingSimulator {
    private static final int numberOfChargingStations = 3; // Adjust as needed but currently there are 5 
    private static final int maxWaitingTime = 15 * 60 * 1000; // 15 minutes in milliseconds
    private static final int maxSimulationUsers = 5; // Adjust as needed but currently there are 15 vehicles
    
    private ChargingStation[] chargingStations;
    private volatile boolean stopSimulation = false;
    
 // ChargingStation class with user management and time slot booking
	
 	private List<User> userList = new ArrayList<>();
    private Queue<User> bookingQueue = new PriorityQueue<>(Comparator.comparingInt(u -> u.getRole().equals("EXTERNAL") ? 1 : 0));
     
    public ChargingSimulator() {
    	
        chargingStations = new ChargingStation[numberOfChargingStations];
        for (int i = 0; i < numberOfChargingStations; i++) {
            chargingStations[i] = new ChargingStation(1); // Each station can charge 1 vehicle
//            chargingStations[i] = new ChargingStation(2); // Each station can charge 2 vehicle
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
            executorService.submit(() -> processVehicles(stationNumber));
        }

        // Shutdown the executor service after the simulation is done
        executorService.shutdown();
    }
    
    private void generateUsers() {
        Random random = new Random();
        int userCount = 0;

        while (userCount < maxSimulationUsers) {
            try {
                Thread.sleep(random.nextInt(3000)); // Random arrival time from 0 to 3 seconds
                
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
    
    private void processVehicles(int stationNumber) {
    	
        while (!stopSimulation || !bookingQueue.isEmpty()) {
        	
            User user;
            synchronized (bookingQueue) {
                if (!bookingQueue.isEmpty()) {
                    
                	user = bookingQueue.poll();
                    
                } else {
                	
                    continue; // No vehicle in the queue
                	
                }
            }

            long waitingTime = System.currentTimeMillis() - user.getVehicle().getArrivalTime();

            if (waitingTime > maxWaitingTime) {
                
            	System.out.println(user.getUsername() + " waited too long and left the queue.");
            	
            } else {
                
            	boolean charged = chargingStations[stationNumber].chargeVehicle();
                
                if (charged) {
                    System.out.println(user.getUsername() + " is charging his vehicle battery at Station number " + (stationNumber + 1) +
                            "  (Waiting time: " + waitingTime / 1000 + " seconds)");
                    try {
                        Thread.sleep(5000); // Simulating charging time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    chargingStations[stationNumber].releaseStation();

                    System.out.println(user.getUsername() + " finished charging at Station number " + (stationNumber + 1));
                }
            }
        }
    }
    
    
    /*
    public void simulateCharging1() {
    	
    	//using ExecutorService for parallel execution
    	
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfChargingStations);

        // Start a thread to generate vehicles randomly
        executorService.submit(() -> {
            Random random = new Random();
            int vehicleCount = 0;
            while (!stopSimulation) {
                try {
                    Thread.sleep(random.nextInt(5000)); // Random arrival time from 0 to 5 seconds
                    Vehicle vehicle = new Vehicle(vehicleCount + 1);
                    synchronized (waitingQueue) {
                        waitingQueue.add(vehicle);
                    }
                    System.out.println("Vehicle number " + vehicle.getId() + " arrived  (Queue size: " + waitingQueue.size() + ")");
                    vehicleCount++;

                    if (vehicleCount >= maxSimulationVehicles) {
                        stopSimulation = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start threads to simulate charging
        for (int i = 0; i < numberOfChargingStations; i++) {
            int stationNumber = i;
            executorService.submit(() -> {
                while (!stopSimulation) {
                    Vehicle vehicle;
                    synchronized (waitingQueue) {
                        if (!waitingQueue.isEmpty()) {
                            vehicle = waitingQueue.poll();
                        } else {
                            continue; // number vehicle in the queue
                        }
                    }

                    // waiting time calculation
                    long waitingTime = System.currentTimeMillis() - vehicle.getArrivalTime(); 
                    
                    if (waitingTime > maxWaitingTime) {
                    	
                        System.out.println("Vehicle number " + vehicle.getId() + " waited too long and left the queue.");
                        
                    } else {
                    	
                        boolean charged = chargingStations[stationNumber].chargeVehicle();
                        
                        if (charged) {
                            System.out.println("Vehicle number " + vehicle.getId() + " is charging at Station number " + (stationNumber + 1) +
                                    "  (Waiting time: " + waitingTime / 1000 + " seconds)");
                            try {
                                Thread.sleep(5000); // Simulating charging time
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            
                            chargingStations[stationNumber].releaseStation();
                            
                            System.out.println("Vehicle number " + vehicle.getId() + " finished charging at Station number " + (stationNumber + 1));
                        }
                    }
                }
            });
        }

        // Shutdown the executor service after the simulation is done
        executorService.shutdown();
    }*/
}
