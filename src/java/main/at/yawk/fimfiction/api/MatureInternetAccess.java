package at.yawk.fimfiction.api;

public interface MatureInternetAccess extends InternetAccess {
	public boolean isMature();
	
	public MatureInternetAccess setMature(boolean flag);
}
