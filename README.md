# Proof of concept bot detection system

## Objectives

The objectives of this proof of concept (POC) is to be able to be able to detect some bots in http traffic from request logs.
Rules to determine if an http request is a bot:
- absence of a useragent identification, as traffic is assumed to have to come from a web browser.
- absence of a Referer header in http request as valid URL as traffic is assumed to must come from a website
- in a first time, even non malicious bots would be refused (even google bots).

To conserve high traffic analysis rate (no shuffling, no batch), each http request is treated independently.
Post analysis would be done next, aggregating data to analyse groups of data to detect abnormal behavior for the same client IP (too much requests per minute, . 
This post analysis could generate a user IP blacklist could be generated and shared to first traffic analysis jobs to reject bots requests.

## Environment

POC will use following architecture:
- Scala 2
- Flink 1
- Kafka 2
- tests: Scalatest 3 and ScalatestMockito 1
- logging: Logback 1 

## Data source

Logs came from http://www.almhuette-raith.at/apache-log/access.log 
(a short summary of its content: https://stackoverflow.com/questions/9234699/understanding-apaches-access-log).
Logs are in Combined Log Format and can be read from file **apache_logs.log** as data source.

## System capacity

To allow high treatment rates, the system uses kafka as messaging the logs to the analysis jobs. 
To enhance requests per second absorption rate, many kafka brokers can be used (here we use 2 kafka brokers, each log data being randomly send to one of them).
As each log data line is treated independently, using only immutable values, Flink server could be duplicated if needed. No shuffling would be generated, no data sharing needed (sharing data through network is really slow).

## Launch

in folder containing the docker-compose.yml file, in a terminal:
- pull dockers through command _docker-compose pull_
- create dockers through command _docker-compose up -d_
- launch the main class in _Bootstrap.scala_

## TODO remaining and improvements

document system environment variables used, their default values, and what is their impact.

installation:
- create separate projects for traffic generation and traffic analysis to be able to install specialized code on separated servers as micro services.
- write correct docker-compose.yml to have correct kafka and flink servers

improve bot detection:
- sink flink traffic analysis treatments results in separated files (bot and no bot files)
- create a flink job that will read files containing traffic first analysis results to perform complementary analysis that would feed a userIP blacklist
- add distributed cache (like ignite ?) sharing the userIP blacklist cache to flink traffic analysis jobs
- would have to accept non malicious bots (google, bing...)

data stream:
- to ensure high traffic generation, logs has been put directly in memory (see object WebServersDataConfig). Bad idea as logs are too big and prevent compilation. Stream logs from file. Eventually, use an hdfs system to stream file faster.
