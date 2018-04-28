package com.client;

import java.io.Serializable;
import java.util.ArrayList;

import utility.Constants;

public class FileHandle implements Serializable{
	private static final long serialVersionUID = 5199772076276151769L;
	
	private ArrayList<String> chunks;
	private ArrayList<String> servers;
	private ArrayList<Integer> serverPorts;
	private String fileName;
	
	public FileHandle() {
		servers = new ArrayList<String>();
		chunks = new ArrayList<String>();
		serverPorts = new ArrayList<Integer>();

		servers.add(Constants.ChunkServerIP);
		serverPorts.add(Constants.chunkServerPort);
	}
	
	public FileHandle(String name){
		fileName = name;
		
		servers = new ArrayList<String>();
		chunks = new ArrayList<String>();
		serverPorts = new ArrayList<Integer>();
		
		servers.add(Constants.ChunkServerIP);
		serverPorts.add(Constants.chunkServerPort);
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

	public ArrayList<Integer> getServerPorts() {
		return serverPorts;
	}

	public void addServerPort(int serverPort) {
		serverPorts.add(serverPort);
	}
	
	public void copy(FileHandle file) {
		fileName = file.fileName;
		chunks = file.chunks;
		servers = file.servers;
	}

	public ArrayList<String> getServers() {
		return servers;
	}

	public void addServer(String server) {
		servers.add(server);
	}
	
	public Boolean isValidFile() {
		return servers.size() > 0 && serverPorts.size() > 0 && fileName.length() > 0;
	}
}
