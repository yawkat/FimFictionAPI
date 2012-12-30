package at.yawk.fimfiction.api.actions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserException;

import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.URLs;

public final class AccountActions {
	private AccountActions() {
		
	}
	
	public static void setReadLater(Identifier story, boolean readlater, InternetAccess access) throws IOException {
		final URLConnection c = access.connect(new URL(URLs.READLATER));
		c.connect();
		c.getOutputStream().write(("story=" + story.getId() + "&selected=" + (readlater ? 1 : 0)).getBytes());
		clear(c.getInputStream());
	}
	
	public static void setFavorite(Identifier story, boolean favorite, boolean emailNotification, InternetAccess access) throws IOException {
		final URLConnection c = access.connect(new URL(URLs.FAVORITE));
		c.connect();
		c.getOutputStream().write(("story=" + story.getId() + "&selected=" + (favorite ? 1 : 0) + "&email=" + (emailNotification ? 1 : 0)).getBytes());
		clear(c.getInputStream());
	}
	
	public static void toggleUnread(Identifier chapter, InternetAccess access) throws IOException {
		final URLConnection c = access.connect(new URL(URLs.READLATER));
		c.connect();
		c.getOutputStream().write(("chapter=" + chapter.getId()).getBytes());
		clear(c.getInputStream());
	}
	
	public static boolean[] getHasRead(final Identifier story, final InternetAccess access) throws IOException, ParserException {
		final List<Boolean> l = new ArrayList<Boolean>();
		final Lexer parser = new Lexer(access.connect(new URL(URLs.STORY + story.getId())));
		Node n;
		boolean isInChapter = false;
		while((n = parser.nextNode()) != null) {
			if(n instanceof TagNode && !((TagNode)n).isEndTag()) {
				if(isInChapter) {
					if(((TagNode)n).getTagName().equals("IMG") && ((TagNode)n).getAttribute("id") != null) {
						l.add(((TagNode)n).getAttribute("src").endsWith("tick.png"));
						isInChapter = false;
					}
				} else if(((TagNode)n).getTagName().equals("DIV") && ((TagNode)n).getAttribute("class").equals("chapter_container"))
					isInChapter = true;
			}
		}
		final boolean[] ab = new boolean[l.size()];
		int i = 0;
		for(Boolean b : l)
			ab[i++] = b;
		return ab;
	}
	
	public static void setLike(Identifier story, boolean isLike, String token, InternetAccess access) throws IOException {
		final URLConnection c = access.connect(new URL(URLs.FAVORITE));
		c.connect();
		c.getOutputStream().write(("story=" + story.getId() + "&rating=" + (isLike ? 100 : 0) + "&ip=" + token).getBytes());
		clear(c.getInputStream());
	}
	
	public static String getLikeToken(Identifier story, InternetAccess access) throws ParserException, IOException {
		final Lexer parser = new Lexer(access.connect(new URL(URLs.STORY + story.getId())));
		Node n;
		while((n = parser.nextNode()) != null) {
			if(n instanceof TagNode && !((TagNode)n).isEndTag() && ((TagNode)n).getTagName().equals("A")) {
				final String clazz = ((TagNode)n).getAttribute("class");
				if(clazz != null && clazz.equals("like_button ")) {
					final String s = ((TagNode)n).getAttribute("onclick").substring(21);
					return s.substring(0, s.indexOf('\''));
				}
			}
		}
		return null;
	}
	
	public static void setLike(Identifier story, boolean isLike, InternetAccess access) throws IOException, ParserException {
		setLike(story, isLike, getLikeToken(story, access), access);
	}
	
	private static void clear(InputStream is) throws IOException {
		while(is.read() >= 0)
			;
	}
}
