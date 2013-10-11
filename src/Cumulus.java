import java.util.Scanner;


public class Cumulus {
	
	public static String myId = null;
	public static String APP_PATH = "";
	
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
		
		boolean done = false;
		
		while (!done) {
			
			System.out.println("State your intent ?");
			System.out.println("1. Do you want to see peers ?");
			System.out.println("2. Quit ?");

			int choice = in.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Whose file do you want ?");
				pd.printPeers ();
				String name = in.next(); // get the peer name 
				// show peer files ... 
				pd.printPeerFiles (name);
				System.out.println("Which file do you want ?");
				String file = in.nextLine();
				break;
			case 2:
				done = true;
				break;
			}
			System.out.println("State your intent ?");
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
