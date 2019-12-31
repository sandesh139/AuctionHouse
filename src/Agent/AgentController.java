package Agent;


/**
 * Project 5 - CS351, Fall 2019, Class for agent controller from where agent client
 * is created.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class AgentController {

    /**
     * this main method creates the agent object and calls the start server.
     * @param args
     */
    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.startServer();
    }

}
