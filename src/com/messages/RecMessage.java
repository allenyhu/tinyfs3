package com.messages;

import java.io.Serializable;

import com.client.ClientFS.FSReturnVals;
import com.client.FileHandle;

public class RecMessage implements Serializable{
	public enum RecMessageType {
		AppendRec,
		DeleteRec,
		ReadFirstRec,
		ReadLastRec,
		ReadNextRec,
		ReadPrevRec
	}
	
	public FileHandle ofh;
	public FSReturnVals returnVal;
	public RecMessageType type;
	
	public RecMessage(FileHandle ofh, RecMessageType type){
		this.type = type;
		this.ofh = ofh;
	}
}
