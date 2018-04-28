package com.messages;

import java.io.Serializable;
import com.client.ClientFS.FSReturnVals;
import com.client.FileHandle;
import com.client.RID;

public class ClientFSMessages {
	
	public enum FSMessageType {
		CreateDir,
		CloseFile,
		CreateFile,
		DeleteDir,
		OpenFile,
		ListDir,
		DeleteFile
	}

	public class FSMessage implements Serializable{
		public FSReturnVals returnVal;
		public FSMessageType type;
	}
	
	public class CreateDirMessage extends FSMessage{
		public String src;
		public String dirname;
		public CreateDirMessage(String src, String dirname){
			this.src = src;
			this.dirname = dirname;
			this.type = CreateDir;
		}

	}
	
	public class CloseFileMessage extends FSMessage{
		public FileHandle ofh;
		public CloseFileMessage(FileHandle ofh){
			this.ofh = ofh;
			this.type = CloseFile;
		}
	}
	
	public class CreateFileMessage extends FSMessage{
		public String tgtdir;
		public String filename;
		public CreateFileMessage(String tgtdir, String filename){
			this.tgtdir = tgtdir;
			this.filename = filename;
			this.type = CreateFile;
		}
	}

	public class DeleteDirMessage extends FSMessage{
		public String src;
		public String dirname;
		public DeleteDirMessage(String src, String dirname){
			this.src = src;
			this.dirname = dirname;
			this.type = DeleteDir;
		}
	}
	
	public class OpenFileMessage extends FSMessage{
		public String FilePath;
		public FileHandle ofh;
		public OpenFileMessage(String FilePath, FileHandle ofh){
			this.FilePath = FilePath;
			this.ofh = ofh;
			this.type = OpenFile;
		}
	}
	
	public class ListDirMessage extends FSMessage{
		public String [] returnedDirs;
		public String tgt;
		public ListDirMessage(String tgt){
			this.tgt = tgt;
			this.type = ListDir;
		}
	}
	
	public class DeleteFileMessage extends FSMessage{
		public String tgtdir;
		public String filename;
		public DeleteFileMessage(String tgtdir, String filename){
			this.tgtdir = tgtdir;
			this.filename = filename;
			this.type = DeleteFile;
		}
	}
	
}
