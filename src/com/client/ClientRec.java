package com.client;

import java.nio.ByteBuffer;

import com.client.ClientFS.FSReturnVals;

public class ClientRec {
	
	private void connectToCS(String ip, int port) {
		try {
			s = new Socket(ip, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			
			oos.writeObject("client");
			oos.flush();
		} catch (IOException e) {
			System.out.println("Exception in clientRec connecting to chunkserver");
		}
	}

	/**
	 * Appends a record to the open file as specified by ofh Returns BadHandle
	 * if ofh is invalid Returns BadRecID if the specified RID is not null
	 * Returns RecordTooLong if the size of payload exceeds chunksize RID is
	 * null if AppendRecord fails
	 *
	 * Example usage: AppendRecord(FH1, obama, RecID1)
	 */
	public FSReturnVals AppendRecord(FileHandle ofh, byte[] payload, RID RecordID) {
		// Check FileHandle validity
		if (!ofh.isValid()) {
			return FSReturnVals.BadHandle;
		}
		// Check RID validity
		if (!RecordID.isValid()) {
			return FSReturnVals.BadRecID;
		}
		//Check Payload size
		if (payload.length > Constants.chunkSize) {
			return FSReturnVals.RecordTooLong;
		}
		connectToCS(ofh.getServerIPAddress(), ofh.getServerPort());
		try{
			oos.reset();
			oos.writeObject(new AppendRecMessage(ofh, payload, RecordID));
			oos.flush();
			AppendRecMessage returnMessage = (AppendRecMessage) ois.readObject();
			ofh.copy(returnMessage.ofh);
			RecordID.copy(returnMessage.rid);
			return returnMessage.returnVal;
		}
		catch (IOException | ClassNotFoundException  ioe){
			System.out.println("Exception in append record: "+ioe.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Deletes the specified record by RecordID from the open file specified by
	 * ofh Returns BadHandle if ofh is invalid Returns BadRecID if the specified
	 * RID is not valid Returns RecDoesNotExist if the record specified by
	 * RecordID does not exist.
	 *
	 * Example usage: DeleteRecord(FH1, RecID1)
	 */
	public FSReturnVals DeleteRecord(FileHandle ofh, RID RecordID) {
		//Checking for ofh validity
		if (!ofh.isValid()) {
			return FSReturnVals.BadHandle;
		}
		if (!RecordID.isValid()) {
			return FSReturnVals.BadRecID;
		}
		
		connectToCS(ofh.getServerIPAddress(), ofh.getServerPort());
		try{
			oos.reset();
			oos.writeObject(new DeleteRecMessage(ofh, RecordID));
			oos.flush();
			DeleteRecMessage returnMessage = (DeleteRecMessage) ois.readObject();
			ofh.copy(returnMessage.ofh);
			RecordID.copy(returnMessage.RecordID);
			return returnMessage.returnVal;
		}
		catch (IOException | ClassNotFoundException  ioe){
			System.out.println("exception in delete rec: "+ioe.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Reads the first record of the file specified by ofh into payload Returns
	 * BadHandle if ofh is invalid Returns RecDoesNotExist if the file is empty
	 *
	 * Example usage: ReadFirstRecord(FH1, tinyRec)
	 */
	public FSReturnVals ReadFirstRecord(FileHandle ofh, TinyRec rec){
		if (!ofh.isValid()) {
			return FSReturnVals.BadHandle;
		}
		connectToCS(ofh.getServerIPAddress(), ofh.getServerPort());
		try{
			oos.reset();
			oos.writeObject(new ReadFirstRecMessage(ofh, rec));
			oos.flush();
			ReadFirstRecMessage returnMessage = (ReadFirstRecMessage) ois.readObject();
			rec.copy(returnMessage.rec);
			return returnMessage.returnVal;
		}
		catch (IOException | ClassNotFoundException  ioe){
			System.out.println("exception in read first rec: "+ioe.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Reads the last record of the file specified by ofh into payload Returns
	 * BadHandle if ofh is invalid Returns RecDoesNotExist if the file is empty
	 *
	 * Example usage: ReadLastRecord(FH1, tinyRec)
	 */
	public FSReturnVals ReadLastRecord(FileHandle ofh, TinyRec rec){
		if (!ofh.isValid()) {
			return FSReturnVals.BadHandle;
		}
		connectToCS(ofh.getServerIPAddress(), ofh.getServerPort());
		try{
			oos.reset();
			oos.writeObject(new ReadLastRecMessage(ofh, rec));
			oos.flush();
			ReadLastRecMessage returnMessage = (ReadLastRecMessage) ois.readObject();
			rec.copy(returnMessage.rec);
			return returnMessage.returnVal;
		}
		catch (IOException | ClassNotFoundException  ioe){
			System.out.println("exception in read last rec: "+ioe.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Reads the next record after the specified pivot of the file specified by
	 * ofh into payload Returns BadHandle if ofh is invalid Returns
	 * RecDoesNotExist if the file is empty or pivot is invalid
	 *
	 * Example usage: 1. ReadFirstRecord(FH1, tinyRec1) 2. ReadNextRecord(FH1,
	 * rec1, tinyRec2) 3. ReadNextRecord(FH1, rec2, tinyRec3)
	 */
	public FSReturnVals ReadNextRecord(FileHandle ofh, RID pivot, TinyRec rec){
		if (!ofh.isValid()) {
			return FSReturnVals.BadHandle;
		}
		if (!pivot.isValid()) {
			return FSReturnVals.RecDoesNotExist;
		}
		connectToCS(ofh.getServerIPAddress(), ofh.getServerPort());
		try{
			oos.reset();
			oos.writeObject(new ReadNextRecMessage(ofh, pivot, rec));
			oos.flush();
			ReadNextRecMessage returnMessage = (ReadNextRecMessage) ois.readObject();
			rec.copy(returnMessage.rec);
			return returnMessage.returnVal;
		}
		catch (IOException | ClassNotFoundException  ioe){
			System.out.println("exception in read next rec: "+ioe.getMessage());
			return FSReturnVals.Fail;
		}
	}

	/**
	 * Reads the previous record after the specified pivot of the file specified
	 * by ofh into payload Returns BadHandle if ofh is invalid Returns
	 * RecDoesNotExist if the file is empty or pivot is invalid
	 *
	 * Example usage: 1. ReadLastRecord(FH1, tinyRec1) 2. ReadPrevRecord(FH1,
	 * recn-1, tinyRec2) 3. ReadPrevRecord(FH1, recn-2, tinyRec3)
	 */
	public FSReturnVals ReadPrevRecord(FileHandle ofh, RID pivot, TinyRec rec){
		if (!ofh.isValid()) {
			return FSReturnVals.BadHandle;
		}
		if (!pivot.isValid()) {
			return FSReturnVals.RecDoesNotExist;
		}
		connectToCS(ofh.getServerIPAddress(), ofh.getServerPort());
		try{
			oos.reset();
			oos.writeObject(new ReadPrevRecMessage(ofh, rec, pivot));
			oos.flush();
			ReadPrevRecMessage returnMessage = (ReadPrevRecMessage) ois.readObject();
			rec.copy(returnMessage.rec);
			return returnMessage.returnVal;
		}
		catch (IOException | ClassNotFoundException  ioe){
			System.out.println("exception in read prev rec: "+ioe.getMessage());
			return FSReturnVals.Fail;
		}
	}

}
