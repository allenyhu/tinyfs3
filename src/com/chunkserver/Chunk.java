package com.chunkserver;

import java.io.IOException;
import java.io.RandomAccessFile;

import utility.Constants;

public class Chunk {
	private byte[] bytes; //actual bytes in chunk
	private int numRecords;
	private int freeSpace; //# bytes available; this will always be 4 bytes less than actual free bytes to store record's offset
	private int freeOffset; //offset of free space from front of byte[]
	private String chunkHandle;
	
	public Chunk(String chunkHandle) {
		this.chunkHandle = chunkHandle;
		numRecords = 0;
		freeSpace = Constants.chunkSize - 16; //12 for numRecords, freeSpace, freeOffset; 4 for RID offset
		freeOffset = 12;
		try {
			RandomAccessFile raf = new RandomAccessFile(chunkHandle, "rw");
			raf.seek(0);
			raf.writeInt(numRecords);
			raf.writeInt(freeSpace);
			raf.writeInt(freeOffset);
			raf.close();
		} catch (IOException ioe) {
			System.out.println("Chunk " + chunkHandle + " constructor ioe: " + ioe.getMessage());
		}
	}
	
	public boolean enoughSpace(int size) {
		return (freeSpace <= size);
	}
	
	
}
