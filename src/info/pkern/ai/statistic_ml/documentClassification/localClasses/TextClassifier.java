package info.pkern.ai.statistic_ml.documentClassification.localClasses;

import info.pkern.hackerrank.commons.ListTypeConverter;
import info.pkern.hackerrank.commons.MapUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TextClassifier {

	//Maybe make changeable?
	private final Integer maxNumberAllowedTerms;
	private int lowerInteresstingBoarder = 0;
	private int lastInteresstingBoarder = 0;
	private List<String> idfOrderedTerms = new ArrayList<>();
	private Map<String, DocumentClass> docClasses = new HashMap<>();

	private boolean trainingFinished = false;
	//Dictionary of the clasifier
	private  BagOfWords docClassFrequencies = new BagOfWords();
	
	private final boolean keepDetailsOfDocumentsInClasses;
	
	//For littlest caching
	private List<Entry<String, Double>> lastClassificationResult;
	//Better keep bag itselfe to use the equals() later!
	private int lastClassifiedBag;
	private Map<String, Double> termIDFs;
	
	public TextClassifier() {
		this(null, false);
	}
	
	public TextClassifier(Integer maxNumberOfTerms) {
		this(maxNumberOfTerms, false);
	}

	public TextClassifier(Integer maxNumberOfTerms, boolean keepDetailsOfDocumentsInClasses) {
		this.keepDetailsOfDocumentsInClasses = keepDetailsOfDocumentsInClasses;
		this.maxNumberAllowedTerms = maxNumberOfTerms;
	}

	public void train(BagOfWords bag, String docClassName) {
		DocumentClass docClass = docClasses.get(docClassName);
		if (null == docClass) {
			docClass = new DocumentClass(docClassName, keepDetailsOfDocumentsInClasses);
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
			docClassFrequencies.addTerms(docClass.getTerms());
		} else {
			docClassFrequencies.addTerms(bag.termsNotIn(docClass.getTerms()));
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
		}
		trainingFinished = false;
	}
	
	//Due to issues with the keepDocClassDetails state of the classifier! 
	public void train(DocumentClass documentClass) {
		throw new RuntimeException("Not yet implemented!");
	}
	
	//TODO Create external analyzer component(s) to be used for this analyzer (create Interface). 
	// CosinSimilarity (VSM), KNN, ...
	// If more than one analyzer merge results to the best fitting probability!
	// Interface: getProbabilities(), analyze(). Use a set/list/map of DocumentClasses as input dataSet's.
	
	public Entry<String, Double> classify(BagOfWords queryBag) {
		checkReadyForClassification();
		if (bagNotAlreadyAnalyzed(queryBag)) {
			lastClassificationResult = getClassificationProbabilities(queryBag);
		}
		return lastClassificationResult.get(0);
	}
	
	private boolean bagNotAlreadyAnalyzed(BagOfWords bag) {
		return lastClassifiedBag != bag.hashCode();
	}
	
	
	public List<Entry<String, Double>> getClassificationProbabilities(BagOfWords queryBag) {
		checkReadyForClassification();
		if (bagNotAlreadyAnalyzed(queryBag)) {
			List<Entry<String, Double>> classifications;
			classifications = new ArrayList<>(docClasses.size());
			
			Map<String, Double> weightedBag = weightBagOfWordsWithIDF(queryBag);
			Map<String, Double> filteredBag = filterMapByIDFTo(getNumberOfUsedTerms(), weightedBag);
			
			for (DocumentClass docClass : docClasses.values()) {
				
				Double classificationProbability = null;
				try {
					docClass.setTermFilter(getFilterList(getNumberOfUsedTerms()));
					classificationProbability = VectorMath.cosineSimilarityEuclideanNorm(
//							VectorMath.normlizeVectorEuclideanNorm(queryBag.getFrequencies()),
							//Without stopWordFilter much better precision ~+25% better
							VectorMath.normlizeVectorEuclideanNorm(filteredBag),
							
							//Only used by BiggerExample - better result without StopWordFilter!
							//Result better about 20% false
//							VectorMath.normlizeVectorEuclideanNorm(docClass.getTermFrequencies().getFrequencies())
							
							//Used currently with best results
							//Result better about 10-20% false. Only with stopWordFilter and on real trainings data?!
//							VectorMath.normlizeVectorEuclideanNorm(docClass.getWeightedFrequencies())
							
							//Trial
							VectorMath.normlizeVectorEuclideanNorm(docClass.getWeightedFrequenciesFiltered())
					);
				} catch (InvalidObjectException e) {
					e.printStackTrace();
				}
				classifications.add(new SimpleEntry<>(docClass.getName(), classificationProbability));
			}
			lastClassificationResult = new ArrayList<>(classifications);
			lastClassifiedBag = queryBag.hashCode();
			Collections.sort(lastClassificationResult, new Comparator<Entry<String, Double>>() {
				@Override
				public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
		}
		return Collections.unmodifiableList(lastClassificationResult);
	}

	private Map<String, Double> weightBagOfWordsWithIDF(BagOfWords queryBag) {
		Map<String, Double> weighted = new HashMap<>();
		for (Entry<String, Integer> entry : queryBag.getFrequencies().entrySet()) {
			String term = entry.getKey();
			Double classifierCorpusIDF = termIDFs.get(term);
			weighted.put(entry.getKey(), (null == classifierCorpusIDF)?0d:classifierCorpusIDF * queryBag.getFrequency(term));
		}
		return weighted;
	}
	
	private List<String> getFilterList(int filterBoearder) {
		return Collections.unmodifiableList(idfOrderedTerms.subList(0, filterBoearder));
	}
	
	private Map<String, Double> filterMapByIDFTo(int idfIndex, Map<String, Double> values) {
		Map<String, Double> filtered = new HashMap<>();
		for (String term : getFilterList(idfIndex)){
			Double value = values.get(term);
			if (null != value) {
				value = 0d;
			} 
			filtered.put(term, value);
		}
		return filtered;
	}
	
	public Integer getNumberOfUsedTerms() {
		return (null == maxNumberAllowedTerms)?lowerInteresstingBoarder:maxNumberAllowedTerms;
	}

	public Integer getDictionarySize() {
		return docClassFrequencies.getNumberOfTerms();
	}

	@Deprecated
	public Double getIDFOrZero(String term) {
		checkReadyForClassification();
		Double idf = termIDFs.get(term);
		if (null == idf) {
			return 0d;
		} else {
			return idf;
		}
	}

	//Calculate all data for performance classification. This again adds a state what is not really good!
	public void finishTraining() {
		if (docClasses.isEmpty()) {
			throw new IllegalStateException("Can not finish training! The classifier does not contain any document classes.");
		}
		termIDFs = calculateTermIDF();
		idfOrderedTerms = calculateInteresstingIdfBoarders(termIDFs);
		//If too deep the result is inAccurate! (ie. in the experiment with real data!)
//		termIDFs = reduceTo(5000, termIDFs);
		//Good solution if no maxCount is set!
//		termIDFs = reduceTo(null, termIDFs);
		processDocumentClasses(termIDFs);
		//TODO Keep all frequencies - not reduced!
//		inverseDocumentFrequency = calculateIDF();
		//TODO Keep separated
//		reduceIdfToMaxNumberOfTerms();
		trainingFinished = true;
	}

	private Map<String, Double> calculateTermIDF() {
		
//		SimpleTableNamedColumnAdapter<Double> docClassesWeightedTerms = new SimpleTableNamedColumnAdapter<>(Double.class);
		BagOfWords docClassFrequencies = new BagOfWords();
		
		for (DocumentClass docClass : docClasses.values()) {
			docClassFrequencies.addTerms(docClass.getTerms());
//			docClassesWeightedTerms.extendTableColumns(docClass.getTerms());
//			docClassesWeightedTerms.addRow(docClass.getWeightedFrequencies());
		}
		
		Map<String, Double> termIDFs = new HashMap<>();
		for (String term : docClassFrequencies.getTerms()) {
			Integer docFrequency = docClassFrequencies.getFrequency(term);
			Double docFraction = docClasses.size() / (1 + docFrequency.doubleValue());
			termIDFs.put(term, Math.log10(docFraction));
		}
		
		return termIDFs;
	}

	private List<String> calculateInteresstingIdfBoarders(Map<String, Double> idf) {
		List<String> termIDFsOrdered = new ArrayList<>();
		List<Entry<String, Double>> sortedMap = MapUtil.sortAsListByValuesDescending(termIDFs);
		
		int counter = 0;
		for (Entry<String, Double> entry : sortedMap) {
			termIDFsOrdered.add(entry.getKey());
			if (entry.getValue() > 0) {	//Maybe better: x >= 0!?
				lowerInteresstingBoarder = counter;
			}
			if (entry.getValue() == 0) {
				lastInteresstingBoarder = counter;
			}
			counter++;
		}
		System.out.println("Total terms="+idf.size()+", lowerIneresstingBoearder="+lowerInteresstingBoarder
				+ ", lastInteresstingBoarder="+lastInteresstingBoarder);
		return termIDFsOrdered;
	}
	
	private void processDocumentClasses(Map<String, Double> termIDFs) {
		for (DocumentClass docClass : docClasses.values()) {
			docClass.getWeightedFrequencies();
		}
	}
	
	
	private void checkReadyForClassification() {
		if (! trainingFinished) {
			throw new IllegalStateException("The training of this classifier was not yet finished! Use finishTrainig() first.");
		}
	}
	
	//TODO Should deep copy the doc classes list!
	public List<DocumentClass> getDocumenClasses() {
		List<DocumentClass> docClasses = new ArrayList<>();
		docClasses.addAll(this.docClasses.values());
		return docClasses;
	}

	//TODO IMPLEMENT
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TextClassifier [maxNumberAllowedTerms=");
		builder.append(maxNumberAllowedTerms);
		builder.append(", lastInteresstingBoarder=");
		builder.append(lastInteresstingBoarder);
		builder.append(", lowerInteresstingBoarder=");
		builder.append(lowerInteresstingBoarder);
		builder.append(", docClasses=");
		builder.append(docClasses.keySet());
		builder.append(", trainingFinished=");
		builder.append(trainingFinished);
		builder.append(", keepDetailsOfDocumentsInClasses=");
		builder.append(keepDetailsOfDocumentsInClasses);
		builder.append(",").append(System.lineSeparator());
		builder.append("docClassFrequencies=");
		builder.append(docClassFrequencies);
		builder.append(",").append(System.lineSeparator());
		builder.append("termIDFs=");
		builder.append(termIDFs);
		builder.append(",").append(System.lineSeparator());
		builder.append(",").append(System.lineSeparator());
		builder.append("lastClassificationResult=");
		builder.append(lastClassificationResult);
		builder.append(", lastClassifiedBag=");
		builder.append(lastClassifiedBag);
		builder.append("]");
		return builder.toString();
	}
}
