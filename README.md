- # General
    - #### Team#: 
    team-cha
    - #### Names:
    Howard You, Benjamin Wu
    - #### Project 5 Video Demo Link:
    https://youtu.be/O8rstOzKhe8
    - #### Instruction of deployment:
Assuming Java, MySql, Tomcat10, AWS, and Maven installed
    - MySql should have the user mytestuser with all the access required
Git clone the repository 
    git clone https://github.com/uci-jherold2-fall23-cs122b/2023-fall-cs122b-team-cha.git
Go into the 2023-fall-cs122b-team-cha directory
Create the database moviedb using createtable.sql
Create the stored procedure using stored-procedure.sql
Populate moviedb database with movie-data.sql
CD into 122b-encryptor directory run the following commands:
    mvn compile
    mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="UpdateSecurePasswordCustomers"
    mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="UpdateSecurePasswordEmployee"
CD out of 122b-encryptor directory
CD into 122b-parser directory run the following commands:
    mvn package
    java -cp target/122b-parser-0.0.1-SNAPSHOT.jar DomParser
CD out of 122b-parser directory
You should be in 2023-fall-cs122b-team-cha directory
Run this command there shouldn't be a war file yet:
    ls -lah /var/lib/tomcat10/webapps/
Run the following commands to build the war file for 2023-fall-cs122b-team-cha:
    mvn package
    sudo cp ./target/*.war /var/lib/tomcat10/webapps/
Run this command there should be a war file now:
    ls -lah /var/lib/tomcat10/webapps/
Go the deployed website
    

    - #### Collaborations and Work Distribution:

  
- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
  ./src/MovieRatedServlet.java
  ./src/SingleMovieServlet.java
  ./src/SingleStarServlet.java
  ./src/StarsServlet.java
  ./src/Browse/GenreServlet.java
  ./src/Browse/MetaDataServlet.java
  ./src/Browse/SingleGenreServlet.java
  ./src/Browse/SingleTitleServlet.java
  ./src/Browse/TableNameServlet.java
  ./src/Insertions/AddMovie.java
  ./src/Insertions/AddStar.java
  ./src/Search/AdvancedSearchServlet.java
  ./src/Search/AutocompleteServlet.java
  ./src/Search/SearchServlet.java
  ./src/cart/PaymentServlet.java
  ./src/cart/SaleServlet.java
  ./src/login/eLoginServlet.java
  ./src/login/LoginServlet.java
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    The connection pool in our code has a max of 100 connections that can be reused when unallocated and requested by a servlet. After the connection is closed by the servlet, it goes back into the connection pool. In our pool, at most 30 connections can stay idle, if more than 30, our pool releases the extra connections.

When a servlet requests a connection, there is a max wait limit of 10 seconds to find a connection in the pool before an exception is thrown.
    - #### Explain how Connection Pooling works with two backend SQL.
    The connection pool in our code for the two backend SQL is that each backend has a max of 100 connections that can be reused when unallocated and requested by a servlet. After the connection is closed by the servlet, it goes back into the connection pool. In our pool, at most 30 connections can stay idle, if more than 30, our pool releases the extra connections.

    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
  ./src/Insertions/AddMovie.java
  ./src/Insertions/AddStar.java
  ./src/cart/SaleServlet.java
    - #### How read/write requests were routed to Master/Slave SQL?
    In the Master/Slave instances requests were routed through the context.xml resources. We made all read requests go through the local host MySQL and for write requests we made all requests go through the master MySQL database.
 
    <?xml version="1.0" encoding="UTF-8"?>

<Context>

    <!-- Defines a Data Source Connecting to localhost moviedbexample-->
    <Resource
            name="jdbc/moviedb"
            auth="Container"
            driverClassName="com.mysql.cj.jdbc.Driver"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            maxTotal="100" maxIdle="30" maxWaitMillis="10000"
            type="javax.sql.DataSource"
            username="root"
            password="newpassword"
            url="jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&amp;allowPublicKeyRetrieval=true&amp;useSSL=false&amp;cachePrepStmts=true"/>

    <Resource
            name="master"
            auth="Container"
            driverClassName="com.mysql.cj.jdbc.Driver"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            maxTotal="100" maxIdle="30" maxWaitMillis="10000"
            type="javax.sql.DataSource"
            username="root"
            password="newpassword"
            url="jdbc:mysql://<MASTER PRIVATE IP>:3306/moviedb?autoReconnect=true&amp;allowPublicKeyRetrieval=true&amp;useSSL=false&amp;cachePrepStmts=true"/>


</Context>
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
The log_processing.py script is in the github with the path ./time_log/log_processing.py. To use the script, run the command: python ./log_processing.py TJ1 TS1 TJ2 TS2
Where TJ1/TS1 are the TJ/TS files of the first instance and TJ2/TS2 are the TJ/TS files of the second instance. You may also run it with a single set of TJ/TS file or more than two:
python ./log_processing.py TJ1 TS1

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/singlehttp1thread.png/)   | 81                        | 30.67733821783363                   |  20.420948128266506      | It seems like servlet time takes most of the query time when querying.           |
| Case 2: HTTP/10 threads                        | ![](/img/singlehttp10threads.png/)   | 214                        |   184.87746761318564                 |  18.384260016846852         | The time for search servlet is the majority of the query time.            |
| Case 3: HTTPS/10 threads                       | ![](/img/singlehttps10threads.png/)   | 194                         | 170.76422187833057                  | 18.61608283533758          | The time for search servlet is the majority of the query time.           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](/img/single10threadnopooling.png/)   | 230                         | 169.3504853285028      | 59.14927007519046     | Since there is no connection pooling the times for querying, search, and JDBC are worse than the other respective single instance testings  |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/scaled1thread.png/)   | 83                         |  21.66625855465587                  | 20.44821045237588         | The time for both JDBC and Servlet are roughly the same.           |
| Case 2: HTTP/10 threads                        | ![](/img/scaled10threadspooling.png/)   | 136                        | 66.11203849605563                  | 13.458691226781067        | The time for Servlet is the majority of the query time.           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](/img/scaled10threadsnopooling.png/)   | 153                         | 79.86636647227525                   | 31.866251207349876        | Since there is no connection pooling the times for querying, search, and JDBC are worse than the other respective scaled testings           |

