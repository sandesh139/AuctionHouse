package Bank;

/**
 * Project 5 - CS351, Fall 2019, Class for bank controller from where bank server
 * is created.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class BankController {

    /**
     * main method of the bank controller
     * bank object is created.
     * @param args
     */
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.startServer();
    }
}
