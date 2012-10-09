package dk.itu.smds.e2012.lab02.serialization.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class User {
    public String name;
    public String password;
}
