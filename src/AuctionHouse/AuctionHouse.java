/**
 * Project 5 - CS351, Fall 2019, Class to start auction house client and server
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */

package AuctionHouse;

import java.io.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AuctionHouse {

    /** List of items in the auction house*/
    private List<Item> itemList = new ArrayList<Item>();

    /** InputStream to get data from socket*/
    private  DataInputStream inputStream = null;

    /** OutputStream to send data to sockets*/
    private DataOutputStream outputStream = null;

    /** Socket for communication*/
    private Socket socket = null;

    /** Balance of the Auction House*/
    private int balance = 0;

    /** Boolean to check if someone is currently bidding in the auctionHouse*/
    private boolean isCurrentlyBidding = false;

    /** Auction House Number registered in the bank*/
    private int auctionNumber = -1;




    /**
     * Starts the connection with bank and opens server for connecting
     * with auction house
     *
     *
     */


    public void startServer() {
        try {

            //asks for bank's host name
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Type Bank Host Name: ");
            String bankHostNumber = br.readLine();
            setItem();

            //asks for bank's port number, must be integer
            String bankPortNumber = "";
            boolean isLegal = false;
            while(!isLegal){
                System.out.println("Type Bank's port number: ");
                bankPortNumber = br.readLine();
                if(isInteger(bankPortNumber)){
                    isLegal = true;
                }
            }

            //asks for auction houses host name to register in bank
            System.out.println("Type Your Host Name: ");
            String auctionHostNumber = br.readLine();

            //asks for port name for registration
            String auctionPortNumber = "";
            boolean isLegalPort = false;
            while(!isLegalPort){
                System.out.println("Type Auction's port number: ");
                auctionPortNumber = br.readLine();
                if(isInteger(auctionPortNumber)){
                    isLegalPort = true;
                }
            }


            //makes connections with bank
            socket = new Socket(bankHostNumber, Integer.parseInt(bankPortNumber));
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            int a = 0;
            String intialMessage = inputStream.readUTF();

            String number = "";

            while (intialMessage.charAt(a) != ' ' ) {
                number = number + intialMessage.charAt(a);
                a++;
            }

            //gets client number of auction house
            auctionNumber = Integer.parseInt(number);


            //starts server for auction house
            Thread threadServer = new Thread(new AuctionServer
                    (Integer.parseInt(auctionPortNumber),auctionHostNumber
                            , itemList, socket, auctionNumber));
            threadServer.start();

            String clientMessage = "", serverMessage = "";


            //registers to bank with auctionHostNumber and auctionPortNumber
            outputStream.writeUTF("h " + auctionHostNumber +
                    " "+ auctionPortNumber);
            outputStream.flush();

            serverMessage = inputStream.readUTF();

            menu();



        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }



    /**
     * Menu for the auction house to check balance or terminate the program
     */

    public void menu() {

        String menuInput = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Menu\n1) Type '1' to check balance \n" +"2) Type '2'" +
                " to terminate the program");

        try {
             menuInput = br.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        switch (menuInput) {
            case "1":
                try {
                    //asks for balance in the balance
                    outputStream.writeUTF("balance");
                    outputStream.flush();
                    System.out.println(inputStream.readUTF());
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
                menu();
                break;

            case "2":

                //if auctionHouse is currently Bidding cannot terminate the program
                if(AuctionServer.isCurrentlyBidding()) {
                    System.out.println("Bid is in process at the moment");
                    menu();
                }

                else {
                    try {
                        //boolean condition, check if any item is currently on bidding.
                        outputStream.writeUTF("terminate "+auctionNumber);
                        outputStream.flush();
                        try {
                            outputStream.close();
                            inputStream.close();
                        } catch (IOException ex) {
                            System.out.println(ex.toString());
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());

                    }
                    exitProgram();
                    System.exit(0);
                }
                break;
            default:
                menu();
        }


    }


    /**
     * Exists the program
     */
    public void exitProgram() {
        System.out.println("sending message, closing the account");
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }


    /**
     * Sets item in the list of items
     */
    public void setItem(){
        itemList.add(new Item("microwave", 10));
        itemList.add(new Item("Freezer", 200));
        itemList.add(new Item("Game", 50));
        itemList.add(new Item("Paper_Towel", 100));
        itemList.add(new Item("Laptop", 250));
        itemList.add(new Item("Headphone", 150));
        itemList.add(new Item("Laptop", 123));
        itemList.add(new Item("Cup", 500));
        itemList.add(new Item("Filter", 400));
        itemList.add(new Item("Ball", 40));
        itemList.add(new Item("Bottle", 15));
        itemList.add(new Item("Chair", 50));
        itemList.add(new Item("Banana", 5));
        itemList.add(new Item("Sweater", 15));
        itemList.add(new Item("Coke", 12));
    }


    /**
     * Checks if given string is integer or not
     *
     * @return boolean, true if integer, else false
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
