import java.util.ArrayList;
import java.util.HashMap;

public class PeerData {
	
	private HashMap<String, ArrayList<String>>	mPeers = null;
	
	PeerData() {
		mPeers = new HashMap<String, ArrayList<String>> ();
	}
	
	public void updatePeerData (Util.CloudMsg msg) {
		mPeers.put(msg.peer, msg.params);
		
		System.out.println("User " + msg.peer + " has shared the following files");
		System.out.println("================================================");
		for (String s : msg.params)
			System.out.println (s);
		System.out.println("================================================\n");
	}
	
	public HashMap<String, ArrayList<String>> getPeerSharedData () {return mPeers;}

}
