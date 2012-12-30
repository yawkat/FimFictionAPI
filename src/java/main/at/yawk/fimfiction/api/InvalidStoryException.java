package at.yawk.fimfiction.api;

public class InvalidStoryException extends NullPointerException {
	private static final long	serialVersionUID	= 1L;
	
	public InvalidStoryException() {
		super();
	}
	
	public InvalidStoryException(String s) {
		super(s);
	}
}
