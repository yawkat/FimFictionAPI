package at.yawk.fimfiction.api;

public enum Category {
	ROMANCE("Romance"),
	TRAGEDY("Tragedy"),
	SAD("Sad"),
	DARK("Dark"),
	COMEDY("Comedy"),
	RANDOM("Random"),
	CROSSOVER("Crossover"),
	ADVENTURE("Adventure"),
	SLICE_OF_LIFE("Slice of Life"),
	ALTERNATE_UNIVERSE("Alternate Universe"),
	HUMAN("Human");
	
	private final String	searchRequestIdentifier, name;
	
	private Category(String name) {
		this.searchRequestIdentifier = "category_" + toString().toLowerCase();
		this.name = name;
	}
	
	public String getSearchRequestIdentifier() {
		return searchRequestIdentifier;
	}

	public String getName() {
		return name;
	}
}
