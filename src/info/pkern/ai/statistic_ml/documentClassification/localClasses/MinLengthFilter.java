package info.pkern.ai.statistic_ml.documentClassification.localClasses;


public class MinLengthFilter implements WordFilter {

	private final Integer minLength;
	
	public MinLengthFilter(Integer minLenght) {
		this.minLength = minLenght;
	}
	
	@Override
	public boolean doesPass(String word) {
		return word.length() >= minLength;
	}

	@Override
	public boolean doesNotPass(String word) {
		return word.length() < minLength;
	}

}
