package com.client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.messages.CloseFileMessage;
import com.messages.CreateDirMessage;
import com.messages.CreateFileMessage;
import com.messages.DeleteDirMessage;
import com.messages.DeleteFileMessage;
import com.messages.ListDirMessage;
import com.messages.OpenFileMessage;
import com.messages.RenameDirMessage;

public class ClientFS {
	
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private String masterIP;
	private Integer masterPort;

	public enum FSReturnVals {
		DirExists, // Returned by CreateDir when directory exists
		DirNotEmpty, //Returned when a non-empty directory is deleted
		SrcDirNotExistent, // Returned when source directory does not exist
		DestDirExists, // Returned when a destination directory exists
		FileExists, // Returned when a file exists
		FileDoesNotExist, // Returns when a file does not exist
		BadHandle, // Returned when the handle for an open file is not valid
		RecordTooLong, // Returned when a record size is larger than chunk size
		BadRecID, // The specified RID is not valid, used by DeleteRecord
		RecDoesNotExist, // The specified record does not exist, used by DeleteRecord
		NotImplemented, // Specific to CSCI 485 and its unit tests
		Success, //Returned when a method succeeds
		Fail //Returned when a method fails
	}
	
	//Client connection with the master, communicates through the MasterClientThread
	public ClientFS(){
		try {
			s = new Socket(masterIP, masterPort);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			oos.writeObject("client");
			oos.flush();
		} catch (IOException e) {
			System.out.println("Exception in ClientFS constructor: "+e.getMessage());
		}
	}

	/**
	 * Creates the specified dirname in the src directory Returns
	 * SrcDirNotExistent if the src directory does not exist Returns
	 * DestDirExists if the specified dirname exists
	 *
	 * Example usage: CreateDir("/", "Shahram"), CreateDir("/Shahram/",
	 * "CSCI485"), CreateDir("/Shahram/CSCI485/", "Lecture1")
	 */
	public FSReturnVals CreateDir(String src, String dirname) {
		try {
			oos.writeObject(new CreateDirMessage(src, dirname));
			oos.flush();
			CreateDirMessage returnMessage = (CreateDirMessage) ois.readObject();
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in creating directory: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Deletes the specified dirname in the src directory Returns
	 * SrcDirNotExistent if the src directory does not exist Returns
	 * DestDirExists if the specified dirname exists
	 *
	 * Example usage: DeleteDir("/Shahram/CSCI485/", "Lecture1")
	 */
	public FSReturnVals DeleteDir(String src, String dirname) {
		try {
			oos.writeObject(new DeleteDirMessage(src, dirname));
			oos.flush();
			DeleteDirMessage returnMessage = (DeleteDirMessage) ois.readObject();
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in deleting directory: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Renames the specified src directory in the specified path to NewName
	 * Returns SrcDirNotExistent if the src directory does not exist Returns
	 * DestDirExists if a directory with NewName exists in the specified path
	 *
	 * Example usage: RenameDir("/Shahram/CSCI485", "/Shahram/CSCI550") changes
	 * "/Shahram/CSCI485" to "/Shahram/CSCI550"
	 */
	public FSReturnVals RenameDir(String src, String NewName) {
		try {
			oos.writeObject(new RenameDirMessage(src, NewName));
			oos.flush();
			RenameDirMessage returnMessage = (RenameDirMessage) ois.readObject();
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in renaming directory: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Lists the content of the target directory Returns SrcDirNotExistent if
	 * the target directory does not exist Returns null if the target directory
	 * is empty
	 *
	 * Example usage: ListDir("/Shahram/CSCI485")
	 */
	public String[] ListDir(String tgt) {
		try {
			oos.writeObject(new ListDirMessage(tgt));
			oos.flush();
			ListDirMessage returnMessage = (ListDirMessage) ois.readObject();
			return returnMessage.returnedDirs;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in listing directory: "+e.getMessage());
			return null;
		}
	}

	/**
	 * Creates the specified filename in the target directory Returns
	 * SrcDirNotExistent if the target directory does not exist Returns
	 * FileExists if the specified filename exists in the specified directory
	 *
	 * Example usage: Createfile("/Shahram/CSCI485/Lecture1/", "Intro.pptx")
	 */
	public FSReturnVals CreateFile(String tgtdir, String filename) {
		try {
			oos.writeObject(new CreateFileMessage(tgtdir, filename));
			oos.flush();
			CreateFileMessage returnMessage = (CreateFileMessage) ois.readObject();
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in creating file: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Deletes the specified filename from the tgtdir Returns SrcDirNotExistent
	 * if the target directory does not exist Returns FileDoesNotExist if the
	 * specified filename is not-existent
	 *
	 * Example usage: DeleteFile("/Shahram/CSCI485/Lecture1/", "Intro.pptx")
	 */
	public FSReturnVals DeleteFile(String tgtdir, String filename) {
		try {
			oos.writeObject(new DeleteFileMessage(tgtdir, filename));
			oos.flush();
			DeleteFileMessage returnMessage = (DeleteFileMessage) ois.readObject();
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in deleting file: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Opens the file specified by the FilePath and populates the FileHandle
	 * Returns FileDoesNotExist if the specified filename by FilePath is
	 * not-existent
	 *
	 * Example usage: OpenFile("/Shahram/CSCI485/Lecture1/Intro.pptx", FH1)
	 */
	public FSReturnVals OpenFile(String FilePath, FileHandle ofh) {
		try {
			oos.writeObject(new OpenFileMessage(FilePath, ofh));
			oos.flush();
			OpenFileMessage returnMessage = (OpenFileMessage) ois.readObject();
			ofh.copy(returnMessage.ofh);
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in opening file: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Closes the specified file handle Returns BadHandle if ofh is invalid
	 *
	 * Example usage: CloseFile(FH1)
	 */
	public FSReturnVals CloseFile(FileHandle ofh) {
		try {
			oos.writeObject(new CloseFileMessage(ofh));
			oos.flush();
			CloseFileMessage returnMessage = (CloseFileMessage) ois.readObject();
			ofh = returnMessage.ofh;
			return returnMessage.returnVal;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Exception in closing file: "+e.getMessage());
			return FSReturnVals.Fail;
		}
	}

}
