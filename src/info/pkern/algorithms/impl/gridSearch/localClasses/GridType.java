package info.pkern.algorithms.impl.gridSearch.localClasses;

public enum GridType {
	
	GRID(" "), 
	PATTERN("\t");
	
	private String shapeSizeSeparator;

	private GridType(String shapeSizeSeparator) {
		this.shapeSizeSeparator = shapeSizeSeparator;
	}

	public String getShapeSizeSeparator() {
		return shapeSizeSeparator;
	}
}
