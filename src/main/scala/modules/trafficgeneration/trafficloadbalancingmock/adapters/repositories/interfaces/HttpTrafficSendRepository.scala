package modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.interfaces

import scala.concurrent.Future

trait HttpTrafficSendRepository {
  def createTopic(topicName: String, numPartitions: Int, replicationFactor: Short): Future[Unit]

  def closeTopic(topicName: String): Future[Unit]

  def pushToTopic(message: String, topicName: String, server: String): Future[Unit]
}
