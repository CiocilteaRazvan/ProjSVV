package lab3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStringProvider {

	@Test
	void testStringContent() {
		String expected = "<html><head><title>Page under construction</title></head><body><center><b>Page under construction</b></body></html>";
		HttpStringProvider provider = new HttpStringProvider();	
		assertEquals(expected, provider.getStringForAddress(HelperClass.getUrlForPage("TestPage.html")));
	}
}
