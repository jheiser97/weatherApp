/**
 * @author Josh Heiser
 */
package weatherApp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.json.*;
public class WeatherApp {
	
	//value that represents if the user wants the values to be converted to the SI system or if they want the values to be returned in the imperial system.
	protected static boolean convert = false;	
	
											//INFORMATION ABOUT THE GLOBAL VARIABLES\\
						//The following static values are global variables instead of local variables because
						//it is possible for the API to not pass the wind direction angle if the wind is calm
						//in that location. This means that in order to print the weather information when there
						//is no wind, the weather information will need to be printed in the catch statement. Because the
						//catch statement cannot see the local variables in the try statement where we originally
						//calculate these values, these variable must be global so that both the try and catch
						//statements can access this information.
	
	
	//integer that represents the angle that the wind is blowing from (0-360 degrees). 
	private static int directionDegree = 0;
	
	//value that represents the current temperature in the location that the user has specified
	private static int temp = 0;
	
	//integer that represents the maximum temperature in that location
	private static int maxTemp = 0;
	
	//integer that represents the lowest temperature in that location
	private static int minTemp = 0;
	
	//String that represents the direction that the wind is blowing from
	private static String direction = "";
	
	//integer that represents the speed of the wind
	private static int speed = 0;
	
	public static void main(String[] args) {
		while (true){
			//string that keeps track of user input. This input will either be a city or a signal to end the program.
			String location = "";
			System.out.println("Please Enter a city to get its current weather conditions or type Exit to end the program.");
			Scanner s = new Scanner (System.in);
			//waits for the user to place input
			while (location.equals("")){
				location+= s.nextLine();
				//checks if it is empty or null (this prevents the API from searching for a blank name or NULL for the city)
				if (location.equals("") || location.equals(null)){
					System.out.println("This is an invalid input, please insert another city.");
				}
				//this checks if the user entered exit. If they did, then the program should end
				if (location.equals("exit") || location.equals("Exit")|| location.equals("EXIT")){
					System.exit(0);
		
				}
				System.out.println("Do you want SI units (Celsius, m/s) or Imperial units (Fahrenheit, mph)?");
				System.out.println("Yes = SI, No = Imperial. This program defaults to Imperial if anything besides Yes is entered.");
				//variable that keeps track of the unit that the user wants the weather to return as
				String unit = s.nextLine();
				//if the user has inputted yes, then we want the values to return as SI and thus needs to be converted
				if (unit.equals("yes") || unit.equals("YES") || unit.equals("Yes")){
					convert = true;
				}
			}
			getWeatherConditions(location);
			//This resets the conversion to false. This is so if the user wants to lookup another city, it will not automatically 
			//give the user SI units (since it would be true always otherwise).
			convert = false;
		}
	}
	
	/**
	 * This method calls the OpenWeatherMap API to get weather information from a certain location and prints the results to the user.
	 * @param location The city that the user has requested weather information for.
	 */
	public static void getWeatherConditions (String location) {
		try {
			//Beginning part of the url that is used to connect to the OpenWeather API to get our data from
			String url = "http://api.openweathermap.org/data/2.5/weather?q=";
			//API key used to connect to the OpenWeather API
			String API = "&APPID=844107e94c850ace5d28ba5043d4e625";
			//the total URL that will sent to the OpenWeather API
			String updatedURL = url + location + API;
			//goes to the URL, and stores the returned JSON as a JSONObject
			JSONObject json = new JSONObject(IOUtils.toString(new URL(updatedURL), Charset.forName("UTF-8")));
			
			//gets the current condition at the location that the user has specified
			JSONObject condition = json.getJSONArray("weather").getJSONObject(0);
			System.out.println("The current condition in " + location + " is " + condition.getString("description"));
			
			//this gets the current temperature
			JSONObject temperature = json.getJSONObject("main");
			double temp2 = temperature.getDouble("temp");
			temp = ConvertTemp(temp2);
			
			//the maximum temperature is found below
			JSONObject tempMax = json.getJSONObject("main");
			double max = tempMax.getDouble("temp_max");
			maxTemp = ConvertTemp(max);

			
			//the minimum temperature is found below
			JSONObject tempMin = json.getJSONObject("main");
			double min = tempMin.getDouble("temp_min");
			 minTemp = ConvertTemp(min);
			
			//gets the current windspeed
			JSONObject windSpeed = json.getJSONObject("wind");
			speed = windSpeed.getInt("speed");
			directionDegree = windSpeed.getInt("deg");
			speed = ConvertWindSpeed(speed);
			//String direction = FindWindDirection(directionDegree);
			direction = FindWindDirection(directionDegree);
			
			//checks if the user has specified for the data to be returned as SI. If so, it will print the statements in this loop
			if (convert){
				System.out.println("The current temperature is " + temp +" C");
				System.out.println("The current windspeed is from the " + direction + " at " + speed + " m/s");
				System.out.println("The High temperature was " + maxTemp + " C");
				System.out.println("The low temperature was " + minTemp + " C");
			}
			//since the user has not specified that it should be in SI, it will be returned in Imperial units instead.
			else {
				System.out.println("The current temperature is " + temp + " F");
				System.out.println("The current windspeed is from the " + direction + " at " + speed + " mph");
				System.out.println("The High temperature was " + maxTemp + " F");
				System.out.println("The low temperature was " + minTemp + " F");


			}
		}
		catch (MalformedURLException m){
			System.out.println("An error occured with the URL passed through the API. Additional information is shown below");
			m.printStackTrace();
		}
		catch (JSONException j){
			//since the wind is calm in this location, it will throw a JSONException (since it cannot find the wind degree
			//this means that we must print the weather information in the catch statement instead of the try statement. 
			if (convert){
				System.out.println("The current temperature is " + temp +" C");
				System.out.println("The current windspeed is from the " + direction + " at " + speed + " m/s");
				System.out.println("The High temperature was " + maxTemp + " C");
				System.out.println("The low temperature was " + minTemp + " C");
			}
			System.out.println("The current temperature is " + temp + " F");
			System.out.println("The current windspeed is from the " + direction + " at " + speed + " mph");
			System.out.println("The High temperature was " + maxTemp + " F");
			System.out.println("The low temperature was " + minTemp + " F");
		}
		catch (IOException e){
			System.out.println("Oops! That city doesn't exist. Please Enter another location or try again.");
		}
	}
	
	/**
	 * This method converts the temperature from Kelvin to Celsius or Fahrenheit (depends on user preference).
	 * @param temperature The temperature that is returned from the OpenWeather API.
	 * @return Integer that represents the temperature in the proper unit that the user has set.
	 */
	public static int ConvertTemp (double temperature){
		//checks if the user wanted the temperature to be in SI.
		//Kelvin to Celsius = temperature - 273
		if (convert) {
			//if so, converts to SI units
			temperature = temperature - 273;
			return (int) temperature;
		}
		//since the user does not want the value to be in SI, it is converted to imperial units
		//Celsius to Fahrenheit = (9/5 * temp) + 32
		temperature = temperature - 273;
		temperature = ((temperature * (1.8)) + 32);
		return (int) temperature;
	}
	
	/**
	 * This method takes in the wind speed that was returned from the OpenWeather API and converts it mph if user wants imperial data returned.
	 * @param windSpeed The wind Speed that was returned by the OpenWeather API.
	 * @return Integer that represents the wind speed in the correct unit that the user specified.
	 */
	public static int ConvertWindSpeed (int windSpeed){
		//checks to see if the user wants the values to be returned SI. if so, then the value returned by the OpenWeather API does not be changed.
		if (convert){
			return windSpeed;
		}
		//since it is not in SI, it needs to be converted to Imperial units.
		//conversion from m/s (what OpenweatherMap API returns) to mph = (speed * 2.237);
		return windSpeed = (int) (windSpeed * 2.237);
	}
	
	/**
	 * This method converts the integer that represents the degree that the wind is coming from and converts it to a String that represents the direction
	 * that the wind is coming from.
	 * @param directionDegree Integer that represents the degree that the wind is coming from (from 0-360).
	 * @return String that represents the direction that the wind is blowing from.
	 */
	public static String FindWindDirection (int directionDegree){
		//these values were calculated using this URL: http://snowfence.umn.edu/Components/winddirectionanddegreeswithouttable3.htm
		//depending on the angle that is given to use by the OpenWeather API, it will represent a certain direction
		//we can use this to determine and return the direction that the wind is blowing from.
		if (directionDegree < 11 || directionDegree > 348){
			return "North";
		}
		if (directionDegree >= 11 && directionDegree <= 33){
			return "NNE";
		}
		if (directionDegree > 33 && directionDegree < 56){
			return "NE";
		}
		if (directionDegree >= 56 && directionDegree < 79){
			return "ENE";
		}
		if (directionDegree >= 79 && directionDegree < 101){
			return "East";
		}
		if (directionDegree >= 101 && directionDegree < 124){
			return "ESE";
		}
		if (directionDegree >= 124 && directionDegree < 146){
			return "SE";
		}
		if (directionDegree >= 146 && directionDegree < 169){
			return "SSE";
		}
		if (directionDegree >= 169 && directionDegree <= 191){
			return "South";
		}
		if (directionDegree > 191 && directionDegree <= 214){
			return "SSW";
		}
		if (directionDegree > 214 && directionDegree < 236){
			return "SW";
		}
		if (directionDegree >= 236 && directionDegree <= 259){
			return "WSW";
		}
		if (directionDegree > 259 && directionDegree < 281){
			return "West";
		}
		if (directionDegree >= 281 && directionDegree <= 304 ){
			return "WNW";
		}
		if (directionDegree > 304 && directionDegree < 326){
			return "NW";
		}
		if (directionDegree >= 326 && directionDegree <= 348 ){
			return "NNW";
		}
		return "This is not a proper angle that can be used to calculate wind direction";
	}
}
