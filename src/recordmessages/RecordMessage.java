package recordmessages;

import java.io.Serializable;

import com.client.ClientFS.FSReturnVals;
import com.messages.FSMessage.FSMessageType;

public class RecordMessage implements Serializable {

	public enum RecordMessageType {
		AppendRec,
		DeleteRec,
		ReadRec,
		ReadNextRec,
		ReadPrevRec,
		ReadFirstRec,
		ReadLastRec
	}
	
	public FSReturnVals returnVal;
	public RecordMessageType type;
	
	public RecordMessage(RecordMessageType type) {
		this.type = type;
	}
	
	public void setReturnValue(FSReturnVals returnVal) {
		this.returnVal = returnVal;
	}
}
