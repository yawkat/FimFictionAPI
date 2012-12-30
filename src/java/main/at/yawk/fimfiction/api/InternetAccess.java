package at.yawk.fimfiction.api;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public interface InternetAccess {
	public URLConnection connect(URL url) throws IOException;
}
