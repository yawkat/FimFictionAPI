package at.yawk.fimfiction.api;

public enum MatureCategory {
	ALL(0),
	SEX(1),
	GORE(2);
	
	private final byte searchRequestIdentifier;
	
	private MatureCategory(int searchRequestIdentifier) {
		this.searchRequestIdentifier = (byte)searchRequestIdentifier;
	}
	
	public byte getSearchRequestIdentifier() {
		return searchRequestIdentifier;
	}
}
