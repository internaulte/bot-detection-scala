# Scalability and architecture

## Increase http requests support to 20000 per second in six months. Possible ?

Once flink treatments would be separated from data generation in bootstrap (separated projects, see _README.md_, part **TODO remaining and improvements**), increasing http requests support could require to increase number of kafka brokers and flink nodes (more tasks nodes, maybe more manager nodes if necessary) if memory or CPUs are overcharged.

## How to architecture high requests number support ?

Data should be send to a distributed kafka topic(s). Then distributed flink can stream data as group consumers (as group consumers of a topic, kafka guaranty that each message is only read once).
Then flink job can handle the data and detect if request is acceptable or not (first analysis).
A second treatment could be done after that detection (not done in present code here), through another flink server that would perform analysis of first treatment results to detect abnormal client IP traffic behavior and constantly create a blacklist of clientIP.
That client IP blacklist would be shared to flink first analysis jobs through a distributed cache (ignite for instance).
That allows to perform bulk analysis without slowing down traffic treatments as that post analysis phase is asynchronous and blacklist freshness is not a problem for traffic first analysis.

## What needed to push to production environment ?

Code must be obfuscated before to push to production to avoid code leak, as it could ease bot development to pass the analysis.
To be sure that the system will not block the client web server, an adapted max usable resource configuration for the system must be prepared. Or the system could be installed on a different machine than the client web server, if the additional network latency is acceptable.

Production environment should also be prepared by adding appropriate environment variables values, and appropriate connection of http request entry flow to the kafka brokers on the one hand, appropriate connection to the receiving client web server after http request analysis.
Be sure that no firewall or other security would reject data stream entry in kafka or requests arriving to web server after request acceptance.

## Attest system health in production

To check system health, memory usage variations and CPU charge must be monitor. If needed, a new instance of the system could be created to balance the charge.

## How to store results ?

Analysis treatments results could be send to a distributed database. A Time Series Database could be used (example: Druid) as timestamped logged data fit well to that logic. It would allow high performance for time-based windowing aggregations and filtering, for monitoring (number of rejected requests in the last minutes for instance) or post analysis.
Data deletion could be done periodically through time segment dropping of each segment with end time superior to a given value (10 days here).
Another solution could be a database with faceting possibilities, like ElasticSearch. It would allow fast query on any prepared facet.

## How to be sure system is really doing its job ?

To ensure system is doing what is expected, it should be useful to use real bots to request a protected site. If the bot cannot access to the site, test is successful.
In parallel, do not forget to try to connect as real human to the same protected site, with various browsers and systems and check if normal user can access.

## Monitoring

System resources usages can be monitor using probes on CPU and memory usages for instances, without slowing down the system.
Grafana could be used to monitor easily resource usages. It could also request the stored results to monitor number of requests accepted an rejected in the last defined period. Requesting on the distributed data storage as exposed in part _How to store results ?_ would have no impact on system performance.