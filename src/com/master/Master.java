package com.master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.client.FileHandle;
import com.client.ClientFS.FSReturnVals;

import utility.Constants;

public class Master extends Thread {
	public Set<String> directories; 	// Keeps track of all directories
	public Map<String, FileHandle> fileMap;
		
	private ServerSocket ss;
	private Socket chunkServerSocket;
	
	//private Socket toChunkServerSocket;
	private ObjectInputStream fromChunkServerStream;
	private ObjectOutputStream toChunkServerStream;
	
	Master() {
		//TODO load previous directory snapshot
		loadState();
		
		try {
			ss = new ServerSocket(Constants.masterPort);
			chunkServerSocket = new Socket(Constants.ChunkServerIP, Constants.chunkServerPort);
			
			//create a socket to the chunk server and store it
			toChunkServerStream = new ObjectOutputStream(chunkServerSocket.getOutputStream());
			toChunkServerStream.flush();
			
			fromChunkServerStream = new ObjectInputStream(chunkServerSocket.getInputStream());
			toChunkServerStream.writeObject("master");
			toChunkServerStream.flush();
			//TODO Specify chunk size to server?
			//TODO Master needs a list of all chunkServers
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.start();
	}
	
	@Override
	public void run() {
		try {
			while (true){
				Socket s = ss.accept();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				//read in the identifier that will be sent so we know if this is the master
				String identifier = (String) ois.readObject();
				
				if (identifier.equals("server")){
					new MasterServerThread(s, ois, oos, this);
				}
				
				else if (identifier.equals("client")){
					//don't need to store client threads
					new MasterClientThread(s, ois, oos, this);
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();;
		}
	}
	
	public void loadState() {
		//TODO reload from checkpoint
		directories = new HashSet<String>();
		fileMap = new HashMap<String, FileHandle>();
		directories.add("/");
	}
	
	public FSReturnVals CreateDir(String src, String dirname) {
		if (!directories.contains(src)) {
			return FSReturnVals.SrcDirNotExistent;
		}
		
		if (directories.contains(src + dirname + '/')) {
			return FSReturnVals.DirExists;
		}
		
		directories.add(src + dirname + '/');
		return FSReturnVals.Success;
	}
	
	public FSReturnVals DeleteDir(String src, String dirname) {
		if (!directories.contains(src)) {
			return FSReturnVals.SrcDirNotExistent;
		}
		
		String dirToDelete = src + dirname + "/";

		for (String directory : directories) {
			if (directory.startsWith(dirToDelete) && !directory.equals(dirToDelete)) {
				return FSReturnVals.DirNotEmpty;
			}
		}
		
		for (String filePath : fileMap.keySet()) {
			if (filePath.startsWith(dirToDelete)) {
				return FSReturnVals.DirNotEmpty;
			}
		}
		
		directories.remove(dirToDelete);
		return FSReturnVals.Success;
	}
	
	public FSReturnVals RenameDir(String src, String newname) {
		src += "/";
		newname += "/";
		
		if (!directories.contains(src)) {
			return FSReturnVals.SrcDirNotExistent;
		}
		
		if (directories.contains(newname)) {
			return FSReturnVals.DestDirExists;	
		}
		
		for (String directory : directories) {
			if (directory.startsWith(src)) {
				String newDirectory = directory.replace(src, newname);
				directories.remove(directory);
				directories.add(newDirectory);
			}
		}
		
		for (String key : fileMap.keySet()) {
			if (key.startsWith(src)) {
				String newFilePath = key.replace(src, newname);
				FileHandle file = fileMap.get(key);
				file.setFileName(newFilePath);
				
				fileMap.remove(key);
				fileMap.put(newFilePath, file);
			}
		}

		return FSReturnVals.Success;
		
	}
	
	public String[] ListDir(String target) {
		ArrayList<String> directoryList = new ArrayList<String>();
		
		target += "/";
		
		for (String directory : directories) {
			if (directory.startsWith(target) && !directory.equals(target)) {
				// Trim out the last character: "/"
				directoryList.add(directory.substring(0, directory.length() - 1));
			}
		}
		
		for (String key : fileMap.keySet()) {
			if (key.startsWith(target)) {
				// Trim out the last character: "/"
				directoryList.add(key.substring(0, key.length() - 1));
			}
		}

		
		if (directoryList.size() == 0) {
			return null;
		} else {
			return directoryList.toArray(new String[directoryList.size()]);
		}
	}
	
	public FSReturnVals CreateFile(String tgtdir, String filename) {
		String filePath = tgtdir + filename;
		
		if (!directories.contains(tgtdir)) {
			return FSReturnVals.SrcDirNotExistent;
		}
		
		if (directories.contains(filePath)) {
			return FSReturnVals.FileExists;	
		}
		
		FileHandle newFileHandle = new FileHandle(filePath);		
		fileMap.put(filePath, newFileHandle);
		
		return FSReturnVals.Success;
	}
	
	public FSReturnVals DeleteFile(String tgtdir, String filename) {
		String filePath = tgtdir + filename;
		
		if (!directories.contains(tgtdir)) {
			return FSReturnVals.SrcDirNotExistent;
		}
		
		if (!directories.contains(filePath)) {
			return FSReturnVals.FileDoesNotExist;	
		}
		
		fileMap.remove(filePath);
		
		return FSReturnVals.Success;
	}
	
	public FSReturnVals OpenFile(String FilePath, FileHandle ofh) {
		
		if (!directories.contains(FilePath)) {
			return FSReturnVals.FileDoesNotExist;
		}
		
		if (!fileMap.containsKey(FilePath))
		{
			return FSReturnVals.Fail;
		}
		
		//Needs to get sent back to the network
		ofh.copy(fileMap.get(FilePath));
		return FSReturnVals.Success;
	}
	
	public FSReturnVals CloseFile(FileHandle ofh){
		//TODO calls operation log to checkpoint?
		return FSReturnVals.Success;
	}
	
}
