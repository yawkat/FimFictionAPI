package at.yawk.fimfiction.api;

import java.util.HashMap;
import java.util.Map;

public enum StoryStatus {
	COMPLETE("Complete"),
	INCOMPLETE("Incomplete"),
	ON_HIATUS("On Hiatus"),
	CANCELLED("Cancelled");
	
	private final String name;

	private StoryStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	private static Map<String, StoryStatus> names = new HashMap<String, StoryStatus>(values().length);
	
	static {
		for(StoryStatus ss : values())
			names.put(ss.getName(), ss);
	}
	
	public static StoryStatus parse(String s) {
		return names.get(s);
	}
}
