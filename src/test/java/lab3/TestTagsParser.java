package lab3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class TestTagsParser {
	@Test
	public void testCounter() {
		TagsParser parser = new TagsParser(HelperClass.getUrlForPage("TestPage.html"));
		assertEquals(6, parser.getTagsCount());
	}
	
	@Test
	public void testGetTagsCount(){
		TagsParser parser = new TagsParser(HelperClass.getUrlForPage("TestPage.html")) ;
		assertEquals("head", parser.getTagAt(2));
	}
}
