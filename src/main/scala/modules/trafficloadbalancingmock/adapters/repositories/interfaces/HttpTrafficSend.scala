package modules.trafficloadbalancingmock.adapters.repositories.interfaces

import org.apache.kafka.clients.producer.KafkaProducer

import scala.concurrent.Future

trait HttpTrafficSend {
  def createTopic(topicName: String, serverName: String, numPartitions: Int, replicationFactor: Short): Future[Unit]

  def closeTopic(topicName: String, serverName: String): Future[Unit]

  def pushToTopic(message: String, topicName: String, kafkaProducer: KafkaProducer[String, String]): Future[Unit]
}
