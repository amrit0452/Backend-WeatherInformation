package MyPackage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {
	
	private static final String url = "jdbc:mysql://localhost:3306/weather_database";
	private static final String username = "root"; 
	private static final String password = "somepassword [confidential]"; 
	
	 
	 
	private Connection connect() throws SQLException {
        try {
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle error if the driver is not found
        }
        return DriverManager.getConnection(url, username, password);
    }
	
	
	
	public WeatherData getWeatherData(String pincode, Date date) {
		String query = "SELECT temperatureCelsius , humidity , weather FROM WeatherInfo WHERE pincode = ? AND date = ?";
		
		try(Connection con = connect();PreparedStatement pstatement = con.prepareStatement(query)){
			pstatement.setString(1,pincode);
			pstatement.setDate(2,date);
			
			ResultSet rs = pstatement.executeQuery();
			
			if(rs.next()) {
				return new WeatherData(rs.getInt("temperatureCelsius"),rs.getInt("humidity"),rs.getString("weather"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void SaveWeatherData(String pincode, Date date, WeatherData weatherData) {
		String query = "INSERT INTO WeatherInfo (pincode, date, temperatureCelsius, humidity, weather) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = connect();
	             PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, pincode);
	            statement.setDate(2, date);
	            statement.setInt(3, weatherData.getTemperatureCelsius());
	            statement.setInt(4, weatherData.getHumidity());
	            statement.setString(5, weatherData.getWeather());
	            statement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	
}
