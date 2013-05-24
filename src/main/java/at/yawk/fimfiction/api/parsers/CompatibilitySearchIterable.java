package at.yawk.fimfiction.api.parsers;

import java.util.Iterator;

import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.JSONStoryMeta;
import at.yawk.fimfiction.api.SearchRequest;
import at.yawk.fimfiction.api.StoryAccess;
import at.yawk.fimfiction.api.immutable.SimpleStoryAccess;

@SuppressWarnings("rawtypes")
public class CompatibilitySearchIterable extends SearchIterable<StoryAccess> {
    private boolean useIdSearch = false;
    
    public CompatibilitySearchIterable(SearchRequest request, InternetAccess internet) {
        super(request, internet);
    }
    
    public boolean useIdSearch() {
        return useIdSearch;
    }
    
    public void setUseIdSearch(boolean useIdSearch) {
        this.useIdSearch = useIdSearch;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Iterator iterator() {
        if (useIdSearch) {
            final Iterator<Identifier> i = new IDSearchIterable(getSearchRequest(), getInternet()).iterator();
            return new Iterator<StoryAccess<JSONStoryMeta>>() {
                @Override
                public boolean hasNext() {
                    return i.hasNext();
                }
                
                @Override
                public StoryAccess<JSONStoryMeta> next() {
                    final Identifier id = i.next();
                    try {
                        return new SimpleStoryAccess<JSONStoryMeta>(id, JSONMetaLoader.getStoryMeta(id, getInternet()));
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                }
                
                @Override
                public void remove() {
                    i.remove();
                }
            };
        } else {
            return new MetaSearchIterable(getSearchRequest(), getInternet()).iterator();
        }
    }
}
