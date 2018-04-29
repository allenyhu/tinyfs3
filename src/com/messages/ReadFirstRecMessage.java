package com.messages;

import com.client.FileHandle;
import com.client.TinyRec;

public class ReadFirstRecMessage extends RecMessage{
	public TinyRec rec;
	
	public ReadFirstRecMessage(FileHandle ofh, TinyRec rec){
		super(ofh, RecMessageType.ReadFirstRec);
		this.rec = rec;
		
	}
}
