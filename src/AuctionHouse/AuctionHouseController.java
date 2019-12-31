/**
 * Project 5 - CS351, Fall 2019, Class to start AuctionHouse Server and Client
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */


package AuctionHouse;


public class AuctionHouseController {

    public static void main(String[] args) {

        AuctionHouse auctionHouse = new AuctionHouse();
        //start auction house server
        auctionHouse.startServer();
    }
}
