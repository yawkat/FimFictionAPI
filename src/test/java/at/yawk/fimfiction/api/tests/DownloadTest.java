package at.yawk.fimfiction.api.tests;

import java.io.IOException;
import java.util.zip.ZipInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.yawk.fimfiction.api.DownloadType;
import at.yawk.fimfiction.api.SimpleInternetAccess;
import at.yawk.fimfiction.api.actions.Downloader;
import at.yawk.fimfiction.api.immutable.SimpleIdentifier;

@RunWith(JUnit4.class)
public class DownloadTest {
    @Test
    public void downloadStoryEpub() throws IOException {
        final ZipInputStream zipInputStream = new ZipInputStream(Downloader.downloadStory(new SimpleIdentifier(StoryExample.STORY_ID), DownloadType.EPUB, new SimpleInternetAccess()));
        try {
            while(zipInputStream.getNextEntry() != null)
                ;
        } finally {
            zipInputStream.close();
        }
    }
    
    @Test
    public void downloadStoryHtml() throws IOException {
        Downloader.downloadStory(new SimpleIdentifier(StoryExample.STORY_ID), DownloadType.HTML, new SimpleInternetAccess()).close();
    }
    
    @Test
    public void downloadStoryTxt() throws IOException {
        Downloader.downloadStory(new SimpleIdentifier(StoryExample.STORY_ID), DownloadType.TXT, new SimpleInternetAccess()).close();
    }
}
