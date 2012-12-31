package at.yawk.fimfiction.api;

/**
 * Mature category
 * 
 * @author Yawkat
 * 
 */
public enum MatureCategory {
	ALL(0),
	SEX(1),
	GORE(2);
	
	private final byte	searchRequestIdentifier;
	
	private MatureCategory(int searchRequestIdentifier) {
		this.searchRequestIdentifier = (byte)searchRequestIdentifier;
	}
	
	/**
	 * @return The ID (used by GET search)
	 */
	public byte getSearchRequestIdentifier() {
		return searchRequestIdentifier;
	}
}
