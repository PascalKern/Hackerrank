package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NGrammCreatorTest {

	private List<String> words = new ArrayList<>();
	
	@Before
	public void init() {
		String[] words = "A new text writen by me gmail.com. This isn't a long text so what.".split(" ");
		this.words = Arrays.asList(words);
	}

	@Test
	public void testProcess() throws Exception {
		NGrammCreator nGrammCreator = new NGrammCreator(3);
		List<String> processed = nGrammCreator.process(words);
		System.out.println(processed);
		assertEquals("A", processed.get(0));
		assertEquals("A new", processed.get(1));
		assertEquals("A new text", processed.get(2));
		assertEquals("new text writen", processed.get(3));
		assertEquals(16, processed.size());
		assertEquals("long text so", processed.get(16-4));
		assertEquals("text so what.", processed.get(16-3));
		assertEquals("so what.", processed.get(16-2));
		assertEquals("what.", processed.get(16-1));
	}
	
}
