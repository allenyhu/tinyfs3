package com.master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

import com.client.FileHandle;

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
			//System.out.println("exception in master run method");
		}
	}
}
