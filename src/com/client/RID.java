package com.client;

public class RID {
	//NEEDS TOSTRING METHOD FOR LOGGING
	private double chunkHandle;
	private int index = -1; //index for array at end of chunk
	
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
	
	public boolean isValid() {
		if(index == -1) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return this.chunkHandle + "|" + this.index;
	}
	
}
