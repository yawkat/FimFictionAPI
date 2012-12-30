package at.yawk.fimfiction.api.parsers;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TagNode;

import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.StoryAccess;
import at.yawk.fimfiction.api.VisibleStoryMeta;
import at.yawk.fimfiction.api.factories.StoryMetaFactory;
import at.yawk.fimfiction.api.immutable.SimpleIdentifier;
import at.yawk.fimfiction.api.immutable.SimpleStoryAccess;

class MetaSearchIterator extends XMLSearchIterator<StoryAccess<VisibleStoryMeta>> {
	public MetaSearchIterator(String request, InternetAccess internet) {
		super(request, internet);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected StoryAccess<VisibleStoryMeta>[] getNextBlock(Lexer reader) throws Exception {
		final List<StoryAccess<VisibleStoryMeta>> stories = new ArrayList<StoryAccess<VisibleStoryMeta>>();
		StoryMetaFactory currentStory = null;
		Identifier currentStoryId = null;
		Node n;
		boolean awaitingTitle = false;
		while((n = reader.nextNode()) != null) {
			if(currentStory == null) {
				if(n instanceof TagNode && !((TagNode)n).isEndTag())
				System.out.println(((TagNode)n).getAttribute("class"));
				if(n instanceof TagNode && !((TagNode)n).isEndTag() && ((TagNode)n).getTagName().equals("DIV") && "content_box post_content_box".equals(((TagNode)n).getAttribute("class"))) {
					currentStory = new StoryMetaFactory();
				}
			} else {
				if(n instanceof TagNode && !((TagNode)n).isEndTag()) {
					final TagNode tn = (TagNode)n;
					if(tn.getTagName().equals("DIV") && tn.getAttribute("class").equals("story_stats")) {
						stories.add(new SimpleStoryAccess<VisibleStoryMeta>(currentStoryId, currentStory));
						currentStory = null;
						currentStoryId = null;
					} else {
						if(awaitingTitle) {
							if(tn.getTagName().equals("A") && tn.getAttribute("id") == null) {
								final String partHref = ((TagNode)n).getAttribute("href").substring(7);
								currentStoryId = new SimpleIdentifier(Integer.parseInt(partHref.substring(0, partHref.indexOf('/'))));
								currentStory.setTitle(tn.getChildren().toString());
								awaitingTitle = false;
							}
						} else if(tn.getTagName().equals("H2")) {
							awaitingTitle = true;
						}
					}
				}
			}
		}
		return stories.toArray(new StoryAccess[stories.size()]);
	}
}
