package com.messages;

import com.client.FileHandle;
import com.client.RID;
import com.client.TinyRec;

public class ReadNextRecMessage extends RecMessage {
	public RID pivot;
	public TinyRec rec;
	
	public ReadNextRecMessage(FileHandle ofh, RID pivot, TinyRec rec){
		super(ofh, RecMessageType.ReadNextRec);
		this.pivot = pivot;
		this.rec = rec;
	}
}

