package com.messages;

import com.client.FileHandle;

public class CloseFileMessage extends FSMessage {
	private static final long serialVersionUID = -1555273767976509933L;
	public FileHandle ofh;
	public CloseFileMessage(FileHandle ofh){
		super(FSMessageType.CloseFile);
		this.ofh = ofh;
	}
}
