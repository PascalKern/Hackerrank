package info.pkern.ai.statistic_ml.documentClassification.localClasses;

public interface WordFilter {

	boolean doesPass(String word);
	
	boolean doesNotPass(String word);
}
