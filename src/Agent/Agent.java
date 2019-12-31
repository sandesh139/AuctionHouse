package Agent;

import java.io.*;

import java.net.Socket;


/**
 * Project 5 - CS351, Fall 2019, Class for agent from where bank server
 * is created.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class Agent {

    /**declaring the socket to communicate with bank */
    private Socket bankSocket = null;

    /**declaring the data input stream to read message from the bank socket.*/
    private DataInputStream inputStream = null;

    /**declaring the data output stream to write message from the bank socket. */
    private DataOutputStream outputStream = null;

    /**declaring the socket to communicate with the auction. */
    private Socket auctionSocket = null;

    /**declaring the data input stream to read message from the auction socket.*/
    private DataInputStream auctionInputStream = null;

    /**declaring the data output stream to write message from the auction socket. */
    private DataOutputStream auctionOutputStream = null;

    /**declaring the agent number to store the agent id */
    private int agentNumber = 0;

    /**declaring the string auction information */
    private String[] auctionList;

    Thread wait = null;

    /**
     * start server method gets the information of the bank host name and port numberto communicate with with the bank.
     */
    public void startServer(){
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Type Bank Host Name: ");
            String bankHostNumber = br.readLine();

            String bankPortNumber = "";
            boolean isLegal = false;
            while(!isLegal){
                System.out.println("Type Bank's port number: ");
                bankPortNumber = br.readLine();
                if(isInteger(bankPortNumber)){
                    isLegal = true;
                }
            }

            bankSocket = new Socket(bankHostNumber,Integer.parseInt(bankPortNumber));
            inputStream = new DataInputStream(bankSocket.getInputStream());
            outputStream = new DataOutputStream(bankSocket.getOutputStream());
            String serverMessage = "";
            System.out.println("Type in your name : ");
            String agentName = br.readLine();

            String money = "";
            boolean isMoney = false;
            while(!isMoney){
                System.out.println("Type in the money : ");
                money = br.readLine();
                if(isInteger(money) && Integer.parseInt(money)>=0){
                    isMoney = true;
                }
            }
            outputStream.writeUTF("a " + agentName +" "+ money);
            outputStream.flush();
            serverMessage = inputStream.readUTF();
            System.out.println(serverMessage);
            int i = 0;
            String number = "";

            while (serverMessage.charAt(i) != ' ' ) {
                number = number + serverMessage.charAt(i);
                i++;
            }

            agentNumber = Integer.parseInt(number);

            menu();


        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    /**
     * this method guides agent through the menu.
     */
    public void menu() {
        String menuInput = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Menu\n1) Type '1' to get list of Auction Houses \n" +
                "2) Type '2' to check balance \n" +
                "3) Type '3' to close the account");
        try {
            menuInput = br.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        switch (menuInput) {
            case "1":
                try {
                    outputStream.writeUTF("ListAuctionHouse");
                    outputStream.flush();
                    String arr = inputStream.readUTF();
                    auctionList = arr.split(" ");

                    auctionHouseMenu();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                menu();
                break;
            case "2":
                try {
                    outputStream.writeUTF("CheckBalance");
                    outputStream.flush();
                    String bankMessage = inputStream.readUTF();
                    System.out.println(bankMessage);
                    menu();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                menu();
                break;
            case "3":

                if(!(wait == null) && wait.isAlive()) {
                    System.out.println("Agent is waiting for message");
                    menu();
                    break;
                }

                else  {
                    try {
                        //boolean condition, check if any item is currently on bidding.
                        outputStream.writeUTF("terminate "+ agentNumber);
                        outputStream.flush();
                        try {
                            outputStream.close();
                            inputStream.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());

                    }
                    exitProgram();
                }

                break;

            default:
                menu();
        }
    }

    /**
     * this menu gets input from the agent for the auction house number and so gets connected to agent.
     */
    public void auctionHouseMenu(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String menuInput = "";
        for(int i = 0; i<auctionList.length; i++){
            if(i%3 == 0 &&auctionList.length>2){
                System.out.println("Auction House Number : " + (i/3));
            }
        }
        if(auctionList.length<3){
            System.out.println("There are no currently any auction House. Waiting...");
            return;
        }
        System.out.println("Type number to select the Auction House from list");
        boolean isHouse = false;
        int houseNumber =0;
        while(!isHouse){
            System.out.println("Type in the House Number :\nType 'b' to go back");
            try {
                menuInput = br.readLine();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            if(isInteger(menuInput)){
                houseNumber = Integer.parseInt(menuInput);
                if(houseNumber>=0 && houseNumber<auctionList.length/3){
                    isHouse = true;
                }
            }
            else if (menuInput.equals("b")) {
                return;
            }
        }
        connectToAuctionHouse(auctionList[houseNumber*3+1],Integer.parseInt(auctionList[houseNumber*3+2]));

    }




    public void exitProgram() {
        System.exit(0);
    }


    /**
     * this method connects to the socket of the auction house.
     * @param hostNumber
     * @param portNumber
     */
    public void connectToAuctionHouse(String hostNumber, int portNumber) {
        try{
            auctionSocket =new Socket(hostNumber,portNumber);
            auctionInputStream =new DataInputStream(auctionSocket.getInputStream());
            auctionOutputStream =new DataOutputStream(auctionSocket.getOutputStream());
            String clientMessage="",serverMessage="";

            clientMessage = agentNumber + " Agent Present";

            auctionOutputStream.writeUTF(clientMessage);

            serverMessage = auctionInputStream.readUTF();

            System.out.println(serverMessage);

            singleAuctionHouseMenu();

            auctionInputStream.close();
            auctionOutputStream.close();
            auctionSocket.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * this method provides the information of the item list to the agent by the use of socket.
     */
    public void singleAuctionHouseMenu() {
        String agentInput = "";
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1) Please Type 1 to get list of items\n" +
                "2) Please type 2 to go back to previous menu");
        try {
             agentInput = br.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        switch(agentInput){
            case "1":
                try {
                    auctionOutputStream.writeUTF("ItemList");
                    String[] strArr = auctionInputStream.readUTF().split(" ");
                    bidMenu(strArr);

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            case "2":
                try {
                    auctionOutputStream.writeUTF("Terminate");
                    menu();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            default:
                break;

        }

    }

    /**
     * this method gets the input from agent to select the item in auction house, and amount of the bid to be made.
     * @param arr
     */
    public void bidMenu(String[] arr){
        System.out.println("Item list are given below: ");
        for (int i = 0; i<arr.length && arr.length>1;i+=2){
            System.out.println((i/2+1) + ". " +arr[i] + " has bid amount "+arr[i+1]);
        }
        String itemNumber = "";
        String amountBid = "";
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        boolean isValid = false;
        while(!isValid){
            System.out.println("Type in the Item number :\nOr Type 'b' to go back:");
            try {
                 itemNumber = br.readLine();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
            if(itemNumber.equals("1")||itemNumber.equals("2")||itemNumber.equals("3")) {
                System.out.println("Type in the bid amount :\nOr Type 'b' to go back:");
                try {
                    amountBid = br.readLine();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                if (isInteger(amountBid)) {
                    isValid = true;
                } else if (amountBid.equals("b")) {
                    try {
                        auctionOutputStream.writeUTF("b");
                        auctionOutputStream.flush();
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                    bidMenu(arr);
                } else {
                    System.out.println("Invalid input is given");
                    bidMenu(arr);
                }
            }
            else if (itemNumber.equals("b")) {
                try {
                    auctionOutputStream.writeUTF("b");
                    auctionOutputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                singleAuctionHouseMenu();
            }
        }

        try {
            System.out.println("You placed bid on item number" + itemNumber + " and amount bid "  +amountBid);
            auctionOutputStream.writeUTF( itemNumber+ " "+amountBid );
            auctionOutputStream.flush();
            wait = new Thread(new WaitAuctionMessage(auctionInputStream));
            wait.start();
            menu();
        } catch (IOException e) {
            System.out.print(e.toString());
        }


    }




    /**
     *
     * @param str is checked if it is an integer.
     * @return true if the string passed is an integer.
     */
    public boolean isInteger(String str){
        try{
            Integer.parseInt(str);
        } catch(NumberFormatException e){
            return false;
        } catch (NullPointerException e){
            return false;
        }
        return true;
    }



}
