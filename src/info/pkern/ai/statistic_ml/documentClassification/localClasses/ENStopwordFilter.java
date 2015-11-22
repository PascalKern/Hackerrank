package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ENStopwordFilter extends AbstractFilterFromFile {

	private final String CONJUNCTIONS_FILE = "stop_words.txt";
	
	public ENStopwordFilter() throws IOException {
		try (InputStream is = getClass().getResourceAsStream(CONJUNCTIONS_FILE)) {
			wordsToFilterOut = readWordsFromFile(is);
		}
	}
	
	@Override
	public boolean doesPass(String word) {
		return ! wordsToFilterOut.contains(word);
	}

	@Override
	public boolean doesNotPass(String word) {
		return wordsToFilterOut.contains(word);
	}

}
