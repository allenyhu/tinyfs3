package com.master;

import com.client.ClientFS.FSReturnVals;
import com.messages.ClientFSMessages.FSMessage;
import com.messages.ClientFSMessages.CloseFileMessage;
import com.messages.ClientFSMessages.CreateDirMessage;
import com.messages.ClientFSMessages.CreateFileMessage;
import com.messages.ClientFSMessages.DeleteDirMessage;
import com.messages.ClientFSMessages.DeleteFileMessage;
import com.messages.ClientFSMessages.ListDirMessage;
import com.messages.ClientFSMessages.OpenFileMessage;
import com.messages.ClientFSMessages.RenameDirMessage;
import com.messages.ClientFSMessages.FSMessageType;

public class ClientFSMessageHandler {
	public FSMessage getMessage(FSMessage message, Master master) {
		switch (message.type) {
			case CloseFile:
				CloseFileMessage realMess = (CloseFileMessage) message;
				FSReturnVals returnVal = master.CloseFile(realMess.ofh);
				realMess.returnVal = returnVal;
				return realMess;
				
			case CreateDir:
				CreateDirMessage realMess = (CreateDirMessage) message;
				FSReturnVals returnVal = master.CreateDir(realMess.src, realMess.dirname);
				realMess.returnVal = returnVal;
				return realMess;
				
			case CreateFile:
				CreateFileMessage realMess = (CreateFileMessage) message;
				FSReturnVals returnVal = master.CreateFile(realMess.tgtdir, realMess.filename);
				realMess.returnVal = returnVal;
				return realMess;
				
			case DeleteDir:
				DeleteDirMessage realMess = (DeleteDirMessage) message;
				FSReturnVals returnVal = master.DeleteDir(realMess.src, realMess.dirname);
				realMess.returnVal = returnVal;
				return realMess;
				
			case DeleteFile:
				DeleteFileMessage realMess = (DeleteFileMessage) message;
				FSReturnVals returnVal = master.DeleteFile(realMess.tgtdir, realMess.filename);
				realMess.returnVal = returnVal;
				return realMess;
				
			case ListDir:
				ListDirMessage realMess = (ListDirMessage) message;
				realMess.returnedDirs = master.ListDir(realMess.tgt);
				return realMess;
				
			case OpenFile:
				OpenFileMessage realMess = (OpenFileMessage) message;
				FSReturnVals returnVal = master.OpenFile(realMess.FilePath, realMess.ofh);
				realMess.returnVal = returnVal;
				return realMess;
				
			case RenameDir:
				RenameDirMessage realMess = (RenameDirMessage) message;
				FSReturnVals returnVal = master.RenameDir(realMess.src, realMess.NewName);
				realMess.returnVal = returnVal;
				return realMess;
				
			default:
				return null;
		}
	}
}
