package utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.messages.FSMessage.FSMessageType;

public class Constants {
	public static final int chunkSize = 4*1024;
	
	public static final int masterPort = 15003;
	public static final String masterIP = "127.0.0.1";
	
	public static final int chunkServerPort = 1234;
	public static final String ChunkServerIP = "127.0.0.1";
	
	public static final String OP_LOG = "operations.txt";
	public static final String CHECKPOINT = "checkpoint.txt";

	public static final Map<FSMessageType, String> logOperations;
	static {
		Map<FSMessageType, String> map = new HashMap<FSMessageType, String>();
		
		map.put(FSMessageType.CreateDir, "CreateDir");
		map.put(FSMessageType.CreateFile, "CreateFile");
		map.put(FSMessageType.DeleteDir, "DeleteDir");
		map.put(FSMessageType.DeleteFile, "DeleteFile");
		map.put(FSMessageType.RenameDir, "RenameDir");
		
		logOperations = Collections.unmodifiableMap(map);
	}
		
}
