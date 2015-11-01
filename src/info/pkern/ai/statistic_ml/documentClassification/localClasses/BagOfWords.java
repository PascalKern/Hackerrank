package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BagOfWords {

	private Integer maxTermFrequency = 0;
	private Map<String, Integer> terms = new HashMap<>();

	public void addTerm(String term, Integer frequency) {
		int newFrequency = frequency;
		if (terms.keySet().contains(term)) {
			newFrequency += terms.get(term);
			terms.put(term, newFrequency);
		} else {
			terms.put(term, newFrequency);
		}
		maxTermFrequency = Math.max(newFrequency, maxTermFrequency);
	}
	
	public void addTerm(String term) {
		addTerm(term, 1);
	}
	
	public void addTerms(Collection<String> terms) {
		for (String term : terms) {
			addTerm(term, 1);
		}
	}
	
	public void add(BagOfWords bagOfWords) {
		for (Entry<String, Integer> entry : bagOfWords.terms.entrySet()) {
			addTerm(entry.getKey(), entry.getValue());
		}
	}

	public Integer removeTerm(String term) {
		Integer frequency = terms.remove(term);
		if (null == frequency) {
			return 0;
		} else {
			return frequency;
		}
	}
	
	public List<Entry<String, Integer>> removeTerms(List<String> terms) {
		List<Entry<String, Integer>> removed = new ArrayList<>(terms.size());
		for (String term : terms) {
			Entry<String, Integer> entry = new SimpleEntry<>(term, removeTerm(term));
			removed.add(entry);
		}
		return removed;
	}
	
	public Map<String, Integer> clear() {
		Map<String, Integer> terms = new HashMap<>();
		terms.putAll(this.terms);
		this.terms.clear();
		maxTermFrequency = 0;
		return terms;
	}
	
	public Integer getFrequency(String term) {
		if (terms.containsKey(term)) {
			return terms.get(term);
		} else { 
			return 0;
		}
	}
	
	public Double getNormalizedFrequency(String term) {
		Integer frequency = terms.get(term);
		if (null == frequency) {
			return 0d;
		} else {
			return new Double((double)frequency/maxTermFrequency);
		}
	}
	
	/**
	 * Gets a copy of the terms in this bag. Changes to this set are <strong>not</strong> be reflected to this bag!
	 *
	 * @return the terms in this bag.
	 */
	public Set<String> getTerms() {
		return new HashSet<>(this.terms.keySet());
	}
	
	public Integer getNumberOfTerms() {
		return terms.size();
	}
	
	public boolean contains(String term) {
		return terms.containsKey(term);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((maxTermFrequency == null) ? 0 : maxTermFrequency.hashCode());
		result = prime * result + ((terms == null) ? 0 : terms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BagOfWords other = (BagOfWords) obj;
		if (maxTermFrequency == null) {
			if (other.maxTermFrequency != null)
				return false;
		} else if (!maxTermFrequency.equals(other.maxTermFrequency))
			return false;
		if (terms == null) {
			if (other.terms != null)
				return false;
		} else if (!terms.equals(other.terms))
			return false;
		return true;
	}
}
