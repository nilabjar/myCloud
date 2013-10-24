import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


public class Util {
	
	// the various message ids
	public static final int UPDATE_MSG = 1;
	public static final int FILE_REQ = 2;
	public static final int FILE_RSP = 3;	
	
	
	public static class CloudMsg {
		String source;
		String dest = "All";
		int msgType = 0;
		ArrayList<String> params;
		int bytesCount = 0;
		byte[] data;
	};
	
	public static byte[] marshall(CloudMsg msg) {	
	//public static byte[] marshall(String id, ArrayList<String> strs) {
		
		int total_bytes = 1024;
		
		ByteBuffer bb = ByteBuffer.allocate(total_bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		// write my id first .... 
		writeStringToBuf(msg.source, bb);
		
		//write the destination
		writeStringToBuf(msg.dest, bb);
		
		//write the msgtype
		bb.putInt(msg.msgType);
		
		//write the params ...
		bb.putInt(msg.params.size());
		
		for (String s : msg.params) {
			writeStringToBuf(s, bb);
		}
		
		//write the bytes count
		bb.putInt(msg.bytesCount);
		
		if (msg.bytesCount > 0) {
			bb.put(msg.data);
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
		
		//read the source 
		cm.source = readStringFromBuf(bbuf);
		
		//read the dest
		cm.dest = readStringFromBuf(bbuf);
		
		// read the msg type
		cm.msgType = bbuf.getInt();
		
		// get the number of strings 
		int nos = bbuf.getInt();
		
		for (int i = 0;i < nos;i++) {
			cm.params.add(readStringFromBuf(bbuf));
		}
		
		//get the byte data 
		cm.bytesCount = bbuf.getInt();
		
		if (cm.bytesCount > 0) {
			cm.data = new byte[cm.bytesCount];
			bbuf.get(cm.data, 0, cm.bytesCount);
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
