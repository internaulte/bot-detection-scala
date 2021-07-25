# Proof of concept bot detection system

## Objectives

The objectives of this proof of concept (POC) is to be able to be able to detect some bots in http traffic from request logs.
Rules to determine if an http request is a bot:
- absence of a useragent identification, as traffic is assumed to have to come from a web browser.
- absence of a Referer header in http request as valid URL as traffic is assumed to have to come from a website

POC must include:
- logs
- tests
- data treatment from datasource logs

## Environment

POC will use following architecture:
- Scala 2
- Flink 1
- Kafka 2

## Data source

Logs came from http://www.almhuette-raith.at/apache-log/access.log 
(a short summary of its content: https://stackoverflow.com/questions/9234699/understanding-apaches-access-log).
Logs are in Combined Log Format and can be read from file **apache_logs.log** as data source.
