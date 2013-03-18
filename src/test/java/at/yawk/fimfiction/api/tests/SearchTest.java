package at.yawk.fimfiction.api.tests;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.SearchOrder;
import at.yawk.fimfiction.api.SimpleInternetAccess;
import at.yawk.fimfiction.api.factories.SearchRequestFactory;
import at.yawk.fimfiction.api.parsers.IDSearchIterable;
import at.yawk.fimfiction.api.parsers.MetaSearchIterable;

@RunWith(JUnit4.class)
public class SearchTest {
    @Test
    public void trySearchIdentifier() {
        int counter = 0;
        for(Identifier i : new IDSearchIterable(new SearchRequestFactory().setSearchOrder(SearchOrder.RATING), new SimpleInternetAccess())) {
            if(counter++ >= 9) {
                return;
            }
        }
        TestCase.fail();
    }
    
    @Test
    public void trySearchMeta() {
        int counter = 0;
        for(Identifier i : new MetaSearchIterable(new SearchRequestFactory().setSearchOrder(SearchOrder.RATING), new SimpleInternetAccess())) {
            if(counter++ >= 9) {
                return;
            }
        }
        TestCase.fail();
    }
}
