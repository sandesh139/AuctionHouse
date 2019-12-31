/**
 * Project 5 - CS351, Fall 2019, Class for Agent
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */

package AuctionHouse;

import java.io.IOException;
import java.net.Socket;


public class Agent {

    /** socket to communicate with agent*/
    private Socket agentClient = null;

    /** client id of agent*/
    private int agentId;

    /**
     * Constructor for Agent
     *
     * @param Socket socket, socket to communicate with agent
     * @param int agentId, client number of agent
     *
     */
    public Agent(Socket socket, int agentId){
        this.agentClient = socket;
        this.agentId = agentId;
    }


    /**
     * Returns socket to communicate with agent
     *
     * @return Socket, socket to communicate with agent
     *
     */
    public Socket getAgentClient() {
        return agentClient;
    }

    /**
     * Returns client id of agent
     *
     * @return int, client number of agent
     *
     */
    public int getAgentId() {
        return agentId;
    }

    /**
     * Closes the socket
     *
     */
    public void closeSocket() {
        try {
            agentClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
