package com.chunkserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.client.ClientFS.FSReturnVals;
import com.messages.FSMessage;

import recordmessages.RecordMessage;

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
		RecordMessage returnMessage = null;
		try {
			while(true){
				//read message, and get updated message object once the action has executed
				RecordMessage mess = (RecordMessage) ois.readObject();
				returnMess = cfsmh.processMessage(mess, master);

				oos.reset();
				oos.writeObject(returnMess);
				oos.flush();
			}
		} catch (ClassNotFoundException | IOException e) {
			//System.out.println("client disconnected from master");
		}finally{
			if (returnMess.returnVal == FSReturnVals.Success){
				master.WriteLog(returnMess);
			}
		}
	}
}
