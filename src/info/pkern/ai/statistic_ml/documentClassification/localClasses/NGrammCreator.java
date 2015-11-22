package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.ArrayList;
import java.util.List;

public class NGrammCreator {

	private final int ngramSize;
	
	public NGrammCreator(int ngramSize) {
		this.ngramSize = ngramSize;
	}
	
	public List<String> process(List<String> words) {
		List<String> ngrams = new ArrayList<>();
		
		int ngramWindowLowerBound;
		int ngramWindowUpperBound;
		int wordsCount = words.size();
		for (int i = 1; i < wordsCount+ngramSize; i++) {
			ngramWindowLowerBound = calculateNewLowerBound(i);
			ngramWindowUpperBound = calculateNewUpperBound(i, wordsCount);
			
			List<String> ngram = words.subList(ngramWindowLowerBound, ngramWindowUpperBound);
			ngrams.add(ngram.toString().replaceAll("[\\[\\],]", ""));
		}
		
		return ngrams;
	}

	private int calculateNewUpperBound(int wordIndex, int size) {
		if (wordIndex < size) {
			return wordIndex;
		} else {
			return size;
		}
	}

	private int calculateNewLowerBound(int wordIndex) {
		if (wordIndex < ngramSize) {
			return 0;
		} else {
			return wordIndex - ngramSize;
		}
	}
	
}
