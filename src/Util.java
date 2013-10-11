import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


public class Util {
	
	// the various message ids
	public static final int UPDATE_MSG = 1;
	public static final int FETCH_FILE = 2;
	public static final int FILE_REQ = 3;
	public static final int FILE_RSP = 4;	
	
	
	public static class CloudMsg {
		String source;
		String dest;
		int msgType = 0;
		ArrayList<String> params;
		int bytesCount;
		byte[] data;
	};
	
	public static byte[] marshall(CloudMsg msg) {	
	//public static byte[] marshall(String id, ArrayList<String> strs) {
		
		int total_bytes = 100;
		
		ByteBuffer bb = ByteBuffer.allocate(total_bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		// write my id first .... 
		writeStringToBuf(msg.peer, bb);
		
		bb.putInt(msg.params.size());
		
		for (String s : msg.params) {
			writeStringToBuf(s, bb);
		}
		
		return bb.array();
	}	
	
	private static void writeStringToBuf(String str1, ByteBuffer bb) {
		byte[] tmp = new byte[100];
		tmp = str1.getBytes();
		
		bb.putInt(tmp.length);
		bb.put(tmp);
	}
	
	public static CloudMsg unmarshall (byte[] data) {
		
		CloudMsg cm = new Util.CloudMsg();
		
		ByteBuffer bbuf = ByteBuffer.wrap(data);
		bbuf.order(ByteOrder.LITTLE_ENDIAN); // do we need this ? 
		StringBuilder s = new StringBuilder ();
		
		cm.params = new ArrayList<String> ();
		
		cm.peer = readStringFromBuf(bbuf);
		
		// get the number of strings 
		int nos = bbuf.getInt();
		
		for (int i = 0;i < nos;i++) {
			cm.params.add(readStringFromBuf(bbuf));
		}
		
		return cm;
	}

	private static String readStringFromBuf(ByteBuffer bbuf) {
		int len = bbuf.getInt();
		byte[] tmp = new byte[len];
		
		bbuf.get(tmp, 0, len);
		return new String(tmp);
	}
}
