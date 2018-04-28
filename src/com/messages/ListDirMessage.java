package com.messages;

public class ListDirMessage extends FSMessage {
	private static final long serialVersionUID = 96263330671020199L;
	
	public String [] returnedDirs;
	public String tgt;
	public ListDirMessage(String tgt){
		super(FSMessageType.ListDir);
		this.tgt = tgt;
	}
}
