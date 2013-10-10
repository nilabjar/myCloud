
public class Cumulus {
	
	private static String myId = null;
	private static String APP_PATH = "";
	
	/**
	 * @param args 
	 */
	public static void main(String[] args) {
		
		APP_PATH = System.getProperty("user.dir");
		System.out.println("Current working directory : " + APP_PATH);
		
		myId = new String(args[0]);
		
		PeerData pd = new PeerData();
		
		NetComm ncm = new NetComm (myId, APP_PATH, pd);
		ncm.init();
		
		Updater up = new Updater(myId, APP_PATH, ncm);
		up.start();
	}
}
