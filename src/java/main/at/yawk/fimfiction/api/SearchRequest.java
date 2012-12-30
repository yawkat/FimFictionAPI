package at.yawk.fimfiction.api;

import java.util.Map;

public interface SearchRequest {
	public String getSearchTerm();
	
	public SearchOrder getSearchOrder();
	
	public Map<Category, Boolean> getCategories();
	
	public ContentRating getContentRating();
	
	public MatureCategory getMatureCategory();
	
	public boolean getCompleted();
	
	public Integer getMinimumWords();
	
	public Integer getMaximumWords();
	
	public Map<Character, Boolean> getCharacters();
	
	public boolean getUnread();
	
	public boolean getFavorite();
	
	public boolean getReadLater();
}
