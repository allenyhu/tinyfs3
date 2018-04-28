package com.master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.messages.FSMessage;

public class MasterClientThread extends Thread{
	private Socket s;
	private Master master;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ClientFSMessageHandler cfsmh;
	
	//used specifically for the master to communicate with clients
	public MasterClientThread(Socket s, ObjectInputStream ois, ObjectOutputStream oos, Master master){
		this.s = s;
		this.master = master;
		this.oos = oos;
		this.ois = ois;
		cfsmh = new ClientFSMessageHandler();
		this.start();
	}
	
	@Override
	public void run(){
		try {
			while(true){
				//read message, and get updated message object once the action has executed
				FSMessage mess = (FSMessage) ois.readObject();
				FSMessage newMess = cfsmh.processMessage(mess, master);

				oos.reset();
				oos.writeObject(newMess);
				oos.flush();
			}
		} catch (ClassNotFoundException | IOException e) {
			//System.out.println("client disconnected from master");
		}finally{
			//master.save();
		}
	}

}
