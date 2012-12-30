package at.yawk.fimfiction.api;

import java.util.Date;

public interface JSONStoryMeta extends GeneralStoryMeta {
	public String getShortDescription();
	
	public Date getModificationDate();
	
	public Author getAuthor();
	
	public Chapter[] getChapters();
}
