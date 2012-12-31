package at.yawk.fimfiction.api.parsers;

import java.util.Iterator;

import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.SearchRequest;
import at.yawk.fimfiction.api.StoryAccess;
import at.yawk.fimfiction.api.VisibleStoryMeta;
import at.yawk.fimfiction.api.util.SearchUtil;

public class MetaSearchIterable extends SearchIterable<StoryAccess<VisibleStoryMeta>> {
	public MetaSearchIterable(SearchRequest request, InternetAccess internet) {
		super(request, internet);
	}

	@Override
	public Iterator<StoryAccess<VisibleStoryMeta>> iterator() {
		return new MetaSearchIterator(SearchUtil.getSearchGet(getSearchRequest()), getInternet());
	}
}
