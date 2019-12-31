# Auction House Project


## Program Usage

### Starting the file

We have three jar files for the project. We need to open the jar file in order of Bank,
Auction House and then Agent respectively.  <br>
First, we need to open bank1.0_akharel_gautams_stimilsina.jar <br>
Then, we need to open auctionHouse1.0_akharel_gautams_stimilsina.jar <br>
Lastly, we need to open agent1.0_akharel_gautams_stimilsina.jar <br>

In the bank jar file, we need to enter host name of the bank. Suppose 
your host name is 127.0.0.1, then enter it.  <br>
We also need to add port number for socket communication. It should always be an 
integer. Otherwise, it will keep asking bank's port number. Bank server is ready 
to handle client's request.  <br>
In the auction house jar file, we need to add bank's host name, bank's port number, 
auction houses' host name and auction houses' port number. Then we communicate with 
bank automatically and have our bank account. <br>

In the agent jar, we need to add bank's host name, bank's port number, agent's name 
and agent amount in the bank. <br>

### How Bank, Auction House and Agent Interact in Auctioning Items

First, bank opens its port in its computer so that agents and auction houses will be
able to communicate with the bank. After someone connects with the bank, bank will
immediately create a new thread for the client. It will recognize if it is agent or 
auction house, create their bank account and wait for any stream messages coming from the 
clients. If the client is an Auction House, it will provide its host name and port number
for the agent to connect.  <br>

Auction House-  <br>
Auction House plays a role as both a server and a client. It is client to bank, but also server
to the agents.  <br>
As a client, it communicates with bank to know it current balance and to close its bank
account.  <br>
As a server, it sells items to the agent. It will take a bid from an agent and tell
the agent if the bid is high enough. It also checks agent's bank account to see if it has 
sufficient funds. To check sufficiency of funds, it will send a message to the bank
 with agent's number and bidding amount. If an agent can make a bid, auction house will
 request bank server to block that amount. If that bid is outbidded, the amount is unblocked.
 If the bid is successfully made, money is transferred from agent's account to 
 Auction Houses' account.  <br>
 
 Agent - Agent is a client to both bank and auction house. First, it connects with bank
 to open its bank account with certain amount. It gets a list of auction houses from 
 the bank. After it chooses a particular auction house, it can see the items in the 
 auction house with the minimum bid amount required. It can  buy items 
 as long as it has sufficient funds in the bank.  <br>
 Agent will communicate with bank to check balance and close account.  <br>
 It will communicate with auction house to make bids and buy items.  <br>
 
### Description of Menu in Agent
Menu feature of Agent consists of list of auction house, check balance and closing account.
To check list of auction houses type '1', to check balance, type '2' and to close account, type '3'.<br>
If you type 1, you will get current list of auction houses now registered to bank. 
If there are no auction house, it will say so. However, if they have auction houses, it
will start from number 0. To check first auction house and its items, type '0'. 
Now you are connected with auction house. To check their item, type '1'. You will be given
list of items with its minimum bid amount. Type either '1', '2' or '3', since we will just have 3 items. Next up, type 
your bid amount. It should be an integer number. If your bid is successful wait for message
to arrive. 
 
## Description of Program Internals
 
### Description of Classes in Bank Package
 
 Bank Controller- Bank Controller initializes the Bank object of the program. <br>
 
 Bank – Bank opens the socket for connection of its clients. If it finds a client and creates a new thread for each client using Bank Client Thread. <br>
 
 Bank Client Thread – Bank Client Thread is used to communicate with its client whether it is an agent or auction house. Bank Client Thread will give out balance to its clients, block funds for auction house, pay to auction house, etc. <br>
 
 Agent – Information about its client agent is stored in this class. <br>
 
 Auction House – Information about its client auction house is stored in the class. The Host Name and Port Number of auction house is also stored here so that Bank Client Thread can give this information to Agents. <br>
 
### Description of Classes in Auction House Package
  
  Auction House Controller- Initializes Auction House object, which creates connects with Bank and creates server thread to contact agent/agents. <br>
  
  Auction House – It establishes connection with Bank and gives the network information about the current auction house server <br>
  
  Auction Server – Here the socket of auction server is waiting for agent to connect. After connection is made, it creates a separate thread for each agent. <br>
  
  Auction Client Thread – Auction Client Thread takes requests from an Agent and sells items to the agent using timer. <br>
  
  Agent – Stores client id information given by bank of agent and socket of that agent communication in the class <br>
  
  Item – Item present in the auction house including its minimum bid, name, etc. <br>

### Description of Classes in Agent Package
   
   Agent Controller- Initializes Agent object. <br>
   
   Agent – Connects with Bank Server. Gets list of auction houses and can buy items. <br>
   
   Wait Auction Message – Separate Thread created for agent to wait on whether he won the item on auction or got outbidded. <br>
   
## Know Bugs and Feature Request
   1) We forgot to add the item description in the project. Our item has info such as name, minimum-bid but does
   not have description for the item. <br>
   2) We did not create menu for bank. We wanted a menu to terminate bank. It was goal with 
   least priority but we did not get to it since we got busy fixing bugs <br>
   3) Agent does not know what items he has. Agent just gets a congratulations message
   on which item the agent got but it cannot see its list of item. <br>
   4) Items are hard coded in the program and not generated randomly. Every auction house gets the
   same item. <br>
   5) When I terminate Agent, it prints "Eof exception " on auction house, but the program runs normally <br>
   
   
## References
   
   Mostly google and stack-overflow to find simple solutions such as timer, multithreaded
   server, etc. 
   
## Author of ReadMe File - Amun Kharel
   
## Date - 2019/12/08