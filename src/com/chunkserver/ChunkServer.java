package com.chunkserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
//import java.util.Arrays;

import com.client.Client;
import com.interfaces.ChunkServerInterface;
import com.master.MasterClientThread;
import com.master.MasterServerThread;

import utility.Constants;

/**
 * implementation of interfaces at the chunkserver side
 * @author Shahram Ghandeharizadeh
 *
 */

public class ChunkServer extends Thread implements ChunkServerInterface {
	final static String filePath = "csci485/";	//or C:\\newfile.txt
	public final static String ClientConfigFile = "ClientConfig.txt";
	
	//Used for the file system
	public static long counter;
	private ServerSocket ss;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public static int PayloadSZ = Integer.SIZE/Byte.SIZE;  //Number of bytes in an integer
	public static int CMDlength = Integer.SIZE/Byte.SIZE;  //Number of bytes in an integer  
	
	//Commands recognized by the Server
	public static final int CreateChunkCMD = 101;
	public static final int ReadChunkCMD = 102;
	public static final int WriteChunkCMD = 103;
	
	//Replies provided by the server
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	/**
	 * Initialize the chunk server
	 */
	public ChunkServer(){
		try {
			//Allocate a port and write it to the config file for the Client to consume
			ss = new ServerSocket(Constants.chunkServerPort);
			
		} catch (IOException ex) {
			System.out.println("Error, failed to open a new socket to listen on ChunkServer.");
			ex.printStackTrace();
		}
		
		File dir = new File(filePath);
		File[] fs = dir.listFiles();
		
		this.start();
	}
	
	/**
	 * Each chunk is corresponding to a file.
	 * Return the chunk handle of the last chunk in the file.
	 */
	public void createChunk(String chunkHandle) {
		Chunk chunk = new Chunk(this.filePath, chunkHandle);
		String name = chunk.getChunkHandle();
		
		
	}
	
	/**
	 * Write the byte array to the chunk at the offset
	 * The byte array size should be no greater than 4KB
	 */
	public boolean writeChunk(String ChunkHandle, byte[] payload, int offset) {
		try {
			//If the file corresponding to ChunkHandle does not exist then create it before writing into it
			RandomAccessFile raf = new RandomAccessFile(filePath + ChunkHandle, "rw");
			raf.seek(offset);
			raf.write(payload, 0, payload.length);
			raf.close();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * read the chunk at the specific offset
	 */
	public byte[] readChunk(String ChunkHandle, int offset, int NumberOfBytes) {
		try {
			//If the file for the chunk does not exist the return null
			boolean exists = (new File(filePath + ChunkHandle)).exists();
			if (exists == false) return null;
			
			//File for the chunk exists then go ahead and read it
			byte[] data = new byte[NumberOfBytes];
			RandomAccessFile raf = new RandomAccessFile(filePath + ChunkHandle, "rw");
			raf.seek(offset);
			raf.read(data, 0, NumberOfBytes);
			raf.close();
			return data;
		} catch (IOException ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void run()
	{
		try {
			while (true){
				Socket s = ss.accept();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				//read in the identifier that will be sent so we know if this is the master
				String identifier = (String) ois.readObject();
				
				if (identifier.equals("server")){
					new ServerMasterThread(s, ois, oos, this);
				}
				
				else if (identifier.equals("client")){
					//don't need to store client threads
					new ServerClientThread(s, ois, oos, this);
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();;
		}
//		
//		//Used for communication with the Client via the network
//		ObjectOutputStream WriteOutput = null;
//		ObjectInputStream ReadInput = null;
//		
//		boolean done = false;
//		Socket ClientConnection = null;  //A client's connection to the server

		//while (!done){
//			try {
//				ClientConnection = ss.accept();
//				ReadInput = new ObjectInputStream(ClientConnection.getInputStream());
//				WriteOutput = new ObjectOutputStream(ClientConnection.getOutputStream());
//				
//				//Use the existing input and output stream as long as the client is connected
//				while (!ClientConnection.isClosed()) {
//					int payloadsize =  Client.ReadIntFromInputStream("ChunkServer", ReadInput);
//					if (payloadsize == -1) 
//						break;
//					int CMD = Client.ReadIntFromInputStream("ChunkServer", ReadInput);
//					switch (CMD){
//					case CreateChunkCMD:
//						String chunkhandle = this.createChunk();
//						byte[] CHinbytes = chunkhandle.getBytes();
//						WriteOutput.writeInt(ChunkServer.PayloadSZ + CHinbytes.length);
//						WriteOutput.write(CHinbytes);
//						WriteOutput.flush();
//						break;
//
//					case ReadChunkCMD:
//						int offset =  Client.ReadIntFromInputStream("ChunkServer", ReadInput);
//						int payloadlength =  Client.ReadIntFromInputStream("ChunkServer", ReadInput);
//						int chunkhandlesize = payloadsize - ChunkServer.PayloadSZ - ChunkServer.CMDlength - (2 * 4);
//						if (chunkhandlesize < 0)
//							System.out.println("Error in ChunkServer.java, ReadChunkCMD has wrong size.");
//						byte[] CHinBytes = Client.RecvPayload("ChunkServer", ReadInput, chunkhandlesize);
//						String ChunkHandle = (new String(CHinBytes)).toString();
//						
//						byte[] res = this.readChunk(ChunkHandle, offset, payloadlength);
//						
//						if (res == null)
//							WriteOutput.writeInt(ChunkServer.PayloadSZ);
//						else {
//							WriteOutput.writeInt(ChunkServer.PayloadSZ + res.length);
//							WriteOutput.write(res);
//						}
//						WriteOutput.flush();
//						break;
//
//					case WriteChunkCMD:
//						offset =  Client.ReadIntFromInputStream("ChunkServer", ReadInput);
//						payloadlength =  Client.ReadIntFromInputStream("ChunkServer", ReadInput);
//						byte[] payload = Client.RecvPayload("ChunkServer", ReadInput, payloadlength);
//						chunkhandlesize = payloadsize - ChunkServer.PayloadSZ - ChunkServer.CMDlength - (2 * 4) - payloadlength;
//						if (chunkhandlesize < 0)
//							System.out.println("Error in ChunkServer.java, WritehChunkCMD has wrong size.");
//						CHinBytes = Client.RecvPayload("ChunkServer", ReadInput, chunkhandlesize);
//						ChunkHandle = (new String(CHinBytes)).toString();
//
//						//Call the writeChunk command
//						if (this.writeChunk(ChunkHandle, payload, offset))
//							WriteOutput.writeInt(ChunkServer.TRUE);
//						else WriteOutput.writeInt(ChunkServer.FALSE);
//						
//						WriteOutput.flush();
//						break;
//
//					default:
//						System.out.println("Error in ChunkServer, specified CMD "+CMD+" is not recognized.");
//						break;
//					}
//				}
//			} catch (IOException ex){
//				System.out.println("Client Disconnected");
//			} finally {
//				try {
//					if (ClientConnection != null)
//						ClientConnection.close();
//					if (ReadInput != null)
//						ReadInput.close();
//					if (WriteOutput != null) WriteOutput.close();
//				} catch (IOException fex){
//					System.out.println("Error (ChunkServer):  Failed to close either a valid connection or its input/output stream.");
//					fex.printStackTrace();
//				}
//			}
//		}
	}

	public static void main(String args[])
	{
		new ChunkServer();
	}
}
