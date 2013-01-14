package at.yawk.fimfiction.api.parsers;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TagNode;
import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.immutable.SimpleIdentifier;

public class IDSearchIterator extends XMLSearchIterator<Identifier> {
    public IDSearchIterator(String request, InternetAccess internet) {
        super(request, internet);
    }
    
    @Override
    protected Identifier[] getNextBlock(Lexer lexer) throws Exception {
        final List<Identifier> identifiers = new ArrayList<Identifier>(10);
        boolean isInH2 = false;
        Node n;
        while((n = lexer.nextNode()) != null) {
            if(n instanceof TagNode && !((TagNode)n).isEndTag()) {
                if(isInH2) {
                    if(((TagNode)n).getTagName().equals("A") && ((TagNode)n).getAttribute("id") == null) {
                        final String partHref = ((TagNode)n).getAttribute("href").substring(7);
                        identifiers.add(new SimpleIdentifier(Integer.parseInt(partHref.substring(0, partHref.indexOf('/')))));
                        isInH2 = false;
                    }
                } else if(((TagNode)n).getTagName().equals("H2")) {
                    isInH2 = true;
                }
            }
        }
        return identifiers.toArray(new Identifier[identifiers.size()]);
    }
}
