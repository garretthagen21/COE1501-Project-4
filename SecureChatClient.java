import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;
    String encryptionType;  
    SymCipher cipher;
    BigInteger N;
	BigInteger E;
	BigInteger symKey;
	BigInteger encryptSymKey;

    ObjectInputStream streamReader;
    ObjectOutputStream streamWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
	Socket connection;
	
	

    public SecureChatClient ()
    {
        try {
        //Prompt user  
        myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
        
        	
        // Setup connection
        InetAddress addr = InetAddress.getByName(serverName);
        connection = new Socket(addr, PORT); 
        
   
        
        
        streamWriter = new ObjectOutputStream(connection.getOutputStream()); // Get Writer
        streamWriter.flush();
        streamReader = new ObjectInputStream(connection.getInputStream());   // Get Reader 
   
       
        // Initialize handshaking 
        E = (BigInteger)streamReader.readObject();
        N = (BigInteger)streamReader.readObject();
        encryptionType = (String)streamReader.readObject();
        
        
        if(encryptionType.equals("Sub")) {
        	cipher = new Substitute();  
        	encryptionType = "Substitute";
        }
        else {
        	cipher = new Add128();
        	encryptionType = "Add128";
        }
       
        symKey = new BigInteger(1,cipher.getKey());
        encryptSymKey = symKey.modPow(E, N); 
        
        printHandshake();			//Print stats
        
        
        // Output to object stream
        streamWriter.writeObject(encryptSymKey);
        streamWriter.flush();
        streamWriter.writeObject(cipher.encode(myName));
        streamWriter.flush();
        
        // Create GUI
        this.setTitle(myName);
        Box b = Box.createHorizontalBox();  
        outputArea = new JTextArea(8, 30);  
        outputArea.setEditable(false);
        b.add(new JScrollPane(outputArea));

        outputArea.append("Welcome to the Chat Group, " + myName + "\n");

        inputField = new JTextField("");  // This is where user will type input
        inputField.addActionListener(this);

        prompt = new JLabel("Type your messages below:");
        Container c = getContentPane();

        c.add(b, BorderLayout.NORTH);
        c.add(prompt, BorderLayout.CENTER);
        c.add(inputField, BorderLayout.SOUTH);

        Thread outputThread = new Thread(this);  // Thread is to receive strings
        outputThread.start();                    // from Server

		addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    { System.out.println("CLIENT CLOSING");
                      System.exit(0);
                     }
                }
            );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
            e.printStackTrace();
        }
    }
    
    
    private void printHandshake() {
    	 System.out.println("\nE: "+E.toString());
    	 System.out.println("\nN: "+N.toString()+"\n");
    	 System.out.println("\nEncryption Type: "+encryptionType);
    	 System.out.println("\nSymmetric Key: "+symKey.toString());
    	 System.out.println("\nEncrypted Symmetric Key: "+encryptSymKey.toString());
    }
    
    
    
    
    public void run()
    {
        while (true)
        {
             try {
            	  	
                byte [] encryptMsgBytes = (byte[])streamReader.readObject();
                String currMsg = cipher.decode(encryptMsgBytes);
			    
			  
                outputArea.append(currMsg+"\n");
                System.out.println("\n-------------Recieved Message-----------");
		        System.out.println("\tEncrypted Message (Bytes):   "+encryptMsgBytes);
		        System.out.println("\tDecrypted Message (Bytes):   "+currMsg.getBytes());
		        System.out.println("\tOriginal Message (String):   "+currMsg);
             }
             catch (Exception e)
             {
                System.out.println(e +  ", closing client!");
                break;
             }
        }
        System.exit(0);
    }

    //Send message
    public void actionPerformed(ActionEvent e) 
    {
    	String currMsg = e.getActionCommand();      // Get input value
    	inputField.setText("");
        
        currMsg = myName + ": " + currMsg;			//Format the message
        byte [] msgBytes = currMsg.getBytes();
        byte [] encryptMsgBytes = cipher.encode(currMsg);
       
        //Print sending message stats
        System.out.println("\n-------------Sending Message-----------");
        System.out.println("\tOriginal Message (String):   "+currMsg);
        System.out.println("\tDecrypted Message (Bytes):   "+msgBytes);
        System.out.println("\tEncrypted Message (Bytes):   "+encryptMsgBytes);
 
        try {
			streamWriter.writeObject(encryptMsgBytes);
			streamWriter.flush();
		} catch (IOException e1) {	
			System.out.println("There was an error sending the message: ");
			e1.printStackTrace();
		}   
    }                                               // to Server

    public static void main(String [] args)
    {
         SecureChatClient SCC= new SecureChatClient();
         SCC.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}