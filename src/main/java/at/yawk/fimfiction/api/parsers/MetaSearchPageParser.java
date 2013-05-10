package at.yawk.fimfiction.api.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.yawk.fimfiction.api.Category;
import at.yawk.fimfiction.api.Chapter;
import at.yawk.fimfiction.api.Character;
import at.yawk.fimfiction.api.ContentRating;
import at.yawk.fimfiction.api.MatureCategory;
import at.yawk.fimfiction.api.StoryAccess;
import at.yawk.fimfiction.api.StoryStatus;
import at.yawk.fimfiction.api.VisibleStoryMeta;
import at.yawk.fimfiction.api.factories.StoryMetaFactory;
import at.yawk.fimfiction.api.immutable.SimpleAuthor;
import at.yawk.fimfiction.api.immutable.SimpleChapter;
import at.yawk.fimfiction.api.immutable.SimpleIdentifier;
import at.yawk.fimfiction.api.immutable.SimpleStoryAccess;
import at.yawk.yxml.Lexer;

public class MetaSearchPageParser {
    private final Lexer lexer;
    
    public MetaSearchPageParser(Lexer lexer) {
        this.lexer = lexer;
    }
    
    public StoryAccess<VisibleStoryMeta> parseNext() throws IOException {
        int storyId;
        final StoryMetaFactory story = new StoryMetaFactory();
        
        moveUntilClass("story_content_box");
        storyId = Integer.parseInt(lexer.getAttribute("id").substring(6));
        
        readOn();
        final String authorImageUrl = lexer.getAttribute("src");
        final String idString = authorImageUrl.substring(authorImageUrl.lastIndexOf('/') + 1, authorImageUrl.lastIndexOf('_'));
        int authorid = -1;
        if (!idString.equals("none")) {
            authorid = Integer.parseInt(idString);
        }
        
        moveUntilClass("likes");
        readOn();
        story.setLikes(parseContent());
        
        moveUntilClass("dislikes");
        readOn();
        story.setDislikes(parseContent());
        
        moveUntilClass("comment_container");
        readOn(3);
        story.setCommentAmount(parseContent());
        
        readOn(3);
        story.setTotalViews(Integer.parseInt(lexer.getAttribute("title").replaceAll("[^0-9]", "")));
        readOn(3);
        story.setMaximumChapterViews(parseContent());
        
        moveUntilClass("story_name");
        readOn();
        story.setTitle(lexer.getCurrentElement());
        
        moveUntilClass("author");
        readOn(2);
        story.setAuthor(new SimpleAuthor(authorid, lexer.getCurrentElement()));
        
        moveUntilClass("description");
        readOn();
        if (lexer.isTag() && lexer.getTagName().equals("div")) {
            readOn();
            String fullImage = lexer.getAttribute("href");
            fullImage = fullImage.substring(0, fullImage.lastIndexOf('?'));
            story.setFullImageLocation(fullImage);
            
            readOn();
            String image = lexer.getAttribute("src");
            image = image.substring(0, image.lastIndexOf('?'));
            story.setImageLocation(image);
            readOn(3);
            if (!lexer.isEndTagOnly()) {
                readOn(3);
            }
        }
        
        readOn();
        final List<Category> categories = new ArrayList<Category>(7);
        while (lexer.isTag() && lexer.getTagName().equals("a")) {
            readOn();
            String categoryName = lexer.getCurrentElementContent();
            boolean found = false;
            for (Category category : Category.values()) {
                if (category.getName().equals(categoryName)) {
                    categories.add(category);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("Unknown category: " + categoryName);
            }
            readOn(2);
        }
        story.setCategories(categories.toArray(new Category[categories.size()]));
        
        final StringBuilder description = new StringBuilder();
        while (readOnTrue() && (!lexer.isTag() || !lexer.getTagName().equals("div"))) {
            description.append(lexer.getCurrentElementContent());
        }
        story.setDescription(description.toString());
        
        final List<Chapter> chapters = new ArrayList<Chapter>();
        do {
            if (!lexer.isTag() || !lexer.getTagName().equals("div")) {
                continue;
            }
            if (lexer.getAttribute("class").contains("chapter_expander")) {
                readOn(7);
            } else {
                readOn(2);
                final String chapterReadClass = lexer.getAttribute("id");
                final int chapterId;
                if (chapterReadClass == null) {
                    String hr = lexer.getAttribute("href");
                    hr = hr.substring(hr.indexOf("y/") + 2);
                    chapterId = Integer.parseInt(hr.substring(0, hr.indexOf('/')));
                    readOn();
                } else {
                    chapterId = Integer.parseInt(chapterReadClass.substring(chapterReadClass.lastIndexOf('_') + 1));
                    readOn(5);
                }
                final String chapterName = lexer.getCurrentElementContent();
                moveUntilClass("word_count");
                readOn();
                final Chapter chapter = new SimpleChapter(chapterId, chapterName, parseContent(), -1, null);
                chapters.add(chapter);
                readOn(14);
            }
        } while (readOnTrue() && !lexer.getTagName().equals("li"));
        story.setChapters(chapters.toArray(new Chapter[chapters.size()]));
        
        moveUntilClass("bottom");
        readOn(10);
        story.setStoryStatus(StoryStatus.parse(lexer.getCurrentElementContent().trim()));
        
        readOn(5);
        final String conName = lexer.getCurrentElementContent().trim();
        if (conName.equals("Teen")) {
            story.setContentRating(ContentRating.TEEN);
        } else if (conName.equals("Mature")) {
            story.setContentRating(ContentRating.MATURE);
        } else if (conName.equals("Everyone")) {
            story.setContentRating(ContentRating.EVERYONE);
        }
        
        readOn(2);
        final List<MatureCategory> matureCategories = new ArrayList<MatureCategory>(7);
        while (lexer.isTag() && lexer.getTagName().equals("b")) {
            readOn(4);
            final String catName = lexer.getCurrentElementContent().trim();
            if (catName.equals("Sex")) {
                matureCategories.add(MatureCategory.SEX);
            } else if (catName.equals("Gore")) {
                matureCategories.add(MatureCategory.GORE);
            }
            readOn(2);
        }
        story.setMatureCategories(matureCategories.toArray(new MatureCategory[matureCategories.size()]));
        
        readOn(2);
        story.setWords(parseContent());
        
        moveUntilClass("published");
        readOn(9);
        final List<Character> characters = new ArrayList<Character>(7);
        while (lexer.isTag() && lexer.getTagName().equals("a")) {
            readOn();
            String characterImageUrl = lexer.getAttribute("src");
            if (characterImageUrl.startsWith("/")) {
                characterImageUrl = "http:" + characterImageUrl;
            }
            boolean found = false;
            for (Character character : Character.values()) {
                if (character.getImageLocation().equals(characterImageUrl)) {
                    characters.add(character);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("Unknown character image URL: " + characterImageUrl);
            }
            readOn();
        }
        story.setCharacters(characters.toArray(new Character[characters.size()]));
        
        return new SimpleStoryAccess<VisibleStoryMeta>(new SimpleIdentifier(storyId), story);
    }
    
    private void moveUntilClass(String clazz) throws IOException {
        if (lexer.getCurrentElementContent() != null && lexer.isTag() && classAttributeContains(clazz)) {
            return;
        }
        while (lexer.getNext() && (!lexer.isTag() || !classAttributeContains(clazz)));
    }
    
    private boolean classAttributeContains(String classname) {
        if (lexer.getAttribute("class") == null) {
            return false;
        }
        final String[] classes = lexer.getAttribute("class").split(" ");
        for (String c : classes) {
            if (c.equals(classname)) {
                return true;
            }
        }
        return false;
    }
    
    private void readOn(int amount) throws IOException {
        for (int i = 0; i < amount; i++) {
            readOn();
        }
    }
    
    private void readOn() throws IOException {
        lexer.getNext();
        if (!lexer.isTag() && lexer.isEmpty()) {
            readOn();
        }
    }
    
    private boolean readOnTrue() throws IOException {
        readOn();
        return true;
    }
    
    private int parseContent() {
        return Integer.parseInt(lexer.getCurrentElement().replaceAll("[^0-9]", ""));
    }
}
