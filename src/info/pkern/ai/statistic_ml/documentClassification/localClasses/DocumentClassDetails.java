package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DocumentClassDetails {

	private SimpleTableNamedColumnAdapter<Integer> tfPerBag = new SimpleTableNamedColumnAdapter<>(Integer.class);
	private SimpleTableNamedColumnAdapter<Double> tfPerBagNormalized;
	private DocumentClass docClass;
	
	public DocumentClassDetails(DocumentClass docClass) {
		this.docClass = docClass;
	}
	
	public void add(BagOfWords bagOfWords) {
		tfPerBag.extendTableColumns(bagOfWords.getTerms());
		tfPerBag.addRow(bagOfWords.getFrequencies());
		tfPerBagNormalized = null;
	}

	public SimpleTableNamedColumnAdapter<Integer> getTermFrequenciesOfAllBags() {
		return tfPerBag.copy();
	}
	
	public SimpleTableNamedColumnAdapter<Double> getNormalizedTermFrequenciesOfAllBags() {
		if (null == tfPerBagNormalized) {
			tfPerBagNormalized = new SimpleTableNamedColumnAdapter<Double>(Double.class);
			tfPerBagNormalized.extendTableColumns(tfPerBag.getHeader());
			for (int rowIndex = 0; rowIndex < tfPerBag.getRowsCount(); rowIndex++) {
				Map<String,Double> rowNormalized = VectorMath.normlizeVectorEuclideanNorm(tfPerBag.getRow(rowIndex));
				tfPerBagNormalized.addRow(rowNormalized);
			}
		}
		return tfPerBagNormalized;
	}
	
	public DocumentClass getDocumentClass() {
		return docClass;
	}
}
