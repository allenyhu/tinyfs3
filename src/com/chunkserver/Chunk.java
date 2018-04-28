package com.chunkserver;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import utility.Constants;

public class Chunk {
	private String filePath;
	private int numRecords;
	private int freeSpace; //# bytes available; this will always be 4 bytes less than actual free bytes to store record's offset
	private int freeOffset; //offset of free space from front of byte[]
	private String chunkHandle;
	
	/**
	 * Initializes Chunk data and creates File in filespace
	 * @param filePath path that the chunkserver will write to
	 * @param chunkHandle UUID assigned by Master
	 */
	public Chunk(String filePath, String chunkHandle) {
		this.filePath = filePath;
		this.chunkHandle = chunkHandle;
		numRecords = 0;
		freeSpace = Constants.chunkSize - 16; //12 for numRecords, freeSpace, freeOffset; 4 for RID offset
		freeOffset = 12;
		try {
			byte[] fill = new byte[Constants.chunkSize];
			
			//init file to be 4KB of dummy data
			RandomAccessFile raf = new RandomAccessFile(this.filePath + "/" + this.chunkHandle, "rw");
			raf.seek(0);
			raf.write(fill);
			raf.close();
			
			this.writeHeader();
		} catch (IOException ioe) {
			System.out.println("Chunk " + this.chunkHandle + " constructor ioe: " + ioe.getMessage());
			this.chunkHandle = null;
		}
		this.writeHeader();
	}
	
	/**
	 * Helper function to write header data to the Chunk File. Always writes to first 12 bytes (3 ints)
	 */
	private void writeHeader() {
		try {
			//Byte array for numRecords, freeSpace, freeOffset
			ByteBuffer bb = ByteBuffer.allocate(12);
			bb.putInt(0, this.numRecords);
			bb.putInt(4, this.freeSpace);
			bb.putInt(8, this.freeOffset);
			
			//write to beginning of RAF
			RandomAccessFile raf = new RandomAccessFile(this.filePath + "/" + this.chunkHandle, "rw");
			raf.seek(0);
			raf.write(bb.array());
			raf.close();
		} catch (IOException ioe) {
			System.out.println("Chunk " + this.chunkHandle + " writeHeader ioe: " + ioe.getMessage());
		}
	}
	
	
	/**
	 * Appends the given payload (record) to this Chunk
	 * @param payload byte[] of data to be added
	 * @return index in the RID array at end of Chunk
	 * @return -1 failure to write the record
	 */
	public int appendRecord(byte[] payload) {
		try {
			//If the file corresponding to ChunkHandle does not exist then create it before writing into it
			RandomAccessFile raf = new RandomAccessFile(this.filePath + "/" + this.chunkHandle, "rw");
			raf.write(payload, this.freeOffset, payload.length);
			
			//update RID array
			raf.seek(Constants.chunkSize - (4*(this.numRecords+1))); //4 bytes back from current entry
			raf.writeInt(this.freeOffset);
			raf.close();
			
			//update header info to reflect added record
			this.updateHeader(payload.length);
			return (this.numRecords - 1);
		} catch (IOException ioe) {
			System.out.println(this.chunkHandle + " appendRecord ioe: " + ioe.getMessage());
			return -1;
		}
	}
	
	/**
	 * Helper function for writeRecord. Updates and writes header info.
	 * @param size # bytes in payload
	 */
	private void updateHeader(int size) {
		this.numRecords++;
		this.freeSpace -= (size + 4); //4 for the additional RID entry
		this.freeOffset += size;
		
		this.writeHeader();
	}
	
	/**
	 * Checks if there is enough free space in this Chunk for a payload of given size
	 * @param size number of bytes
	 * @return true enough space for bytes and RID entry
	 * @return false not enough space
	 */
	public boolean enoughSpace(int size) {
		return (this.freeSpace <= (size+2));
	}
	
	/**
	 * Gets the byte data of the record who's RID is at given index
	 * @param index index in the RID array at end of chunk
	 * @return payload 	bytes of record
	 * @return null		error with reading
	 */
	// if arraylist is size 1, assume record
	// if arraylist is > 1, assume metarecord
	public ArrayList<byte[]> getRecord(int index) { 
		try {
			RandomAccessFile raf = new RandomAccessFile(this.filePath + "/" + this.chunkHandle, "rw");
			int RIDoffset = Constants.chunkSize - (4*(index+1)); //index of RID in chunk file
			raf.seek(RIDoffset);
			int offset = raf.readInt();
			
			int size = -1;
			if(index == (this.numRecords-1)) { //last record 
				size = offset - this.freeOffset - 2; //goes upto freeOffset
			} else { //use next record's offset to calculate this record's size size
				raf.seek(RIDoffset-4);
				size = offset - raf.readInt() - 2;
			}
			
			ArrayList<byte[]> payloadArrayList = new ArrayList<byte[]>();
			byte[] payload = new byte[size]; //byte[] to be read to
			int counter = 0;
			
			//grabbing payload
			raf.seek(offset); 
			if(raf.readByte() == (byte)1) { //is metadata
				//parse 12 byte sections: chunkhandle + RID index
				raf.seek(offset+2);
				
				//ASSUME SIZE MULTIPLE OF 12
				if(size%12 != 0) {
					System.out.println("CHUNK GET RECORD ASSUMPTION WRONG");
					System.out.println("Size is " + size + " Offset is " + offset);
				}
				
				for(int i=0; i < size; i+=12) {
					byte[] temp = new byte[12];
					int cnt = 0;
					while(cnt < 12) { //read 12 bytes
						cnt = raf.read(temp, cnt, 12);
						if(cnt == -1) {
							raf.close();
							return null;
						}
					}
					payloadArrayList.add(temp);
				}
				
			} else {
				raf.seek(offset+2); //advance past sub/not sub record byte
				while(counter < size) { //insure read correct # of bytes
					counter = raf.read(payload, counter, size);
					if(counter == -1) {
						raf.close();
						return null; //reached end of file incorrectly
					}
				}
				payloadArrayList.add(payload);
			}
			
			raf.close();
			return payloadArrayList;
		} catch (IOException ioe) {
			System.out.println(this.chunkHandle + " getRecord() ioe: " + ioe.getMessage());
		}
		return null;
	}
	
	public String getChunkHandle() {
		return this.chunkHandle;
	}
}
