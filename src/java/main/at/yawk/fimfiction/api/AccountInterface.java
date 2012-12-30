package at.yawk.fimfiction.api;

public interface AccountInterface {
	public boolean login(String username, String password);
	
	public boolean isLoggedIn();
}
