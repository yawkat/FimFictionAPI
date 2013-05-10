package at.yawk.fimfiction.api.parsers;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.StoryAccess;
import at.yawk.fimfiction.api.VisibleStoryMeta;
import at.yawk.yxml.Lexer;

public class MetaSearchIterator extends SearchIterator<StoryAccess<VisibleStoryMeta>> {
    public MetaSearchIterator(String request, InternetAccess internet) {
        super(request, internet);
    }
    
    @Override
    protected StoryAccess<VisibleStoryMeta>[] getNextBlock(URLConnection url) {
        try {
            return getNextBlock(new Lexer(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    protected StoryAccess<VisibleStoryMeta>[] getNextBlock(Lexer reader) throws Exception {
        final List<StoryAccess<VisibleStoryMeta>> stories = new ArrayList<StoryAccess<VisibleStoryMeta>>(10);
        final MetaSearchPageParser parser = new MetaSearchPageParser(reader);
        while (true) {
            try {
                stories.add(parser.parseNext());
            } catch (NullPointerException e) {
                break;
            }
        }
        return stories.toArray(new StoryAccess[stories.size()]);
    }
}
