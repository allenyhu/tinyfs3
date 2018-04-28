package com.chunkserver;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import utility.Constants;

public class Chunk {
	private String filePath;
	//private byte[] bytes; //actual bytes in chunk
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
		return (freeSpace <= size);
	}
	
	/**
	 * Gets the byte data of the record who's RID is at given index
	 * @param index index in the RID array at end of chunk
	 * @return payload 	bytes of record
	 * @return null		error with reading
	 */
	public byte[] getRecord(int index) { 
		try {
			RandomAccessFile raf = new RandomAccessFile(this.filePath + "/" + this.chunkHandle, "rw");
			int RIDoffset = Constants.chunkSize - (4*(index+1)); //index of RID in chunk file
			raf.seek(RIDoffset);
			int offset = raf.readInt();
			
			int size = -1;
			if(index == (this.numRecords-1)) { //last record goes upto freeOffset
				size = offset - this.freeOffset;
			} else { //use next record's offset to calculate this record's size size
				raf.seek(RIDoffset-4);
				size = offset - raf.readInt();
			}
			
			byte[] payload = new byte[size];
			int counter = 0;
			raf.seek(offset);
			while(counter < size) {
				counter = raf.read(payload, counter, size);
				if(counter == -1) {
					raf.close();
					return null; //reached end of file incorrectly
				}
			}
			raf.close();
			return payload;
		} catch (IOException ioe) {
			System.out.println(this.chunkHandle + " getRecord() ioe: " + ioe.getMessage());
		}
		return null;
	}
	
	
}
