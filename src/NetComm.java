import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;


public class NetComm extends ReceiverAdapter{
	
	private String mId = null;
	private String mAppPath = null;
	
	private PeerData peerDb = null;
	
	public NetComm (String myId, String appPath, PeerData pd) {
		mId = myId;
		mAppPath = appPath;
		peerDb = pd;
	}
	
	// TODO Auto-generated method stub
	JChannel channel = null;
	
	public void init () {
		try {
			channel = new JChannel(mAppPath + "/udp.xml");
		} catch (Exception e) {

			System.err.println("Error: udp.xml not found");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		channel.setReceiver (this);
		channel.setName(mId);

		try {
			channel.connect("Cumulus");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void send (byte[] data) {
		try {
			channel.send(new Message(null, data));
			//channel.send(null, "Hello World\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void receive(Message msg) {
		//System.out.println("received msg from " + msg.getSrc());
		
		Util.CloudMsg cm = Util.unmarshall(msg.getBuffer());
		
		switch (cm.msgType) {
		case Util.UPDATE_MSG:
			//System.out.println("Update Message Received");
			peerDb.updatePeerData(cm);
			break;
		case Util.FILE_RSP:
			Cumulus.fm.receiveFile(cm);
			break;
		case Util.FILE_REQ:
			Cumulus.fm.sendFile(cm);
			break;
		default:
			break;
		}

	}
}
