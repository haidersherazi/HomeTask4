

class User {
    private String username;
    private int userId;
    private String role; // "EXTERNAL" or "ADMIN"
    private Vehicle vehicle;
    

	public User(String role, int userId) {
        this.username = "User-" + userId;
        this.role = role;
        this.userId = userId;
        this.vehicle = new Vehicle(userId);
    }

    // Getters and setters
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}