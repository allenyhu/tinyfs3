package com.master;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import utility.Constants;

public class LogParser {
	
	private HashMap<String, ArrayList<String>> fileChunks;
	
	private static BufferedReader reader;
	
	public LogParser() {
		this.fileChunks = new HashMap<String, ArrayList<String>>();
	}
	
	/**
	 * Parses the operation log
	 */
	public void parseOperationLog() {
		try {
			reader = new BufferedReader(new FileReader(Constants.OP_LOG));
			String line;
			String[] op;
			while ((line = reader.readLine()) != null) {
				op = line.split(" "); // Create/Rename/Delete/Write | filename | optional data (chunk replica locs for create, data to write)
				switch(op[0]) {
					case "C": this.create(op[1], Arrays.asList(op[2].split(","))); //filename, list of replica locations
							  break;
					case "R": this.rename(op[1]);
							  break;
					case "D": this.delete(op[1]);
							  break;
					case "W": this.write(op[1], op[2], op[3]); //filename, byte[], RID
							  break;
				}
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("NO OPERATION LOG: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Operation read line error: " + ioe.getMessage());
		}
	}
	
	public void parseCheckpoint() {
		try {
			reader = new BufferedReader(new FileReader(Constants.CHECKPOINT));
			String line;
			String[] data;
			while ((line = reader.readLine()) != null) {
				data = line.split(">"); //{filename, list of chunkhandles}
				fileChunks.put(data[0], new ArrayList<String>(Arrays.asList(data[1].split(","))));
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("NO CHECKPOINT: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("Checkpoint read line error: " + ioe.getMessage());
		}
	}
}
