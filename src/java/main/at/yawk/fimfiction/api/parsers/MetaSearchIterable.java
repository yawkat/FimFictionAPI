package at.yawk.fimfiction.api.parsers;

import java.util.Iterator;

import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.SearchOrder;
import at.yawk.fimfiction.api.SearchRequest;
import at.yawk.fimfiction.api.SimpleInternetAccess;
import at.yawk.fimfiction.api.StoryAccess;
import at.yawk.fimfiction.api.VisibleStoryMeta;
import at.yawk.fimfiction.api.factories.SearchRequestFactory;

public class MetaSearchIterable extends SearchIterable<StoryAccess<VisibleStoryMeta>> {
	public MetaSearchIterable(SearchRequest request, InternetAccess internet) {
		super(request, internet);
	}

	@Override
	public Iterator<StoryAccess<VisibleStoryMeta>> iterator() {
		return new MetaSearchIterator(getSearchTerm(), getInternet());
	}

	/**
	 * Testing.
	 * @param args
	 */
	public static void main(String[] args) {
		for(StoryAccess<VisibleStoryMeta> s : new MetaSearchIterable(new SearchRequestFactory().setSearchOrder(SearchOrder.RATING), new SimpleInternetAccess())) {
			System.out.println(s.getMeta().getTitle());
		}
	}
}
