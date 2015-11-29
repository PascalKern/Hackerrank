package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BagOfWords {

	private Integer maxTermFrequency = 0;
	private Map<String, Integer> termFrequencies = new HashMap<>();
	
	public void addTerm(String term, Integer frequency) {
		Integer newFrequency = new Integer(frequency);
		if (termFrequencies.keySet().contains(term)) {
			newFrequency += termFrequencies.get(term);
			termFrequencies.put(term, newFrequency);
		} else {
			termFrequencies.put(term, newFrequency);
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
	
	public void merge(BagOfWords bagOfWords) {
		for (Entry<String, Integer> entry : bagOfWords.termFrequencies.entrySet()) {
			addTerm(entry.getKey(), entry.getValue());
		}
	}

	public Integer removeTerm(String term) {
		Integer frequency = termFrequencies.remove(term);
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
		terms.putAll(this.termFrequencies);
		this.termFrequencies.clear();
		maxTermFrequency = 0;
		return terms;
	}
	
	/**
	 * Gets a copy of the terms in this bag. Changes to this set are <strong>not</strong> be reflected to this bag!
	 *
	 * @return the terms in this bag.
	 */
	public Set<String> getTerms() {
		return Collections.unmodifiableSet(termFrequencies.keySet());
	}

	public Integer getNumberOfTerms() {
		return termFrequencies.size();
	}

	public boolean contains(String term) {
		return termFrequencies.containsKey(term);
	}

	public Integer getFrequency(String term) {
		if (termFrequencies.containsKey(term)) {
			return termFrequencies.get(term);
		} else { 
			return 0;
		}
	}

	public Set<String> termsNotIn(BagOfWords otherBag) {
		return termsNotIn(otherBag.getTerms());
	}
	
	public Set<String> termsNotIn(Collection<String> terms) {
		Set<String> notYetInThisBag = new HashSet<>(getTerms());
		notYetInThisBag.removeAll(terms);
		return notYetInThisBag;
	}
	
	public Map<String, Integer> getFrequencies() {
		return Collections.unmodifiableMap(termFrequencies);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((maxTermFrequency == null) ? 0 : maxTermFrequency.hashCode());
		result = prime * result + ((termFrequencies == null) ? 0 : termFrequencies.hashCode());
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
		if (termFrequencies == null) {
			if (other.termFrequencies != null)
				return false;
		} else if (!termFrequencies.equals(other.termFrequencies))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BagOfWords [maxTermFrequency=");
		builder.append(maxTermFrequency);
		builder.append(", termCount=");
		builder.append(getNumberOfTerms());
		builder.append(", termFrequencies=");
		builder.append(termFrequencies);
		builder.append("]");
		return builder.toString();
	}
}
