/**
 * Project 5 - CS351, Fall 2019, Class to start auction house server for each client
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */

package AuctionHouse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AuctionServer implements Runnable{

    /** socket to communicate with bank*/
    private Socket bankSocket = null;

    /** Port Number of the server*/
    private int portNumber;

    /** Address of the server*/
    private String address;

    /** ItemList of auctionHouse*/
    private List<Item> itemList = new ArrayList<Item>();

    /** Client Number of Auction House*/
    private int auctionNumber;

    /** Checks if someone is currentlyBidding or not*/
    private static  boolean currentlyBidding = false;

    /**
     * Constructor for Auction server
     *
     * @param int portNumber, portNumber of auctionHouse
     * @param String address, address of auctionHouse
     * @param List<Item> itemList, list of items in the auctionHouse
     * @param Socket bankSocket, socket to communicate with bank
     * @param int auctionNumber, client number of auction house
     *
     */
    public AuctionServer(int portNumber, String address,
                         List<Item> itemList, Socket bankSocket,
                         int auctionNumber) {
        this.portNumber = portNumber;
        this.address = address;
        this.itemList = itemList;
        this.bankSocket = bankSocket;
        this.auctionNumber = auctionNumber;
    }


    /**
     * Runs the thread
     *
     */

    @Override
    public void run() {
        startServer();
    }

    /**
     * Starts server for an agent to communicate with auctionHouse
     *
     */
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);

            //starts new thread for new agent

            while (true) {
                Socket serverClient = serverSocket.accept(); //accept client side
                Thread threadAuctionClientThread = new
                        Thread(new AuctionClientThread(serverClient,
                        itemList, bankSocket, auctionNumber));
                threadAuctionClientThread.start();


            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Returns true if someone is currently bidding and vice versa
     *
     * @return boolean, return true if someone is currently bidding
     *
     */
    public static boolean isCurrentlyBidding() {
        return AuctionClientThread.isCurrentlyBidding();
    }
}
