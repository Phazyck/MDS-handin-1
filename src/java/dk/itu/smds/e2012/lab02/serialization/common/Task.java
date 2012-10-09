package dk.itu.smds.e2012.lab02.serialization.common;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "task")
public class Task implements Serializable {
    @XmlAttribute
    public String status;
    
    @XmlAttribute
    public String date;
    
    @XmlAttribute
    public String name;
    
    @XmlAttribute
    public String id;
    
    public String description;
    
    public String attendants;
}
