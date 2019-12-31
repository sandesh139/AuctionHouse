package Bank;


/**
 * Project 5 - CS351, Fall 2019, Class for auction house which hold the properties of the individual auction house
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class AuctionHouse {

    /**declaring the string to hold the host address of the auction house */
    private String hostname;

    /**declaring the integer to hold the port number that the auction house is using to communicate to agents. */
    private int port;

    /**declaring the integer to hold the balance of the auction house. */
    private int balance = 0;

    /**declaring the integer to hold the balance of the auction unique id. */
    private  int id;

    /**
     * constructor to set the id, host address and the port of the auction house.
     * @param id
     * @param hostname
     * @param port
     */
    public AuctionHouse(int id, String hostname, int port){
        this.hostname = hostname;
        this.port = port;
        this.id = id;
    }


    /**
     *
     * @returns the hostname of the address
     */
    public String getHostname(){
        return hostname;
    }

    /**
     *
     * @returns the port of the auction house.
     */
    public int getPort(){
        return port;
    }

    /**
     *
     * @returns the balance of the auction house.
     */
    public int getBalance(){
        return balance;
    }

    /**
     *
     * @returns the unique id of the auction house.
     */
    public int getId(){
        return id;
    }

    /**
     *
     * @param add is added to the current amount of the auction house.
     */
    public void addBalance(int add){
        balance += add;
    }
}
