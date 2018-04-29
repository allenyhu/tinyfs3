package com.messages;

import com.client.FileHandle;
import com.client.RID;

public class AppendRecMessage extends RecMessage{
	public byte [] payload;
	public RID rid;
	
	public AppendRecMessage(FileHandle ofh, byte [] payload, RID rid){
		super(ofh, RecMessageType.AppendRec);
		this.payload = payload;
		this.rid = rid;
	}
}
