package at.yawk.fimfiction.api;

import java.util.Date;

public interface ChapterMeta extends VisibleChapterMeta {
	public Date getModificationDate();
	
	public int getViews();
}
