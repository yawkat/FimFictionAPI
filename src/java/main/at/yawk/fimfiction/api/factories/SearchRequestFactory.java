package at.yawk.fimfiction.api.factories;

import java.util.HashMap;
import java.util.Map;

import at.yawk.fimfiction.api.Category;
import at.yawk.fimfiction.api.Character;
import at.yawk.fimfiction.api.ContentRating;
import at.yawk.fimfiction.api.MatureCategory;
import at.yawk.fimfiction.api.SearchOrder;
import at.yawk.fimfiction.api.SearchRequest;
import at.yawk.fimfiction.api.immutable.ImmutableHelper;

public class SearchRequestFactory implements SearchRequest {
	private String					searchTerm		= "";
	private SearchOrder				searchOrder		= SearchOrder.FIRST_POSTED_DATE;
	private Map<Category, Boolean>	categories		= new HashMap<Category, Boolean>();
	private ContentRating			contentRating	= ContentRating.ALL;
	private MatureCategory			matureCategory	= MatureCategory.ALL;
	private boolean					completed		= false;
	private Integer					minimumWords	= null;
	private Integer					maximumWords	= null;
	private Map<Character, Boolean>	characters		= new HashMap<Character, Boolean>();
	private boolean					unread			= false;
	private boolean					favorite		= false;
	private boolean					readLater		= false;
	
	public String getSearchTerm() {
		return searchTerm;
	}
	
	public SearchRequestFactory setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
		return this;
	}
	
	public SearchOrder getSearchOrder() {
		return searchOrder;
	}
	
	public SearchRequestFactory setSearchOrder(SearchOrder searchOrder) {
		this.searchOrder = searchOrder;
		return this;
	}
	
	public Map<Category, Boolean> getCategories() {
		return categories;
	}
	
	public SearchRequestFactory setCategories(Map<Category, Boolean> categories) {
		this.categories = categories;
		return this;
	}
	
	public ContentRating getContentRating() {
		return contentRating;
	}
	
	public SearchRequestFactory setContentRating(ContentRating contentRating) {
		this.contentRating = contentRating;
		return this;
	}
	
	public MatureCategory getMatureCategory() {
		return matureCategory;
	}
	
	public SearchRequestFactory setMatureCategory(MatureCategory matureCategory) {
		this.matureCategory = matureCategory;
		return this;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public SearchRequestFactory setCompleted(boolean completed) {
		this.completed = completed;
		return this;
	}
	
	public Integer getMinimumWords() {
		return minimumWords;
	}
	
	public SearchRequestFactory setMinimumWords(Integer minimumWords) {
		this.minimumWords = minimumWords;
		return this;
	}
	
	public Integer getMaximumWords() {
		return maximumWords;
	}
	
	public SearchRequestFactory setMaximumWords(Integer maximumWords) {
		this.maximumWords = maximumWords;
		return this;
	}
	
	public Map<Character, Boolean> getCharacters() {
		return characters;
	}
	
	public SearchRequestFactory setCharacters(Map<Character, Boolean> characters) {
		this.characters = characters;
		return this;
	}
	
	public boolean getUnread() {
		return unread;
	}
	
	public SearchRequestFactory setUnread(boolean unread) {
		this.unread = unread;
		return this;
	}
	
	public boolean getFavorite() {
		return favorite;
	}
	
	public SearchRequestFactory setFavorite(boolean favorite) {
		this.favorite = favorite;
		return this;
	}
	
	public boolean getReadLater() {
		return readLater;
	}
	
	public SearchRequestFactory setReadLater(boolean readLater) {
		this.readLater = readLater;
		return this;
	}
	
	public SearchRequest getCurrentData() {
		return ImmutableHelper.getCurrentData(this);
	}
}
