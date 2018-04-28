package com.messages;

public class DeleteDirMessage extends FSMessage {
	private static final long serialVersionUID = -4549674273272610152L;
	
	public String src;
	public String dirname;
	public DeleteDirMessage(String src, String dirname){
		super(FSMessageType.DeleteDir);
		this.src = src;
		this.dirname = dirname;
	}
}
