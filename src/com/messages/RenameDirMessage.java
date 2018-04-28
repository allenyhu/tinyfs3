package com.messages;

public class RenameDirMessage extends FSMessage{
	private static final long serialVersionUID = 4038230214647482510L;
	public String src;
	public String newName;
	
	public RenameDirMessage(String src, String newName) {
		super(FSMessageType.RenameDir);
		this.src = src;
		this.newName = newName;
	}
}
