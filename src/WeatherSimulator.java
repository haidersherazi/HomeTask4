import java.util.Random;

public class WeatherSimulator {
    private static final Random random = new Random();

    public static String simulateWeather() {
        
    	int weatherCode = random.nextInt(2); // Simulate 3 weather conditions
    	
        switch (weatherCode) {
            case 0:
                return "Sunny";
            case 1:
                return "Cloudy";
            case 2:
                return "Rainy";
            default:
                return "Unknown";
        }
    }
}