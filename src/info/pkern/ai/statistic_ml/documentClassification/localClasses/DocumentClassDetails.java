package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DocumentClassDetails {

	private SimpleTableNamedColumnAdapter<Integer> tfPerBag = new SimpleTableNamedColumnAdapter<>(Integer.class);
	private DocumentClass docClass;
	
	public DocumentClassDetails(DocumentClass docClass) {
		this.docClass = docClass;
	}
	
	public void add(BagOfWords bagOfWords) {
		tfPerBag.extendTableColumns(bagOfWords.getTerms());
		tfPerBag.addRow(bagOfWords.getFrequencies());
	}

	public void merge(DocumentClassDetails details) {
		if (null != details) {
			tfPerBag.merge(details.tfPerBag);
		}
	}
	
	public SimpleTableNamedColumnAdapter<Integer> getTermFrequenciesOfAllBags() {
		return tfPerBag.copy();
	}
	
	public DocumentClass getDocumentClass() {
		return docClass;
	}
}
