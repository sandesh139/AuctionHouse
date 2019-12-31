/**
 * Project 5 - CS351, Fall 2019, Class to start communicating with a single
 * agent with auction house
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */

package AuctionHouse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionClientThread implements Runnable{

    /** Socket of the current agent*/
    private Socket agentClient = null;

    /** Seconds passed in the timer*/
    private int secondsPassed = 0;

    /** Boolean to check if timer is running or not*/
    private static boolean timerRunning = true;

    /** Item currently being bid by agent*/
    private int itemLocation = -1;

    /** Kills the thread if true*/
    private boolean killThread = false;

    /** Bid amount made for an item*/
    private int itemBidAmount = -1;

    /** Checks if there is any bid being made currently*/
    private static  boolean currentlyBidding = false;

    /** Item Name which is being bid*/
    private String itemNameWithBid = "";


    /** Input stream to read data in the socket*/
    private DataInputStream inputStream = null;

    /** Output stream to write data into the socket*/
    private DataOutputStream outputStream = null;

    /** Input stream to communicate with bank*/
    private DataInputStream bankInputStream = null;

    /** Output stream to communicate with bank*/
    private DataOutputStream bankOutputStream = null;

    /** Client Message*/
    private String clientMessage = "";

    /** Server Message*/
    private String serverMessage = "";

    /** List of items on auction house*/
    private List<Item> itemList = new ArrayList<Item>();

    /** Socket for bank*/
    private Socket bankSocket = null;

    /** List of agents communicating with auction house*/
    private static List<Agent> agents = new ArrayList<Agent>();

    /** Client number of current agent*/
    private int agentNumber;

    /** Client Number of current auction house*/
    private int auctionHouseNumber;

    private Agent currentAgent = null;

    /**
     * Constructor for AuctionClientThread
     *
     * @param Socket agentClient, socket for this thread
     * @param List<Item> itemList, list of items in the auctionHouse
     * @param Socket bankSocket, socket for bank
     * @param int auctionNumber, client number of auction house
     *
     */
    public  AuctionClientThread(Socket agentClient,
                                List<Item> itemList, Socket bankSocket,
                                int auctionHouseNumber) {
        this.agentClient = agentClient;
        this.itemList = itemList;
        this.bankSocket = bankSocket;
        agentNumber = 0;
        this.auctionHouseNumber = auctionHouseNumber;

    }

    /**
     * Runs the thread
     *
     */
    @Override
    public void run() {
        try {

            inputStream = new DataInputStream(agentClient.getInputStream());
            outputStream = new DataOutputStream(agentClient.getOutputStream());

            bankInputStream = new DataInputStream(bankSocket.getInputStream());
            bankOutputStream = new DataOutputStream(bankSocket.getOutputStream());

            serverMessage = "You are now connected with Auction House";

            outputStream.writeUTF(serverMessage);

            outputStream.flush();
            clientMessage = inputStream.readUTF();

            int i = 0;
            String number = "";
            //gets the clientNumber of agent
            while (clientMessage.charAt(i) != ' ' ) {
                number = number + clientMessage.charAt(i);
                i++;
            }

            agentNumber = Integer.parseInt(number);

            //adds agent to list of agents
            currentAgent = new Agent(agentClient, agentNumber);
            boolean isPresent = false;
            for(int m = 0; m<agents.size();m++){
                if(agents.get(m).getAgentId()==agentNumber){
                    isPresent= true;
                }
            }
            if(!isPresent){
                agents.add(currentAgent);
            }
            waitForAgent();

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Message from agent is taken and appropriate action
     * is carried out
     *
     */
    public void waitForAgent() {
        try {
            clientMessage = inputStream.readUTF();

            //if agent asks for list of item
            switch(clientMessage){
                case "ItemList":
                    serverMessage = "";
                    for(int i = 0; i<3 && i <itemList.size(); i++){

                        serverMessage += " "+itemList.get(i).getName()
                                + " "+itemList.get(i).getMinBid();
                        if(i == 0){
                            serverMessage =
                                    serverMessage.replaceFirst(" ", "");
                        }
                    }
                    outputStream.writeUTF(serverMessage);
                    outputStream.flush();
                    break;
                case "b":
                    break;

                //if agent wants to terminate
                case "Terminate":
                default:
                    checkBidMenu(clientMessage);
                    break;
            }
            if(clientMessage.equals("terminate")){
                outputStream.close();
                inputStream.close();
                agentClient.close();
            }
            waitForAgent();

        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }


    /**
     * Checks Bid made by user
     *
     */
    public void checkBidMenu(String message) {
        int agentId = 0;
        Agent agent = null;
        if(killThread == true){
            timerSecond.cancel();
            Thread.currentThread().stop();
        }
        DataInputStream agentInputStream = null;

        DataOutputStream agentOutputStream = null;

        Socket agentSocket = null;
        if(message.split(" ").length>1) {
            itemLocation = Integer.parseInt(message.split(" ")[0]);
            itemBidAmount = Integer.parseInt(message.split(" ")[1]);
        }

        try {

            //if current bid is lower than previous bid
            if(itemList.get(itemLocation - 1).getMinBid() > itemBidAmount) {

                    outputStream.writeUTF("fail");
                    outputStream.flush();

            }

            else {
                bankOutputStream.writeUTF("checkAgentAmount "+agentNumber);
                bankOutputStream.flush();
                int agentBalance = Integer.parseInt(bankInputStream.readUTF());


                if(agentBalance>=itemBidAmount) {

                    //if someone is outbidded
                    if (!(itemList.get(itemLocation - 1).getAgentWithBid() == -1)) {
                        timerRunning = false;
                        secondsPassed = 0;
                        agentId = itemList.get(itemLocation - 1).getAgentWithBid();
                        for (int i = 0; i < agents.size(); i++) {
                            if (agents.get(i).getAgentId() == agentId) {
                                agent = agents.get(i);
                            }
                        }
                        boolean sameSocket =false;
                        if(agentId == agentNumber){
                            sameSocket = true;
                        }
                        itemNameWithBid = itemList.get(itemLocation - 1).getName();

                        //unblock previous amount
                        bankOutputStream.writeUTF("Unblock "+ agentId +  " " +
                                itemList.get(itemLocation - 1).getMinBid() );
                        bankOutputStream.flush();


                        itemList.get(itemLocation - 1).setMinBid(itemBidAmount, agentNumber);
                        agentSocket = agent.getAgentClient();
                        agentOutputStream = new DataOutputStream(agentSocket.getOutputStream());
                        bankOutputStream.writeUTF("Block "+ agentNumber+  " " + itemBidAmount );
                        bankOutputStream.flush();
                        if(sameSocket){
                            outputStream.writeUTF("You Out Bidded your own previous bid.CurrentBid waiting:");
                        } else {
                            agentOutputStream.writeUTF("Your bid was Out Bidded by other.");
                        }
                        Thread.sleep(10000);
                        timerRunning = true;
                        timerStart();


                    } else {

                        //someone makes the highest bid
                        itemNameWithBid = itemList.get(itemLocation - 1).getName();
                        itemList.get(itemLocation - 1).setMinBid(itemBidAmount, agentNumber);

                        bankOutputStream.writeUTF("Block "+ agentNumber+  " " + itemBidAmount );
                        bankOutputStream.flush();

                        outputStream.writeUTF("pass");
                        outputStream.flush();

                        if (timerRunning) {
                            timerStart();
                        }

                    }
                }else{
                    outputStream.writeUTF("fail");
                    outputStream.flush();
                }

            }
        }

        catch (IOException e) {
            System.out.println(e.toString());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

    }


    //timer for bidders
    Timer timerSecond = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            currentlyBidding = true;
            secondsPassed++;
            if(!timerRunning){
                timerSecond.cancel();
            }
            if(secondsPassed > 20) {
                setTimeIsOver();
                timerSecond.cancel();
            }
        }
    };



    /**
     * When Time is over
     *
     */
    public void setTimeIsOver() {

        //Item is removed from itemlist
        String itemName = "";
        for(int i = 0; i<itemList.size(); i++){
            if(itemList.get(i).getName().equals(itemNameWithBid)){
                itemName = itemList.get(i).getName();
                itemList.remove(i);
            }
        }

        try {

            //when bid is successfull
            outputStream.writeUTF("Congratulations !! " +
                    "Bid Successful, You got item " + itemName + ".");
            outputStream.flush();
            currentlyBidding = false;
            bankOutputStream.writeUTF("Sold "+
                    agentNumber+ " " + auctionHouseNumber + " " + itemBidAmount );
            bankOutputStream.flush();

            //removeCurrentAgent();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        killThread = true;
        timerSecond.cancel();

    }

    /**
     * Starts a timer
     *
     */
    public void timerStart() {
        timerSecond.scheduleAtFixedRate(task, 1000, 1000);
    }

    /**
     * Returns true if someone is currently bidding and vice versa
     *
     * @return boolean, return true if someone is currently bidding
     *
     */
    public static boolean isCurrentlyBidding() {
        return currentlyBidding;
    }
}
