package dk.itu.smds.e2012.lab02.sockets.server;

import dk.itu.smds.e2012.lab02.serialization.common.Cal;
import dk.itu.smds.e2012.lab02.serialization.common.Task;
import dk.itu.smds.e2012.lab02.serialization.CalSerializer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

public class TaskManagerTcpServer {

    private static Socket socket;

    public static void main(String args[]) {
        try {
            // Get deserialize task-manager.
            Cal cal = CalSerializer.deSerialize();

            // Establish connection.
            socket = getSocket();

            // Receive the request string.
            String request = receiveString();

            // Confirm request.
            sendString(request);

            if (request.equals("POST")) {
                Task task = (Task) receiveObject();
                cal.tasks.add(task);

                sendString("Task posted.");
            } else if (request.equals("PUT")) {
                Task task = (Task) receiveObject();

                boolean updated = false;
                for (Task t : cal.tasks) {
                    if (t.id.equals(task.id)) {
                        cal.tasks.remove(t);
                        cal.tasks.add(task);
                        updated = true;
                        break;
                    }
                }
                if (updated) {
                    sendString("Task updated.");
                } else {
                    sendString("No task with matching id.");
                }
            } else if (request.equals("GET")) {
                String attendant = receiveString();
                List<Task> result = new ArrayList<Task>();
                for (Task t : cal.tasks) {
                    if (t.attendants.contains(attendant)) {
                        result.add(t);
                    }
                }

                sendObject(result);
            } else if (request.equals("DELETE")) {
                String task = receiveString();

                Task removeThis = null;
                for (Task t : cal.tasks) {
                    if (t.id.equals(task)) {
                        removeThis = t;
                        break;
                    }
                }

                if (removeThis == null) {
                    sendString("No task with matching id.");
                } else {
                    cal.tasks.remove(removeThis);
                    sendString("Task deleted.");
                }
            }

            socket.close();
            CalSerializer.serialize(cal);
        } catch (JAXBException ex) {
            Logger.getLogger(TaskManagerTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaskManagerTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskManagerTcpServer.class.getName()).log(Level.SEVERE, null, ex);

            System.out.println("error message: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TaskManagerTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Socket getSocket() throws IOException {
        int serverPort = 7896;
        // create a server socket listening at port 7896
        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Server started at 7896");
        // Server starts accepting requests.
        // This is blocking call, and it wont return, until there is request from a client.
        return serverSocket.accept();
    }

    private static String receiveString() throws IOException {
        // Get the inputstream to receive data sent by client. 
        InputStream is = socket.getInputStream();

        // based on the type of data we want to read, we will open suitbale input stream.  
        DataInputStream dis = new DataInputStream(is);

        // Read the String data sent by client at once using readUTF,
        // Note that read calls also blocking and wont return until we have some data sent by client. 
        String message = dis.readUTF(); // blocking call

        // Print the message.
        System.out.println("Message from Client: " + message);

        return message;
    }

    private static Object receiveObject() throws IOException, ClassNotFoundException {
        // Get the inputstream to receive data sent by client. 
        InputStream is = socket.getInputStream();

        // based on the type of data we want to read, we will open suitbale input stream.  
        ObjectInputStream dis = new ObjectInputStream(is);

        // Read the String data sent by client at once using readUTF,
        // Note that read calls also blocking and wont return until we have some data sent by client. 
        return dis.readObject(); // blocking call
    }

    private static void sendString(String s) throws IOException {
        // Now the server switches to output mode delivering some message to client.
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        outputStream.writeUTF(s);

        System.out.println("Message to Client: " + s);

        outputStream.flush();
    }

    private static void sendObject(Object o) throws IOException {
        // Now the server switches to output mode delivering some message to client.
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

        outputStream.writeObject(o);

        System.out.println("Sending tasks to Client.");

        outputStream.flush();
    }
}