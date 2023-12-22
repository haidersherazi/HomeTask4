
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.Random;

public class ChargingSimulator {
    private static final int numberOfChargingStations = 5; // Adjust as needed but currently there are 5 
    private static final int maxWaitingTime = 15 * 60 * 1000; // 15 minutes in milliseconds
//    private static final int maxWaitingTime = 1000; //use 1 second time to run the queue leaving use case too of the vehicle
    private static final int maxSimulationVehicles = 15; // Adjust as needed but currently there are 15 vehicles
    
    private Queue<Vehicle> waitingQueue = new LinkedList<>();
    private ChargingStation[] chargingStations;
    private volatile boolean stopSimulation = false;
    
    public ChargingSimulator() {
        chargingStations = new ChargingStation[numberOfChargingStations];
        for (int i = 0; i < numberOfChargingStations; i++) {
            chargingStations[i] = new ChargingStation(1); // Each station can charge 1 vehicle
//            chargingStations[i] = new ChargingStation(2); // Each station can charge 2 vehicle
        }
    }

    public void simulateCharging() {
    	
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
    }
}
