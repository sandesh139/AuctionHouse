package Bank;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Project 5 - CS351, Fall 2019, Class for bank where bank server
 * is created.
 * @version Date 2019-12-07
 * @author Amun Kharel, Shreeman Gautam, Sandesh Timilsina
 *
 *
 */
public class Bank {

    /**integer used to provide the unique id to the agent. */
    private int clientCounter = 0;

    /**
     * this method creates bank server and keeps waiting for the new clients and for each new agent, a new thread is
     * created.
     */
    public void startServer() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String bankPortNumber = "";
            boolean isLegalPort = false;
            while(!isLegalPort){
                System.out.println("Type Bank's port number: ");
                bankPortNumber = br.readLine();
                if(isInteger(bankPortNumber)){
                    isLegalPort = true;
                }
            }

            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(bankPortNumber));

            System.out.println("Bank Server Started...");

            while (true) {
                clientCounter++;
                Socket serverClient = serverSocket.accept(); //accept client side
                System.out.println(">> " + "Client No: " + clientCounter + " started!!");
                BankClientThread bankClientThread = new BankClientThread(clientCounter,serverClient);
                bankClientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
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
