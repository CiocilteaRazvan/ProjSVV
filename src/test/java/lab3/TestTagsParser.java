package lab3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static lab3.HelperClass.getUrlForPage;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito.*;

public class TestTagsParser {
	TagsParser parser;

	@Test
	void testCounter() {
		parser = new TagsParser(getUrlForPage("TestPage.html")) ;
		assertEquals("head", parser.getTagAt(2));
	}
	
	@Test
	void testGetTagsCount(){
		parser = new TagsParser(getUrlForPage("TestPage.html"));
		assertEquals(6, parser.getTagsCount());
	}

	@Test
	void testZeroTagPage() {
		parser = new TagsParser(getUrlForPage("ZeroTag.html"));
		assertEquals(0, parser.getTagsCount());
	}

	@Test
	void testEmptyPage() {
		parser = new TagsParser(getUrlForPage("Empty.html"));
		assertEquals(0, parser.getTagsCount());
	}

	@Test
	void testOneTagPage() {
		parser = new TagsParser(getUrlForPage("OneTag.html"));
		assertEquals(2, parser.getTagsCount());
	}

	@Test
	void testTwoTagsPage() {
		parser = new TagsParser(getUrlForPage("TwoTags.html"));
		assertEquals(2, parser.getTagsCount());
	}
}
