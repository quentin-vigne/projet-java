package xyz.cintia_quentin.projet;

import java.io.IOException;
import java.net.MalformedURLException;


public class Main {
    BaseDeTweets myBase;
    
    public static void main(String args[]) throws IOException {
    	

        
    	Main app = new Main();
        try {
			app.startMenu();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void open() throws IOException {
        System.out.println("1 - open collection file");
        
    	System.out.println("Pathname for index ? \n");
        String pathname = ScannerHandler.scanner.nextLine();
        myBase = new BaseDeTweets(pathname);
        
        System.out.println("Path for the tweet file");
        String tweetpath = ScannerHandler.scanner.nextLine();
        myBase.LoadFileWithTweets(tweetpath);
        } 
    
	private void search() {
		System.out.println("2 - Search something");
		
		System.out.println("What term are you looking for ? \n");
        String string = ScannerHandler.scanner.nextLine();

        myBase.SearchTweet(string);
		
	}

	private void printList() {
		myBase.SearchTweet("*");
	}  
    
    public void exit() {
        System.out.println("5 - exit");
        System.exit(0);
    }

    public void startMenu() throws IOException {
        do {
            System.out.println(
                    "====== What do you want : =======\n"
                    + "1 - Import your tweeter file \n"
                    + "2 - Search something \n"
                    + "3 - Print everything (not recommended) \n"
                    + "4 - Filter by\n"
                    + "5 - Exit"
                    );

            int option = Integer.parseInt(ScannerHandler.scanner.nextLine());
            switch (option) {
                case 1:
                    open();
                    break;
                case 2:
                	search();
                    break;
                case 3:
                    printList();
                    break;
                case 4:
                    //LUCENE STUFF();
                    break;
                case 5:
                    exit();
                    break;
                default:
                    break;
            }
        } while (true);
    }
  
}
