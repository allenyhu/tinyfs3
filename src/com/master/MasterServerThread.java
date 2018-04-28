package com.master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.client.FileHandle;

public class MasterServerThread extends Thread {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Master master;
	private String name;
	
	//specifically used for master to communicate with chunk server
	public MasterServerThread(Socket s, ObjectInputStream ois, ObjectOutputStream oos, Master master, String name){
		this.s = s;
		this.master = master;
		this.ois = ois;
		this.oos = oos;
		this.name = name;
		this.start();
	}
	
	@Override
	public void run(){
		//TODO shit still needs to be done
//		try {
//			while (true){
//				//just read simple string command since so few messages needed
//				String command = (String) ois.readObject();
//				if (command.equals("replace_file")){
//					FileHandle ofh = (FileHandle) ois.readObject();
//					master.fileMap.replace(ofh.getFileNamePath(), ofh);
//					master.save();
//				}
//			}
//		} catch (IOException | ClassNotFoundException e) {
//			//System.out.println("chunk server disconnected from master");
//		}
		
	}
}
