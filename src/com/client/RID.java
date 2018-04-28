package com.client;

public class RID {
	//NEEDS TOSTRING METHOD FOR LOGGING
	private double chunkHandle;
	private int index; //index for array at end of chunk
	
	public RID(double chunkHandle, int index) {
		this.chunkHandle = chunkHandle;
		this.index = index;
	}
	
	public RID(String logString) {
		String[] log = logString.split("|");
		this.chunkHandle = Double.parseDouble(log[0]);
		this.index = Integer.parseInt(log[1]);
	}
	
	public double getChunkHandle() {
		return this.chunkHandle;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	@Override
	public String toString() {
		return this.chunkHandle + "|" + this.index;
	}
	
}
