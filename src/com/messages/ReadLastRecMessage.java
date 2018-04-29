package com.messages;

import com.client.FileHandle;
import com.client.TinyRec;

public class ReadLastRecMessage extends RecMessage{
	public TinyRec rec;
	
	public ReadLastRecMessage(FileHandle ofh, TinyRec tinyRec){
		super(ofh, RecMessageType.ReadLastRec);
		this.rec = tinyRec;
	}
}

