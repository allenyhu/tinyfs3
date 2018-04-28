package com.messages;

import java.io.Serializable;

import com.client.ClientFS.FSReturnVals;

public class FSMessage implements Serializable {
	
	public enum FSMessageType {
		CreateDir,
		CloseFile,
		CreateFile,
		DeleteDir,
		OpenFile,
		ListDir,
		DeleteFile,
		RenameDir
	}
	
	public FSReturnVals returnVal;
	public FSMessageType type;
	
	public FSMessage(FSMessageType type) {
		this.type = type;
	}
	
	public void setReturnValue(FSReturnVals returnVal) {
		this.returnVal = returnVal;
	}
}
