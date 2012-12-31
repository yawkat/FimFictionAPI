package at.yawk.fimfiction.api;

public interface VisibleStoryMeta extends GeneralStoryMeta {
	public Character[] getCharacters();
	
	public MatureCategory[] getMatureCategories();
}
