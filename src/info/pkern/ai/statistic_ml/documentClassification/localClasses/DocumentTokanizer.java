package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class DocumentTokanizer {

	private final String text;
	private List<String> tokens;
	private Integer nextToken = 0;
	private List<WordFilter> filters = new ArrayList<>();
	
	public DocumentTokanizer(String text) {
		this.text = text;
	}
	
	@Deprecated
	public DocumentTokanizer() {
		this("");
	}
	
	public List<String> tokanize(String text) {
		return tokanizeText(text);
	}
	
	public void addFilter(WordFilter filter) {
		filters.add(filter);
		tokens = filterWords(tokens);
	}
	
	public void replaceFilters(List<WordFilter> filters) {
		this.filters = filters;
		tokens = tokanizeText(text);
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
	
	//TODO Could also use some command classes (Analyzer) like with the filters to tokanize the string with different analyzers (words, n-grams, ...).
	private List<String> tokanizeText(String text) {
		
		//TODO Move to filter/analyzer class(es)
		/* Could also replace ALL non character chars with a space and remove double spaces afterwards?! */
		//Simplify the text
		text = text.toLowerCase();
		text = text.replaceAll("\\t", " ");
		text = text.replaceAll("\\r\\n", " ");
		
		//Treat punctuation
		text = text.replaceAll("\\b[,\\.:;!?] ", " ");
		
		//Treat text specific constructs - Experimental
		text = text.replaceAll("\\b-\\b", "");
		text = text.replaceAll("-", " ");
		text = text.replaceAll("[\"]", "");
		
		//Normalize spacings
		text = text.replaceAll(" {1,}", " ");
		
		
		
//		List<String> tokens = Arrays.asList(text.split("[\\s,\\.;:]"));
		List<String> tokens = Arrays.asList(text.trim().split("[\\s]"));
		return filterWords(tokens);
		
		/*
		StringTokenizer tokenizer = new StringTokenizer(text, "\n ,.;:'", false);
		List<String> filteredTokens = new ArrayList<>();
		boolean passFilter;
		while (tokenizer.hasMoreTokens()) {
			Iterator<WordFilter> filterIter = filters.iterator();
			String token = tokenizer.nextToken();
			passFilter = true;
			while (passFilter && filterIter.hasNext()) {
				passFilter = filterIter.next().doesPass(token);
			}
			if (passFilter) {
				filteredTokens.add(token);
			}
		}
		isDirty = false;
		return filteredTokens;
		*/
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
			tokens = tokanizeText(text);
		}
	}
}
