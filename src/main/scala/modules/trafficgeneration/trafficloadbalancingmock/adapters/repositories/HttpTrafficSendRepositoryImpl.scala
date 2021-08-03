package modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories

import modules.trafficgeneration.trafficloadbalancingmock.adapters.kafka.interfaces.KafkaClient
import modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.interfaces.HttpTrafficSendRepository

import scala.concurrent.Future

class HttpTrafficSendRepositoryImpl(private val kafkaClient: KafkaClient) extends HttpTrafficSendRepository {
  override def createTopic(
      topicName: String,
      numPartitions: Int,
      replicationFactor: Short
  ): Future[Unit] = {
    kafkaClient.createTopic(topicName, numPartitions, replicationFactor)
  }

  override def closeTopic(topicName: String): Future[Unit] = {
    kafkaClient.closeTopic(topicName)
  }

  override def pushToTopic(message: String, topicName: String, server: String): Future[Unit] = {
    kafkaClient.pushToTopic(message, topicName, server)
  }
}
