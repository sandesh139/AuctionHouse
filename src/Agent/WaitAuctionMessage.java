package Agent;

import java.io.DataInputStream;
import java.io.IOException;
/**
 * Project 5 - CS351, Fall 2019, Class for waiting the bid message to be received from the auction house.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class WaitAuctionMessage implements Runnable {
    private DataInputStream auctionInputStream = null;


    /**
     *
     * @param auctionInputStream gives access to class to keep waiting for the message from the auction house.
     */
    public WaitAuctionMessage(DataInputStream auctionInputStream){
        this.auctionInputStream = auctionInputStream;
    }

    /**
     * this method calls the auctionResponse method.
     */
    @Override
    public void run() {
        auctionResponse();
    }


    /**
     * this method gets message of any updates on bid agent made on the item of the auction house.
     */
    public void auctionResponse() {
        try {
            String serverMessage = auctionInputStream.readUTF();
            String clonedMessage = serverMessage;
            if (serverMessage.contains("Congratulations")) {
                serverMessage = "Sold";

            }
            if(serverMessage.contains("other")){
                serverMessage = "out";
            }
            if(serverMessage.contains("own")){
                serverMessage = "selfout";
            }


            switch (serverMessage) {
                case "fail":
                    System.out.println("Your bid was rejected.");
                    break;
                case "pass":
                    System.out.println("Your bid is placed.");
                    auctionResponse();
                    break;
                case "Sold":
                    System.out.println(clonedMessage);
                    break;
                case "out":
                    System.out.println(clonedMessage);
                    return;
                case "selfout":
                    System.out.println(clonedMessage);
                    auctionResponse();
                    break;
                default:
                    System.out.println(serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
