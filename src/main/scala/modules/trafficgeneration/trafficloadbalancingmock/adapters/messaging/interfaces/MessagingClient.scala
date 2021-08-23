package modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging.interfaces

import modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging.KafkaClientImpl

import scala.concurrent.Future

protected[trafficloadbalancingmock] trait MessagingClient {
  def createTopic(topicName: String, numPartitions: Int, replicationFactor: Short): Future[Unit]

  def closeTopic(topicName: String): Future[Unit]

  def pushToTopic(message: String, topicName: String, server: String): Future[Unit]
}

protected[trafficloadbalancingmock] object MessagingClient {
  val messagingClientSingleton: MessagingClient = KafkaClientImpl.kafkaClientSingleton
}
