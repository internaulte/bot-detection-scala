package modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories

import modules.trafficgeneration.trafficloadbalancingmock.adapters.messaging.interfaces.MessagingClient
import modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.interfaces.HttpTrafficSendRepository

import scala.concurrent.Future

protected[this] class HttpTrafficSendRepositoryImpl(private val messagingClient: MessagingClient)
    extends HttpTrafficSendRepository {
  override def createTopic(
      topicName: String,
      numPartitions: Int,
      replicationFactor: Short
  ): Future[Unit] = {
    messagingClient.createTopic(topicName, numPartitions, replicationFactor)
  }

  override def closeTopic(topicName: String): Future[Unit] = {
    messagingClient.closeTopic(topicName)
  }

  override def pushToTopic(message: String, topicName: String, server: String): Future[Unit] = {
    messagingClient.pushToTopic(message, topicName, server)
  }
}

protected[repositories] object HttpTrafficSendRepositoryImpl {
  val httpTrafficSendRepositoryImplSingleton: HttpTrafficSendRepositoryImpl = new HttpTrafficSendRepositoryImpl(
    MessagingClient.messagingClientSingleton
  )
}
