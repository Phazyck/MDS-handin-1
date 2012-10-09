package dk.itu.smds.e2012.lab02.sockets.client;

import dk.itu.smds.e2012.lab02.serialization.common.Task;
import dk.itu.smds.e2012.lab02.serialization.CalSerializer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManagerTcpClient {

    private static Socket socket;
    
    public static void main(String args[]) {
        args = new String[] {"DELETE"};
        String[] commands = new String[]{"POST", "PUT", "GET", "DELETE"};

        boolean legit = false;
        
        for(String s : commands) {            
            if(args[0].equals(s)) {
                legit = true;
                break;
            }            
        }

        if (!legit) {
            System.out.println("FAILURE: No such command!");
            System.exit(0);
        }

        try {
            socket = getSocket();

            // Send the command, given in args[0].
            sendString(args[0]);

            // Receive the reply.
            String response = receiveString();

            // Supply further data to server.
            if (!response.equals(args[0])) {
                System.out.println("FAILURE: Wrong reply received!");
                System.exit(0);
            }

            if (args[0].equals("POST")) {
                Task t;
                t = new Task();
                t.id = "exer-01";
                t.name = "mandatory exercise 1";
                t.date = "17-09-2012";
                t.status = "not-executed";
                t.description = "Do MDS Mandatory Exercise 1";
                t.attendants = "hclk, olpr, bhas, phoegfeldt";
                
                System.out.println(t.id);

                sendObject((Object) t);

            } else if (args[0].equals("PUT")) {
                Task t = new Task();
                t.id = "exer-01";
                t.name = "mandatory exercise 1";
                t.date = "17-09-2012";
                t.status = "executed";
                t.description = "Do MDS Mandatory Exercise 1";
                t.attendants = "hclk, olpr, bhas, phoegfeldt";

                sendObject(t);

            } else if (args[0].equals("GET")) {
                String s = "hilde";

                sendString(s);
            } else if (args[0].equals("DELETE")) {
                String s = "tch-03";
                
                sendString(s);
            }                       

            // Receive results.
            if (args[0].equals("GET")) {
                receiveTasks();
            } else {
                receiveString();
            }

            // Finnaly close the socket. 
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(TaskManagerTcpClient.class.getName()).log(Level.SEVERE, null, ex);

            System.out.println("error message: " + ex.getMessage());
        }
    }

    private static Socket getSocket() throws UnknownHostException, IOException {
        // IP address of the server,
        InetAddress serverAddress = InetAddress.getByName("localhost");

        // It is the same port where server will be listening.
        int serverPort = 7896;

        // Open a socket for communication.
        return new Socket(serverAddress, serverPort);
    }

    private static void sendString(String s) throws IOException {
        // Get data output stream to send a String message to server.
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        dos.writeUTF(s);

        System.out.println("Message to Server: " + s);

        dos.flush();
    }

    private static void sendObject(Object o) throws IOException {
        // Get data output stream to send a String message to server.
        ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());

        dos.writeObject(o);
        
        System.out.println("Sending task to Server.");

        dos.flush();
    }

    private static String receiveString() throws IOException {
        // Now switch to listening mode for receiving message from server.
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        String response = dis.readUTF();

        System.out.println("Message from Server: " + response);

        // Note that this is a blocking call,  
        return response;
    }

    private static void receiveTasks() throws IOException {

        try {
            // Now switch to listening mode for receiving message from server.
            ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());

            List<Task> tasks;

            tasks = (List<Task>) dis.readObject();

            for (Task t : tasks) {
                CalSerializer.printTaskObject(t);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TaskManagerTcpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}