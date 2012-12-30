package at.yawk.fimfiction.api.immutable;

import at.yawk.fimfiction.api.Author;

public class SimpleAuthor extends SimpleIdentifier implements Author {
	private final String	name;
	
	public SimpleAuthor(int id, String name) {
		super(id);
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
