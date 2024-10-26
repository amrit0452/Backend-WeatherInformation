package MyPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.util.Date;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	
	
	// Start ===>>>
	private static final long serialVersionUID = 1L;
	
    private final String apiKey = "bd5cfe3aab1617a994bf69f3af6cd63b";
	
       
   
    public MyServlet() {
        super();
              
    }

	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pincode = request.getParameter("pincode");
		Date currentDate = new Date(System.currentTimeMillis());
        
        DatabaseHelper dbHelper = new DatabaseHelper();
        WeatherData weatherData = dbHelper.getWeatherData(pincode,currentDate);
		
		
        if (weatherData != null) {
            sendJsonResponse(response, weatherData);
        } else {
            // Fetch data from OpenWeatherMap API if not cached
            weatherData = fetchWeatherFromAPI(pincode);
            if (weatherData != null) {
                dbHelper.SaveWeatherData(pincode, currentDate , weatherData);
                sendJsonResponse(response, weatherData);
            } else {
                response.getWriter().println("Unable to fetch weather data.");
            }
        }
		
	}


	
	
	// MEthod to Fetch weather data 
	 private WeatherData fetchWeatherFromAPI(String pincode) throws IOException {
	        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?zip=" + pincode + ",in&appid=" + apiKey;
	        URL url = new URL(apiUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
	        Scanner scanner = new Scanner(reader);
	        StringBuilder responseContent = new StringBuilder();
	        
	        while (scanner.hasNext()) {
	            responseContent.append(scanner.nextLine());
	        }
	        scanner.close();
	        Gson gson = new Gson();
	        JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
	        
	        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
	        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
	        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
	        String weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

	        return new WeatherData(temperatureCelsius, humidity, weather);
	    }
	 
	 
		 private void sendJsonResponse(HttpServletResponse response, WeatherData weatherData) throws IOException {
		        response.setContentType("application/json");
		        Gson gson = new Gson();
		        String jsonResponse = gson.toJson(weatherData);
		        PrintWriter out = response.getWriter();
		        out.print(jsonResponse);
		    }
		        
	        

}
