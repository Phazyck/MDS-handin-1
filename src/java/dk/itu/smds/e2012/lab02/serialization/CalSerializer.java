package dk.itu.smds.e2012.lab02.serialization;

import dk.itu.smds.e2012.lab02.serialization.common.Cal;
import dk.itu.smds.e2012.lab02.serialization.common.Task;
import dk.itu.smds.e2012.lab02.serialization.common.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class CalSerializer {

    public static void main(String args[]) throws IOException {
        try {
            // Deserialize task-manager-xml into java objects.
            Cal cal = deSerialize();

            printCalObjects(cal);

            // Serialize Cal object into XML.
            serialize(cal);

        } catch (JAXBException ex) {
            Logger.getLogger(CalSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final static String path ="./web/WEB-INF/task-manager-xml.xml";    

    public static void printCalObjects(Cal cal) {
        // Iterate through the collection of user objects and print each user object in the form of XML to console.
        System.out.println("Printing user objects serialized into XML");
        for (User u : cal.users) {
            printUserObject(u);
        }

        // Iterate through the collection of task objects and print each task object in the form of XML to console.
        System.out.println("Printing task objects serialized into XML");
        for (Task t : cal.tasks) {
            printTaskObject(t);
        }
    }

    public static void printUserObject(User user) {
        try {
            StringWriter writer = new StringWriter();

            // create a context object for User Class
            JAXBContext jaxbContext = JAXBContext.newInstance(User.class);

            // Call marshal method to serialize user object into XML.
            jaxbContext.createMarshaller().marshal(user, writer);

            System.out.println(writer.toString());

        } catch (JAXBException ex) {
            Logger.getLogger(CalSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printTaskObject(Task task) {
        try {
            StringWriter writer = new StringWriter();

            // create a context object for Task Class
            JAXBContext jaxbContext = JAXBContext.newInstance(Task.class);

            // Call marshal method to serialize user object into XML.
            jaxbContext.createMarshaller().marshal(task, writer);

            System.out.println(writer.toString());

        } catch (JAXBException ex) {
            Logger.getLogger(CalSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void saveFile(String xml) throws IOException {

        File file = new File(path);

        // create a bufferedwriter to write Xml
        BufferedWriter output = new BufferedWriter(new FileWriter(file));

        output.write(xml);
        output.close();
    }

    public static Cal deSerialize() throws JAXBException, FileNotFoundException {
        // create an instance context class, to deserialize.
        JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

        // Create a file input stream for task-manager-xml.
        FileInputStream stream = new FileInputStream(path);

        // Deserialize task-manager-xml into java objects.
        return (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);
    }

    public static void serialize(Cal cal) throws JAXBException, IOException {
        // create an instance context class, to serialize.
        JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

        // Serialize Cal object into XML.
        StringWriter writer = new StringWriter();

        jaxbContext.createMarshaller().marshal(cal, writer);

        System.out.println("Printing serialized Cal XML before saving into file!");

        // Print the serialized Xml to Console.
        System.out.println(writer.toString());

        // Finally save the Xml back to the file.
        saveFile(writer.toString());
    }
}
