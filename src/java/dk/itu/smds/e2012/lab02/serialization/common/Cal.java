package dk.itu.smds.e2012.lab02.serialization.common;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cal")
public class Cal {
    
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    public List<User> users;
    
    @XmlElementWrapper(name = "tasks")
    @XmlElement(name = "task")
    public List<Task> tasks;
    
}
