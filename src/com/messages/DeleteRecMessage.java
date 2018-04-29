package com.messages;

import com.client.FileHandle;
import com.client.RID;

public class DeleteRecMessage extends RecMessage{
	public RID RecordID;
	
	public DeleteRecMessage(FileHandle ofh, RID rid){
		super(ofh, RecMessageType.DeleteRec);
		RecordID = rid;
	}
}

