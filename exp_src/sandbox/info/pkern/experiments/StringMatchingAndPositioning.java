package sandbox.info.pkern.experiments;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StringMatchingAndPositioning {

	@Test
	public void matchInStringGetPosition() {
		String content = "iwehtüaaa!oahaonalbaaanüaowretl346retl3fülh¨H!OAH)*932h";
		String zeroPosMatchPattern = "iweht";
		String endPosMatchPattern = ")*932h";
		String zeroPosNoMatchPattern = "-iweh";
		String endPosNoMatchPattern = "32h-";
		String multiPosMatchPattern = "aaa";
		String caseMultiPosChangePattern = "!oah";
		
		match(content, zeroPosMatchPattern);
		match(content, endPosMatchPattern); 
		match(content, zeroPosNoMatchPattern);
		match(content, endPosNoMatchPattern);
		match(content, multiPosMatchPattern);
		match(content, caseMultiPosChangePattern);
	}
	
	/*
	 * Better return a empty List instead of null?!
	 */
	private List<Integer> match(String content, String pattern) {
		List<Integer> matchPositions = new ArrayList<>();
//		List<Integer> matchPositions = null;
		if (content.contains(pattern)) {
//			matchPositions = new ArrayList<>();
			Integer matchPos = -1;
			while (0 <= (matchPos = content.indexOf(pattern, matchPos + 1))) {
				matchPositions.add(matchPos);
			}
			System.out.println(String.format("String: '%s' %-25s  %-10s at position(s): %s.", content, "contains pattern:", pattern, matchPositions));			
		} else {
			System.out.println(String.format("String: '%s' %-25s  %-10s (%s).", content, "does NOT contain pattern:", pattern, matchPositions));
		}
		return matchPositions;
	}
	
	
}
