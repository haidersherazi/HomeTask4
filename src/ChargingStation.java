

import java.util.*;

public class ChargingStation {

	// ChargingStation class with user management and time slot booking
	
	private List<User> userList = new ArrayList<>();
    private Queue<User> bookingQueue = new PriorityQueue<>(Comparator.comparingInt(u -> u.getRole().equals("EXTERNAL") ? 1 : 0));

    private int availableChargingCapacity;

    public ChargingStation(int availableCapacity) {
        this.availableChargingCapacity = availableCapacity;
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
