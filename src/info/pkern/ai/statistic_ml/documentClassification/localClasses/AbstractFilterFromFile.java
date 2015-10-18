package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractFilterFromFile implements WordFilter {

	protected Set<String> wordsToFilterOut = new HashSet<String>();

	protected Set<String> readWordsFromFile(InputStream conjunctionsFile) throws IOException {
		InputStreamReader isr = new InputStreamReader(conjunctionsFile);
		try (BufferedReader bfr = new BufferedReader(isr)) {
			String conjunction;
			while (null != (conjunction = bfr.readLine())) {
				if (!conjunction.matches("^[#\\s]")) {
					wordsToFilterOut.add(conjunction);
				}
			}
		} catch (IOException ex) {
			throw new IOException("Could not read the conjunctions from the file! "
					+ "[file="+conjunctionsFile+"]", ex);
		} finally {
			try { isr.close(); } catch (Exception ex) {/**/};
		}
		return wordsToFilterOut;
	}

}
