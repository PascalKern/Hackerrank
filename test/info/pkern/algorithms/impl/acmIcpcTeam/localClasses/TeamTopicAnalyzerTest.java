package info.pkern.algorithms.impl.acmIcpcTeam.localClasses;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TeamTopicAnalyzerTest {

	private static TeamTopicAnalyzer ttAnalyzer;

	@BeforeClass
	public static void initClass() {
		ttAnalyzer = new TeamTopicAnalyzer();
		ttAnalyzer.addPersonTopics("10101");
		ttAnalyzer.addPersonTopics("11100");
		ttAnalyzer.addPersonTopics("11010");
		ttAnalyzer.addPersonTopics("00101");
		System.out.println(ttAnalyzer);
	}

	@Test
	public void testGetMaxPossibleTopics() {
		Integer exptectedPossibleMaxTopics = 5;
		assertEquals(exptectedPossibleMaxTopics, ttAnalyzer.getMaximalPossibleTopicsForTeams());
	}
	
	@Test
	public void testGetTeamsWithMaxTopicsCount() {
		Integer exptectedTeamsCountWithMaxTopics = 2;
		assertEquals(exptectedTeamsCountWithMaxTopics, ttAnalyzer.getTeamsWithMaxTopicsCount());
	}
}
