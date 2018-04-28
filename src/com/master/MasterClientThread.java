package com.master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MasterClientThread extends Thread{
	private Socket s;
	private Master master;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	//used specifically for the master to communicate with clients
	public MasterClientThread(Socket s, ObjectInputStream ois, ObjectOutputStream oos, Master master){
		this.s = s;
		this.master = master;
		this.oos = oos;
		this.ois = ois;

		this.start();
	}
	
	@Override
	public void run(){
		
//		try {
//			while(true){
//				/*
//				 * TODO read message, process it, then respond with a message
//				 */
//				
////				FSMessage mess = (FSMessage) ois.readObject();
////				FSMessage newMess = factory.getAction(mess.getClass()).execute(mess, master);
////
////				oos.reset();
////				oos.writeObject(newMess);
////				oos.flush();
//			}
//		} catch (ClassNotFoundException | IOException e) {
//			//System.out.println("client disconnected from master");
//		}
	}

}
