import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.html.HTMLDocument.Iterator;

public class PeerData {
	
	private HashMap<String, ArrayList<String>>	mPeers = null;
	
	PeerData() {
		mPeers = new HashMap<String, ArrayList<String>> ();
	}
	
	public void updatePeerData (Util.CloudMsg msg) {
		mPeers.put(msg.source, msg.params);
		
/*		System.out.println("User " + msg.source + " has shared the following files");
		System.out.println("================================================");
		for (String s : msg.params)
			System.out.println (s);
		System.out.println("================================================\n");*/
	}
	
	public HashMap<String, ArrayList<String>> getPeerSharedData () {return mPeers;}

	public void printPeers() {
		// TODO Auto-generated method stub
		
	    java.util.Iterator<Entry<String, ArrayList<String>>> it = mPeers.entrySet().iterator();
	    // print the header ..... 
	    System.out.println("\tPeer Name                   Files");
	    System.out.println("\t===============================================================");
	    
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println("\t" + pairs.getKey() + "  \t\t" + pairs.getValue());
	    }
		
	}

	public void printPeerFiles(String peer) {
		// TODO Auto-generated method stub
		
		ArrayList<String> files = 
				mPeers.get(peer);
		if (files == null)
			System.out.println("No peer data " + peer); 
		
		for (String s : files) {
			System.out.println("\t" + s);
		}
	}
}
