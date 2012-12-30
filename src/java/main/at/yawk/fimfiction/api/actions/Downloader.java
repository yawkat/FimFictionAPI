package at.yawk.fimfiction.api.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import at.yawk.fimfiction.api.DownloadType;
import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.URLs;

public final class Downloader {
	private Downloader() {
		
	}
	
	public static void downloadStory(Identifier story, OutputStream output, DownloadType dlType, InternetAccess internet) throws IOException {
		final String s;
		switch(dlType) {
		case EPUB:
			s = URLs.DOWNLOAD_EPUB;
			break;
		case HTML:
			s = URLs.DOWNLOAD_HTML;
			break;
		case TXT:
			s = URLs.DOWNLOAD_TXT;
			break;
		default:
			throw new IllegalArgumentException("Unknown download type " + dlType);
		}
		final URLConnection c = internet.connect(new URL(s + story.getId()));
		c.connect();
		IOUtils.copy(c.getInputStream(), output);
	}
	
	public static void downloadStory(Identifier story, File output, DownloadType dlType, InternetAccess internet) throws IOException {
		final OutputStream os = new FileOutputStream(output);
		downloadStory(story, os, dlType, internet);
		os.close();
	}
}
