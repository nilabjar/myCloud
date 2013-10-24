import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Updater extends Thread {
	
	private String mAppPath = null;
	private String mId = null;
	NetComm mNtComm = null;
	
	Updater (String myid, String appPath, NetComm ntcom) {
		mAppPath = appPath;
		mId = myid;
		mNtComm = ntcom;
	}
	
	public void run () {
		
		while (true) {
			
			Util.CloudMsg m = new Util.CloudMsg();
			m.source = mId;
			m.dest = "All";
			m.msgType = Util.UPDATE_MSG;
			m.params = readFilePaths();

			byte[] data = Util.marshall (m);

			try {
				//System.out.println("Sending Updates");

				mNtComm.send(data);
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

	}
	

	
	public ArrayList<String> readFilePaths() {
		
		ArrayList<String> paths = null;
		try {
			BufferedReader b = new BufferedReader(new FileReader(mAppPath + "/sharedFiles"));
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
}
