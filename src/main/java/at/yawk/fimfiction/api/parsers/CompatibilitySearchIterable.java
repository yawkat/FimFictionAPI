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
    private boolean asyncIdSearch = false;
    
    public CompatibilitySearchIterable(SearchRequest request, InternetAccess internet) {
        super(request, internet);
    }
    
    public boolean useIdSearch() {
        return useIdSearch;
    }
    
    public void setUseIdSearch(boolean useIdSearch) {
        this.useIdSearch = useIdSearch;
    }
    
    public boolean isAsyncIdSearch() {
        return asyncIdSearch;
    }
    
    public void setAsyncIdSearch(boolean asyncIdSearch) {
        this.asyncIdSearch = asyncIdSearch;
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
                    if (asyncIdSearch) {
                        return new StoryAccess<JSONStoryMeta>() {
                            private JSONStoryMeta meta;
                            
                            @Override
                            public int compareTo(Identifier arg0) {
                                return getId() - arg0.getId();
                            }
                            
                            @Override
                            public int getId() {
                                return id.getId();
                            }
                            
                            @Override
                            public JSONStoryMeta getMeta() {
                                try {
                                    return meta == null ? meta = JSONMetaLoader.getStoryMeta(id, getInternet()) : meta;
                                } catch (Exception e) {
                                    throw new Error(e);
                                }
                            }
                            
                            @Override
                            public Identifier getIdentifier() {
                                return id;
                            }
                        };
                    } else {
                        try {
                            return new SimpleStoryAccess<JSONStoryMeta>(id, JSONMetaLoader.getStoryMeta(id, getInternet()));
                        } catch (Exception e) {
                            throw new Error(e);
                        }
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
