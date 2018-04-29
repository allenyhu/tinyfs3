package com.messages;

import com.client.FileHandle;
import com.client.RID;
import com.client.TinyRec;

public class ReadPrevRecMessage extends RecMessage{
	public RID pivot;
	public TinyRec rec;
	
	public ReadPrevRecMessage(FileHandle ofh, TinyRec tinyRec, RID pivot){
		super(ofh, RecMessageType.ReadPrevRec);
		rec = tinyRec;
		this.pivot = pivot;
	}
}
