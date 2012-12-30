package at.yawk.fimfiction.api.immutable;

import java.util.Date;

import at.yawk.fimfiction.api.Author;
import at.yawk.fimfiction.api.Category;
import at.yawk.fimfiction.api.Chapter;
import at.yawk.fimfiction.api.Character;
import at.yawk.fimfiction.api.ContentRating;
import at.yawk.fimfiction.api.StoryMeta;
import at.yawk.fimfiction.api.StoryStatus;

public class SimpleStoryMeta extends SimpleJSONStoryMeta implements StoryMeta {
	private final Character[]	characters;
	
	public SimpleStoryMeta(String description, String shortDescription, String title, Chapter[] chapters, Date modificationDate, String imageLocation, String fullImageLocation, int maximumChapterViews, int totalViews, int commentAmount, Author author, StoryStatus storyStatus, ContentRating contentRating, int likes, int dislikes, int words, Category[] categories, Character[] characters) {
		super(description, shortDescription, title, chapters, modificationDate, imageLocation, fullImageLocation, maximumChapterViews, totalViews, commentAmount, author, storyStatus, contentRating, likes, dislikes, words, categories);
		this.characters = characters;
	}
	
	@Override
	public Character[] getCharacters() {
		return characters;
	}
}
