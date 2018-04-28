package com.client;

import java.io.Serializable;
import java.util.ArrayList;

import utility.Constants;

public class FileHandle implements Serializable{
	private static final long serialVersionUID = 5199772076276151769L;
	
	private ArrayList<String> chunks;
	private ArrayList<String> servers;
	private String fileName;
	private int serverPort = Constants.chunkServerPort;
	
	public FileHandle() {
		
	}
	
	public FileHandle(String name){
		fileName = name;
		servers.add(Constants.ChunkServerIP);
		//TODO modify list to include ports?
	}
	
	public void setFileName(String newName) {
		fileName = newName;
	}
	
	public String getFileName() {
		return fileName;
	}

	public ArrayList<String> getChunks() {
		return chunks;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public void copy(FileHandle file) {
		fileName = file.fileName;
		chunks = file.chunks;
		servers = file.servers;
		serverPort = file.serverPort;
	}

	public ArrayList<String> getServers() {
		return servers;
	}

	public void addServer(String server) {
		servers.add(server);
	}
}
