package at.yawk.fimfiction.api;

public interface GeneralStoryMeta {
	public String getDescription();

	public AuthorMeta getAuthor();
	
	public VisibleChapter[] getChapters();
	
	public String getTitle();
	
	public String getImageLocation();
	
	public String getFullImageLocation();
	
	public int getMaximumChapterViews();
	
	public int getTotalViews();

	public Category[] getCategories();
	
	public int getLikes();
	
	public int getDislikes();
	
	public int getWords();
	
	public ContentRating getContentRating();
	
	public StoryStatus getStoryStatus();
	
	public int getCommentAmount();
}
