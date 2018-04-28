package com.client;

import java.util.ArrayList;

public class FileHandle {

	private ArrayList<String> chunks;
	private String fileName;
	private String serverIPAddress;
	private int serverPort;
	
	public FileHandle(String name){
		fileName = name;
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

	public String getServerIPAddress() {
		return serverIPAddress;
	}

	public void setServerIPAddress(String serverIPAddress) {
		this.serverIPAddress = serverIPAddress;
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
		serverIPAddress = file.serverIPAddress;
		serverPort = file.serverPort;
	}
}
