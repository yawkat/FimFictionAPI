package at.yawk.fimfiction.api;

public interface StoryAccess<M extends GeneralStoryMeta> extends Identifier {
	public Identifier getIdentifier();
	
	public M getMeta();
}
