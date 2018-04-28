package utility;

import java.util.Collections;
import java.util.HashMap;

import com.messages.FSMessage.FSMessageType;

public class Constants {
	public static final int chunkSize = 4*1024;
	
	public static final int masterPort = 15003;
	public static final String masterIP = "localhost";
	
	public static final int chunkServerPort = 1234;
	public static final String ChunkServerIP = "localhost";
	
	public static final String OP_LOG = "operations.txt";
	public static final String CHECKPOINT = "checkpoint.txt";

	public static final HashMap<FSMessageType, String> logOperations;
	static {
		HashMap<FSMessageType, String> map = new HashMap<FSMessageType, String>();
		
		map.put(FSMessageType.CreateDir, "CreateDir");
		map.put(FSMessageType.CreateFile, "CreateFile");
		
		logOperations = (HashMap<FSMessageType, String>) Collections.unmodifiableMap(map);
	}
		
}
