- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
The log_processing.py script is in the github with the path ./time_log/log_processing.py. To use the script, run the command: python ./log_processing.py TJ1 TS1 TJ2 TS2
Where TJ1/TS1 are the TJ/TS files of the first instance and TJ2/TS2 are the TJ/TS files of the second instance. You may also run it with a single set of TJ/TS file or more than two:
python ./log_processing.py TJ1 TS1

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/singlehttp1thread.png/)   | 81                        | 30.67733821783363                   |  20.420948128266506      | Using connection pooling on the single instance we notice that the time for JDBC and Search were close in time.           |
| Case 2: HTTP/10 threads                        | ![](/img/singlehttp10threads.png/)   | 214                        |   184.87746761318564                 |  18.384260016846852         | Using HTTP with 10 threads on the single instance we notice that the time for Search increased significantly with more threads while JDBC seems to have gotten faster with more threads.            |
| Case 3: HTTPS/10 threads                       | ![](/img/singlehttps10threads.png/)   | 194                         | 170.76422187833057                  | 18.61608283533758          |Using HTTPS with 10 threads on the single instance we notice that the time for Search increased significantly but some how faster single instance without HTTPS while JDBC seems to have stayed with HTTPS.           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](/img/single10threadnopooling.png/)   | 230                         | 169.3504853285028      | 59.14927007519046     | Since there is no connection pooling the times for querying, search, and JDBC are worse than the other respective single instance testings  |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](/img/scaled1thread.png/)   | 83                         |  21.66625855465587                  | 20.44821045237588         | Using connection pooling on the load balancer instances, we split the load between the two instances, and the time for both search and JDBC seems to be roughly the same while also being faster than the single instance.          |
| Case 2: HTTP/10 threads                        | ![](/img/scaled10threadspooling.png/)   | 136                        | 66.11203849605563                  | 13.458691226781067        |Using HTTP with 10 threads on the load balancer instances, we split the load between the two instances, and the time for both search and JDBC. Additionally, Search seems to only increase slightly while JDBC time seems to have decreased even further.          |
| Case 3: HTTP/10 threads/No connection pooling  | ![](/img/scaled10threadsnopooling.png/)   | 153                         | 79.86636647227525                   | 31.866251207349876        | Since there is no connection pooling the times for querying, search, and JDBC are worse than the other respective scaled testings           |

