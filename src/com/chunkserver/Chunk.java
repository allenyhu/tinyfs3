package com.chunkserver;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import utility.Constants;

public class Chunk {
	private String filePath;
	private byte[] bytes; //actual bytes in chunk
	private int numRecords;
	private int freeSpace; //# bytes available; this will always be 4 bytes less than actual free bytes to store record's offset
	private int freeOffset; //offset of free space from front of byte[]
	private String chunkHandle;
	
	public Chunk(String filePath, String chunkHandle) {
		this.filePath = filePath;
		this.chunkHandle = chunkHandle;
		numRecords = 0;
		freeSpace = Constants.chunkSize - 16; //12 for numRecords, freeSpace, freeOffset; 4 for RID offset
		freeOffset = 12;
		
		this.writeHeader();
	}
	
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
	
	public int writeRecord(byte[] payload) {
		try {
			//If the file corresponding to ChunkHandle does not exist then create it before writing into it
			RandomAccessFile raf = new RandomAccessFile(this.filePath + "/" + this.chunkHandle, "rw");
			raf.write(payload, this.freeOffset, payload.length);
			raf.close();
			
			this.updateHeader(payload.length);
			return (this.numRecords - 1);
		} catch (IOException ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Helper function for writeRecord
	 * @param size # bytes in payload
	 */
	private void updateHeader(int size) {
		this.numRecords++;
		this.freeSpace -= (size + 4); //4 for the additional RID entry
		this.freeOffset += size;
		
		this.writeHeader();
	}
	
	public boolean enoughSpace(int size) {
		return (freeSpace <= size);
	}
	
	
}
