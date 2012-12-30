package at.yawk.fimfiction.api.cache;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import at.yawk.fimfiction.api.Character;

import at.yawk.fimfiction.api.InternetAccess;

public class CharacterImageCache {
	private static final boolean		DEFAULT_ANONYMOUS		= false;
	private static final int			DEFAULT_MAXIMUM_THREADS	= 5;
	
	private static CharacterImageCache	currentInstance			= null;
	
	public static CharacterImageCache getCurrentInstance() {
		return currentInstance;
	}
	
	public static CharacterImageCache getCurrentInstance(InternetAccess defaultAccess, int defaultMaximumThreads) {
		return currentInstance == null ? currentInstance = new CharacterImageCache(defaultAccess, defaultMaximumThreads) : currentInstance;
	}
	
	public static CharacterImageCache getCurrentInstance(InternetAccess defaultAccess) {
		return getCurrentInstance(defaultAccess, DEFAULT_MAXIMUM_THREADS);
	}
	
	private final Map<Character, BufferedImage>	images	= new EnumMap<Character, BufferedImage>(Character.class);
	private final InternetAccess				internet;
	
	private final Executor						downloader;
	
	public CharacterImageCache(InternetAccess internet, int maximumThreads, boolean anonymous) {
		this.internet = internet;
		downloader = Executors.newFixedThreadPool(maximumThreads);
		if(!anonymous)
			currentInstance = this;
	}
	
	public CharacterImageCache(InternetAccess internet, int maximumThreads) {
		this(internet, maximumThreads, DEFAULT_ANONYMOUS);
	}
	
	public CharacterImageCache(InternetAccess internet, boolean anonymous) {
		this(internet, DEFAULT_MAXIMUM_THREADS, anonymous);
	}
	
	public CharacterImageCache(InternetAccess internet) {
		this(internet, DEFAULT_MAXIMUM_THREADS);
	}
	
	public BufferedImage getImage(Character character) {
		if(!images.containsKey(character))
			updateCharacter(character);
		return images.get(character);
	}
	
	public void updateCharacter(final Character character) {
		images.put(character, null);
		downloader.execute(new Runnable() {
			@Override
			public void run() {
				try {
					images.put(character, ImageIO.read(internet.connect(new URL(character.getImageLocation())).getInputStream()));
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
