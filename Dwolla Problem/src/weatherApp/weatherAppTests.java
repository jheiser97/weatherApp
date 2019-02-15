/**
 * @author Josh Heiser
 */
package weatherApp;

import static org.junit.Assert.*;

import org.junit.Test;

public class weatherAppTests {
	@Test
	public void ConvertTempTest() {
		//test when you do not want units converted to SI
		//(273 - 273) * (5/9) + 32 is how to get 32 as the expected ouput
		assertEquals(32, WeatherApp.ConvertTemp(273));
		WeatherApp.convert = true;
		//tests when units should be converted to SI
		//273 - 273 = 0 is how I got expected output
		assertEquals(0, WeatherApp.ConvertTemp(273));
	}
	@Test
	public void ConvertWindSpeedTest() {
		WeatherApp.convert = false;
		//test wind speed when it is in imperial units
		//22 = (10 * 2.237) is how I got the expected output
		assertEquals(22, WeatherApp.ConvertWindSpeed(10));
		
		//test when you want units in SI
		WeatherApp.convert = true;
		assertEquals(10, WeatherApp.ConvertWindSpeed(10));	
		
	}
	@Test
	public void FindWindDirectionTests(){
		//test wind from the north
		assertEquals(WeatherApp.FindWindDirection(0), "North");
		
		//test wind from the NNE
		assertEquals(WeatherApp.FindWindDirection(20), "NNE");
		
		//test wind from the NE
		assertEquals(WeatherApp.FindWindDirection(40), "NE");
		
		//test wind from the ENE
		assertEquals(WeatherApp.FindWindDirection(60), "ENE");
				
		//test wind from the East
		assertEquals(WeatherApp.FindWindDirection(85), "East");
		
		//test wind from the ESE
		assertEquals(WeatherApp.FindWindDirection(115), "ESE");
		
		//test wind from the SE
		assertEquals(WeatherApp.FindWindDirection(135), "SE");
		
		//test wind from the SSE
		assertEquals(WeatherApp.FindWindDirection(156), "SSE");
		
		//test wind from the South
		assertEquals(WeatherApp.FindWindDirection(180), "South");
		
		//test wind from the SSW
		assertEquals(WeatherApp.FindWindDirection(200), "SSW");
		
		//test wind from the SW
		assertEquals(WeatherApp.FindWindDirection(225), "SW");
		
		//test wind from the WSW
		assertEquals(WeatherApp.FindWindDirection(250), "WSW");
		
		//test wind from the West
		assertEquals(WeatherApp.FindWindDirection(270), "West");
		
		//test wind from the WNW
		assertEquals(WeatherApp.FindWindDirection(290), "WNW");
		
		//test wind from the NW
		assertEquals(WeatherApp.FindWindDirection(310), "NW");
		
		//test wind from the NNW
		assertEquals(WeatherApp.FindWindDirection(330), "NNW");
	}

}
