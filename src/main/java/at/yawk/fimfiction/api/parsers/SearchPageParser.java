package at.yawk.fimfiction.api.parsers;

import static at.yawk.yxml.dom.DOMNode.getAttributeEqualsMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import at.yawk.yxml.EntityNamespace;
import at.yawk.yxml.Node;
import at.yawk.yxml.TagNode;
import at.yawk.yxml.TagNode.Attribute;
import at.yawk.yxml.TextNode;
import at.yawk.yxml.dom.DOMNode;
import at.yawk.yxml.dom.DOMNode.DOMMatcher;

public class SearchPageParser {
    private final DOMNode document;
    private final List<StoryAccess<VisibleStoryMeta>> stories = new ArrayList<StoryAccess<VisibleStoryMeta>>(10);
    
    public SearchPageParser(DOMNode document) {
        this.document = document;
    }
    
    public List<StoryAccess<VisibleStoryMeta>> getStories() {
        return Collections.unmodifiableList(stories);
    }
    
    public void parse() throws IOException {
        final List<DOMNode> n = document.getChildForMatch(DOMNode.getAttributeEqualsMatcher("class", "content"), true).getChildrenForMatch(new DOMMatcher() {
            @Override
            public boolean matches(DOMNode arg0) {
                if (arg0.getElement() instanceof TagNode) {
                    for (Attribute a : ((TagNode) arg0.getElement()).getAttributes()) {
                        if (a.getKey().equals("id") && a.getValue().startsWith("story_")) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }, Integer.MAX_VALUE, true);
        for (DOMNode d : n) {
            final StoryContainerParser cont = new StoryContainerParser(d);
            cont.parse();
            final StoryMetaFactory mf = new StoryMetaFactory();
            mf.setAuthor(new SimpleAuthor(-1, cont.author));
            final Category[] categories = new Category[cont.categories.size()];
            for (int i = 0; i < categories.length; i++) {
                String s = cont.categories.get(i);
                for (Category c : Category.values()) {
                    if (enumEquals(c, s)) {
                        categories[i] = c;
                        break;
                    }
                }
            }
            mf.setCategories(categories);
            final Chapter[] chapters = new Chapter[Math.min(cont.chapterIds.size(), Math.min(cont.chapterNames.size(), cont.chapterWordCounts.size()))];
            for (int i = 0; i < chapters.length; i++) {
                chapters[i] = new SimpleChapter(cont.chapterIds.get(i), cont.chapterNames.get(i), cont.chapterWordCounts.get(i), -1, null);
            }
            mf.setChapters(chapters);
            mf.setLikes(cont.likes);
            mf.setDislikes(cont.dislikes);
            mf.setCommentAmount(cont.comments);
            mf.setTotalViews(cont.totalViews);
            mf.setMaximumChapterViews(cont.maxChapterViews);
            mf.setTitle(cont.title);
            mf.setImageLocation(cont.imageUrl);
            mf.setFullImageLocation(cont.fullImageUrl);
            mf.setDescription(cont.description);
            mf.setWords(cont.totalWordCount);
            for (StoryStatus status : StoryStatus.values()) {
                if (enumEquals(status, cont.status)) {
                    mf.setStoryStatus(status);
                    break;
                }
            }
            String rating = cont.matureTags.get(0);
            for (ContentRating cr : ContentRating.values()) {
                if (enumEquals(cr, rating)) {
                    mf.setContentRating(cr);
                    break;
                }
            }
            final List<MatureCategory> mcl = new ArrayList<MatureCategory>(2);
            for (int i = 1; i < cont.matureTags.size(); i++) {
                String s = cont.matureTags.get(i);
                for (MatureCategory mc : MatureCategory.values()) {
                    if (enumEquals(mc, s)) {
                        mcl.add(mc);
                        break;
                    }
                }
            }
            mf.setMatureCategories(mcl.toArray(new MatureCategory[mcl.size()]));
            {
                Character[] characters = new Character[cont.characterImageUrls.size()];
                int i = 0;
                for (String src : cont.characterImageUrls) {
                    for (Character character : Character.values()) {
                        if (character.getImageLocation().equals(src)) {
                            characters[i] = character;
                            break;
                        }
                    }
                    i++;
                }
                mf.setCharacters(characters);
            }
            stories.add(new SimpleStoryAccess<VisibleStoryMeta>(new SimpleIdentifier(cont.id), mf));
        }
    }
    
    private class StoryContainerParser {
        private final DOMNode container;
        
        int id;
        @SuppressWarnings("unused")
        String authorAvatarUrl;
        int likes;
        int dislikes;
        int comments;
        int totalViews;
        int maxChapterViews;
        String title;
        String author;
        String imageUrl;
        String fullImageUrl;
        List<String> categories;
        String description;
        List<Integer> chapterIds;
        List<String> chapterNames;
        List<Integer> chapterWordCounts;
        int totalWordCount;
        String status;
        List<String> matureTags;
        List<String> characterImageUrls;
        
        public StoryContainerParser(DOMNode container) {
            this.container = container;
        }
        
        public void parse() {
            id = parseInt(getAttribute((TagNode) container.getElement(), "id"));
            {
                final DOMNode n = container.getChildForMatch(getAttributeEqualsMatcher("class", "story_avatar"), false);
                if (n != null) {
                    authorAvatarUrl = getAttribute((TagNode) n.getElement(), "src");
                }
            }
            likes = parseInt(container.getChildForMatch(getAttributeEqualsMatcher("class", "likes"), true).getChildren().get(0).getElement().getRawContent());
            dislikes = parseInt(container.getChildForMatch(getAttributeEqualsMatcher("class", "dislikes"), true).getChildren().get(0).getElement().getRawContent());
            {
                final DOMNode commentContainer = container.getChildForMatch(getAttributeEqualsMatcher("class", "comment_container"), true);
                comments = parseInt(commentContainer.getChildForMatch(getTagNameMatcher("b"), false).getChildren().get(0).getElement().getRawContent());
                final DOMNode viewSpan = commentContainer.getChildForMatch(getTagNameMatcher("span"), false);
                totalViews = parseInt(getAttribute((TagNode) viewSpan.getElement(), "title"));
                maxChapterViews = parseInt(viewSpan.getChildForMatch(getTagNameMatcher("b"), false).getChildren().get(0).getElement().getRawContent());
            }
            {
                final DOMNode titleDiv = container.getChildForMatch(getAttributeEqualsMatcher("class", "title"), true);
                title = ((TextNode) titleDiv.getChildForMatch(getTagNameMatcher("a"), false).getChildren().get(0).getElement()).getUnescapedText(EntityNamespace.HTML_NAMESPACE);
                author = titleDiv.getChildForMatch(getAttributeEqualsMatcher("class", "author"), false).getChildren().get(0).getChildren().get(0).getElement().getRawContent();
            }
            {
                final DOMNode paddingDiv = container.getChildForMatch(getAttributeEqualsMatcher("class", "padding"), true);
                final DOMNode imageAP = paddingDiv.getChildForMatch(getAttributeEqualsMatcher("class", "story_image"), true);
                if (imageAP != null) {
                    final DOMNode imageA = imageAP.getChildren().get(0);
                    fullImageUrl = getAttribute((TagNode) imageA.getElement(), "href");
                    imageUrl = getAttribute((TagNode) imageA.getChildren().get(0).getElement(), "src");
                }
                final DOMNode descriptionDiv = paddingDiv.getChildForMatch(getAttributeEqualsMatcher("class", "description"), false);
                categories = new ArrayList<String>(5);
                for (DOMNode d : descriptionDiv.getChildrenForMatch(getTagNameMatcher("a"), Integer.MAX_VALUE, false)) {
                    categories.add(d.getChildren().get(0).getElement().getRawContent());
                }
                final DOMNode descriptionP = descriptionDiv.getChildren().get(descriptionDiv.getChildren().size() - 1);
                description = childrenToXml(descriptionP, false);
            }
            {
                final DOMNode chaptersList = container.getChildForMatch(getAttributeEqualsMatcher("class", "chapters"), true);
                chapterIds = new ArrayList<Integer>();
                chapterNames = new ArrayList<String>();
                chapterWordCounts = new ArrayList<Integer>();
                for (DOMNode node : chaptersList.getChildrenForMatch(new DOMMatcher() {
                    @Override
                    public boolean matches(DOMNode arg0) {
                        if (arg0.getElement() instanceof TagNode) {
                            String s = getAttribute((TagNode) arg0.getElement(), "class");
                            if (s != null) {
                                return s.indexOf("chapter_container") >= 0 && s.indexOf("chapter_expander") == -1;
                            }
                        }
                        return false;
                    }
                }, Integer.MAX_VALUE, false)) {
                    node = node.getChildren().get(0);
                    final DOMNode a = node.getChildForMatch(getTagNameMatcher("a"), true);
                    String href = getAttribute((TagNode) a.getElement(), "href");
                    href = href.substring(7);
                    href = href.substring(href.indexOf('/'));
                    chapterIds.add(parseInt(href));
                    chapterNames.add(((TextNode) a.getChildren().get(0).getElement()).getUnescapedText(EntityNamespace.HTML_NAMESPACE));
                    chapterWordCounts.add(parseInt(node.getChildForMatch(getTagNameMatcher("div"), true).getChildren().get(0).getElement().getRawContent()));
                }
                final DOMNode bottomNode = chaptersList.getChildren().get(chaptersList.getChildren().size() - 1);
                totalWordCount = parseInt(bottomNode.getChildForMatch(getTagNameMatcher("div"), false).getChildren().get(0).getChildren().get(0).getElement().getRawContent());
                status = bottomNode.getChildren().get(3).getElement().getRawContent();
                matureTags = new ArrayList<String>(3);
                for (DOMNode span : bottomNode.getChildrenForMatch(getTagNameMatcher("span"), Integer.MAX_VALUE, false)) {
                    matureTags.add(span.getChildren().get(0).getElement().getRawContent());
                }
            }
            {
                final DOMNode croot = container.getChildForMatch(getAttributeEqualsMatcher("class", "extra_story_data"), true).getChildren().get(0);
                characterImageUrls = new ArrayList<String>(8);
                for (DOMNode charA : croot.getChildrenForMatch(getTagNameMatcher("a"), Integer.MAX_VALUE, false)) {
                    String src = getAttribute((TagNode) charA.getChildren().get(0).getElement(), "src");
                    src = "http:" + src;
                    characterImageUrls.add(src);
                }
            }
        }
    }
    
    private static String getAttribute(TagNode n, String key) {
        for (Attribute a : n.getAttributes()) {
            if (a.getKey().equals(key)) {
                return a.getValue();
            }
        }
        return null;
    }
    
    private static int parseInt(String s) {
        return Integer.parseInt(s.replaceAll("[^0-9]", ""));
    }
    
    private static DOMMatcher getTagNameMatcher(final String tagName) {
        return new DOMMatcher() {
            @Override
            public boolean matches(DOMNode arg0) {
                return arg0.getElement() instanceof TagNode && ((TagNode) arg0.getElement()).getTagName().equals(tagName);
            }
        };
    }
    
    private static String childrenToXml(DOMNode node, boolean nice) {
        String s = "";
        for (DOMNode c : node.getChildren()) {
            if (nice) {
                s += "\n";
            }
            s += toXml(c, nice);
        }
        return s;
    }
    
    private static String toXml(DOMNode node, boolean nice) {
        Node n = node.getElement();
        if (n instanceof TextNode) {
            return ((TextNode) n).getText();
        } else if (n instanceof TagNode) {
            String s = "<";
            s += ((TagNode) n).getTagName();
            for (Attribute attribute : ((TagNode) n).getAttributes()) {
                s += " ";
                s += attribute.getKey();
                s += "=\"";
                s += attribute.getValue();
                s += '"';
            }
            if (node.getChildren().isEmpty()) {
                s += " />";
            } else {
                s += ">";
                if (nice) {
                    for (String t : childrenToXml(node, nice).split("\n")) {
                        s += "\t";
                        s += t;
                        s += "\n";
                    }
                } else {
                    s += childrenToXml(node, nice);
                }
                s += "</";
                s += ((TagNode) n).getTagName();
                s += ">";
            }
            return s;
        } else if (n == null) {
            return childrenToXml(node, nice);
        } else {
            return "";
        }
    }
    
    private static boolean enumEquals(Enum<?> enom, String s) {
        char[] cs = enom.name().toCharArray();
        char[] ct = s.toCharArray();
        int i = 0;
        int j = 0;
        while (i < cs.length && j < ct.length) {
            char c;
            do {
                c = cs[i++];
            } while (c == '_');
            char d;
            do {
                d = ct[j++];
            } while (d == ' ');
            
            if (c >= 'A' && c <= 'Z') {
                c -= 'a' - 'A';
            }
            if (d >= 'A' && d <= 'Z') {
                d -= 'a' - 'A';
            }
            
            if (c != d) {
                return false;
            }
        }
        return true;
    }
}
