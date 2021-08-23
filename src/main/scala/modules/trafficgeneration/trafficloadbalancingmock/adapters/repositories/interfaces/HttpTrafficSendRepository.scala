package modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.interfaces

import modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.HttpTrafficSendRepositoryImpl

import scala.concurrent.Future

protected[trafficloadbalancingmock] trait HttpTrafficSendRepository {
  def createTopic(topicName: String, numPartitions: Int, replicationFactor: Short): Future[Unit]

  def closeTopic(topicName: String): Future[Unit]

  def pushToTopic(message: String, topicName: String, server: String): Future[Unit]
}

protected[trafficloadbalancingmock] object HttpTrafficSendRepository {
  val httpTrafficSendRepositorySingleton: HttpTrafficSendRepository =
    HttpTrafficSendRepositoryImpl.httpTrafficSendRepositoryImplSingleton
}
