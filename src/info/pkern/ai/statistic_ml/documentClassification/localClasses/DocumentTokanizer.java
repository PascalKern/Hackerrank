package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class DocumentTokanizer {

	private final String text;
	private List<String> tokens;
	private Integer nextToken = 0;
	private List<WordFilter> filters = new ArrayList<>();
	//Punctuation: One of !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
	private Pattern removeCharPattern = Pattern.compile("\\p{Punct}\\p{Space}\\p{Digit}");
	//Exclude: One of
	private Pattern excludedRemoveCharPattern = Pattern.compile("'-");
	
	public DocumentTokanizer(String text) {
		this.text = text;
	}
	
	@Deprecated
	public DocumentTokanizer() {
		this("");
	}
	
	public void addFilter(WordFilter filter) {
		filters.add(filter);
		tokens = filterWords(tokens);
	}
	
	public void replaceFilters(List<WordFilter> filters) {
		this.filters = filters;
		tokens = tokanize(text);
	}
	
	public List<WordFilter> getFilters() {
		return new ArrayList<>(filters);
	}

	public String nextToken() {
		checkTokanizerIsInitialized();
		if (tokens.size() == nextToken) {
			return null;
		}
		String token = tokens.get(nextToken);
		nextToken += 1;
		return token;
	}

	public List<String> getTokens() {
		checkTokanizerIsInitialized();
		return new ArrayList<>(tokens);
	}
	
	public Integer getNumberOfTokens() {
		checkTokanizerIsInitialized();
		return tokens.size();
	}
	
	private List<String> tokanize(String text) {
		
		//Remove unwanted chars like punctuation and more.
		text = text.replaceAll("["+removeCharPattern+"&&[^"+excludedRemoveCharPattern+"]]", " ");
		
		//Treat text specific constructs
		//negative lookahead = Word boundary NOT followed by a upper case char. Could also be pos. lookahead: (?=\\p{Lower})
		text = text.replaceAll("\\b-\\b(?!\\p{Upper})", "");	
		//positive lookahead = Word boundary followed by a upper case char.
		text = text.replaceAll("\\b-\\b(?=\\p{Upper})", " ");
		
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
	
	private void checkTokanizerIsInitialized() {
		if (null == tokens) {
			tokens = tokanize(text);
		}
	}
}
