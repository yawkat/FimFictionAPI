package at.yawk.fimfiction.api.cache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.json.simple.parser.ParseException;

import at.yawk.fimfiction.api.Identifier;
import at.yawk.fimfiction.api.InternetAccess;
import at.yawk.fimfiction.api.StoryMeta;
import at.yawk.fimfiction.api.immutable.EmptyStory;
import at.yawk.fimfiction.api.parsers.JSONMetaLoader;

public class StoryMetadataCache {
	private static final boolean		DEFAULT_ANONYMOUS		= false;
	private static final int			DEFAULT_MAXIMUM_THREADS	= 5;
	
	private static StoryMetadataCache	currentInstance			= null;
	
	public static StoryMetadataCache getCurrentInstance() {
		return currentInstance;
	}
	
	public static StoryMetadataCache getCurrentInstance(InternetAccess defaultAccess, int defaultMaximumThreads) {
		return currentInstance == null ? currentInstance = new StoryMetadataCache(defaultAccess, defaultMaximumThreads) : currentInstance;
	}
	
	public static StoryMetadataCache getCurrentInstance(InternetAccess defaultAccess) {
		return getCurrentInstance(defaultAccess, DEFAULT_MAXIMUM_THREADS);
	}
	
	private final Map<Identifier, StoryMeta>	metadata	= new HashMap<Identifier, StoryMeta>();
	private final InternetAccess				internet;
	
	private final Executor						downloader;
	
	public StoryMetadataCache(InternetAccess internet, int maximumThreads, boolean anonymous) {
		this.internet = internet;
		downloader = Executors.newFixedThreadPool(maximumThreads);
	}
	
	public StoryMetadataCache(InternetAccess internet, int maximumThreads) {
		this(internet, maximumThreads, DEFAULT_ANONYMOUS);
	}
	
	public StoryMetadataCache(InternetAccess internet, boolean anonymous) {
		this(internet, DEFAULT_MAXIMUM_THREADS, anonymous);
	}
	
	public StoryMetadataCache(InternetAccess internet) {
		this(internet, DEFAULT_MAXIMUM_THREADS);
	}
	
	public StoryMeta getFinalStoryMeta(Identifier id) {
		if(!metadata.containsKey(id))
			updateStoryMeta(id);
		return metadata.get(id);
	}
	
	public StoryMeta getStoryMetaAsync(Identifier id) {
		if(metadata.get(id) == null) {
			if(!metadata.containsKey(id))
				updateStoryMetaAsync(id);
			return EmptyStory.instance();
		} else
			return metadata.get(id);
	}
	
	public void updateStoryMetaAsync(final Identifier id) {
		metadata.put(id, null);
		downloader.execute(new Runnable() {
			@Override
			public void run() {
				updateStoryMeta(id);
			}
		});
	}
	
	public void updateStoryMeta(Identifier id) {
		try {
			metadata.put(id, JSONMetaLoader.getStoryMeta(id, internet));
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ParseException e) {
			e.printStackTrace();
		}
	}
}
