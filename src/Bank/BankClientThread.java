package Bank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Project 5 - CS351, Fall 2019, Class for creating each bank client thread to handle each unique auction house and agent.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class BankClientThread extends Thread {

    /** declaring the socket to communicate with the client. */
    private Socket serverClient;

    /**declaring the integer to store the client number */
    private int clientNumber;

    /**declaring the data input stream to get access to read the message coming from the socket */
    private DataInputStream inputStream;

    /**declaring the data output stream to get access to write the message going out to the socket. */
    private DataOutputStream outputStream;

    /**declaring the string to store the client message recieved from the socket. */
    private String clientMessage = "";

    /**declaring the string to store the server message to be sent to the socket. */
    private String serverMessage = "";

    /**declaring the arraylist to store the auction house object. */
    private static List<AuctionHouse> allHouses = new ArrayList<AuctionHouse>();

    /**declaring the arraylist to store the agent object. */
    private static List<Agent> agents = new ArrayList<Agent>();
    private AuctionHouse auctionHouse = null;
    private Agent agent = null;


    /**
     * constructor to set the client number and the server client
     * @param clientNumber
     * @param serverClient
     */
    public BankClientThread(int clientNumber, Socket serverClient) {
        this.serverClient = serverClient;
        this.clientNumber = clientNumber;
    }

    /**
     * overriding the run method of the thread class.
     * here we keep waiting for the client message and write to server based on the client message
     * we use two separate method to handle auction client and agent client separately.
     */
    public void run() {
        try {
            inputStream = new DataInputStream(serverClient.getInputStream());
            outputStream = new DataOutputStream(serverClient.getOutputStream());
            serverMessage = clientNumber + " You are now connected to bank server.";
            outputStream.writeUTF(serverMessage);
            outputStream.flush();
            clientMessage = inputStream.readUTF();
            if(clientMessage.split(" ")[0].equals("a")) {
                interactWithAgent(clientMessage.split(" ")[1],
                        Integer.parseInt(clientMessage.split(" ")[2]));
            }

            else if(clientMessage.split(" ")[0].equals("h")) {
                interactWithAuctionHouse(clientMessage.split(" ")[1],
                        Integer.parseInt(clientMessage.split(" ")[2]));
            }

            inputStream.close();
            outputStream.close();
            serverClient.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        }finally{
            System.out.println("Client -" + clientNumber + " exit!! ");
        }
    }

    /**
     * this method interacts auction house.
     * we add new auction object to the auction arraylist.
     * @param hostName of the auction house
     * @param portNumber of the auction house.
     */
    public void interactWithAuctionHouse(String hostName, int portNumber) {

        try {
                auctionHouse = new AuctionHouse(clientNumber,hostName,portNumber);
                allHouses.add(auctionHouse);
                System.out.print("AuctionHouse got registered in Bank");
                outputStream.writeUTF("Your house is successfully registered.");
                System.out.println(" with host name " + hostName + " and port number "+ portNumber);
                outputStream.flush();
                waitForAuctionHouse();
            } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * this method keeps waiting for the message from the auction house. And replies to the auction house based on
     * the auction house.
     * @throws IOException
     */
    public void waitForAuctionHouse() throws IOException {
        int agentNumber = -1;
        clientMessage = inputStream.readUTF();
        int takeBalanceFromAgentID = -1;
        int deposittoAuctionID = -1;
        int amountSold = -1;

        //if the item is sold, item cost is added to the auction house account.
        if(clientMessage.contains("Sold")){
            String[] strArr = clientMessage.split(" ");
            deposittoAuctionID = Integer.parseInt(strArr[2]);
            amountSold = Integer.parseInt(strArr[3]);
            for(int i = 0 ; i<allHouses.size(); i++){
                if(allHouses.get(i).getId()== deposittoAuctionID){
                    allHouses.get(i).addBalance(amountSold);
                }
            }
        }

        //if the agent puts bid in the item, bank takes the money from agent account.
        if(clientMessage.contains("Block")){
            String[] strArr = clientMessage.split(" ");
            takeBalanceFromAgentID = Integer.parseInt(strArr[1]);
            amountSold = Integer.parseInt(strArr[2]);
            for(int i = 0 ; i<agents.size(); i++){
                if(agents.get(i).getId()== takeBalanceFromAgentID){
                    agents.get(i).subtract(amountSold);
                }
            }
        }

        //if agent gets outbidded, then the bank puts back money to the agents account
        if(clientMessage.contains("Unblock")){
            String[] strArr = clientMessage.split(" ");
            takeBalanceFromAgentID = Integer.parseInt(strArr[1]);
            amountSold = Integer.parseInt(strArr[2]);
            for(int i = 0 ; i<agents.size(); i++){
                if(agents.get(i).getId()== takeBalanceFromAgentID){
                    agents.get(i).add(amountSold);
                }
            }
        }

        //if the auction says terminate, then we remove auction house from the arraylist.
        if(clientMessage.contains("terminate")){
            String arr[] = clientMessage.split(" ");
            int removeFromList = Integer.parseInt(arr[1]);
            for(int i = 0; i< allHouses.size(); i++){
                if(allHouses.get(i).getId() == removeFromList){
                    allHouses.remove(i);
                }
            }
            inputStream.close();
            outputStream.close();
            serverClient.close();
        }

        //checks the amount of the agent.
        if(clientMessage.contains("checkAgentAmount")){
            agentNumber = Integer.parseInt(clientMessage.split(" ")[1]);
            for(int i = 0; i<agents.size(); i++){
                if(agents.get(i).getId()==agentNumber){
                    outputStream.writeUTF(""+agents.get(i).getAmount());
                }
            }
        }

        //creating the switch statement to check the message from the auction house.
        switch(clientMessage){
            case "balance":
                outputStream.writeUTF("Your balance is "+ auctionHouse.getBalance());
                waitForAuctionHouse();
                break;
            case "terminate":
                outputStream.writeUTF("Your program is terminating");
                break;

            default:
                waitForAuctionHouse();
        }

    }


    /**
     *
     * @param agentName is set for agent name
     * @param amount is set for the amount agent has while agent is opening the new account.
     */
    public void interactWithAgent(String agentName, int amount){

        try{
            agent = new Agent(clientNumber, amount, agentName);
            agents.add(agent);
            System.out.println("Agent got registered in Bank");
            waitForAgent();
        } catch (IOException e){
            System.out.println(e.toString());
        }

    }

    /**
     * this method is called after the agent is created.
     * here, bank keeps waiting for the agent message.
     * @throws IOException
     */
    public void waitForAgent() throws IOException {
        clientMessage = inputStream.readUTF();
        switch(clientMessage){
            case "ListAuctionHouse":
                outputStream.writeUTF(auctionInformation());
                outputStream.flush();
                waitForAgent();
                break;
            case "CheckBalance":
                outputStream.writeUTF("Your balance is "+agent.getAmount());
                waitForAgent();
                break;
        }

        if(clientMessage.contains("terminate")) {
            for (int i = 0; i < agents.size(); i++) {
                if (agents.get(i).getId() == Integer.parseInt(clientMessage.split(" ")[1])) {
                    agents.remove(i);
                }
            }
            inputStream.close();
            outputStream.close();
            serverClient.close();
        }
    }

    /**
     * @returns the string of the list of the auction houses currently registered in the bank.
     */
    public String auctionInformation(){
        String str="";
        for(int i =0; i <allHouses.size();i ++){
            str += allHouses.get(i).getId() + " " +allHouses.get(i).getHostname() + " "+allHouses.get(i).getPort()+" ";
        }
        return str;
    }

}
