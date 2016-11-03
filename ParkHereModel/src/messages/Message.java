package messages;

public abstract class Message {
	public static final String insert = "INSERT";
	public static final String update = "UPDATE";
	public static final String get = "GET";
	public static final String search = "SEARCH";
	
	public String action;
}
