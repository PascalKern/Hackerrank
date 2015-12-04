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
	private Integer maxNumberAllowedTerms;
	private final Double percentageOfCorpusForAllowedTerms;
	private int calculatedInteresstingIDFIndexForFilter = 0;
	private Integer firstIDFIndexForTermInAllDocClasses = 0;
	private boolean calculateMaxTermFromIDF = false;
	
	private final boolean keepDetailsOfDocumentsInClasses;

	//Dictionary of the classifier
	private BagOfWords termCorpus = new BagOfWords();
	
	//DocumentClasses informations within the classifier.
	private BagOfWords docClassFrequencyPerTerm = new BagOfWords();
	private Map<String, Double> docClassTermIDFs;
	private List<String> docClassTermIDFsOrdered = new ArrayList<>();

	private Map<String, DocumentClass> docClasses = new HashMap<>();

	private boolean trainingFinished = false;
	
	
	//For littlest caching
	private List<Entry<String, Double>> lastClassificationResult;
	//Better keep bag itself to use the equals() later!
	private int lastClassifiedBag;
	
	public TextClassifier() {
		this(1d, false, true);
	}
	
	public TextClassifier(Double percentageOfCorpusForAllowedTerms) {
		this(percentageOfCorpusForAllowedTerms, false);
	}

	public TextClassifier(boolean keepDetailsOfDocumentsInClasses) {
		this(1d, true, keepDetailsOfDocumentsInClasses);
	}

	public TextClassifier(Double percentageOfCorpusForAllowedTerms, boolean keepDetailsOfDocumentsInClasses) {
		this(percentageOfCorpusForAllowedTerms, keepDetailsOfDocumentsInClasses, false);
	}

	public TextClassifier(boolean calculateMaxTermFromIDF, boolean keepDetailsOfDocumentsInClasses) {
		this(1d, keepDetailsOfDocumentsInClasses, calculateMaxTermFromIDF);
	}

	private TextClassifier(Double percentageOfCorpusForAllowedTerms, boolean keepDetailsOfDocumentsInClasses, boolean calculateMaxTermFromIDF) {
		this.keepDetailsOfDocumentsInClasses = keepDetailsOfDocumentsInClasses;
		this.percentageOfCorpusForAllowedTerms = percentageOfCorpusForAllowedTerms;
		this.calculateMaxTermFromIDF = calculateMaxTermFromIDF;
	}

	public void train(BagOfWords bag, String docClassName) {
		termCorpus.merge(bag);
		DocumentClass docClass = docClasses.get(docClassName);
		if (null == docClass) {
			docClass = new DocumentClass(docClassName, keepDetailsOfDocumentsInClasses);
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
			docClassFrequencyPerTerm.addTerms(docClass.getTerms());
		} else {
			docClassFrequencyPerTerm.addTerms(bag.termsNotIn(docClass.getTerms()));
			docClass.add(bag);
			docClasses.put(docClassName, docClass);
		}
		trainingFinished = false;
	}
	
	public void train(DocumentClass documentClass) {
		//Due to issues with the keepDocClassDetails state of the classifier! 
		throw new RuntimeException("Not yet implemented!");
	}
	
	//Calculate all data for performance classification. This again adds a state what is not really good!
	public void finishTraining() {
		if (docClasses.isEmpty()) {
			throw new IllegalStateException("Can not finish training! The classifier does not contain any document classes yet.");
		}
		calculateDocClassTermIDFs();
		analyzeDocClassTermIDFs();
		//Just to improve the performance of the later probability calculation! Pre-Initialize the docClasses weighted frequencies! 
		for (DocumentClass docClass : docClasses.values()) {
			docClass.getWeightedFrequencies();
		}
		maxNumberAllowedTerms = new Integer((int) (getCorpusSize() * percentageOfCorpusForAllowedTerms));
		trainingFinished = true;
	}

	private void calculateDocClassTermIDFs() {
		docClassTermIDFs = new HashMap<>();
		for (String term : docClassFrequencyPerTerm.getTerms()) {
			Integer docFrequency = docClassFrequencyPerTerm.getFrequency(term);
			Double docFraction = docClasses.size() / (1 + docFrequency.doubleValue());
			docClassTermIDFs.put(term, Math.log10(docFraction));
		}
	}

	private void analyzeDocClassTermIDFs() {
		docClassTermIDFsOrdered = new ArrayList<>();
		List<Entry<String, Double>> sortedMap = MapUtil.sortAsListByValuesDescending(docClassTermIDFs);
		
		//Must never be higher than docClasses.size()! Best it will also not be the same size!
		double maxNumberOfDocClassesContainingTermX = docClasses.size() - 2;//* 0.85d;
		double minIDFToKeep = Math.log10(docClasses.size() / maxNumberOfDocClassesContainingTermX);
		
		int counter = 0;
		for (Entry<String, Double> entry : sortedMap) {
			docClassTermIDFsOrdered.add(entry.getKey());
			if (entry.getValue() >= minIDFToKeep) {
				calculatedInteresstingIDFIndexForFilter = counter;
			}
			//Keep only the first index which is smaller than 0 which means the term apears in all document classes!
			if (entry.getValue() < 0) {
				firstIDFIndexForTermInAllDocClasses = counter;
				break;
			}
			counter++;
		}
	}

	public Entry<String, Double> classify(BagOfWords queryBag) {
		checkFinishedTraining();
		if (bagNotAlreadyAnalyzed(queryBag)) {
			lastClassificationResult = getClassificationProbabilities(queryBag);
		}
		return lastClassificationResult.get(0);
	}
	
	public List<Entry<String, Double>> getClassificationProbabilities(BagOfWords queryBag) {
			checkFinishedTraining();
			if (bagNotAlreadyAnalyzed(queryBag)) {
				List<Entry<String, Double>> classifications = new ArrayList<>(docClasses.size());
				
				//Test only if the distances differ from the cosine similarity?! No they don't!
				List<Entry<String, Double>> distances = new ArrayList<>(docClasses.size());
				
				Map<String, Double> preparedQueryBag = prepareQueryBagOfWords(queryBag);
				
				for (DocumentClass docClass : docClasses.values()) {
					Double cosinSimilarity = calculateCosinSimilairty(preparedQueryBag, docClass);
					classifications.add(new SimpleEntry<>(docClass.getName(), cosinSimilarity));
					Double distance = calculateDistances(preparedQueryBag, docClass);
					distances.add(new SimpleEntry<>(docClass.getName(), distance));
				}
				
				lastClassifiedBag = queryBag.hashCode();
				lastClassificationResult = new ArrayList<>(classifications);
				
				//Order results. Not in own method while not clear if mor than one analyze method will be used (ie. KNN plus cosineSimilarity)
				Collections.sort(lastClassificationResult, new Comparator<Entry<String, Double>>() {
					@Override
					public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
						return o2.getValue().compareTo(o1.getValue());
					}
				});

				//Ascending because the smallest is the most important one!
				Collections.sort(distances, new Comparator<Entry<String, Double>>() {
					@Override
					public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				});
//				System.out.println(lastClassificationResult);
//				System.out.println(distances);
			}
			return Collections.unmodifiableList(lastClassificationResult);
		}

	private Map<String, Double> prepareQueryBagOfWords(BagOfWords queryBag) {
		Map<String, Double> weightedBag = weightBagOfWordsWithIDF(queryBag);
		Map<String, Double> preparedQueryBag = filterMapByIDF(weightedBag);
		return preparedQueryBag;
	}

	private Double calculateDistances(Map<String, Double> filteredBag, DocumentClass docClass) {
		Double distance = null;
		try {
			distance = VectorMath.distanceEuclideanNorm(
					VectorMath.normlizeVectorEuclideanNorm(filteredBag),
					VectorMath.normlizeVectorEuclideanNorm(docClass.getWeightedFrequencies(getIDFTermFilter())));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return distance;
	}
	private Double calculateCosinSimilairty(Map<String, Double> filteredBag, DocumentClass docClass) {
		Double cosinSimilarity = null;
		try {
			cosinSimilarity = VectorMath.cosineSimilarityEuclideanNorm(
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
					VectorMath.normlizeVectorEuclideanNorm(docClass.getWeightedFrequencies(getIDFTermFilter()))
			);
		} catch (InvalidObjectException e) {
			e.printStackTrace();
		}
		return cosinSimilarity;
	}

	private boolean bagNotAlreadyAnalyzed(BagOfWords bag) {
		return lastClassifiedBag != bag.hashCode();
	}
	
	
	private Map<String, Double> weightBagOfWordsWithIDF(BagOfWords queryBag) {
		Map<String, Double> weighted = new HashMap<>();
		for (Entry<String, Integer> entry : queryBag.getFrequencies().entrySet()) {
			String term = entry.getKey();
			Double classifierCorpusIDF = docClassTermIDFs.get(term);
			weighted.put(entry.getKey(), (null == classifierCorpusIDF)?0d:classifierCorpusIDF * queryBag.getFrequency(term));
		}
		return weighted;
	}
	
	private List<String> getIDFTermFilter() {
		return Collections.unmodifiableList(docClassTermIDFsOrdered.subList(0, getTermCountInIDFFilter()));
	}
	
	private Map<String, Double> filterMapByIDF(Map<String, Double> values) {
		Map<String, Double> filtered = new HashMap<>();
		for (String term : getIDFTermFilter()){
			Double value = values.get(term);
			if (null == value) {
				value = 0d;
			} 
			filtered.put(term, value);
		}
		return filtered;
	}
	
	public Integer getTermCountInIDFFilter() {
		checkFinishedTraining();
		int targetFilterIndex = (calculateMaxTermFromIDF)?calculatedInteresstingIDFIndexForFilter:maxNumberAllowedTerms;
		return Math.min(docClassTermIDFsOrdered.size(), targetFilterIndex);
	}

	public Integer getCorpusSize() {
		return termCorpus.getNumberOfTerms();
	}

	//TODO Should deep copy the doc classes list!
	public List<DocumentClass> getDocumenClasses() {
		List<DocumentClass> docClasses = new ArrayList<>();
		docClasses.addAll(this.docClasses.values());
		return docClasses;
	}

	@Deprecated
	public Double getIDFOrZero(String term) {
		checkFinishedTraining();
		Double idf = docClassTermIDFs.get(term);
		if (null == idf) {
			return 0d;
		} else {
			return idf;
		}
	}

	private void checkFinishedTraining() {
		if (! trainingFinished) {
			throw new IllegalStateException("The training of this classifier was not yet finished! Use finishTrainig() first.");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TextClassifier [maxNumberAllowedTerms=");
		builder.append(maxNumberAllowedTerms);
		builder.append(", calculatedInteresstingIDFIndexForFilter=");
		builder.append(calculatedInteresstingIDFIndexForFilter);
		builder.append(", firstIDFIndexForTermInAllDocClasses=");
		builder.append(firstIDFIndexForTermInAllDocClasses);
		builder.append(", docClasses=");
		builder.append(docClasses.keySet());
		builder.append(", trainingFinished=");
		builder.append(trainingFinished);
		builder.append(", keepDetailsOfDocumentsInClasses=");
		builder.append(keepDetailsOfDocumentsInClasses);
		builder.append(",").append(System.lineSeparator());
		builder.append("docClassFrequencies=");
		builder.append(docClassFrequencyPerTerm);
		builder.append(",").append(System.lineSeparator());
		builder.append("termIDFs=");
		builder.append(docClassTermIDFs);
		builder.append(",").append(System.lineSeparator());
		builder.append("lastClassificationResult=");
		builder.append(lastClassificationResult);
		builder.append(", lastClassifiedBag=");
		builder.append(lastClassifiedBag);
		builder.append("]");
		return builder.toString();
	}
}
