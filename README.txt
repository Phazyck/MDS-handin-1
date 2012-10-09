The project contains six classes:

Cal, Task and User are used to represent the XML data from task-manager-xml.
CalSerializer handles serialization and deserialization to and from XML.
-it also handles printing of User and Task objects.
TcpClient handles client processes.
TcpServer handles server processes.

Program execution:

First the TcpServer should be run, in order to get the server ready and waiting for a request.
Then, the TcpClient is run, testing either GET, POST, PUT or DELETE.
The code was first written to allow specifying the command through args[0].
Then, we couldn't find out how to provide arguments in Netbeans, so we hardcoded it instead.