import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class FileManager {
	
	NetComm mNetComm = null; 
	
	FileManager (NetComm nm) {
		mNetComm = nm;
	}

	public void sendFileRequest (String filename, String peer) {
		
		Util.CloudMsg msg = new Util.CloudMsg();
		msg.source = Cumulus.myId;
		msg.dest = peer;
		msg.msgType = Util.FILE_REQ;
		msg.params = new ArrayList<String>();
		msg.params.add(filename);
		
		System.out.println("Sending file request");
		mNetComm.send(Util.marshall(msg));
	}
	
	public void receiveFile (Util.CloudMsg cm) {
		
		System.out.println("Receiving File");
		
		try {
			FileOutputStream fos = new FileOutputStream(cm.params.get(0));
			BufferedOutputStream bos = new BufferedOutputStream(fos);
	    
			bos.write(cm.data, 0 , cm.bytesCount);
			bos.flush();
		    bos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendFile (Util.CloudMsg cm) {

	
		if (!cm.dest.equals(Cumulus.myId))
			return;
		System.out.println("Got file request");
	
		Util.CloudMsg msg = new Util.CloudMsg();
		msg.source = Cumulus.myId;
		msg.msgType = Util.FILE_RSP;
		msg.params = new ArrayList<String>();
		msg.params.add(cm.params.get(0));
		// send data ..
		
		File theFile = new File (cm.params.get(0));
		msg.data = new byte [(int)theFile.length()];
		msg.bytesCount = msg.data.length;
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(theFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(msg.data,0,msg.data.length);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mNetComm.send(Util.marshall(msg));
		
	}
}
