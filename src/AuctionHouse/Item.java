/**
 * Project 5 - CS351, Fall 2019, Class for Item
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */

package AuctionHouse;

public class Item {

    /** Name of item */
    private String name;

    /** Minimum amount required to bid for item */
    private int minBid;

    /** Agent who has the current highest bid*/
    private int agentWithBid;

    /**
     * Constructor for Item
     *
     * @param String name, name of item
     * @param int minBid, minimum bid required to purchase item
     *
     */
    public Item(String name, int minBid){
        this.name = name;
        this.minBid = minBid;
        this.agentWithBid = -1;

    }

    /**
     * Gets min bid
     *
     * @return  String name, name of item
     *
     */
    public String getName(){
        return name;
    }

    /**
     * Gets min bid of item
     * @return  int minBid, minimum bid required to purchase item
     *
     */
    public int getMinBid(){
        return minBid;
    }

    /**
     * Sets min bid of item and agent
     *
     * @param int agentId, agent with new highest bid
     * @param int amount, minimum bid required to purchase item
     *
     */
    public void setMinBid(int amount, int agentID){
        this.minBid = amount;
        this.agentWithBid = agentID;
    }

    /**
     * Returns agent id with current highest bid
     * @return  int , agent client id
     *
     */
    public int getAgentWithBid(){
        return agentWithBid;
    }
}
