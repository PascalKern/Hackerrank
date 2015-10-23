package sandbox.info.pkern.experiments;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StringMatchingAndPositioning {

	private String content = "iwehtüaaaa!oahaonalbaaanüaowretl346retl3fülh¨H!OAH)*932h";
	private String multiPosMatchPattern = "aaa";
	private String zeroPosNoMatchPattern = "-iweh";

	@Test
	public void matchInStringGetPosition() {
		String zeroPosMatchPattern = "iweht";
		String endPosMatchPattern = ")*932h";
		String endPosNoMatchPattern = "32h-";
		String caseMultiPosChangePattern = "!oah";
		
		getMatches(content, zeroPosMatchPattern);
		getMatches(content, endPosMatchPattern); 
		getMatches(content, zeroPosNoMatchPattern);
		getMatches(content, endPosNoMatchPattern);
		getMatches(content, multiPosMatchPattern);
		getMatches(content, caseMultiPosChangePattern);
	}
	
	@Test
	public void testMatchFromIndex() {
		System.out.println(matches(content, multiPosMatchPattern, 7));
		System.out.println(matches(content, multiPosMatchPattern, 8));
		System.out.println(matches(content, multiPosMatchPattern, 6));
		System.out.println(matches(content, zeroPosNoMatchPattern, 0));
	}
	
	@Test
	public void test() {
		List<Integer> testList = new ArrayList<>();
		for (Integer colInt : testList) {
			System.out.println(colInt);
		}
		testList.add(5);
		testList.add(45);
		for (Integer colInt : testList) {
			System.out.println(colInt);
		}
	}
	
	
	/*
	 * Better return a empty List instead of null?!
	 */
	private List<Integer> getMatches(String content, String pattern) {
//		List<Integer> matchPositions = new ArrayList<>();
		List<Integer> matchPositions = null;
		if (content.contains(pattern)) {
			matchPositions = new ArrayList<>();
			Integer matchPos = -1;
			while (0 <= (matchPos = content.indexOf(pattern, matchPos + 1))) {
				matchPositions.add(matchPos);
			}
			System.out.println(String.format("String: '%s' %-25s  %-10s at position(s): %s.", content, "contains pattern:", pattern, matchPositions));			
		} else {
			System.out.println(String.format("String: '%s' %-25s  %-10s [%s].", content, "does NOT contain pattern:", pattern, matchPositions));
		}
		return matchPositions;
	}
	
	private boolean matches(String content, String pattern, int ofIndex) {
		int matchedIndex = -1;
		if (content.contains(pattern)) {
//			matchedIndex = content.indexOf(pattern, ofIndex);
			matchedIndex = content.indexOf(pattern, ofIndex);
			System.out.println(String.format("String: '%s' %-26s %-10s in column: %05d.",content , "contains pattern:", pattern, ofIndex));
//			matchedIndex -= ofIndex;
		} else {
			System.out.println(String.format("String: '%s' %-25s %-10s in column: %05d.",content , "does NOT contain pattern:", pattern, ofIndex));
		}
		return ofIndex == matchedIndex; 
	}
	
}
