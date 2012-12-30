package at.yawk.fimfiction.api.immutable;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import at.yawk.fimfiction.api.Author;
import at.yawk.fimfiction.api.Category;
import at.yawk.fimfiction.api.Character;
import at.yawk.fimfiction.api.SearchRequest;
import at.yawk.fimfiction.api.StoryMeta;

public final class ImmutableHelper {
	private ImmutableHelper() {
		
	}
	
	public static SearchRequest getCurrentData(SearchRequest request) {
		return new SimpleSearchRequest(request.getSearchTerm(), request.getSearchOrder(), new HashMap<Category, Boolean>(request.getCategories()), request.getContentRating(), request.getMatureCategory(), request.getCompleted(), request.getMinimumWords(), request.getMaximumWords(), new HashMap<Character, Boolean>(request.getCharacters()), request.getUnread(), request.getFavorite(), request.getReadLater());
	}
	
	public static StoryMeta getCurrentData(StoryMeta meta) {
		return new SimpleStoryMeta(meta.getDescription(), meta.getShortDescription(), meta.getTitle(), Arrays.copyOf(meta.getChapters(), meta.getChapters().length), new Date(meta.getModificationDate().getTime()), meta.getImageLocation(), meta.getFullImageLocation(), meta.getMaximumChapterViews(), meta.getTotalViews(), meta.getCommentAmount(), meta.getAuthor(), meta.getStoryStatus(), meta.getContentRating(), meta.getLikes(), meta.getDislikes(), meta.getWords(), Arrays.copyOf(meta.getCategories(), meta.getCategories().length), Arrays.copyOf(meta.getCharacters(), meta.getCharacters().length));
	}
	
	public static Author getCurrentData(Author author) {
		return new SimpleAuthor(author.getId(), author.getName());
	}
}
