package core.adapters.kafka

import org.apache.kafka.clients.producer.KafkaProducer

import scala.concurrent.Future

trait KafkaClient {
  def createTopic(topicName: String, serverName: String, numPartitions: Int, replicationFactor: Short): Future[Unit]

  def closeTopic(topicName: String, serverName: String): Future[Unit]

  def pushToKafkaTopic(message: String, topicName: String, kafkaProducer: KafkaProducer[String, String]): Future[Unit]
}
