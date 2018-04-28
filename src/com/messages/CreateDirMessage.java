package com.messages;

public class CreateDirMessage extends FSMessage{

	private static final long serialVersionUID = 3940459466768935637L;
	public String src;
	public String dirname;
	
	public CreateDirMessage(String src, String dirname){
		super(FSMessageType.CreateDir);
		this.src = src;
		this.dirname = dirname;
	}
}
