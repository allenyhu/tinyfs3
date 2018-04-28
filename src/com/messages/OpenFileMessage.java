package com.messages;

import com.client.FileHandle;

public class OpenFileMessage extends FSMessage {
	private static final long serialVersionUID = -7969673384992759387L;
	
	public String FilePath;
	public FileHandle ofh;
	public OpenFileMessage(String FilePath, FileHandle ofh){
		super(FSMessageType.OpenFile);
		this.FilePath = FilePath;
		this.ofh = ofh;
	}
}
