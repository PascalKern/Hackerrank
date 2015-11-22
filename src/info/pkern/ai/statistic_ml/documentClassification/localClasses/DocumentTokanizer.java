package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class DocumentTokanizer {

	private List<WordFilter> filters = new ArrayList<>();
	//Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
	//Space: A whitespace character: [ \t\n\x0B\f\r]
	//Digit: A decimal digit: [0-9]
	private Pattern removeCharPattern = Pattern.compile("\\p{Punct}\\p{Space}\\p{Digit}");
	//Exclude: One of
	private Pattern excludedRemoveCharPattern = Pattern.compile("'-");
	
	public void addFilter(WordFilter filter) {
		filters.add(filter);
	}
	
	public List<WordFilter> getFilters() {
		return filters;
	}

	public List<String> tokanize(String text) {
		
		//Remove unwanted chars like punctuation and more.
		text = text.replaceAll("["+removeCharPattern+"&&[^"+excludedRemoveCharPattern+"]]", " ");
		
		//Treat text specific constructs
		//negative lookahead = Word boundary NOT followed by a upper case char. Could also be pos. lookahead: (?=\\p{Lower})
		text = text.replaceAll("\\b-\\b(?!\\p{Upper})", "");	
		//positive lookahead = Word boundary followed by a upper case char.
		text = text.replaceAll("\\b-\\b(?=\\p{Upper})", " ");
		text = text.replaceAll("-", "");
		
		//Normalize spacings and case
		text = text.replaceAll("\\p{Space}{2,}", " ");
		text = text.toLowerCase();
		
		List<String> tokens = Arrays.asList(text.trim().split("\\p{Space}"));
		return filterWords(tokens);
		
	}

	private List<String> filterWords(List<String> words) {
		List<String> tokens = new ArrayList<>();
		boolean passFilter;
		for (String word : words) {
			Iterator<WordFilter> filterIter = filters.iterator();
			passFilter = true;
			while (passFilter && filterIter.hasNext()) {
				passFilter = filterIter.next().doesPass(word);
			}
			if (passFilter) {
				tokens.add(word);
			}
		}
		return tokens;
	}
}
