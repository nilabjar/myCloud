import java.util.Scanner;


public class Cumulus {
	
	public static String myId = null;
	public static String APP_PATH = "";
	
	public static FileManager fm = null;
	
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

		Scanner in = new Scanner(System.in);
		
		fm = new FileManager(ncm);
		
		boolean done = false;
		
		while (!done) {
			
			System.out.println("State your intent ?");
			System.out.println("\t1. Do you want to see peers ?");
			System.out.println("\t2. Quit ?");

			int choice = in.nextInt();

			switch (choice) {
			case 1:
				System.out.println("\nWhose file do you want ?");
				pd.printPeers ();
				String name = in.next(); // get the peer name 
				// show peer files ... 
				pd.printPeerFiles (name);
				System.out.println("\nWhich file do you want ?");
				String file = in.next();
				fm.sendFileRequest(file, name);
				break;
			case 2:
				done = true;
				break;
			default:
				System.out.println("Cannot understand option");
			}
		}
		
		try {
			up.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void getFile (String peer, String filename) {
		Util.CloudMsg msg = new Util.CloudMsg ();
	}
}
