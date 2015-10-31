package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class DocumentTokanizerTest {

	private final String TEST_STRING = "1 champion products ch approves stock split champion products inc said "
			+ "its board of directors approved a two for one stock split of its common shares for shareholders "
			+ "of record as of april the company also said its board voted to recommend to shareholders at the "
			+ "annual meeting april an increase in the authorized capital stock from five mln to mln shares reuter";

	private final String TEST_STRING2 = "1 champion products ch approves stock split champion products inc said "
			+ "its board of directors - approved a two, for one stock split of its common shares for shareholders "
			+ "of record as of april \"the company\" also said: its board   voted to, recommend to shareholders at the "
			+ "annual meeting april an increase in the authorized capital-stock from five mln to mln blub shares reuter";

	private final DocumentTokanizer tokanizer = new DocumentTokanizer(TEST_STRING);
	
	
	@Test
	public void testGetTokens() throws Exception {
		assertTrue(tokanizer.getTokens().contains("champion"));
		assertTrue(tokanizer.getTokens().contains("one"));
		assertTrue(tokanizer.getTokens().contains("ch"));
		assertTrue(tokanizer.getTokens().contains("of"));
	}


	@Test
	public void testNextTokenTokanizerInitialized() throws Exception {
		assertTrue(tokanizer.nextToken().equals("1"));
		assertTrue(tokanizer.nextToken().equals("champion"));
		assertTrue(tokanizer.nextToken().equals("products"));
		assertTrue(tokanizer.nextToken().equals("ch"));
	}


	@Test
	public void testAddLengthFilter() throws Exception {
		assertTrue(tokanizer.nextToken().equals("1"));
		assertTrue(tokanizer.nextToken().equals("champion"));
		assertTrue(tokanizer.nextToken().equals("products"));
		assertTrue(tokanizer.nextToken().equals("ch"));
		
		tokanizer.addFilter(new MinLengthFilter(3));

		assertFalse(tokanizer.getTokens().contains("1"));
		assertTrue(tokanizer.getTokens().contains("champion"));
		assertTrue(tokanizer.getTokens().contains("products"));
		assertTrue(tokanizer.getTokens().contains("one"));
		assertFalse(tokanizer.getTokens().contains("ch"));
	}

	@Test
	public void testMultipleFilter() throws IOException {
		assertTrue(tokanizer.getTokens().contains("ch"));
		assertTrue(tokanizer.getTokens().contains("champion"));
		assertTrue(tokanizer.getTokens().contains("products"));
		assertTrue(tokanizer.getTokens().contains("for"));
		assertTrue(tokanizer.getTokens().contains("1"));
		assertTrue(tokanizer.getTokens().contains("one"));
		
		tokanizer.addFilter(new MinLengthFilter(3));
		tokanizer.addFilter(new ENConjunctionsFilter());
		
		assertFalse(tokanizer.getTokens().contains("ch"));
		assertTrue(tokanizer.getTokens().contains("champion"));
		assertTrue(tokanizer.getTokens().contains("products"));
		assertFalse(tokanizer.getTokens().contains("for"));
		assertFalse(tokanizer.getTokens().contains("1"));
		assertTrue(tokanizer.getTokens().contains("one"));
	}

	@Test
	public void testAddConjunctionsFilter() throws Exception {
		assertTrue(tokanizer.getTokens().contains("for"));
		assertTrue(tokanizer.getTokens().contains("as"));
		assertTrue(tokanizer.getTokens().contains("the"));
		
		tokanizer.addFilter(new ENConjunctionsFilter());
		
		assertFalse(tokanizer.getTokens().contains("for"));
		assertFalse(tokanizer.getTokens().contains("as"));
		assertTrue(tokanizer.getTokens().contains("the"));
		
	}


	@Test
	public void testGetNumberOfTokens() throws Exception {
		assertEquals("Tokenscount after opening the tokanizer is not correct!", new Integer(63), tokanizer.getNumberOfTokens());
		tokanizer.addFilter(new MinLengthFilter(3));
		assertEquals("Tokenscount after opening the tokanizer is not correct!", new Integer(49), tokanizer.getNumberOfTokens());
	}

	@Test
	public void testGetNumberOfTokens2() throws Exception {
		DocumentTokanizer tokanizer = new DocumentTokanizer(TEST_STRING2);
		assertEquals("Tokenscount after opening the tokanizer is not correct!", new Integer(63), tokanizer.getNumberOfTokens());
		tokanizer.addFilter(new MinLengthFilter(3));
		assertEquals("Tokenscount after opening the tokanizer is not correct!", new Integer(49), tokanizer.getNumberOfTokens());
	}
	
}
