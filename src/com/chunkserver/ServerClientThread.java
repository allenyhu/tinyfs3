package com.chunkserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerClientThread extends Thread {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ChunkServer cs;
	
	//specifically used for master to communicate with chunk server
	public ServerClientThread(Socket s, ObjectInputStream ois, ObjectOutputStream oos, ChunkServer cs) {
		this.s = s;
		this.cs = cs;
		this.ois = ois;
		this.oos = oos;
		this.start();
	}
	
	@Override
	public void run() {
		
	}
}
