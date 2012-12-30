package at.yawk.fimfiction.api;

public enum ContentRating {
	ALL(-1),
	EVERYONE(0),
	TEEN(1),
	MATURE(2);
	
	private static final int	MINIMUM_ID	= -1;
	
	private final byte			searchRequestIdentifier;
	
	private ContentRating(int searchRequestIdentifier) {
		this.searchRequestIdentifier = (byte)searchRequestIdentifier;
	}
	
	public byte getSearchRequestIdentifier() {
		return searchRequestIdentifier;
	}
	
	private static ContentRating[]	ids	= new ContentRating[values().length];
	
	static {
		for(ContentRating cr : values())
			ids[cr.getSearchRequestIdentifier() - MINIMUM_ID] = cr;
	}
	
	public static ContentRating parse(int id) {
		return ids[id - MINIMUM_ID];
	}
}
