package com.messages;

public class DeleteFileMessage extends FSMessage {
	private static final long serialVersionUID = -5191104824605724501L;
	
	public String tgtdir;
	public String filename;
	public DeleteFileMessage(String tgtdir, String filename){
		super(FSMessageType.DeleteFile);
		this.tgtdir = tgtdir;
		this.filename = filename;
	}
}
