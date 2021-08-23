package modules.trafficgeneration.trafficloadbalancingmock.domain.usecases

import modules.common.config.MessagingServersConfig
import modules.common.utils.RandomUtils
import modules.trafficgeneration.trafficloadbalancingmock.adapters.repositories.interfaces.HttpTrafficSendRepository
import modules.trafficgeneration.trafficloadbalancingmock.domain.usecases.interfaces.HttpTrafficGenerationUseCases

import scala.concurrent.Future

protected[this] class HttpTrafficGenerationUseCasesImpl(
    private val httpTrafficSendRepository: HttpTrafficSendRepository
) extends HttpTrafficGenerationUseCases {
  def createTopic(topicName: String, numPartitions: Int, replicationFactor: Short): Future[Unit] = {
    httpTrafficSendRepository.createTopic(topicName, numPartitions, replicationFactor)
  }

  def closeTopic(topicName: String): Future[Unit] = {
    httpTrafficSendRepository.closeTopic(topicName)
  }

  def pushToTopic(message: String, topicName: String): Future[Unit] = {
    val server = getLoadBalancedTargetServer
    httpTrafficSendRepository.pushToTopic(message, topicName, server)
  }

  private def getLoadBalancedTargetServer: String = {
    val indexOfServer = RandomUtils.getRandomInt(maxExcludedValue = MessagingServersConfig.messagingServers.size)
    MessagingServersConfig.messagingServers(indexOfServer)
  }
}

protected[usecases] object HttpTrafficGenerationUseCasesImpl {
  val httpTrafficGenerationUseCasesImplSingleton: HttpTrafficGenerationUseCasesImpl =
    new HttpTrafficGenerationUseCasesImpl(HttpTrafficSendRepository.httpTrafficSendRepositorySingleton)
}
