import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Scanner;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;


public class Cumulus extends ReceiverAdapter {
	
	private static Cumulus c = null; 
	
	private static String myId = null;
	
	private static final String APP_PATH = "/home/nilabjar/Books/Android/LearningCode/Cumulus/";
	
	private static String peer = null;

	private class CumulusMsg {
		String node_name;
		int msgType;
	};
	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		
		c = new Cumulus ();
		
		myId = new String(args[0]);
		
		// TODO Auto-generated method stub
		JChannel channel = null;
		try {
			channel = new JChannel(APP_PATH + "udp.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		channel.setReceiver (c);
	    channel.setName(args[0]);
		
		try {
			channel.connect("Cumulus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		while (true) {
			
			byte[] data = marshall (readFilePaths());
			
			try {
				channel.send(new Message(null, data));
				//channel.send(null, "Hello World\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	//	channel.close();

	}
	
	public static ArrayList<String> readFilePaths() {
		
		ArrayList<String> paths = null;
		try {
			BufferedReader b = new BufferedReader(new FileReader(APP_PATH + "sharedFiles"));
			Scanner in = new Scanner (b);
			
			paths = new ArrayList<String> ();
			
			while (in.hasNextLine())
				paths.add(in.nextLine());
			
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("No sharedFiles file found");
			e.printStackTrace();
		}
		
		return paths;
	}
	
	public static byte[] marshall(ArrayList<String> strs) {
		
		int total_bytes = 100;
		
		ByteBuffer bb = ByteBuffer.allocate(total_bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		// write my id first .... 
		writeStringToBuf(myId, bb);
		
		bb.putInt(strs.size());
		
		for (String s : strs) {
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
	
	public static ArrayList<String> unmarshall (byte[] data) {
		
		ByteBuffer bbuf = ByteBuffer.wrap(data);
		bbuf.order(ByteOrder.LITTLE_ENDIAN); // do we need this ? 
		StringBuilder s = new StringBuilder ();
		ArrayList<String> strings = new ArrayList<String> ();
		
		peer = readStringFromBuf(bbuf);
		
		// get the number of strings 
		int nos = bbuf.getInt();
		
		for (int i = 0;i < nos;i++) {
			strings.add(readStringFromBuf(bbuf));
		}
		
		return strings;
	}

	private static String readStringFromBuf(ByteBuffer bbuf) {
		int len = bbuf.getInt();
		byte[] tmp = new byte[len];
		
		bbuf.get(tmp, 0, len);
		return new String(tmp);
	}
	
	public void receive(Message msg) {
		//System.out.println("received msg from " + msg.getSrc() + ": " + msg.getObject());
		
		ArrayList<String> strings = unmarshall (msg.getBuffer());
		
		System.out.println("User " + peer + " has shared the following files");
		System.out.println("================================================");
		for (String s : strings)
			System.out.println (s);
		System.out.println("================================================\n");
	}
}
