# creditproduct-selection

This project is a demo application for selecting appropriate loan products. 

**How to install and run**

 - Download Camunda 7.4.0-Final Tomcat Distribution from
   [Camunda 7.4.0-Final](https://camunda.org/download/)
 - Extract package into user's home (Name of the folder should be "camunda-bpm-tomcat-7.4.0")
 - Import this project into your IDE
 - Build the project by `mvn clean install`
 
 - Start the Tomcat server by running `startup.bat` or `startup.sh` in folder "/Users/your.name/camunda-bpm-tomcat-7.4.0/server/apache-tomcat-8.0.24/bin/"
 - Copy the 'venit-creditproduct-selection-0.0.1-SNAPSHOT.war' file into Tomcat's webapps folder ("/Users/your.name/camunda-bpm-tomcat-7.4.0/server/apache-tomcat-8.0.24/webapps")
 - Open your browser (Chrome or Firefox is recommended)
 - Navigate to `http://localhost:8080/camunda/app/tasklist`

You can login by using user: ***demo***, password: ***demo***

 - After you logged in click on *Start process* in the upper right
   corner of the screen
 - Choose *Darlehensproduktauswahl mit DMN* process from the list
 - Fill the form (look at Note below) and click on *Start*

> ***Note:*** You can get all available Kunden-IDs from our prepared XML. This XML is our demo database including all the information about clients. Please find this XMl in the creditproduct-selection project under folder **input_XML/SC-DPA-DMN/**
> 'geburtszahl' tag represents 'Kunden-ID'