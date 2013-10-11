import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class FileManager {
	
	NetComm mNetComm = null; 
	
	FileManager (NetComm nm) {
		mNetComm = nm;
	}

	public void sendFileRequest (String filename) {
		
		Util.CloudMsg msg = new Util.CloudMsg();
		msg.peer = Cumulus.myId;
		msg.msgType = Util.FETCH_FILE;
		msg.params = new ArrayList<String>();
		msg.params.add(filename);
		
		mNetComm.send(Util.marshall(msg));
	}
	
	public void receiveFile (String filename, byte[] data) {
		
	}
	
	public void sendFile (String filename) {
	
		Util.CloudMsg msg = new Util.CloudMsg();
		msg.peer = Cumulus.myId;
		msg.msgType = Util.FILE_RSP;
		msg.params = new ArrayList<String>();
		msg.params.add(filename);
		// send data ..
		
		File theFile = new File (filename);
		msg.data = new byte [(int)theFile.length()];
		msg.bytesCount = msg.data.length;
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(theFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(msg.data,0,msg.data.length);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
