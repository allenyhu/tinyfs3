package com.messages;

public class CreateFileMessage extends FSMessage {
	private static final long serialVersionUID = -8300914653017349258L;
	
	public String tgtdir;
	public String filename;
	public CreateFileMessage(String tgtdir, String filename){
		super(FSMessageType.CreateFile);
		this.tgtdir = tgtdir;
		this.filename = filename;
	}
}
