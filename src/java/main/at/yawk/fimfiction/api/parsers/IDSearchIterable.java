package at.yawk.fimfiction.api.parsers;

import java.util.Iterator;

import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.SearchRequest;
import at.yawk.fimfiction.api.util.SearchUtil;

public class IDSearchIterable extends SearchIterable<Identifier> {
	public IDSearchIterable(SearchRequest request, InternetAccess internet) {
		super(request, internet);
	}

	@Override
	public Iterator<Identifier> iterator() {
		return new IDSearchIterator(SearchUtil.getSearchGet(getSearchRequest()), getInternet());
	}
}
