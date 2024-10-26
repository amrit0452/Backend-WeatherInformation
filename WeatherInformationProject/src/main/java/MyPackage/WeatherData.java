package MyPackage;

import java.time.LocalDate;

public class WeatherData {
	 private int temperatureCelsius;
	    private int humidity;
	    private String weather;

	    // Default constructor (if needed)
	    public WeatherData() {}

	    // Constructor with temperature, humidity, and weather parameters
	    public WeatherData(int temperatureCelsius, int humidity, String weather) {
	        this.temperatureCelsius = temperatureCelsius;
	        this.humidity = humidity;
	        this.weather = weather;
	    }
	    
	    //Getter method for temperaturCelsius
	    public int getTemperatureCelsius() { return temperatureCelsius; }

	    // Getter method for humidity
	    public int getHumidity() { return humidity; }

	    // Getter method for weather condition
	    public String getWeather() { return weather; }

}
