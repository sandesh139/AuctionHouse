package Bank;

/**
 * Project 5 - CS351, Fall 2019, Class for bank controller from where bank server
 * is created.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class Agent {

    /**integer declared to store the unique agent id. */
    private int id;

    /**integer declared to store the amount id. */
    private int amount;

    /**string declared to store the agent name */
    private String name;

    /**
     * constructor to set the properties of the agent
     * @param id of the agent
     * @param amount sets the bank balance of the agent
     * @param name sets the name of the agent
     */
    public Agent(int id, int amount, String name) {

        this.id = id;

        this.amount = amount;

        this.name = name;
    }

    /**
     *
     * @param price is subtracted from the account of the agent
     */
    public void subtract(int price){
        amount = amount -price;
    }

    /**
     *
     * @param price is added to the agents account
     */
    public void add(int price){
        amount = amount +price;
    }

    /**
     *
     * @return amount in the agents account
     */
    public int getAmount(){
        return amount;
    }

    /**
     *
     * @return id of the agent
     */
    public int getId(){
        return id;
    }

}
